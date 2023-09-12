package com.atm.atmmachine.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.atmmachine.dto.EmailDto;
import com.atm.atmmachine.dto.OtpGeneration;
import com.atm.atmmachine.dto.Password;
import com.atm.atmmachine.dto.UserLogin;
import com.atm.atmmachine.entity.Address;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.exception.HandleException;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.repository.UserRequestRepository;
import com.atm.atmmachine.service.UserRegistrationService;

@RestController
@CrossOrigin(origins="http://localhost:4200/")
public class UserRegistrationController {
	@Autowired
	UserRegistrationService userRegistrationService;
	
	//UserRegistration userRegisterDetails=null;
	Integer otp=null;
	
//	@GetMapping("/")
//	public String krati() {
//		return "Hello welcome";
//	}
	
	@PostMapping("/user/otp/")
	public OtpGeneration registerUser(@RequestBody UserRegistration userRegistration) throws HandleException {
		
		return this.userRegistrationService.userRegistrationDetails(userRegistration);
		
	}
	
	@PostMapping("/user/")
	public UserRegistration verifyAndSaveRegisterUser(@RequestBody UserRegistration userRegisterDetails) throws HandleException {
		return this.userRegistrationService.saveUserDetail(userRegisterDetails);
		
	}
	
	@PatchMapping("/card/{userId}")
	public CardDetails saveUserCard(@RequestBody CardDetails cardDetails,@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.addUserCard(userId, cardDetails);
	}
	
	@PostMapping("/user/login/")
	public UserRegistration login(@RequestBody UserLogin userLogin) throws HandleException {
		return this.userRegistrationService.checkLoginDetails(userLogin); //redirect to portal after success.
	}
	
	@GetMapping("/user/profile/{userId}")
	public CardDetails viewProfile(@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.viewUserProfile(userId);
	}
	
	@PatchMapping("/user/otp/phoneNo/{userId}")
	public Integer otpForUpdatePhoneNumber(@RequestBody UserRegistration userRegistration, @PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.otpForUpdatingPhoneNumber(userRegistration, userId);
	}
	
	@PatchMapping("/user/phoneNo/{userId}")
	public UserRegistration updateUserPhoneNumber(@RequestBody UserRegistration userRegistration, @PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.updatingUserPhoneNumber(userRegistration, userId);
	}
	
	@GetMapping("/address/{userId}")
	public Address getUserAddress(@PathVariable("userId") String userId) throws HandleException {
		System.out.println("in controller");
		return this.userRegistrationService.getAddress(userId);
	}
	
	@PatchMapping("/user/address/{userId}")
	public UserRegistration updateUserAddress(@RequestBody Address address,@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.changeUserAddress(address,userId);
	}
	
	
	@PostMapping("/user/email/{userId}")
	public Integer verifyEmailAndSendOtp(@RequestBody EmailDto emailDto, @PathVariable("userId") String userId) throws HandleException {
		//System.out.println("hii email dto");
		otp = this.userRegistrationService.sendOtpOnEmail(emailDto, userId);
		return otp;
	}
	
	@PostMapping("/user/password/{userId}")
	public UserRegistration saveUpdatedPassword(@RequestBody Password password,@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.savePassword(password, userId);
	}

	@PostMapping("/user/session/")
	public UserRegistration fetchingUserToStoreInSessionAtLoginTime(@RequestBody UserLogin userLogin) throws HandleException {
		return this.userRegistrationService.fetchingUser(userLogin);
	}
}
