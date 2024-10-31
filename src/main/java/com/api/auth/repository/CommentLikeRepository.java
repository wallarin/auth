package com.api.auth.repository;

import com.api.auth.DTO.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    // 특정 댓글에 대한 사용자 추천/비추천 조회
    @Query("SELECT cl FROM CommentLike cl WHERE cl.comment.commentId = :commentId AND cl.userId = :userId")
    CommentLike findByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") String userId);
}
