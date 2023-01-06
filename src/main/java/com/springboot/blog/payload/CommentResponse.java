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
public class CommentResponse {

	private long totalElements;
	private String sortedBy;
	private String sortedDirection;
	private List<CommentDto> comments;
}
