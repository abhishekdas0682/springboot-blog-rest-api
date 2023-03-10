/**
 * 
 */
package com.springboot.blog.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;

/**
 * @author Abhishek Das
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/signin")
	public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
		}
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
		try {
			// add check for username exists in dB
			if (userRepository.existsByUsername(signUpDto.getUsername())) {
				return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
			}

			// add check for email exists in dB
			if (userRepository.existsByEmail(signUpDto.getEmail())) {
				return new ResponseEntity<>("Email already exists!", HttpStatus.BAD_REQUEST);
			}

			// create user object
			User user = new User();
			user.setName(signUpDto.getName());
			user.setUsername(signUpDto.getUsername());
			user.setEmail(signUpDto.getEmail());
			user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
			
			Role roles = roleRepository.findByName("ROLE_ADMIN").get();
			user.setRoles(Collections.singleton(roles));
			
			userRepository.save(user);
			return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
		}
	}

}
