package com.api.auth.controller;

import com.api.auth.DTO.CommentRequest;
import com.api.auth.DTO.CommentResponse;
import com.api.auth.enums.LikeDislikeStatus;
import com.api.auth.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 특정 게시글의 모든 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable("postId") Long postId, @RequestParam("loginId") String loginId) {
        List<CommentResponse> comments = commentService.getCommentsByPostId(postId, loginId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequest commentRequest) {
        CommentResponse comment = commentService.createComment(postId, commentRequest);
        return ResponseEntity.ok(comment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequest commentRequest) {
        CommentResponse updatedComment = commentService.updateComment(commentId, commentRequest);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 추천
    @PostMapping("/{commentId}/like")
    public ResponseEntity<String> likeComment(@PathVariable("commentId") Long commentId, @RequestParam("userId") String userId) {
        LikeDislikeStatus status = commentService.likeOrDislikeComment(commentId, userId, true);
        return switch (status) {
            case OWN_COMMENT -> ResponseEntity.ok("본인 글에는 추천할 수 없습니다.");
            case ALREADY_DONE -> ResponseEntity.ok("이미 추천한 댓글입니다.");
            case SUCCESS -> ResponseEntity.ok("댓글이 추천되었습니다.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("추천 처리 중 오류가 발생했습니다.");
        };
    }

    // 댓글 비추천
    @PostMapping("/{commentId}/dislike")
    public ResponseEntity<String> dislikeComment(@PathVariable("commentId") Long commentId, @RequestParam("userId") String userId) {
        LikeDislikeStatus status = commentService.likeOrDislikeComment(commentId, userId, false);
        return switch (status) {
            case OWN_COMMENT -> ResponseEntity.ok("본인 글에는 비추천할 수 없습니다.");
            case ALREADY_DONE -> ResponseEntity.ok("이미 비추천한 댓글입니다.");
            case SUCCESS -> ResponseEntity.ok("댓글이 비추천되었습니다.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비추천 처리 중 오류가 발생했습니다.");
        };
    }

}
