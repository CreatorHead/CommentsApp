package com.intuit.comments.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDTO {

	@NotNull(message = "Post ID cannot be null")
	private Long postId;
	private Long parentId; // For replies, this will be the ID of the comment it's replying to
	
	@NotNull(message = "User ID cannot be null")
	private Long userId;
	
	@NotEmpty(message = "Comment content cannot be empty")
	private String content;

}
