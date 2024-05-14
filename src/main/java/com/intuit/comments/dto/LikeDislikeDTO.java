package com.intuit.comments.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeDislikeDTO {
	
	@NotNull(message = "Comment ID cannot be null")
	private Long commentId;
	
    @NotNull(message = "User ID cannot be null")
	private Long userId;
    
    @NotNull(message = "isLike cannot be null")
	private Boolean isLike; // true for like, false for dislike
}
