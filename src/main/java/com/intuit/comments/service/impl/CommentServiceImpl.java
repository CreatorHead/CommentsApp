package com.intuit.comments.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.intuit.comments.dto.CommentDTO;
import com.intuit.comments.entity.Comment;
import com.intuit.comments.repo.CommentRepository;
import com.intuit.comments.repo.PostRepository;
import com.intuit.comments.repo.UserRepository;
import com.intuit.comments.service.CommentService;


@Service 
public class CommentServiceImpl implements CommentService {
	
	@Autowired CommentRepository commentRepository;
	@Autowired PostRepository postRepository;
	@Autowired UserRepository userRepository;
	
	
	@Override 
	public Comment addComment(CommentDTO commentDTO) {
		Comment newComment = commentDtoToComment(commentDTO);
		System.out.println(newComment);
		return postRepository.findById(commentDTO.getPostId()).map(post -> {
			newComment.setPost(post);
            return commentRepository.save(newComment);
        }).orElseThrow(() -> new RuntimeException("User not found with ID: " + commentDTO.getPostId()));
	}	
	
	private Comment commentDtoToComment(CommentDTO commentDTO) {
		Comment comment = new Comment();
		comment.setContent(commentDTO.getContent());
		
		if(commentDTO.getParentId() != null) {
			comment.setParent(commentRepository.findById(commentDTO.getParentId()).orElse(null));
		}
		
		comment.setUser(userRepository.findById(commentDTO.getUserId()).orElse(null));
		return comment;
	}

	@Override
	public List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable) {
		return commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable).getContent();
	}

	@Override
	public List<Comment> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable) {
		return commentRepository.findByParentIdOrderByCreatedAtDesc(parentId, pageable).getContent();
	}

	
	

}
