/**
 * 
 */
package com.springboot.blog.utils;

import lombok.Data;

/**
 * @author Abhishek Das
 *
 */
@Data
public class CustomError {

	private boolean successful;
	private String message;
	public CustomError(String message) {
		this.message = message;
		this.successful = false;
	}
}
