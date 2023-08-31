package com.atm.atmmachine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRequest;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, String>{

	List<UserRequest> findAllByUserRegistration(UserRegistration userId);

//	List<UserRequest> findByuser_id(String user_id);
//	
}
