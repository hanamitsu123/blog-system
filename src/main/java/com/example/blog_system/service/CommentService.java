package com.example.blog_system.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blog_system.entity.Comment;
import com.example.blog_system.entity.Post;
import com.example.blog_system.repository.CommentRepository;
import com.example.blog_system.repository.PostRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	/**
	 * コメントを保存し、対象の投稿の最終更新日を更新する
	 * 
	 * @param comment 保存するコメント
	 */
	@Transactional
	public void save(Comment comment) {
		// コメントをデータベースに保存
		commentRepository.save(comment);
		// コメントに紐付いた投稿の更新日時を現在の時刻に更新
		updatePostTimestamp(comment.getPost());
	}

	/**
	 * 指定された投稿の最終更新日を現在の時刻に更新する
	 * 
	 * @param post 更新対象の投稿
	 */
	private void updatePostTimestamp(Post post) {
		// updatedAt を現在の時刻に設定
		post.setUpdatedAt(LocalDateTime.now());
		// 変更をデータベースに反映（postエンティティの保存）
		postRepository.save(post);
	}

	/**
	 * 指定された投稿IDに紐づくコメント一覧を取得する
	 * 
	 * @param postId 取得するコメントの投稿ID
	 * @return コメントのリスト
	 */
	public List<Comment> findByPostId(Long postId) {
		return commentRepository.findByPostId(postId);
	}


}
