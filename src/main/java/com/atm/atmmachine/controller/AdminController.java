package com.atm.atmmachine.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.atmmachine.dto.AdminRemark;
import com.atm.atmmachine.dto.CardLimit;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;
import com.atm.atmmachine.entity.UserRequest.RequestStatus;
import com.atm.atmmachine.exceptions.AdminException;
import com.atm.atmmachine.service.AdminService;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/admin/")
	public String krati() {
		return "Hello welcome shivam";
	}

	// to display the requests by their id
	@GetMapping("/admin/requests/{id}")
	public Optional<UserRequest> displayRequestById(@PathVariable("id") String id) throws AdminException {
		try {
			return this.adminService.getRequestById(id);
		} catch (AdminException e) {
			throw e;
		}
	}

	// used when admin wants to display all pending requests
	@GetMapping("/admin/requests")
	public List<UserRequest> displayAllPendingRequests() {
		return this.adminService.displayRequestByStatus();
	}

	// used to display when admin wants to see all cardlost requests
	@GetMapping("/admin/users/request/cardlost")
	public List<UserRequest> displayCardLostRequests() {
		return this.adminService.displayByRequest();
	}

	// used to display when admin wants to see all cardReplacement requests
	@GetMapping("/admin/users/request/cardreplacement")
	public List<UserRequest> displayCardReplacementRequests() {
		return this.adminService.displayAllCardReplacementRequests();
	}

	// used to approve the cardLost request of specific requestid
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


	// to display users with status inactive
	@GetMapping("/admin/users/inactive")
	public List<UserRegistration> displayUsersWithStatusInactive() {
		return this.adminService.displayUsersWithStatusInactive();
	}

	// to set user registration approval active
	@PatchMapping("/admin/change/userregistrationapproval/{userid}")
	public UserRegistrationApproval changeUserRegistrationApproval(@PathVariable("userid") String userId)
			throws AdminException {
		try {
			return this.adminService.updateUserRegistrationApproval(userId);
		} catch (AdminException e) {
			throw e;
		}
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
	public Double setCardLimit(@RequestBody CardLimit cardLimit) throws AdminException{
		try {
			return this.adminService.changeCardLimit(cardLimit);
		} catch (AdminException e) {
			throw e;
			
		}
	}

	// to validate aadhar card
	@GetMapping("/admin/aadharcard/{userid}")
	public Boolean validateAadharCard(@PathVariable("userid") String userId) throws AdminException {
		Optional<UserRegistration> user = this.adminService.findByUserId(userId);
		if (!user.isPresent())
			throw new AdminException("User doesn't exist");
		return this.adminService.validAadharCard(user.get().getAadharNumber().toString());
	}

}
