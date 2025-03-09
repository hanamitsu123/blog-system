package com.example.blog_system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

import lombok.Data;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // コメントの内容
    @Column(length = 2000)
    private String content;

    // コメント作成日時
    private LocalDateTime createdAt;

    // コメントを投稿したユーザー名（必要に応じて User エンティティとのリレーションも実装できます）
    private String username;

    // コメントがどの投稿に紐付くか (多対一のリレーション)
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
