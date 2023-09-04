package com.atm.atmmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.atm.atmmachine.dto.Email;
import com.atm.atmmachine.entity.UserRegistration;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, String>{

	Optional<UserRegistration> findByEmailId(String userId);

	Optional<UserRegistration> findByPhoneNo(String phoneNo);

	Optional<UserRegistration> findByAadharNumber(Long aadharNumber);

	Optional<UserRegistration> findByUserId(String userId);

	Optional<UserRegistration> findByUserIdAndEmailId(String string, Email email);

}
