/**
 * 
 */
package com.springboot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blog.entity.Post;

/**
 * @author Abhishek Das
 *
 */
public interface PostRepository extends JpaRepository<Post, Long>{
	
	public Post findById(final long id);
}
