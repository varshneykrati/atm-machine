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
import com.atm.atmmachine.dto.Transaction;
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
	@GetMapping("/vendors/{vendorname}")
	public List<String> vendorname(@PathVariable("vendorname") TypeOfVendor typeOfVendor) {
		List<Vendors> vendors_name = this.billPaymentsService.getVendorByName(typeOfVendor);
		List<String> vendors = new ArrayList<>();
		for (Vendors v : vendors_name)
			vendors.add(v.getVendorName());
		return vendors;
	}

	// 2/Razorpay will create transaction
	@GetMapping("/create/transaction/{amount}/{userId}")
	public Transaction createTransaction(@PathVariable(name = "amount") Integer amount, @PathVariable("userId") String userId)
			throws BillPaymentsException {
		return billPaymentsService.createTransactionAmount(amount,userId);
	}

	// 3/ user -> DTH amount
	@PostMapping("/vendor/amount/dth/{userId}")
	public Double dthAmounToBePaidById(@RequestBody DthBill dthBill,@PathVariable("userId") String userId) throws BillPaymentsException {
		return this.billPaymentsService.getAmountToBePaid(dthBill,userId);
	}

	// 4/ user- Electricity amount
	@PostMapping("/vendor/amount/electricity/{userId}")
	public Double electricityAmountToBePaidById(@RequestBody ElectricityBillDto electricityBill,@PathVariable("userId") String userId)
			throws BillPaymentsException {
		return this.billPaymentsService.getElectricityAmountToBePaid(electricityBill,userId);
	}

	// 5/
	@PostMapping("/dth/bill/{vendorName}/{userId}")
	public DTH payDthBill(@PathVariable("vendorName") String vendorName, @RequestBody DthBill dthBill,@PathVariable("userId") String userId) throws BillPaymentsException {
		return this.billPaymentsService.payUserBill(vendorName,dthBill,userId);
	}

	// 6/
	@PostMapping("/electricity/bill/{vendorName}/{userId}")
	public ElectricityBill payElectricityBill(@PathVariable("vendorName") String vendorName, @RequestBody ElectricityBillDto electricitybill,@PathVariable("userId") String userId)
			throws BillPaymentsException {
		return this.billPaymentsService.payElectricityUserBill(vendorName,electricitybill,userId);

	}

}
