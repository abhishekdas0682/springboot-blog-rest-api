/**
 * 
 */
package com.springboot.blog.service;

import java.util.List;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.CommentResponse;

/**
 * @author Abhishek Das
 *
 */
public interface CommentService {

	CommentDto createComment(long postId, CommentDto commentDto);
	
	CommentDto getCommentById(long postId, long id);
	
	List<CommentDto> getCommentsByPostId(long postId);
	
	CommentResponse getAllCommentsWithPagination(long postId, int pageNo, int pageSize, String sortBy, String sortDir);
	
	CommentDto updateComment(long postId, long id, CommentDto commentDto);
	
	void deleteComment(long postId, long id);
}
