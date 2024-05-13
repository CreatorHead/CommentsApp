package com.intuit.comments.service;


import java.util.List;

import org.springframework.data.domain.Pageable;

import com.intuit.comments.dto.CommentDTO;
import com.intuit.comments.entity.Comment;

public interface CommentService {
	
    Comment addComment(CommentDTO commentDTO);
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    List<Comment> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable);

}
