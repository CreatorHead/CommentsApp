package com.intuit.comments.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intuit.comments.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	

	@Query(value = "SELECT * FROM posts WHERE user_id = :userId ORDER BY created_at DESC OFFSET :offset ROWS FETCH FIRST :limit ROWS ONLY", 
	           nativeQuery = true)
	    List<Post> findByUserIdOrderByCreatedAtDesc(
	        @Param("userId") Long userId,
	        @Param("offset") Integer offset,
	        @Param("limit") Integer limit);
	
	// Query to find top liked posts with pagination
    @Query(value = "SELECT p.*, COUNT(r.id) as reaction_count FROM posts p " +
                   "LEFT JOIN post_reactions r ON p.id = r.post_id AND r.type = 'like' " +
                   "GROUP BY p.id ORDER BY reaction_count DESC, p.created_at DESC",
           countQuery = "SELECT count(DISTINCT p.id) FROM posts p " +
                        "LEFT JOIN post_reactions r ON p.id = r.post_id AND r.type = 'like'",
           nativeQuery = true)
    Page<Post> findTopLikedPosts(Pageable pageable);

    // Query to find top disliked posts with pagination
    @Query(value = "SELECT p.*, COUNT(r.id) as reaction_count FROM posts p " +
                   "LEFT JOIN post_reactions r ON p.id = r.post_id AND r.type = 'dislike' " +
                   "GROUP BY p.id ORDER BY reaction_count DESC, p.created_at DESC",
           countQuery = "SELECT count(DISTINCT p.id) FROM posts p " +
                        "LEFT JOIN post_reactions r ON p.id = r.post_id AND r.type = 'dislike'",
           nativeQuery = true)
    Page<Post> findTopDislikedPosts(Pageable pageable);
	
}
