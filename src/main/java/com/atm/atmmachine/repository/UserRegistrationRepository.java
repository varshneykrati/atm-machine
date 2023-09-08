package com.atm.atmmachine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, String>{

	//admin
	//public Optional<UserRegistration> findByUserId(String id);
	
	public List<UserRegistration> findByUserRegistrationApproval(UserRegistrationApproval userRegistrationApproval); 

}
