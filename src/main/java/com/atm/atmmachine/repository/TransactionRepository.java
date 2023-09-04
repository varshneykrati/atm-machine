package com.atm.atmmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.TransactionDetails;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionDetails, String>{

}
