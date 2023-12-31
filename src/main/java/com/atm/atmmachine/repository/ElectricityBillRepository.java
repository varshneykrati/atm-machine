package com.atm.atmmachine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.ElectricityBill;

@Repository
public interface ElectricityBillRepository extends JpaRepository<ElectricityBill, String>{

	Optional<ElectricityBill> findByUserElectricityId(String userElectricityId);

}
