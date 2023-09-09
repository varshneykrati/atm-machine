package com.atm.atmmachine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atm.atmmachine.entity.Vendors;
import com.atm.atmmachine.entity.Vendors.TypeOfVendor;

@Repository
public interface VendorsRepository extends JpaRepository<Vendors, String>{

	
	public List<Vendors> findByTypeOfVendor(TypeOfVendor typeOfVendor);

	public Optional<Vendors> findByVendorName(String vendorName);

}
