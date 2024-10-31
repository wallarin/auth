package com.api.auth.enums;

public enum LikeDislikeStatus {
    SUCCESS, //추천,비추천 성공
    ALREADY_DONE, // 이미 추천, 비추천을 했음
    OWN_COMMENT // 본인이 작성한 댓글
}
