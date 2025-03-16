package com.example.blog_system.controller;

import java.time.LocalDateTime;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.blog_system.entity.Comment;
import com.example.blog_system.entity.Post;
import com.example.blog_system.service.CommentService;
import com.example.blog_system.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;


	// 一覧表示
	@GetMapping
	public String listPosts(Model model) {
		model.addAttribute("posts", postService.findAll());
		return "posts/list";
	}

	// 新規投稿フォーム表示
	@GetMapping("/new")
	public String newPostForm(Model model) {
		model.addAttribute("post", new Post());
		return "posts/new";
	}

	// 新規投稿の登録
	@PostMapping
	public String createPost(@ModelAttribute Post post) {
		//現在の認証情報からユーザー名を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		//投稿にユーザー名をセットする。
		post.setUsername(currentUsername);
		post.setCreatedAt(LocalDateTime.now()); 
		post.setUpdatedAt(LocalDateTime.now()); 

		postService.save(post);
		return "redirect:/posts";
	}

	// 投稿詳細表示
	@GetMapping("/{id}")
	public String showPost(@PathVariable Long id, Model model) {
		Post post = postService.findById(id);
		if (post == null) {
			return "redirect:/posts";
		}
		model.addAttribute("post", post);
		model.addAttribute("comments", commentService.findByPostId(id));
		model.addAttribute("newComment", new Comment());
		return "posts/show";
	}

	//投稿編集フォーム表示（投稿者のみがアクセス可能）
	@GetMapping("/{id}/edit")
	public String editPostForm(@PathVariable Long id, Model model, Authentication authentication) {
		Post post = postService.findById(id);

		if (post == null) {
			return "redirect:/posts";
		}

		String currentUsername = authentication.getName();

		// 投稿の所有者でない場合、403エラーページへリダイレクト
		if (!post.getUsername().equals(currentUsername)) {
			return "redirect:/error/403"; 
		}

		model.addAttribute("post", post);
		return "posts/edit";
	}

	//投稿更新（投稿者のみが実行可能）
	@PostMapping("/{id}")
	public String updatePost(@PathVariable Long id, @ModelAttribute Post post, Authentication authentication) {
		Post existingPost = postService.findById(id);

		if (existingPost == null) {
			return "redirect:/posts";
		}

		String currentUsername = authentication.getName();

		// 投稿の所有者でない場合、403エラーページへリダイレクト
		if (!existingPost.getUsername().equals(currentUsername)) {
			return "redirect:/error/403"; 
		}

		// タイトル・内容を更新
		existingPost.setTitle(post.getTitle());
		existingPost.setContent(post.getContent());
		existingPost.setUpdatedAt(LocalDateTime.now()); //

		postService.save(existingPost);
		return "redirect:/posts/" + id;
	}

	// 投稿削除（投稿者のみが削除可能）
	@PostMapping("/{id}/delete")
	public String deletePost(@PathVariable Long id, Authentication authentication) {
		Post post = postService.findById(id);
		if (post == null) {
			return "redirect:/posts";
		}

		//  現在の認証情報を取得
		String currentUsername = authentication.getName();

		//  管理者ロールの確認(未実装)
		//        boolean isAdmin = authentication.getAuthorities().stream()
		//            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

		// 投稿したユーザーと一致しないでなければ削除拒否
		if (!post.getUsername().equals(currentUsername) /*&& !isAdmin*/) {
			return "redirect:/error/403"; 
		}

		postService.deleteById(id);
		return "redirect:/posts";
	}

	// コメント登録（コメント追加時に最終更新日を更新）
	@PostMapping("/{postId}/comments")
	public String addComment(
			@PathVariable Long postId,
			@Valid @ModelAttribute() Comment newComment,
			BindingResult bindingResult,
			Model model) {

		//指定された投稿が存在するか確認します
		Post post = postService.findById(postId);
		if (post == null) {
			return "redirect:/posts";
		}

		// バリデーションエラーがある場合は、再度投稿詳細ページに戻る
		if (bindingResult.hasErrors()) {
			model.addAttribute("post", post);
			model.addAttribute("comments", commentService.findByPostId(postId));
			return "posts/show";
		}

		// コメントに投稿情報をセット
		newComment.setPost(post);

		// 現在の認証情報からユーザー名を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		// ユーザー名をコメントにセット
		newComment.setUsername(username);

		// コメントを保存
		commentService.save(newComment);

		// 投稿の最終更新日を更新
		post.setUpdatedAt(LocalDateTime.now());
		postService.save(post);

		return "redirect:/posts/" + postId;
	}
}
