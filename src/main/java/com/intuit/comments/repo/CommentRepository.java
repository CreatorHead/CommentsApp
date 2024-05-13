package com.intuit.comments.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intuit.comments.entity.Comment;

/**
 * Repository interface for handling database operations related to
 * {@link Comment} entities. Extends JpaRepository to leverage Spring Data JPA
 * functionalities for CRUD operations and pagination.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	/**
	 * Retrieves a paginated list of comments associated with a specific post,
	 * sorted by creation time in descending order.
	 * 
	 * @param postId   The ID of the post for which comments are to be retrieved.
	 * @param pageable Pagination and sorting information.
	 * @return Page of {@link Comment} objects.
	 */
	Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

	/**
	 * Retrieves a paginated list of child comments associated with a specific
	 * parent comment, sorted by creation time in descending order.
	 * 
	 * @param parentId The ID of the parent comment for which child comments are to
	 *                 be retrieved.
	 * @param pageable Pagination and sorting information.
	 * @return Page of {@link Comment} objects.
	 */
	Page<Comment> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable);

	/**
	 * Retrieves a paginated list of comments for a specific post sorted by net
	 * likes (likes minus dislikes). This method utilizes a custom query with
	 * subqueries to calculate the net likes for each comment.
	 * 
	 * @param postId   The ID of the post for which comments are to be ranked by net
	 *                 likes.
	 * @param pageable Pagination and sorting information.
	 * @return Page of arrays with {@link Comment} objects and their respective net
	 *         like count.
	 */
	@Query("SELECT c, (SELECT COUNT(cr) FROM CommentReaction cr WHERE cr.comment = c AND cr.type = 'LIKE') - "
			+ "(SELECT COUNT(cr) FROM CommentReaction cr WHERE cr.comment = c AND cr.type = 'DISLIKE') as netLikes "
			+ "FROM Comment c " + "WHERE c.post.id = :postId " + "ORDER BY netLikes DESC")
	Page<Object[]> findTopCommentsByPost(@Param("postId") Long postId, Pageable pageable);

	/**
	 * Retrieves a list of comments with the highest number of 'LIKE' reactions,
	 * sorted by like count in descending order.
	 * 
	 * @param pageable Pagination information.
	 * @return List of arrays containing {@link Comment} objects and their
	 *         respective like counts.
	 */
	@Query("SELECT cr.comment, COUNT(cr.id) as likeCount FROM CommentReaction cr " + "WHERE cr.type = 'LIKE' " +

			"GROUP BY cr.comment " + "ORDER BY likeCount DESC")
	List<Object[]> findTopLikedComments(Pageable pageable);

	/**
	 * Retrieves a list of comments with the highest number of 'DISLIKE' reactions,
	 * sorted by dislike count in descending order.
	 * 
	 * @param pageable Pagination information.
	 * @return List of arrays containing {@link Comment} objects and their
	 *         respective dislike counts.
	 */
	@Query("SELECT cr.comment, COUNT(cr.id) as dislikeCount FROM CommentReaction cr " + "WHERE cr.type = 'DISLIKE' " +

			"GROUP BY cr.comment " + "ORDER BY dislikeCount DESC")
	List<Object[]> findTopDislikedComments(Pageable pageable);

}
