package com.api.auth.repository;

import com.api.auth.DTO.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // 특정 게시글과 사용자 ID에 대한 추천 상태 확인
    PostLike findByPostIdAndUserId(Long postId, String userId);
    boolean existsByPostIdAndUserId(Long postId, String loginId);
}
