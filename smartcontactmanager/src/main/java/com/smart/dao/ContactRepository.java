package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	// pagination method... we made this repository for pagination 
	
//	@Query("from Contact as c where c.user.id =:userId")
//	public List<Contact> findContactsByUser(@Param("userId")int userId);
// i am making this custom method to find the contacts by logged in user id
	
	// we will give the user id and this method will return the data of that user
	
//	--------------------------
	
	@Query("from Contact as c where c.user.id =:userId")
	// CurrentPage -
	//Contact per page-5
	public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pageable);
	//we use page for pagination
	
	
	//for search
	public List<Contact> findByNameContainingAndUser(String name, User user);

	
}
