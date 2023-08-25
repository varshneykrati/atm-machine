package com.atm.atmmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.Vendors;

@Repository
public interface VendorsRepository extends JpaRepository<Vendors, String>{

}
