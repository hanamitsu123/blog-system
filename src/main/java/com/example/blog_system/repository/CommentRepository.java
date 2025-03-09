package com.example.blog_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog_system.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 特定の投稿に紐付くコメントを取得するためのメソッド
    List<Comment> findByPostId(Long postId);
}
