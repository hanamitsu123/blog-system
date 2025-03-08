package com.example.blog_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog_system.entity.Post;
public interface PostRepository extends JpaRepository<Post, Long> {
   
}
