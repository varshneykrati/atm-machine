package com.atm.atmmachine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>{

}
