package com.intuit.comments.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.comments.dto.CommentDTO;
import com.intuit.comments.service.CommentService;


@RestController
@RequestMapping("/api")
public class CommentController {
	
    @Autowired private CommentService commentService;
    
    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.addComment(commentDTO));
    }  

    
    @GetMapping("/comments/recent/{postId}")
    public ResponseEntity<?> findByPostIdWithRecentComments(@PathVariable("postId") Long postId, Pageable pageable) {
        return ResponseEntity.ok(commentService.findByPostIdOrderByCreatedAtDesc(postId, pageable));
    }  
    
    
    @GetMapping("/comments/recent/replies/{parentId}")
    public ResponseEntity<?> findByParentIdWithRecentReplies(@PathVariable("parentId") Long parentId, Pageable pageable) {
        return ResponseEntity.ok(commentService.findByParentIdOrderByCreatedAtDesc(parentId, pageable));
    }  
    
    
    @GetMapping("/comments/top/{postId}")
    public ResponseEntity<?> findByPostIdWithTopComments(@PathVariable("postId") Long postId, Pageable pageable) {
        return ResponseEntity.ok(commentService.findByPostIdOrderByCreatedAtDesc(postId, pageable));
    }  
    
    
    @GetMapping("/comments/top/replies/{parentId}")
    public ResponseEntity<?> findByParentIdWithTopReplies(@PathVariable("parentId") Long parentId, Pageable pageable) {
        return ResponseEntity.ok(commentService.findByParentIdOrderByCreatedAtDesc(parentId, pageable));
    }  
    
    
}
