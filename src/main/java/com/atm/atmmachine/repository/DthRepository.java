package com.atm.atmmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.DTH;

@Repository
public interface DthRepository extends JpaRepository<DTH, String>{

}
