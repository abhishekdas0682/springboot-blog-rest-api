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
public class LoginDto {
	private String usernameOrEmail;
	private String password;
}
