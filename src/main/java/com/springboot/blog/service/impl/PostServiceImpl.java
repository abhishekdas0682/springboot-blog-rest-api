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
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;

/**
 * @author Abhishek Das
 *
 */
@Service
public class PostServiceImpl implements PostService{
	
	private PostRepository postRepository;
	
	private ObjectMapper objectMapper;

	@Autowired
	public PostServiceImpl(PostRepository postRepository, ObjectMapper objectMapper) {
		this.postRepository = postRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	public PostDto createPost(PostDto postDto) {
		// Covert DTO to entity
		Post post = mapToPostEntity(postDto);
		
		Post newPost = postRepository.save(post);
		
		// Convert entity to DTO
		PostDto postResponse = createPostDtoResponses(newPost);
		
		return postResponse;
	}

	private Post mapToPostEntity(PostDto postDto) {
		Post post = new Post();
		updatePostFromPostDto(postDto, post);
		return post;
	}

	private void updatePostFromPostDto(PostDto postDto, Post post) {
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
	}

	private PostDto createPostDtoResponses(Post newPost) {
		PostDto postResponse = new PostDto();
		postResponse.setId(newPost.getId());
		postResponse.setTitle(newPost.getTitle());
		postResponse.setContent(newPost.getContent());
		postResponse.setDescription(newPost.getDescription());
		return postResponse;
	}

	@Override
	public List<PostDto> getAllPosts() {
		List<Post> posts = postRepository.findAll();
		List<PostDto> postDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(posts)) {
			posts.forEach(post -> {
				PostDto postResponse = createPostDtoResponses(post);
				postDtos.add(postResponse);
			});
		}
		// One line return using map
		return posts.stream().map(post -> createPostDtoResponses(post)).collect(Collectors.toList());
//		return postDtos;
	}

	@Override
	public PostDto getPostById(long id) {
		Post post = postRepository.findById(id);
		if (Objects.isNull(post)) {
			throw new ResourceNotFoundException(AppConstants.POST, AppConstants.ID, id);
		}
		PostDto postResponse = createPostDtoResponses(post);
		
		return postResponse;
	}

	@Override
	public PostDto updatePost(PostDto postDto, long id) {
		Post post = postRepository.findById(id);
		if (Objects.isNull(post)) {
			throw new ResourceNotFoundException(AppConstants.POST, AppConstants.ID, id);
		}
		updatePostFromPostDto(postDto, post);
		postRepository.save(post);
		PostDto postResponse = createPostDtoResponses(post);

		return postResponse;
	}

	@Override
	public void deletePostById(long id) {
		Post post = postRepository.findById(id);
		if (Objects.isNull(post)) {
			throw new ResourceNotFoundException(AppConstants.POST, AppConstants.ID, id);
		}
		postRepository.delete(post);
		
	}

	@Override
	public PostResponse getAllPostsWithPagination(int pageNo, int pageSize, String sortBy, String sortDir) {
		
		var sortParams = List.of("title", "description", "content");
		if (Objects.nonNull(sortBy) && !sortParams.contains(sortBy)) {
			sortBy = "id";
		}
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<Post> posts = postRepository.findAll(pageable);

		// get content for pageable object
		List<Post> listOfPosts = posts.getContent();

		List<PostDto> content = listOfPosts.stream().map(post -> createPostDtoResponses(post)).collect(Collectors.toList());
		PostResponse postResp = new PostResponse();
		postResp.setContent(content);
		postResp.setPageNo(posts.getNumber());
		postResp.setPageSize(posts.getSize());
		postResp.setSortedBy(sortBy);
		postResp.setSortedDirection(sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? "Descending" : "Ascending");
		postResp.setTotalElements(posts.getTotalElements());
		postResp.setTotalPages(posts.getTotalPages());
		postResp.setLast(posts.isLast());
		
		return postResp;
	}

}
