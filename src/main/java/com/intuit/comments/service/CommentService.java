package com.intuit.comments.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.intuit.comments.dto.CommentDTO;
import com.intuit.comments.entity.Comment;

/**
 * Provides service-layer methods for managing comments on a social platform or
 * blog. This interface handles operations related to adding new comments and
 * retrieving existing comments based on various criteria such as post and
 * parent comment identifiers.
 */
public interface CommentService {

	/**
	 * Adds a new comment to the database.
	 *
	 * @param commentDTO the data transfer object containing comment details from
	 *                   the client
	 * @return the newly created Comment entity with generated identifiers and
	 *         timestamps
	 */
	Comment addComment(CommentDTO commentDTO);

	/**
	 * Retrieves a list of comments associated with a specific post, ordered by
	 * creation time in descending order.
	 *
	 * @param postId   the identifier of the post for which comments are being
	 *                 retrieved
	 * @param pageable pagination information including page number, page size, and
	 *                 sorting criteria
	 * @return a list of comments for the specified post, sorted by the 'createdAt'
	 *         timestamp in descending order
	 */
	List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

	/**
	 * Retrieves a list of comments that are replies to a specific parent comment,
	 * ordered by creation time in descending order.
	 *
	 * @param parentId the identifier of the parent comment for which replies are
	 *                 being retrieved
	 * @param pageable pagination information including page number, page size, and
	 *                 sorting criteria
	 * @return a list of reply comments for the specified parent, sorted by the
	 *         'createdAt' timestamp in descending order
	 */
	List<Comment> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable);

}
