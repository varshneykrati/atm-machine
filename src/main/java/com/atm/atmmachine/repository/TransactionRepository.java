package com.atm.atmmachine.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>{

	List<Transaction> findByTransactionDateAndCardDetails(LocalDate transactionDate, CardDetails cardDetails);

}
