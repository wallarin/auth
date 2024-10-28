package com.api.auth.DTO;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "posts")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;       // 게시글 ID

    @Column(nullable = false)
    private String userId;       // 작성자의 회원 ID

    @Column(nullable = false, length = 50)
    private String title;      // 글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;    // 글 내용

    @Column(name = "like_count", nullable = false, columnDefinition = "int default 0")
    private int likeCount;     // 추천수

    @Column(name = "write_date", nullable = false)
    private Date writeDate;    // 작성일 (DATE)

    @Column(name = "write_time", nullable = false)
    private Timestamp writeTime; // 작성 시간 (TIMESTAMP)

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt; // 글 생성 시간

    @Column(name = "updated_at")
    private Timestamp updatedAt; // 글 수정 시간

    // 엔티티가 처음 저장되기 전 호출되는 메서드
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.writeDate = new Date();
        this.writeTime = new Timestamp(System.currentTimeMillis());
        this.likeCount = 0;  // 기본 추천수 설정
    }

    // 엔티티가 업데이트되기 전 호출되는 메서드
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
