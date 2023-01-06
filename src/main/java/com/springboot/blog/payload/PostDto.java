/**
 * 
 */
package com.springboot.blog.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * @author Abhishek Das
 *
 */
@Data
public class PostDto {
	
	private Long id;
	@NotEmpty
	@Size(min = 2, message = "Should have more than 2 characters")
	private String title;
	@NotEmpty
	@Size(min = 5, message = "Should have more than 5 characters")
	private String description;
	@NotEmpty
	private String content;
}
