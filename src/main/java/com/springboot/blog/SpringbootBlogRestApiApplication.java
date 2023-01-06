package com.springboot.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class SpringbootBlogRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}

}
