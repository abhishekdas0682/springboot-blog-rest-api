/**
 * 
 */
package com.springboot.blog.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Entity
@Table(name ="comments")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;
	private String email;
	private String body;
	
	@ManyToOne(fetch = FetchType.LAZY)					// The FetchType.LAZY tells Hibernate to only fetch the related entities from database when you use the relationship
	@JoinColumn(name = "post_id", nullable = false)		// It is used to specify a foreign key, i.e "post_id"
	private Post post;
}
