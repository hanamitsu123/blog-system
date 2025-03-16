package com.example.blog_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blog_system.entity.Post;
import com.example.blog_system.repository.PostRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;
	// データベースからすべての投稿（Post）を取得する
	public List<Post> findAll() {
		return postRepository.findAll();
	}
	// 指定された ID を持つ投稿を取得する
	public Post findById(Long id) {
		return postRepository.findById(id).orElse(null);
	}
	// 投稿を保存または更新する
	public Post save(Post post) {
		return postRepository.save(post);
	}
	// 指定された ID の投稿をデータベースから削除する
	public void deleteById(Long id) {
		postRepository.deleteById(id);
	}
}

