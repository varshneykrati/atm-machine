package com.atm.atmmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.CardDetails;

@Repository
public interface CardDetailsRepository extends JpaRepository<CardDetails, String>{

}
