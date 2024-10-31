package com.api.auth.service;

import com.api.auth.DTO.Board;
import com.api.auth.DTO.PostLike;
import com.api.auth.DTO.PostResponse;
import com.api.auth.DTO.PostUpdate;
import com.api.auth.enums.LikeDislikeStatus;
import com.api.auth.repository.BoardRepository;
import com.api.auth.repository.PostLikeRepository;
import com.api.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository, PostLikeRepository postLikeRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
    }

    public Board savePost(String userId, String title, String content) {
        Board board = new Board();
        board.setUserId(userId);
        board.setTitle(title);
        board.setContent(content);
        board.setLikeCount(0);  // 초기 추천수는 0
        board.setWriteDate(new Date());  // 현재 날짜
        board.setWriteTime(new Timestamp(System.currentTimeMillis()));  // 현재 시간
        board.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return boardRepository.save(board);  // 데이터베이스에 저장
    }

//    public List<Board> getAllPosts() {
//        return boardRepository.findAll();  // 데이터베이스에서 모든 게시글을 가져옴
//    }

    public Page<PostResponse> getAllPosts(Pageable pageable, String loginId) {
        Page<Board> posts = boardRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),Sort.by(Sort.Direction.DESC, "writeDate", "writeTime")));

        // 게시글 데이터 가공
        List<PostResponse> postResponses = posts.stream().map(post -> {
            // user_id를 사용하여 users 테이블에서 nickname 조회
            String nickname = userRepository.findNicknameByUserId(post.getUserId());

            // 오늘 날짜인지 확인
            LocalDate today = LocalDate.now();
            LocalDate writeDate = post.getWriteDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String formattedWriteDate = writeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));  // yyyy-MM-dd 형식
            String formattedWriteTime = post.getWriteTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));  // HH:mm 형식

            // 날짜 포맷 정의 (yyyy-MM-dd)
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // 오늘이라면 시/분 반환, 아니라면 날짜 반환
            String displayDate = writeDate.isEqual(today)
                    ? post.getWriteTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                    : writeDate.format(dateFormatter);

            String isUserPostOwner = post.getUserId().equals(loginId) ? "Y" : "N";

            // 사용자가 이 게시글에 추천을 했는지 확인
            boolean isUserLiked = postLikeRepository.existsByPostIdAndUserId(post.getPostId(), loginId);

            // PostResponse 객체로 가공하여 반환
            return new PostResponse(
                    post.getPostId(),
                    //post.getUserId(),
                    post.getTitle(),
                    post.getContent(),
                    nickname,
                    formattedWriteDate,
                    formattedWriteTime,
                    post.getLikeCount(),
                    isUserPostOwner,
                    isUserLiked ? "Y" : "N"
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(postResponses, pageable, posts.getTotalElements());
    }

    public PostResponse getPostDetail(Long postId, String loginId) {
        // 특정 게시글을 조회
        Board post = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // user_id를 사용하여 users 테이블에서 nickname 조회
        String nickname = userRepository.findNicknameByUserId(post.getUserId());

        // 오늘 날짜인지 확인
        LocalDate today = LocalDate.now();
        LocalDate writeDate = post.getWriteDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedWriteDate = writeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String formattedWriteTime = post.getWriteTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        // 오늘이라면 시/분 반환, 아니라면 날짜 반환
        String displayDate = writeDate.isEqual(today)
                ? post.getWriteTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                : formattedWriteDate;

        // 로그인한 사용자가 해당 게시글의 작성자인지 확인하여 'Y' 또는 'N' 설정
        String isUserPostOwner = post.getUserId().equals(loginId) ? "Y" : "N";

        // 사용자가 이 게시글에 추천을 했는지 확인
        boolean isUserLiked = postLikeRepository.existsByPostIdAndUserId(postId, loginId);

        // PostResponse 객체로 반환
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                nickname,
                formattedWriteDate,
                formattedWriteTime,
                post.getLikeCount(),
                isUserPostOwner,
                isUserLiked ? "Y" : "N"
        );
    }

    // 게시글 수정 로직
    public Board updatePost(Long postId, PostUpdate postUpdate) throws Exception {
        Optional<Board> optionalPost = boardRepository.findById(postId);

        if (optionalPost.isPresent()) {
            Board post = optionalPost.get();

            // 제목과 내용을 수정
            post.setTitle(postUpdate.getTitle());
            post.setContent(postUpdate.getContent());
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis())); // 수정 시간 업데이트

            return boardRepository.save(post);  // 수정된 게시글 저장 후 반환
        } else {
            throw new Exception("게시글을 찾을 수 없습니다.");
        }
    }

    public void deletePost(Long postId) {
        if (boardRepository.existsById(postId)) {
            boardRepository.deleteById(postId);
        } else {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
    }

    @Transactional
    public LikeDislikeStatus likePost(Long postId, String userId) {

        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (board.getUserId().equals(userId)) {
            return LikeDislikeStatus.OWN_COMMENT; // 자신의 글인 경우 추천 불가
        }

        // 추천 상태를 확인하고 토글 처리
        PostLike existingLike = postLikeRepository.findByPostIdAndUserId(postId, userId);

        if (existingLike != null) {
            postLikeRepository.delete(existingLike); // 추천 취소
            board.setLikeCount(board.getLikeCount() - 1);  // 추천 수 감소
            boardRepository.save(board);  // 업데이트 반영
            return LikeDislikeStatus.ALREADY_DONE;  // 기존에 추천한 경우 취소 후 상태 반환
        } else {
            // 새 추천 추가
            PostLike newLike = new PostLike();
            newLike.setPostId(postId);
            newLike.setUserId(userId);
            newLike.setIsLiked(true);
            postLikeRepository.save(newLike);

            board.setLikeCount(board.getLikeCount() + 1);  // 추천 수 증가
            boardRepository.save(board);  // 업데이트 반영

            return LikeDislikeStatus.SUCCESS;  // 새로운 추천 상태 반환
        }
    }

}
