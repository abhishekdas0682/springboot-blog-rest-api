/**
 * 
 */
package com.springboot.blog.payload;

import lombok.Data;

/**
 * @author Abhishek Das
 *
 */
@Data
public class CommentDto {

	private Long id;
	private String name;
	private String email;
	private String body;
}
