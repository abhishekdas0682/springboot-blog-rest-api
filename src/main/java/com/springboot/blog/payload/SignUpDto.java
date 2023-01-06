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
public class SignUpDto {
	private String name;
	private String username;
	private String email;
	private String password;

}
