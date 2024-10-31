package com.api.auth.DTO;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data  // Getter, Setter, toString, equals, hashCode 자동 생성
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq_gen")
    @SequenceGenerator(name = "comments_seq_gen", sequenceName = "comments_seq", allocationSize = 1)
    private Long commentId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String userId;

    @Lob
    @Column(nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date writeDate = new Date();

    @Column(nullable = false, columnDefinition = "NUMBER DEFAULT 0")
    private int likeCount = 0;

    @Column(nullable = false, columnDefinition = "NUMBER DEFAULT 0")
    private int unlikeCount = 0;

    @Column
    private Long parentCommentId;
}
