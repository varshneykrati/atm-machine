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

/********************************************************************************************************
 * @author :Vaidehi Kalore 
 * Description: It is a controller that provide the API
 *         for doing bill payments of DTH and an electricity bill like viewing all
 *         vendors list, creating transaction, for viewing the amount that user need to pay to
 *         respective vendor and to save it into the database.It provides endpoints to interact with user data in the system.
 *         
 *EndPoints-
 *-GET/vendors/{vendorname}:User can view list of particular vendors on selecting DTH or electricity-bill.
 *-GET/create/transaction/{amount}/{userId}:It is used for creating transaction using razorpay.
 *-POST/vendor/amount/dth/{userId}:It is used for letting user know how much he needs to pay for DTH.
 *-POST/vendor/amount/electricity/{userId}:It is used for letting user know how much amount he needs to pay for electricity-bill.
 *-POST/dth/bill/{vendorName}/{userId}:It is used for deducting paid-amount from user and adding it to vendor account and to save respective changes in database for DTH.
 *-POST/electricity/bill/{vendorName}/{userId}:It is used for deducting paid-amount from user and adding to vendor account and to save respective changes in database for electricity-bill. 
 *   
 *Version: 1.0 
 *Created date: 04-09-2023
 *********************************************************************************************************/

@RestController
@Validated
@CrossOrigin(origins = "http://localhost:4200/")

public class BillPaymentsController {
	@Autowired
	private BillPaymentsService billPaymentsService;

	
	@GetMapping("/vendors/{vendorname}")
	public List<String> vendorname(@PathVariable("vendorname") TypeOfVendor typeOfVendor) {
		List<Vendors> vendors_name = this.billPaymentsService.getVendorByName(typeOfVendor);
		List<String> vendors = new ArrayList<>();
		for (Vendors v : vendors_name)
			vendors.add(v.getVendorName());
		return vendors;
	}

	@GetMapping("/create/transaction/{amount}/{userId}")
	public Transaction createTransaction(@PathVariable(name = "amount") Integer amount,
			@PathVariable("userId") String userId) throws BillPaymentsException {
		return billPaymentsService.createTransactionAmount(amount, userId);
	}

	@PostMapping("/vendor/amount/dth/{userId}")
	public Double dthAmounToBePaidById(@RequestBody DthBill dthBill, @PathVariable("userId") String userId)
			throws BillPaymentsException {
		return this.billPaymentsService.getAmountToBePaid(dthBill, userId);
	}

	@PostMapping("/vendor/amount/electricity/{userId}")
	public Double electricityAmountToBePaidById(@RequestBody ElectricityBillDto electricityBill,
			@PathVariable("userId") String userId) throws BillPaymentsException {
		return this.billPaymentsService.getElectricityAmountToBePaid(electricityBill, userId);
	}

	@PostMapping("/dth/bill/{vendorName}/{userId}")
	public DTH payDthBill(@PathVariable("vendorName") String vendorName, @RequestBody DthBill dthBill,
			@PathVariable("userId") String userId) throws BillPaymentsException {
		return this.billPaymentsService.payUserBill(vendorName, dthBill, userId);
	}

	@PostMapping("/electricity/bill/{vendorName}/{userId}")
	public ElectricityBill payElectricityBill(@PathVariable("vendorName") String vendorName,
			@RequestBody ElectricityBillDto electricitybill, @PathVariable("userId") String userId)
			throws BillPaymentsException {
		return this.billPaymentsService.payElectricityUserBill(vendorName, electricitybill, userId);
	}

}
