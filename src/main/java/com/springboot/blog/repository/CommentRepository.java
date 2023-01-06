/**
 * 
 */
package com.springboot.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blog.entity.Comment;

/**
 * @author Abhishek Das
 *
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

	public Comment findById(final long id);
	
	public List<Comment> findByPostId(final long postId);
	
	public Comment findByPostIdAndId(final long postId, final long id);

}
