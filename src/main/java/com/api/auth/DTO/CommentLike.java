package com.api.auth.DTO;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comment_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"comment_id", "user_id"})
})
@Data
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_likes_seq")
    @SequenceGenerator(name = "comment_likes_seq", sequenceName = "comment_likes_seq", allocationSize = 1)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "is_like", nullable = false)
    private Boolean isLike;
}
