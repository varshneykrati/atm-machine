package com.atm.atmmachine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.entity.UserRequest.RequestStatus;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, String>{

	//admin
	public List<UserRequest> findAll();
	
	public List<UserRequest> findByRequestStatus(RequestStatus status);
	
	public List<UserRequest> findByRequest(String request);
	
	public Optional<UserRequest> findByRequestId(String id);
	
	

}
