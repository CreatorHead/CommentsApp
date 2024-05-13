package com.intuit.comments.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.comments.dto.PostDTO;
import com.intuit.comments.entity.Post;
import com.intuit.comments.service.PostService;


@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired private PostService postService;
    
    @PostMapping("/post")
    public ResponseEntity<?> addPost(@RequestBody PostDTO postDTO) {
        return ResponseEntity.status(201).body(postService.createPost(postDTO));
    }
    
    
    @GetMapping("/get/posts/by-user")
    public ResponseEntity<?> getPostByUser(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Post> posts = postService.getPostsByUser(userId, pageable);
        return ResponseEntity.ok(posts);
    }
}
