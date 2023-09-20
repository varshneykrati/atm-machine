package com.atm.atmmachine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.atmmachine.dto.AdminRemark;
import com.atm.atmmachine.dto.CardLimit;
import com.atm.atmmachine.dto.TransactionDateInfo;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.entity.UserRequest.RequestStatus;
import com.atm.atmmachine.exceptions.AdminException;
import com.atm.atmmachine.service.AdminService;

/************************************************************************************
 * @author Shivam 
 * Description : It is a controller that provides the services
 *         for displaying all pending requests,setting admin remark,change card
 *         limit of specific card type, sum of daily transaction. 
 *         Endpoints:
 * 
 *         - PATCH /admin/request/status/{reqid}: Approve pending requests if conditions
 *         satisfied.
 *         
 *         - PATCH /admin/adminremark/{reqid}: set admin remark for specific request.
 *         
 *         - PATCH /admin/cardlimit/: change card limit of specific card type.
 *         
 *         - GET /admin/users/request/{request}: Retrieve all pending and specific request.
 * 
 *         - GET /admin/transaction: Retrieve the transaction sum datewise.
 * 
 *         
 *         Created Date 12-Sept-2023
 ************************************************************************************/

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/admin/")
	public String krati() {
		return "Hello welcome shivam";
	}
	

	// used when admin wants to display all pending requests
	@GetMapping("/admin/requests")
	public List<UserRequest> displayAllPendingRequests() {
		return this.adminService.displayRequestByStatus();
	}

	
	@GetMapping("/admin/users/request/{request}")
	public List<UserRequest> displayCardLostRequests(@PathVariable("request") String request) {
		return this.adminService.displayByRequest(request);
	}

	//to change request status of specific requestId
	@PatchMapping("/admin/request/status/{reqid}")
	public RequestStatus changeRequestStatus(@PathVariable("reqid") String reqId) throws AdminException {

		UserRequest currentUserRequest;
		try {
			currentUserRequest = this.adminService.updateUserRequestStatus(reqId);
		} catch (AdminException e) {
			throw e;
		}
		return currentUserRequest.getRequestStatus();
	}

	// to set the admin remark for specific req id
	@PatchMapping("/admin/adminremark/{reqid}")
	public String setAdminRemarkForRequest(@PathVariable("reqid") String reqId, @RequestBody AdminRemark adminRemark)
			throws AdminException {
		try {
			return this.adminService.setAdminRemark(reqId, adminRemark.getRemarkOnRequest());
		} catch (AdminException e) {
			throw e;
		}
	}

	// to change card limit of specific card type
	@PatchMapping("/admin/cardlimit/")
	public Double setCardLimit(@RequestBody CardLimit cardLimit) throws AdminException {
		try {
			return this.adminService.changeCardLimit(cardLimit);
		} catch (AdminException e) {
			throw e;

		}
	}


	// admin wants to see today's transaction sum
	@GetMapping("/admin/transaction")
	public List<TransactionDateInfo> sumOfTodayTransaction() {
		return this.adminService.sumOfTodayTransaction();
	}

}
