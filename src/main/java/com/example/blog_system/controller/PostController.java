package com.example.blog_system.controller;

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
        postService.save(post);
        return "redirect:/posts";
    }

    // 投稿詳細表示
    @GetMapping("/{id}")
    public String showPost(@PathVariable Long id, Model model) {
    	Post post = postService.findById(id);
    	if(post == null) {
    		return "redirect:/posts";
    	}
        model.addAttribute("post", postService.findById(id));
        model.addAttribute("comments", commentService.findByPostId(id));
        model.addAttribute("newComment", new Comment());
        return "posts/show";
    }

    // 投稿編集フォーム表示
    @GetMapping("/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findById(id));
        return "posts/edit";
    }

    // 投稿更新
    @PostMapping("/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post) {
        post.setId(id);
        postService.save(post);
        return "redirect:/posts";
    }

    // 投稿削除
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deleteById(id);
        return "redirect:/posts";
    }
    
    @PostMapping("/{postId}/comments")
    public String addComment(
            @PathVariable Long postId,
            @Valid @ModelAttribute("newComment") Comment newComment,
            BindingResult bindingResult,
            Model model) {
        
        // まず、指定された投稿が存在するか確認します
        Post post = postService.findById(postId);
        if (post == null) {
            // 該当投稿が見つからない場合、一覧にリダイレクト
            return "redirect:/posts";
        }
        
        // バリデーションエラーがある場合は、再度投稿詳細ページに戻る
        if (bindingResult.hasErrors()) {
            // 投稿詳細ページを再表示するため、必要なデータを再セット
            model.addAttribute("post", post);
            model.addAttribute("comments", commentService.findByPostId(postId));
            return "posts/show";
        }
        
        // コメントに投稿情報をセット
        newComment.setPost(post);
        
        // 現在の認証情報からユーザー名を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // 通常はユーザー名が返る
        
        // ユーザー名をコメントにセット
        newComment.setUsername(username);
        newComment.setPost(post);
        
        // コメントを保存
        commentService.save(newComment);
        
        // 登録後、投稿詳細ページにリダイレクト
        return "redirect:/posts/" + postId;
    }

    
    
    
    
}
