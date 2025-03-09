package com.example.blog_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blog_system.entity.Comment;
import com.example.blog_system.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // 特定の投稿IDに対応するコメント一覧を取得
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // コメントを保存
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    // コメント削除など、必要に応じたメソッドも追加可能
}
