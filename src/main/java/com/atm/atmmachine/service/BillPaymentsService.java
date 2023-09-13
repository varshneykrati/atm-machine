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

	DTH payUserBill(String vendorName,DthBill dthBill) throws BillPaymentsException;

	ElectricityBill payElectricityUserBill(String vendorName,ElectricityBillDto electricityBill) throws BillPaymentsException;

	Double getAmountToBePaid(DthBill dthBill)throws BillPaymentsException;

	Double getElectricityAmountToBePaid(ElectricityBillDto electricityBill) throws BillPaymentsException;

	Transaction createTransactionAmount(Integer amount) throws BillPaymentsException;
	
}
