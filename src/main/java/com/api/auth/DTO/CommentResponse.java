package com.api.auth.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data  // Getter, Setter, toString, equals, hashCode 자동 생성
@AllArgsConstructor  // 모든 필드를 포함하는 생성자 생성
@NoArgsConstructor  // 기본 생성자 생성
public class CommentResponse {
    private Long commentId;
    private Long postId;
    private String nickname;
    private String content;
    private Date writeDate;
    private int likeCount;
    private int unlikeCount;
    private Long parentCommentId;
    private String modifiable;
}
