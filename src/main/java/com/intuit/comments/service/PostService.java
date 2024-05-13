package com.intuit.comments.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.intuit.comments.dto.PostDTO;
import com.intuit.comments.entity.Post;


public interface PostService {
	Post createPost(PostDTO postDTO);
	public List<Post> getPostsByUser(Long userId, Pageable pageable);
}
