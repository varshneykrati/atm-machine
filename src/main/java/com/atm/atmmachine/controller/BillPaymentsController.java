package com.atm.atmmachine.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.atmmachine.dto.DthBill;
import com.atm.atmmachine.dto.ElectricityBillDto;
import com.atm.atmmachine.entity.DTH;
import com.atm.atmmachine.entity.ElectricityBill;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.entity.Vendors;
import com.atm.atmmachine.entity.Vendors.TypeOfVendor;
import com.atm.atmmachine.exception.BillPaymentsException;
import com.atm.atmmachine.service.BillPaymentsService;

@RestController
@Validated
@CrossOrigin(origins = "http://localhost:4200/")

public class BillPaymentsController {
	@Autowired
	private BillPaymentsService billPaymentsService;

	// 1/respective vendors-list would be visible to user when user selects on DTH
	// OR ELECTRICITY
	@GetMapping("/vendor/vendorList/{vendorname}")
	public List<String> vendorname(@PathVariable("vendorname") TypeOfVendor typeOfVendor) {
		List<Vendors> vendors_name = this.billPaymentsService.getVendorByName(typeOfVendor);
		List<String> vendors = new ArrayList<>();
		for (Vendors v : vendors_name)
			vendors.add(v.getVendorName());
		return vendors;
	}

	// 2/Razorpay will create transaction
	@GetMapping("/create/transaction/{amount}")
	public TransactionDetails createTransaction(@PathVariable(name = "amount") Integer amount)
			throws BillPaymentsException {
		return billPaymentsService.createTransactionAmount(amount);
	}

	// 3/ user -> DTH amount
	@PostMapping("/vendor/amount/dth")
	public Double dthAmounToBePaidById(@RequestBody DthBill dthBill) throws BillPaymentsException {
		return this.billPaymentsService.getAmountToBePaid(dthBill);
	}

	// 4/ user- Electricity amount
	@PostMapping("/vendor/amount/electricity")
	public Double electricityAmountToBePaidById(@RequestBody ElectricityBillDto electricityBill)
			throws BillPaymentsException {
		return this.billPaymentsService.getElectricityAmountToBePaid(electricityBill);
	}

	// 5/
	@PostMapping("/dth/bill/")
	public DTH payDthBill(@RequestBody DthBill dthBill) throws BillPaymentsException {
		return this.billPaymentsService.payUserBill(dthBill);
	}

	// 6/
	@PostMapping("/electricity/bill")
	public ElectricityBill payElectricityBill(@RequestBody ElectricityBillDto electricitybill)
			throws BillPaymentsException {
		return this.billPaymentsService.payElectricityUserBill(electricitybill);

	}

}
