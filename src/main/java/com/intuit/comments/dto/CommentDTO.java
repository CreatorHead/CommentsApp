package com.intuit.comments.dto;

import lombok.Data;

@Data
public class CommentDTO {
	
    private Long postId;
    private Long parentId; // For replies, this will be the ID of the comment it's replying to
    private Long userId;
    private String content;
  
}
