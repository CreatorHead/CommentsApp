package com.intuit.comments.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.comments.dto.PostDTO;
import com.intuit.comments.entity.Post;
import com.intuit.comments.exceptions.ResourceNotFoundException;
import com.intuit.comments.service.PostService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * The PostController class handles web requests to manage posts. It provides
 * methods to add new posts and retrieve posts by a specific user.
 */
@RestController
@RequestMapping("/api")
@Validated
public class PostController {

	private static final Logger logger = LoggerFactory.getLogger(PostController.class);

	@Autowired
	private PostService postService;

	/**
	 * Creates a new post based on the data received in the PostDTO.
	 * 
	 * @param postDTO the DTO containing post data from the client
	 * @return ResponseEntity with HTTP status 201 and created post data
	 */
	@PostMapping("/post")
	public ResponseEntity<?> addPost(@Valid @RequestBody PostDTO postDTO) {
		logger.info("Request to create a new post: {}", postDTO);
		try {
			Post createdPost = postService.createPost(postDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
		} catch (Exception e) {
			logger.error("Error occurred while creating post: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating post");
		}
	}

	/**
	 * Retrieves a list of posts made by a specific user, with pagination.
	 * 
	 * @param userId     the ID of the user whose posts are to be retrieved
	 * @param pageNumber the page number to retrieve (zero-based)
	 * @param pageSize   the number of posts per page
	 * @return ResponseEntity containing the list of posts
	 */
	@GetMapping("/get/posts/by-user")
	public ResponseEntity<?> getPostByUser(
			@RequestParam("userId") @NotNull(message = "User ID cannot be null") Long userId,
			@RequestParam(value = "pageNumber", defaultValue = "0") @Min(value = 0, message = "Page number cannot be negative") Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5") @Min(value = 1, message = "Page size must be at least 1") Integer pageSize) {

		logger.info("Request to get posts for userId: {}, pageNumber: {}, pageSize: {}", userId, pageNumber, pageSize);
		try {
			Pageable pageable = PageRequest.of(pageNumber, pageSize);
			List<Post> posts = postService.getPostsByUser(userId, pageable);
			if (posts.isEmpty()) {
				throw new ResourceNotFoundException("No posts found for user with ID: " + userId);
			}
			return ResponseEntity.ok(posts);
		} catch (ResourceNotFoundException e) {
			logger.warn("Resource not found: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Error occurred while retrieving posts: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving posts");
		}
	}
}
