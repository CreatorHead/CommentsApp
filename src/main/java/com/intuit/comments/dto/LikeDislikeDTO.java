package com.intuit.comments.dto;

import lombok.Data;

@Data
public class LikeDislikeDTO {
	
    private Long commentId;
    private Long userId;
    private Boolean isLike; // true for like, false for dislike
    
}
