package com.atm.atmmachine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.UserRegistration;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, String>{

	Optional<UserRegistration> findByUserId(String userId);

}
