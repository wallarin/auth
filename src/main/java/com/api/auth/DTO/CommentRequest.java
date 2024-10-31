package com.api.auth.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Getter, Setter, toString, equals, hashCode 자동 생성
@AllArgsConstructor  // 모든 필드를 포함하는 생성자 생성
@NoArgsConstructor  // 기본 생성자 생성
public class CommentRequest {
    private String userId;
    private String content;
    private Long parentCommentId;
}
