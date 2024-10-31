package com.api.auth.repository;

import com.api.auth.DTO.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 모든 댓글 조회
    List<Comment> findByPostId(Long postId);

    // 특정 게시글의 최상위 댓글(대댓글이 아닌 댓글)만 조회 (parentCommentId가 NULL인 경우)
    List<Comment> findByPostIdAndParentCommentIdIsNull(Long postId);

    List<Comment> findByPostIdOrderByWriteDateAsc(Long postId);
}
