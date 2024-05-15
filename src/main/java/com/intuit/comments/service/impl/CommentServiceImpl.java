package com.intuit.comments.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.intuit.comments.dto.CommentDTO;
import com.intuit.comments.entity.Comment;
import com.intuit.comments.exceptions.CommentValidationException;
import com.intuit.comments.exceptions.PostNotFoundException;
import com.intuit.comments.exceptions.UserNotFoundException;
import com.intuit.comments.repo.CommentRepository;
import com.intuit.comments.repo.PostRepository;
import com.intuit.comments.repo.UserRepository;
import com.intuit.comments.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

	@Autowired
	CommentRepository commentRepository;
	@Autowired
	PostRepository postRepository;
	@Autowired
	UserRepository userRepository;

	@Transactional
	@Override
	public Comment addComment(CommentDTO commentDTO) {
		validateCommentDTO(commentDTO);
		Comment newComment = commentDtoToComment(commentDTO);
		logger.debug("Adding comment: {}", newComment);

		return postRepository.findById(commentDTO.getPostId()).map(post -> {
			newComment.setPost(post);
			return commentRepository.save(newComment);
		}).orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + commentDTO.getPostId()));
	}

	@Override
	public List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable) {
	    try {
	        logger.debug("Finding comments for post ID: {} with pageable: {}", postId, pageable);
	        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable).getContent();
	        if (comments.isEmpty()) {
	            logger.warn("No comments found for post ID: {}", postId);
	        }
	        return comments;
	    } catch (Exception e) {
	        logger.error("Error occurred while finding comments for post ID: {}", postId, e);
	        throw new RuntimeException("Error occurred while finding comments for post ID: " + postId, e);
	    }
	}

	@Override
	public List<Comment> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable) {
	    try {
	        logger.debug("Finding comments for parent comment ID: {} with pageable: {}", parentId, pageable);
	        List<Comment> comments = commentRepository.findByParentIdOrderByCreatedAtDesc(parentId, pageable).getContent();
	        if (comments.isEmpty()) {
	            logger.warn("No comments found for parent comment ID: {}", parentId);
	        }
	        return comments;
	    } catch (Exception e) {
	        logger.error("Error occurred while finding comments for parent comment ID: {}", parentId, e);
	        throw new RuntimeException("Error occurred while finding comments for parent comment ID: " + parentId, e);
	    }
	}

	private Comment commentDtoToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());

        if (commentDTO.getParentId() != null) {
            comment.setParent(commentRepository.findById(commentDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with ID: " + commentDTO.getParentId())));
        }

        comment.setUser(userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + commentDTO.getUserId())));
        return comment;
    }

	private void validateCommentDTO(CommentDTO commentDTO) {
		if (commentDTO == null || !StringUtils.hasText(commentDTO.getContent())) {
			throw new CommentValidationException("Comment content must not be empty");
		}
		if (commentDTO.getUserId() == null) {
			throw new CommentValidationException("User ID must not be null");
		}
		if (commentDTO.getPostId() == null) {
			throw new CommentValidationException("Post ID must not be null");
		}
	}

}
