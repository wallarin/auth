package com.api.auth.controller;

import com.api.auth.DTO.Board;
import com.api.auth.DTO.PostResponse;
import com.api.auth.DTO.PostUpdate;
import com.api.auth.enums.LikeDislikeStatus;
import com.api.auth.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"api/board"})
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 목록 가져오기
    @GetMapping("/list")
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perSize", defaultValue = "10") int perSize,
            @RequestParam(value = "loginId", defaultValue = "unknown") String loginId,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchQuery", required = false) String searchQuery
    ) {

        Pageable pageable = PageRequest.of(page, perSize);
        Page<PostResponse> posts = boardService.getAllPosts(pageable, loginId, searchType, searchQuery);
        return ResponseEntity.ok(posts);  // 글 목록을 반환
    }

    // 게시글 상세보기
    @GetMapping("view/{postId}")
    public ResponseEntity<PostResponse> getDetailPosts(
            @PathVariable("postId") Long postId,
            @RequestParam(value = "loginId", defaultValue = "unknown") String loginId
    ) {

        PostResponse posts = boardService.getPostDetail(postId, loginId);
        return ResponseEntity.ok(posts);  // 글 목록을 반환
    }

    @PostMapping("/write")
    public ResponseEntity<String> Write(@RequestBody Board boardDto) {
        // JWT 토큰으로부터 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();  // 현재 인증된 사용자 이름 또는 ID

        Board savedBoard = boardService.savePost(userId, boardDto.getTitle(), boardDto.getContent());


        logger.info("Post saved: {}", savedBoard);
        return ResponseEntity.ok("Post saved successfully");
    }

    // 게시글 수정 요청 처리
    @PutMapping("/{postId}/edit")
    public ResponseEntity<?> updatePost(
            @PathVariable("postId") Long postId,
            @RequestBody PostUpdate postUpdate) {

        try {
            Board updatedPost = boardService.updatePost(postId, postUpdate);
            return ResponseEntity.ok(updatedPost);  // 수정된 게시글 반환
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 수정 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId) {
        System.out.println(postId);
        try {
            boardService.deletePost(postId);
            return ResponseEntity.noContent().build(); // 204 No Content 응답
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found 응답
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(
            @PathVariable("postId") Long postId,
            @RequestParam("userId") String userId
    ) {
        LikeDislikeStatus status = boardService.likePost(postId, userId);
        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("추천되었습니다.");
            case ALREADY_DONE -> ResponseEntity.ok("추천이 취소되었습니다.");
            case OWN_COMMENT -> ResponseEntity.ok("본인 글에는 추천할 수 없습니다.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("추천 처리 중 오류가 발생했습니다.");
        };

    }
}
