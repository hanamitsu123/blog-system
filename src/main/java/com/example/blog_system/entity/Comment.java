package com.example.blog_system.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String content;
    private String username;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Comment が持つ Post へのリレーションを定義
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // 外部キーのカラム名
    private Post post;
    
    // 自身の子コメント（もし必要なら）
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
    
    // コメントが親コメントを持つ場合（もし必要なら）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;
    
    // Post自身の更新日時と関連するコメントの作成日時から、最新の日時を返す
    public LocalDateTime getLatestUpdate() {
        LocalDateTime latest = (updatedAt != null) ? updatedAt : createdAt;
        if (comments != null) {
            for (Comment comment : comments) {
                LocalDateTime commentTime = comment.getCreatedAt();
                if (commentTime != null && commentTime.isAfter(latest)) {
                    latest = commentTime;
                }
            }
        }
        return latest;
    }
}
