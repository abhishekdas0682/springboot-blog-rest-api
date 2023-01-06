/**
 * 
 */
package com.springboot.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blog.entity.Role;

/**
 * @author Abhishek Das
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByName(final String name);
}
