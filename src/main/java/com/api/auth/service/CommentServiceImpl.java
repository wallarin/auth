package com.api.auth.service;

import com.api.auth.DTO.Comment;
import com.api.auth.DTO.CommentLike;
import com.api.auth.DTO.CommentRequest;
import com.api.auth.DTO.CommentResponse;
import com.api.auth.enums.LikeDislikeStatus;
import com.api.auth.repository.BoardRepository;
import com.api.auth.repository.CommentLikeRepository;
import com.api.auth.repository.CommentRepository;
import com.api.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BoardRepository boardRepository, UserRepository userRepository, CommentLikeRepository commentLikeRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId, String loginId) {
        // loginId에 해당하는 nickname 조회
        String loginNickname = userRepository.findNicknameByUserId(loginId);

        return commentRepository.findByPostIdOrderByWriteDateAsc(postId)
                .stream()
                .map(comment -> {
                    CommentResponse response = convertToResponse(comment);
                    // 로그인 nickname과 댓글 작성자 nickname을 비교하여 수정 가능 여부 설정
                    response.setModifiable(comment.getUserId().equals(loginId) ? "Y" : "N");
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        boardRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(commentRequest.getUserId());
        comment.setContent(commentRequest.getContent());
        comment.setParentCommentId(commentRequest.getParentCommentId());

        Comment savedComment = commentRepository.save(comment);
        return convertToResponse(savedComment);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        comment.setContent(commentRequest.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return convertToResponse(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }

    private CommentResponse convertToResponse(Comment comment) {
        String nickname = userRepository.findNicknameByUserId(comment.getUserId());
        String modifiable = "Y";
        return new CommentResponse(
                comment.getCommentId(),
                comment.getPostId(),
                nickname,
                comment.getContent(),
                comment.getWriteDate(),
                comment.getLikeCount(),
                comment.getUnlikeCount(),
                comment.getParentCommentId(),
                modifiable
        );
    }

    @Transactional
    public LikeDislikeStatus likeOrDislikeComment(Long commentId, String userId, boolean isLike) {
        // 댓글 작성자가 본인인지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        if (comment.getUserId().equals(userId)) {
            return LikeDislikeStatus.OWN_COMMENT; // 본인이 작성한 댓글은 추천/비추천 불가
        }

        // 기존 추천/비추천 기록을 확인
        CommentLike existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);

        // 이미 같은 상태로 추천/비추천을 한 경우 false 반환
        if (existingLike != null && existingLike.getIsLike() == isLike) {
            return LikeDislikeStatus.ALREADY_DONE;
        }

        // handleLikeDislike 메서드 호출, isLike에 따라 추천/비추천 처리
        handleLikeDislike(commentId, userId, isLike);
        return LikeDislikeStatus.SUCCESS; // 새로운 추천/비추천이 성공한 경우 true 반환
    }

    // 댓글 추천 비추천 관리 부분
    @Transactional
    public boolean likeComment(Long commentId, String userId) {
        // 댓글 작성자가 본인인지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        if (comment.getUserId().equals(userId)) {
            return false; // 본인이 작성한 댓글은 추천 불가
        }

        CommentLike existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);

        // 이미 추천한 경우 false 반환
        if (existingLike != null && existingLike.getIsLike()) {
            return false;
        }

        handleLikeDislike(commentId, userId, true);
        return true; // 새로운 추천이 성공한 경우 true 반환
    }

    @Transactional
    public boolean dislikeComment(Long commentId, String userId) {
        // 댓글 작성자가 본인인지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        if (comment.getUserId().equals(userId)) {
            return false; // 본인이 작성한 댓글은 비추천 불가
        }
        // 비추천 여부를 확인
        CommentLike existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);

        if (existingLike != null && !existingLike.getIsLike()) {
            return false;
        }

        handleLikeDislike(commentId, userId, false);
        return true; // 새로운 추천이 성공한 경우 true 반환
    }

    private void handleLikeDislike(Long commentId, String userId, boolean isLike) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        CommentLike existingLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userId);

        if (existingLike != null) {
            // 이미 추천/비추천을 했으면 상태 변경
            if (existingLike.getIsLike() != isLike) {
                existingLike.setIsLike(isLike);
                commentLikeRepository.save(existingLike);

                // 추천/비추천 카운트를 업데이트
                if (isLike) {
                    comment.setLikeCount(comment.getLikeCount() + 1);
                    comment.setUnlikeCount(comment.getUnlikeCount() - 1);
                } else {
                    comment.setLikeCount(comment.getLikeCount() - 1);
                    comment.setUnlikeCount(comment.getUnlikeCount() + 1);
                }
            }
        } else {
            // 추천/비추천이 없다면 새로 추가
            CommentLike newLike = new CommentLike();
            newLike.setComment(comment);
            newLike.setUserId(userId);
            newLike.setIsLike(isLike);
            commentLikeRepository.save(newLike);

            if (isLike) {
                comment.setLikeCount(comment.getLikeCount() + 1);
            } else {
                comment.setUnlikeCount(comment.getUnlikeCount() + 1);
            }
        }
        commentRepository.save(comment);
    }

}
