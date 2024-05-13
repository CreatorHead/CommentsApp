package com.intuit.comments.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intuit.comments.entity.Post;

/**
 * Repository for managing CRUD operations for {@link Post} entities.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	/**
	 * Retrieves a list of posts made by a specific user, sorted by creation date in
	 * descending order.
	 *
	 * @param userId The ID of the user whose posts are to be retrieved.
	 * @param offset The position of the first result to retrieve.
	 * @param limit  The maximum number of posts to retrieve.
	 * @return A list of {@link Post} objects.
	 */
	@Query(value = "SELECT * FROM posts WHERE user_id = :userId ORDER BY created_at DESC OFFSET :offset ROWS FETCH FIRST :limit ROWS ONLY", nativeQuery = true)
	List<Post> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	/**
	 * Finds the most liked posts using pagination. This includes a count of likes
	 * for sorting purposes.
	 *
	 * @param pageable The pagination information.
	 * @return A {@link Page} of {@link Post} objects, sorted by the number of likes
	 *         in descending order.
	 */
	@Query(value = "SELECT p.*, COUNT(r.id) as reaction_count FROM posts p "
			+ "LEFT JOIN post_reactions r ON p.id = r.post_id AND r.type = 'like' "
			+ "GROUP BY p.id ORDER BY reaction_count DESC, p.created_at DESC", countQuery = "SELECT count(DISTINCT p.id) FROM posts p "
					+ "LEFT JOIN post_reactions r ON p.id = r.post_id AND r.type = 'like'", nativeQuery = true)
	Page<Post> findTopLikedPosts(Pageable pageable);

	/**
	 * Finds the most disliked posts using pagination. This includes a count of
	 * dislikes for sorting purposes.
	 *
	 * @param pageable The pagination information.
	 * @return A {@link Page} of {@link Post} objects, sorted by the number of
	 *         dislikes in descending order.
	 */
	@Query(value = "SELECT p.*, COUNT(r.id) as reaction_count FROM posts p "
			+ "LEFT JOIN post_reactions r ON p.id = r.post_id AND r.type = 'dislike' "
			+ "GROUP BY p.id ORDER BY reaction_count DESC, p.created_at DESC", countQuery = "SELECT count(DISTINCT p.id) FROM posts p "
					+ "LEFT JOIN post_reactions r ON p.id = r.post_id AND r.type = 'dislike'", nativeQuery = true)
	Page<Post> findTopDislikedPosts(Pageable pageable);

	/**
     * Retrieves the top posts based on activity, which includes reactions and comments.
     * Posts are sorted by the total number of reactions, the time of the latest comment, and the post creation date.
     *
     * @param offset The position of the first result to retrieve.
     * @param limit The maximum number of posts to retrieve.
     * @return A list of {@link Post} objects.
     */
	@Query(value = "SELECT p.*, " + "COUNT(distinct pr.id) as reaction_count, "
			+ "COUNT(distinct c.id) as comment_count, " + "MAX(c.created_at) as latest_comment_time " + "FROM posts p "
			+ "LEFT JOIN post_reactions pr ON p.id = pr.post_id " + "LEFT JOIN comments c ON p.id = c.post_id "
			+ "GROUP BY p.id " + "ORDER BY reaction_count DESC, latest_comment_time DESC, p.created_at DESC "
			+ "OFFSET :offset ROWS FETCH FIRST :limit ROWS ONLY", nativeQuery = true)
	List<Post> findTopPostsByActivity(@Param("offset") Integer offset, @Param("limit") Integer limit);

}
