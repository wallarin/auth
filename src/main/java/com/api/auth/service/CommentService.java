package com.api.auth.service;

import com.api.auth.DTO.CommentRequest;
import com.api.auth.DTO.CommentResponse;
import com.api.auth.enums.LikeDislikeStatus;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getCommentsByPostId(Long postId, String loginId);
    CommentResponse createComment(Long postId, CommentRequest commentRequest);
    CommentResponse updateComment(Long commentId, CommentRequest commentRequest);
    void deleteComment(Long commentId);

    // 댓글 추천/비추천 메서드 추가
    boolean likeComment(Long commentId, String userId);
    boolean dislikeComment(Long commentId, String userId);

    LikeDislikeStatus likeOrDislikeComment(Long commentId, String userId, boolean isLike);
}
