package com.atm.atmmachine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.atm.atmmachine.dto.EmailDto;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, String>{

	Optional<UserRegistration> findByEmailId(String userId);

	Optional<UserRegistration> findByPhoneNo(String phoneNo);

	Optional<UserRegistration> findByAadharNumber(Long aadharNumber);

	
	Optional<UserRegistration> findByUserIdAndEmailId(String string, String email);
	
	public List<UserRegistration> findByUserRegistrationApproval(UserRegistrationApproval userRegistrationApproval);

//	Optional<UserRegistration> findByUserIdAndPhoneNo(String userId, String phoneNo); 

}
