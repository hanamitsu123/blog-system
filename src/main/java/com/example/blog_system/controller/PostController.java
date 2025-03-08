package com.example.blog_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.blog_system.entity.Post;
import com.example.blog_system.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

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
}

