/**
 * 
 */
package com.springboot.blog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.CommentResponse;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.utils.AppConstants;

/**
 * @author Abhishek Das
 *
 */
@Service
public class CommentServiceImpl implements CommentService{

	private CommentRepository commentRepository;
	private PostRepository postRepository;

	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
	}

	@Override
	public CommentDto createComment(long postId, CommentDto commentDto) {
		// Convert DTO to entity
		Comment comment = mapToCommentEntity(commentDto);
		
		// retrieve post entity by id
		Post post = postRepository.findById(postId);
		if (Objects.isNull(post)) {
			throw new ResourceNotFoundException(AppConstants.POST, AppConstants.ID, postId);
		}
		// set post to comment entity
		comment.setPost(post);
		Comment newComment = commentRepository.save(comment);

		// Convert entity to DTO
		return createCommentDtoResponses(newComment);
	}

	/**
	 * @param newComment
	 * @return
	 */
	private CommentDto createCommentDtoResponses(Comment newComment) {
		CommentDto commentResp = new CommentDto();
		commentResp.setId(newComment.getId());
		commentResp.setName(newComment.getName());
		commentResp.setEmail(newComment.getEmail());
		commentResp.setBody(newComment.getBody());
		return commentResp;
	}

	/**
	 * @param commentDto
	 * @return
	 */
	private Comment mapToCommentEntity(CommentDto commentDto) {
		Comment comment = new Comment();
		updateCommentFromCommentDto(commentDto, comment);
		return comment;
	}

	@Override
	public List<CommentDto> getCommentsByPostId(long postId) {

		List<Comment> comments = commentRepository.findByPostId(postId);
		return comments.stream().map(comment -> createCommentDtoResponses(comment)).collect(Collectors.toList());
	}

	@Override
	public CommentDto getCommentById(long postId, long id) {
		// retrieve post entity by id
		Post post = postRepository.findById(postId);
		if (Objects.isNull(post)) {
			throw new ResourceNotFoundException(AppConstants.POST, AppConstants.ID, postId);
		}
		Comment comment = commentRepository.findByPostIdAndId(postId, id);
		if (Objects.isNull(comment)) {
			throw new ResourceNotFoundException("Comment", "id", id);
		}

		return createCommentDtoResponses(comment);
	}

	@Override
	public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
		Post post = postRepository.findById(postId);
		if (Objects.isNull(post)) {
			throw new ResourceNotFoundException(AppConstants.POST, AppConstants.ID, postId);
		}
		Comment comment = commentRepository.findByPostIdAndId(postId, id);
		if (Objects.isNull(comment)) {
			throw new ResourceNotFoundException("Comment", "id", id);
		}
		updateCommentFromCommentDto(commentDto, comment);
		
		commentRepository.save(comment);
		
		return createCommentDtoResponses(comment);
	}

	/**
	 * @param commentDto
	 * @param comment
	 */
	private void updateCommentFromCommentDto(CommentDto commentDto, Comment comment) {
		comment.setName(commentDto.getName());
		comment.setEmail(commentDto.getEmail());
		comment.setBody(commentDto.getBody());
	}

	@Override
	public CommentResponse getAllCommentsWithPagination(long postId, int pageNo, int pageSize, String sortBy, String sortDir) {
		var sortParams = List.of("name", "email", "body");
		if (Objects.nonNull(sortBy) && !sortParams.contains(sortBy)) {
			sortBy = "id";
		}
		Post post = postRepository.findById(postId);
		if (Objects.isNull(post)) {
			throw new ResourceNotFoundException(AppConstants.POST, AppConstants.ID, postId);
		}
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		
		Page<Comment> allComments = commentRepository.findAll(pageable);
		List<CommentDto> commentDtos = new ArrayList<>();
		allComments.getContent().forEach(comment -> {
			if (postId == comment.getPost().getId()) {
				commentDtos.add(createCommentDtoResponses(comment));
			}
		});
		
		CommentResponse commentResp = new CommentResponse();
		commentResp.setComments(commentDtos);
		commentResp.setSortedBy(sortBy);
		commentResp.setSortedDirection(sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? "Descending" : "Ascending");
		commentResp.setTotalElements(commentDtos.size());
		
		return commentResp;
	}

	@Override
	public void deleteComment(long postId, long id) {
		Post post = postRepository.findById(postId);
		if (Objects.isNull(post)) {
			throw new ResourceNotFoundException(AppConstants.POST, AppConstants.ID, postId);
		}
		Comment comment = commentRepository.findByPostIdAndId(postId, id);
		if (Objects.isNull(comment)) {
			throw new ResourceNotFoundException("Comment", "id", id);
		}
		commentRepository.delete(comment);
		
	}

}
