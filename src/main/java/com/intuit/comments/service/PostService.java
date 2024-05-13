package com.intuit.comments.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.intuit.comments.dto.PostDTO;
import com.intuit.comments.entity.Post;

/**
 * Interface for managing post-related operations.
 * This service provides methods to create and retrieve posts by user.
 */
public interface PostService {
	
	/**
     * Creates a new post based on the provided PostDTO.
     *
     * @param postDTO The DTO containing the data for the new post. This includes the post content,
     *                and may include metadata such as the post's visibility and any tags associated with it.
     * @return The created Post entity, which now includes a generated ID and possibly other modifications
     *         made during the creation process such as timestamps.
     */
	Post createPost(PostDTO postDTO);

	
	/**
     * Retrieves a list of posts made by a specific user, with support for pagination.
     *
     * @param userId The ID of the user whose posts are to be retrieved.
     * @param pageable A Pageable object specifying the pagination information such as page number and size.
     *                 This is used to handle large sets of data by breaking the data set into manageable pages.
     * @return A list of Post entities that belong to the specified user, constrained by the pagination settings.
     */
	public List<Post> getPostsByUser(Long userId, Pageable pageable);
}
