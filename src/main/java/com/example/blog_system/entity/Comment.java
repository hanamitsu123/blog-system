package com.example.blog_system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
	@JoinColumn(name = "post_id")
	private Post post;

	// Post自身の更新日時と関連するコメントの作成日時から、最新の日時を返す
	public LocalDateTime getLatestUpdate() {
		//投稿が更新された場合、updatedAtを更新し、ない場合createdAtを初期値にする。
		LocalDateTime latest = (updatedAt != null) ? updatedAt : createdAt;

		return latest;
	}
}
