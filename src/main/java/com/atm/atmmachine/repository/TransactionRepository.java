package com.atm.atmmachine.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.dto.TransactionDateInfo;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.TransactionDetails;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetails, String>{

	 List<TransactionDetails> findByTransactionDateAndCardDetails(LocalDate transactionDate,CardDetails cardDetails);
	
	 List<TransactionDetails> findByCardDetailsOrderByTransactionDateDesc(CardDetails cardDetails);
	 
	 @Query(nativeQuery = true)
	 List<TransactionDateInfo> getSumOfTransaction();
}
