package com.intuit.comments.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PostDTO {
   
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
}