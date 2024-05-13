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

/**
 * Handles requests related to comments on posts and replies. Provides endpoints
 * for adding comments, and retrieving recent or top comments and replies.
 */
@RestController
@RequestMapping("/api")
public class CommentController {

	@Autowired
	private CommentService commentService;

	/**
	 * Adds a new comment to a post or as a reply to another comment.
	 * 
	 * @param commentDTO Data transfer object containing comment details.
	 * @return ResponseEntity containing the added comment or an error message.
	 */
	@PostMapping("/comments")
	public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO) {
		return ResponseEntity.ok(commentService.addComment(commentDTO));
	}

	/**
	 * Retrieves the most recent comments for a specific post, sorted by creation
	 * date in descending order.
	 * 
	 * @param postId   The ID of the post for which comments are requested.
	 * @param pageable Pagination information.
	 * @return ResponseEntity containing a page of recent comments or an error
	 *         message.
	 */
	@GetMapping("/comments/recent/{postId}")
	public ResponseEntity<?> findByPostIdWithRecentComments(@PathVariable("postId") Long postId, Pageable pageable) {
		return ResponseEntity.ok(commentService.findByPostIdOrderByCreatedAtDesc(postId, pageable));
	}

	/**
	 * Retrieves the most recent replies for a specific comment, sorted by creation
	 * date in descending order.
	 * 
	 * @param parentId The ID of the parent comment for which replies are requested.
	 * @param pageable Pagination information.
	 * @return ResponseEntity containing a page of recent replies or an error
	 *         message.
	 */
	@GetMapping("/comments/recent/replies/{parentId}")
	public ResponseEntity<?> findByParentIdWithRecentReplies(@PathVariable("parentId") Long parentId,
			Pageable pageable) {
		return ResponseEntity.ok(commentService.findByParentIdOrderByCreatedAtDesc(parentId, pageable));
	}

	/**
	 * Retrieves the top comments for a specific post, typically based on criteria
	 * such as number of likes or replies. Note: This endpoint currently behaves the
	 * same as findByPostIdWithRecentComments.
	 * 
	 * @param postId   The ID of the post for which top comments are requested.
	 * @param pageable Pagination information.
	 * @return ResponseEntity containing a page of top comments or an error message.
	 */
	@GetMapping("/comments/top/{postId}")
	public ResponseEntity<?> findByPostIdWithTopComments(@PathVariable("postId") Long postId, Pageable pageable) {
		return ResponseEntity.ok(commentService.findByPostIdOrderByCreatedAtDesc(postId, pageable));
	}

	/**
	 * Retrieves the top replies for a specific parent comment, typically based on
	 * criteria such as number of likes or replies. Note: This endpoint currently
	 * behaves the same as findByParentIdWithRecentReplies.
	 * 
	 * @param parentId The ID of the parent comment for which top replies are
	 *                 requested.
	 * @param pageable Pagination information.
	 * @return ResponseEntity containing a page of top replies or an error message.
	 */
	@GetMapping("/comments/top/replies/{parentId}")
	public ResponseEntity<?> findByParentIdWithTopReplies(@PathVariable("parentId") Long parentId, Pageable pageable) {
		return ResponseEntity.ok(commentService.findByParentIdOrderByCreatedAtDesc(parentId, pageable));
	}

}
