package com.intuit.comments.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intuit.comments.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    Page<Comment> findByParentIdOrderByCreatedAtDesc(Long parentId, Pageable pageable);
    
    @Query("SELECT c, (SELECT COUNT(cr) FROM CommentReaction cr WHERE cr.comment = c AND cr.type = 'LIKE') - " +
            "(SELECT COUNT(cr) FROM CommentReaction cr WHERE cr.comment = c AND cr.type = 'DISLIKE') as netLikes " +
            "FROM Comment c " +
            "WHERE c.post.id = :postId " +
            "ORDER BY netLikes DESC")
     Page<Object[]> findTopCommentsByPost(@Param("postId") Long postId, Pageable pageable);

     @Query("SELECT cr.comment, COUNT(cr.id) as likeCount FROM CommentReaction cr " +
            "WHERE cr.type = 'LIKE' " +  // Enum value as a string
            "GROUP BY cr.comment " +
            "ORDER BY likeCount DESC")
     List<Object[]> findTopLikedComments(Pageable pageable);

     @Query("SELECT cr.comment, COUNT(cr.id) as dislikeCount FROM CommentReaction cr " +
            "WHERE cr.type = 'DISLIKE' " +  // Enum value as a string
            "GROUP BY cr.comment " +
            "ORDER BY dislikeCount DESC")
     List<Object[]> findTopDislikedComments(Pageable pageable);

}
