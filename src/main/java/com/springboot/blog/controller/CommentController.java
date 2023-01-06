/**
 * 
 */
package com.springboot.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.utils.AppConstants;
import com.springboot.blog.utils.CustomError;

/**
 * @author Abhishek Das
 *
 */
@RestController
@RequestMapping(value = "/api")
public class CommentController {

	private CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping(value = "/posts/{postId}/comments")
	public ResponseEntity<Object> createComment(@PathVariable("postId") long postId,
			@RequestBody CommentDto commentDto) {
		try {
			return new ResponseEntity<Object>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
		} catch (Exception ex) {
			CustomError customError = new CustomError(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(customError);
		}
	}
	
	@GetMapping(value = "/posts/{postId}/getAllComments")
	public ResponseEntity<Object> getCommentsByPostId(@PathVariable("postId") long postId) {
		return new ResponseEntity<Object>(commentService.getCommentsByPostId(postId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/posts/{postId}/getComments")
	public ResponseEntity<Object> getCommentsByPostIdAndWithPagination(@PathVariable("postId") long postId,
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
		try {
			return new ResponseEntity<Object>(
					commentService.getAllCommentsWithPagination(postId, pageNo, pageSize, sortBy, sortDir),
					HttpStatus.OK);
		} catch (Exception ex) {
			CustomError customError = new CustomError(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(customError);
		}
	}
	
	@GetMapping(value = "/posts/{postId}/comments/{id}")
	public ResponseEntity<Object> getCommentByPostId(@PathVariable("postId") long postId, @PathVariable("id") long id) {
		try {
			return new ResponseEntity<Object>(commentService.getCommentById(postId, id), HttpStatus.OK);
		} catch (Exception ex) {
			CustomError customError = new CustomError(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(customError);
		}
	}
	
	@PutMapping(value = "/posts/{postId}/comments/{id}")
	public ResponseEntity<Object> updateComment(@PathVariable("postId") long postId, @PathVariable("id") long id,
			@RequestBody CommentDto commentDto) {
		try {
			return new ResponseEntity<Object>(commentService.updateComment(postId, id, commentDto), HttpStatus.ACCEPTED);
		} catch (Exception ex) {
			CustomError customError = new CustomError(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(customError);
		}
	}
	
	@DeleteMapping(value = "/posts/{postId}/comments/{id}")
	public ResponseEntity<Object> deleteComment(@PathVariable("postId") long postId, @PathVariable("id") long id) {
		try {
			commentService.deleteComment(postId, id);
			return new ResponseEntity<Object>("Comment Deleted Successfully", HttpStatus.OK);
		} catch (Exception ex) {
			CustomError customError = new CustomError(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(customError);
		}
	}
}
