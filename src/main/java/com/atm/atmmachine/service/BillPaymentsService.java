package com.atm.atmmachine.service;

import java.util.List;

import com.atm.atmmachine.dto.DthBill;
import com.atm.atmmachine.dto.ElectricityBillDto;
import com.atm.atmmachine.entity.DTH;
import com.atm.atmmachine.entity.ElectricityBill;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.entity.Vendors;
import com.atm.atmmachine.entity.Vendors.TypeOfVendor;
import com.atm.atmmachine.exception.BillPaymentsException;

public interface BillPaymentsService {

	List<Vendors> getVendorByName(TypeOfVendor typeOfVendor);

	DTH payUserBill(DthBill dthBill) throws BillPaymentsException;

	ElectricityBill payElectricityUserBill(ElectricityBillDto electricityBill) throws BillPaymentsException;

	Double getAmountToBePaid(DthBill dthBill)throws BillPaymentsException;

	Double getElectricityAmountToBePaid(ElectricityBillDto electricityBill) throws BillPaymentsException;

	TransactionDetails createTransactionAmount(Integer amount) throws BillPaymentsException;
	
}
