/**
 * 
 */
package com.springboot.blog.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Abhishek Das
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
	
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private String sortedBy;
	private String sortedDirection;
	private boolean last;
	private List<PostDto> content;
}
