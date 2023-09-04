package com.atm.atmmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.UserRequest;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, String>{

	UserRequest findByRequestId(String req);

}
