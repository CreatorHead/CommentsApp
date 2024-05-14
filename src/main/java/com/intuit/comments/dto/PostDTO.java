package com.intuit.comments.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDTO {

	private Long id;

	@NotNull(message = "User ID cannot be null")
	private Long userId;
	
	@NotEmpty(message = "Post Title cannot be empty")
    @Size(max = 255, message = "Post Title cannot exceed 255 characters")
	private String title;
	
	@NotEmpty(message = "Post Content cannot be empty")
	private String content;
	private LocalDateTime createdAt = LocalDateTime.now();
}