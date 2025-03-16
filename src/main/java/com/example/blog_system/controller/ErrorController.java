package com.example.blog_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

	@GetMapping("/error/403")
	public String accessDenied() {
		return "error/403";
	}
}
