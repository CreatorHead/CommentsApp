package com.intuit.comments.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.intuit.comments.dto.CommentDTO;
import com.intuit.comments.entity.Comment;
import com.intuit.comments.exceptions.CommentNotFoundException;
import com.intuit.comments.service.CommentService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;

/**
 * Handles requests related to comments on posts and replies. Provides endpoints
 * for adding comments, and retrieving recent or top comments and replies.
 */
@RestController
@RequestMapping("/api")
@Validated
public class CommentController {

	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private CommentService commentService;

	/**
	 * Adds a new comment to a post or as a reply to another comment.
	 * 
	 * @param commentDTO Data transfer object containing comment details.
	 * @return ResponseEntity containing the added comment or an error message.
	 */
	@PostMapping("/comments")
	public ResponseEntity<?> addComment(@Valid @RequestBody CommentDTO commentDTO) {
		logger.info("Adding a new comment: {}", commentDTO);
		try {
			Comment addedComment = commentService.addComment(commentDTO);
			return ResponseEntity.ok(addedComment);
		} catch (CommentNotFoundException e) {
			logger.error("Comment not found: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
		} catch (ValidationException e) {
			logger.error("Validation failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed: " + e.getMessage());
		} catch (Exception e) {
			logger.error("An error occurred while adding the comment: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
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
		logger.info("Fetching recent comments for post ID: {}", postId);

		// Validate postId
		if (postId == null || postId <= 0) {
			return new ResponseEntity<>("Invalid post ID", HttpStatus.BAD_REQUEST);
		}

		try {
			return ResponseEntity.ok(commentService.findByPostIdOrderByCreatedAtDesc(postId, pageable));
		} catch (EntityNotFoundException ex) {
			logger.error("Post not found: {}", postId, ex);
			return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			logger.error("An error occurred while fetching comments for post ID: {}", postId, ex);
			return new ResponseEntity<>("An internal error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
		logger.info("Fetching recent replies for parent comment ID: {}", parentId);

		// Validate parentId
		if (parentId == null || parentId <= 0) {
			logger.error("Invalid parent comment ID: {}", parentId);
			return ResponseEntity.badRequest().body("Invalid parent comment ID");
		}

		try {
			List<Comment> replies = commentService.findByParentIdOrderByCreatedAtDesc(parentId, pageable);
			return ResponseEntity.ok(replies);
		} catch (CommentNotFoundException e) {
			logger.error("Comment not found for ID: {}", parentId, e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parent comment not found");
		} catch (Exception e) {
			logger.error("Error fetching replies for parent comment ID: {}", parentId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching replies");
		}
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
	public ResponseEntity<?> findByPostIdWithTopComments(@PathVariable("postId") @NotNull Long postId,
			Pageable pageable) {
		logger.info("Fetching top comments for post ID: {}", postId);
		try {
			var comments = commentService.findByPostIdOrderByCreatedAtDesc(postId, pageable);
			return ResponseEntity.ok(comments);
		} catch (IllegalArgumentException e) {
			logger.error("Invalid postId: {}", postId, e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid postId", e);
		} catch (Exception e) {
			logger.error("Error fetching top comments for post ID: {}", postId, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching top comments", e);
		}
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
		try {
			logger.info("Fetching top replies for parent comment ID: {}", parentId);

			// Validate the parentId
			if (parentId == null || parentId <= 0) {
				logger.error("Invalid parent comment ID: {}", parentId);
				return ResponseEntity.badRequest().body("Invalid parent comment ID");
			}

			// Fetch the comments
			List<Comment> comments = commentService.findByParentIdOrderByCreatedAtDesc(parentId, pageable);

			// If no comments are found, return 404
			if (comments.isEmpty()) {
				logger.info("No comments found for parent comment ID: {}", parentId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No comments found");
			}

			return ResponseEntity.ok(comments);
		} catch (CommentNotFoundException e) {
			logger.error("Parent comment not found: {}", parentId, e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parent comment not found");
		} catch (DataAccessException e) {
			logger.error("Database error while fetching comments for parent ID: {}", parentId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
		} catch (Exception e) {
			logger.error("An unexpected error occurred", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
		}
	}

}
