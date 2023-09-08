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
import com.atm.atmmachine.dto.Password;
import com.atm.atmmachine.dto.UserLogin;
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
	
	UserRegistration userRegisterDetails=null;
	Integer otp=null;
	
//	@GetMapping("/")
//	public String krati() {
//		return "Hello welcome";
//	}
	
	@PostMapping("/user/")
	public Integer registerUser(@RequestBody UserRegistration userRegistration) throws HandleException {
		//System.out.println("we are adding");
		userRegisterDetails = userRegistration;
		otp = this.userRegistrationService.userRegistrationDetails(userRegistration);
		return otp;
	}
	
	@PostMapping("/saveUser/")
	public UserRegistration verifyAndSaveRegisterUser(@RequestBody UserRegistration userRegisterDetails) throws HandleException {
		return this.userRegistrationService.saveUserDetail(userRegisterDetails);
		
	}
	
	@PatchMapping("/card/")
	public CardDetails saveUserCard(@RequestBody CardDetails cardDetails) throws HandleException {
		return this.userRegistrationService.addUserCard(userRegisterDetails, cardDetails);
	}
	
	@PostMapping("/user/login/")
	public String login(@RequestBody UserLogin userLogin) throws HandleException {
		return this.userRegistrationService.checkLoginDetails(userLogin); //redirect to portal after success.
	}
	
	@GetMapping("/view/profile/{userId}")
	public UserRegistration viewProfile(@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.viewUserProfile(userId);
	}
	
	@PatchMapping("/update/{userId}")
	public UserRegistration updateAdress(@RequestBody UserRegistration userRegistration, @PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.updateUserAddress(userRegistration, userId);
	}
	
	@PostMapping("/email/")
	public Integer verifyEmailAndSendOtp(@RequestBody EmailDto emailDto) throws HandleException {
		//System.out.println("hii email dto");
		otp = this.userRegistrationService.sendOtpOnEmail(emailDto, "user1");
		return otp;
	}
	
	@PatchMapping("/password/")
	public UserRegistration saveUpdatedPassword(@RequestBody Password password) throws HandleException {
		return this.userRegistrationService.savePassword(password, "user1");
	}

}
