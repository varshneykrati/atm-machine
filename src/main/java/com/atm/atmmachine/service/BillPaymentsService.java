package com.atm.atmmachine.service;

import java.util.List;

import com.atm.atmmachine.dto.DthBill;
import com.atm.atmmachine.dto.ElectricityBillDto;
import com.atm.atmmachine.entity.DTH;
import com.atm.atmmachine.entity.ElectricityBill;
import com.atm.atmmachine.dto.Transaction;
import com.atm.atmmachine.entity.Vendors;
import com.atm.atmmachine.entity.Vendors.TypeOfVendor;
import com.atm.atmmachine.exception.BillPaymentsException;

public interface BillPaymentsService {

	List<Vendors> getVendorByName(TypeOfVendor typeOfVendor);

	DTH payUserBill(String vendorName,DthBill dthBill,String userId) throws BillPaymentsException;

	ElectricityBill payElectricityUserBill(String vendorName,ElectricityBillDto electricityBill,String userId) throws BillPaymentsException;

	Double getAmountToBePaid(DthBill dthBill,String userId)throws BillPaymentsException;

	Double getElectricityAmountToBePaid(ElectricityBillDto electricityBill,String userId) throws BillPaymentsException;

	Transaction createTransactionAmount(Integer amount,String userId) throws BillPaymentsException;
	
}
