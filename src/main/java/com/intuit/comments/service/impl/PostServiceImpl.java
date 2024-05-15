package com.intuit.comments.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.intuit.comments.dto.PostDTO;
import com.intuit.comments.entity.Post;
import com.intuit.comments.exceptions.UserNotFoundException;
import com.intuit.comments.repo.PostRepository;
import com.intuit.comments.repo.UserRepository;
import com.intuit.comments.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	@Autowired
	PostRepository postRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public Post createPost(PostDTO postDTO) {
		validatePostDTO(postDTO);
		Post newPost = postDtoToPost(postDTO);

		return userRepository.findById(postDTO.getUserId()).map(user -> {
			newPost.setUser(user);
			return postRepository.save(newPost);
		}).orElseThrow(() -> {
			logger.error("User not found with ID: {}", postDTO.getUserId());
			return new UserNotFoundException("User not found with ID: " + postDTO.getUserId());
		});
	}
	
	@Override
	public List<Post> getPostsByUser(Long userId, Pageable pageable) {
	    logger.info("Fetching posts for user with ID: {} and page number: {}", userId, pageable.getPageNumber());
	    
	    try {
	        // Ensure pageNumber starts from 0
	        int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize(); // Adjust if pageNumber starts at 1
	        int limit = pageable.getPageSize();
	        
	        List<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, offset, limit);
	        logger.info("Successfully fetched {} posts for user with ID: {}", posts.size(), userId);
	        return posts;
	    } catch (Exception e) {
	        logger.error("An error occurred while fetching posts for user with ID: {}", userId, e);
	        throw new RuntimeException("Failed to fetch posts for user with ID: " + userId, e);
	    }
	}

	private Post postDtoToPost(PostDTO postDTO) {
		Post post = new Post();
		post.setContent(postDTO.getContent());
		post.setId(postDTO.getId());
		post.setTitle(postDTO.getTitle());
		post.setUser(post.getUser());
		return post;
	}

	private void validatePostDTO(PostDTO postDTO) {
		if (postDTO == null) {
			throw new IllegalArgumentException("PostDTO cannot be null");
		}
		if (postDTO.getUserId() == null) {
			throw new IllegalArgumentException("UserId cannot be null");
		}
		if (!StringUtils.hasText(postDTO.getTitle())) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		if (!StringUtils.hasText(postDTO.getContent())) {
			throw new IllegalArgumentException("Content cannot be empty");
		}
	}

}
