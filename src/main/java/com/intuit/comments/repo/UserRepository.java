package com.intuit.comments.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intuit.comments.entity.User;


/**
 * Repository for managing {@link User} entities.
 * Extends {@link JpaRepository} to provide standard CRUD operations on User entities.
 * Utilizes Spring's {@link Repository} annotation for automatic detection during component scanning.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}