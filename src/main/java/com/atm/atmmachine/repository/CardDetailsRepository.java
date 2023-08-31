package com.atm.atmmachine.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.UserRegistration;

@Repository
public interface CardDetailsRepository extends JpaRepository<CardDetails, String>{

	Optional<CardDetails> findByAccountNumber(BigInteger toAccountNumber);


	

}
