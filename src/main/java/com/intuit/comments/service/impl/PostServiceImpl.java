package com.intuit.comments.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.intuit.comments.dto.PostDTO;
import com.intuit.comments.entity.Post;
import com.intuit.comments.repo.PostRepository;
import com.intuit.comments.repo.UserRepository;
import com.intuit.comments.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public Post createPost(PostDTO postDTO) {
		Post newPost = postDtoToPost(postDTO);
		return userRepository.findById(postDTO.getUserId()).map(user -> {
			newPost.setUser(user);
			return postRepository.save(newPost);
		}).orElseThrow(() -> new RuntimeException("User not found with ID: " + postDTO.getUserId()));
	}

	private Post postDtoToPost(PostDTO postDTO) {
		Post post = new Post();
		post.setContent(postDTO.getContent());
		post.setId(postDTO.getId());
		post.setTitle(postDTO.getTitle());
		post.setUser(post.getUser());
		return post;
	}

	public List<Post> getPostsByUser(Long userId, Pageable pageable) {
		// Ensure pageNumber starts from 0
		int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize(); // Adjust if pageNumber starts at 1
		int limit = pageable.getPageSize();
		List<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, offset, limit);
		return posts;
	}

}
