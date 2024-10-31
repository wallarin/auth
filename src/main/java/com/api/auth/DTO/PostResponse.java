package com.api.auth.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Getter, Setter, toString, equals, hashCode 자동 생성
@AllArgsConstructor  // 모든 필드를 포함하는 생성자 생성
@NoArgsConstructor  // 기본 생성자 생성
public class PostResponse {
    private Long postId;       // 게시글 ID
    //private String userId;
    private String title;      // 글 제목
    private String content;
    private String nickname;   // 작성자의 닉네임
    private String writeDate;  // 작성일 (yyyy-MM-dd 형식)
    private String writeTime;  // 작성 시간 (HH:mm 형식)
    private int likeCount;     // 추천 수
    private String isUserPostOwner;
    private String isUserLiked;
}
