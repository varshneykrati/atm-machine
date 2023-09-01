package com.atm.atmmachine.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.dto.CardLimit.CardTypeInCardLimit;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.CardDetails.CardType;

@Repository
public interface CardDetailsRepository extends JpaRepository<CardDetails, String>{

	Optional<CardDetails> findByAccountNumber(BigInteger toAccountNumber);

	//admin
	List<CardDetails> findByCardType(CardType cardType);
	
}



