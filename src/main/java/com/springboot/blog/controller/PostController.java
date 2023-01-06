/**
 * 
 */
package com.springboot.blog.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import com.springboot.blog.utils.CustomError;

/**
 * @author Abhishek Das
 *
 */
@RestController
@RequestMapping("api/posts")
public class PostController {
	
	private PostService postService;

	/**
	 * @param postService
	 */
	public PostController(PostService postService) {
		this.postService = postService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	// create blog post
	@PostMapping(value = "/createPost")
	public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto) {
		try {
			return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
		} catch (Exception ex) {
			CustomError customError = new CustomError(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(customError);
		}
	}
	
	@GetMapping(value = "/getAllPosts")
	public ResponseEntity<List<PostDto>> getAllPosts() {
		return new ResponseEntity<List<PostDto>>(postService.getAllPosts(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getPosts")
	public ResponseEntity<Object> getAllPostsWithPagination(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {

		return new ResponseEntity<Object>(postService.getAllPostsWithPagination(pageNo, pageSize, sortBy, sortDir),
				HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> getPostById(@PathVariable("id") long id) {
		try {
			return new ResponseEntity<Object>(postService.getPostById(id), HttpStatus.OK);
		} catch (Exception ex) {
			CustomError customError = new CustomError(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(customError);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Object> updatePostById(@Valid @RequestBody PostDto postDto, @PathVariable("id") long id) {
		try {
			return new ResponseEntity<Object>(postService.updatePost(postDto, id), HttpStatus.ACCEPTED);
		} catch (Exception ex) {
			CustomError customError = new CustomError(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(customError);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> deletePostById(@PathVariable("id") long id) {
		try {
			postService.deletePostById(id);
			return new ResponseEntity<Object>("Post Deleted Successfully", HttpStatus.OK);
		} catch (Exception ex) {
			CustomError customError = new CustomError(ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
					.body(customError);
		}
	}
}
