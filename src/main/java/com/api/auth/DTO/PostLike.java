package com.api.auth.DTO;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "post_likes")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_likes_seq_gen")
    @SequenceGenerator(name = "post_likes_seq_gen", sequenceName = "post_likes_seq", allocationSize = 1)
    private Long likeId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, columnDefinition = "NUMBER(1) DEFAULT 1")
    private Boolean isLiked;
}
