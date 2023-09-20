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



/******************************************************************************
 * @author Krati Varshney 
 * Description UserRegistratiion Controller is responsible for taking userDetails from user registration form,
  			 then all the details like phone number and email got verify by sending OTP on Email and SMS as well as aadhar card number will verify,
  			 After verfication user Account will get created and debit card will be send on email for that user,
  			 then user has to submit his card details on portal using register card details form, then card Pin will be send on Email to that user,
  			 then login form will be there for user, where we have forgot Password module as well, and also user can view Profile where he/she can update phone number,
  			 and can update address as well, and change Card Pin functionality is also there.
 * Endpoints: 
 * - GET /user/otp/: Sending OTP on email and phone to verify User Register form details. 
 * - POST /user/ : If user types correct otp that are sending on email and SMS, then user Details will get submitted 
 				and account will open for user and debit card will be send on email as well.
 * - PATCH /card/{userId} : when user submit his card details through card register form, 6 digit of card Pin will be sent on email,
 * 							and his card get submitted.
 * - POST /user/login/ : User login form 
 * - GET /user/profile/{userId}: View User Profile
 * - PATCH /user/otp/phoneNo/{userId}: In user profile user can update his phone number then OTP will be sent on his SMS for verify
 * - PATCH /user/phoneNo/{userId}: After verify user phone number, User register form get updated.
 * - GET /address/{userId}: To get user Address
 * - PATCH /user/address/{userId}: For updating user address
 * - POST /user/email/{userId}: Verify email in Forgot password module
 * - POST /user/password/{userId}: Password will get modify
 * - GET /verify/user/pin/{userId}: verify email for update user card PIN
 * - PATCH /user/pin/{userId}: update user Card PIN
 * - POST /user/session/: fetch user detail to store user in session to track user onto the portal.
 * Version 1.0 
 * Created Date 12-Sept-2023
 ******************************************************************************/

@RestController
@CrossOrigin(origins="http://localhost:4200/")
public class UserRegistrationController {
	@Autowired
	UserRegistrationService userRegistrationService;
	
	//UserRegistration userRegisterDetails=null;
	Integer otp=null;
	
	/******************************************************************************
	 * Method -registerUser 
	 * Description - verify user by sending OTP on email and on SMS.
	 * @return OtpGeneration - OTP on email and on SMS.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PostMapping("/user/otp/")
	public OtpGeneration registerUser(@RequestBody UserRegistration userRegistration) throws HandleException {
		
		return this.userRegistrationService.userRegistrationDetails(userRegistration);
		
	}
	
	/******************************************************************************
	 * Method -verifyAndSaveRegisterUser 
	 * Description - after verify OTPs, submit user details to create an account.
	 * @return UserRegistration - user details.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PostMapping("/user/")
	public UserRegistration verifyAndSaveRegisterUser(@RequestBody UserRegistration userRegisterDetails) throws HandleException {
		return this.userRegistrationService.saveUserDetail(userRegisterDetails);
		
	}
	
	/******************************************************************************
	 * Method - saveUserCard 
	 * Description - register user card.
	 * @return CardDetails - user debit card details.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PatchMapping("/card/{userId}")
	public CardDetails saveUserCard(@RequestBody CardDetails cardDetails,@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.addUserCard(userId, cardDetails);
	}
	
	/******************************************************************************
	 * Method - login 
	 * Description - user login.
	 * @return UserRegistration - user details.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PostMapping("/user/login/")
	public UserRegistration login(@RequestBody UserLogin userLogin) throws HandleException {
		return this.userRegistrationService.checkLoginDetails(userLogin); //redirect to portal after success.
	}
	
	/******************************************************************************
	 * Method - viewProfile 
	 * Description - view profile.
	 * @return CardDetails - user card details.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@GetMapping("/user/profile/{userId}")
	public CardDetails viewProfile(@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.viewUserProfile(userId);
	}
	
	/******************************************************************************
	 * Method - otpForUpdatePhoneNumber 
	 * Description - sending Otp for verify phone number while updation.
	 * @return Integer - generated otp.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PatchMapping("/user/otp/phoneNo/{userId}")
	public Integer otpForUpdatePhoneNumber(@RequestBody UserRegistration userRegistration, @PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.otpForUpdatingPhoneNumber(userRegistration, userId);
	}
	
	/******************************************************************************
	 * Method - updateUserPhoneNumber 
	 * Description - update phone number after verify.
	 * @return UserRegistration - user details.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PatchMapping("/user/phoneNo/{userId}")
	public UserRegistration updateUserPhoneNumber(@RequestBody UserRegistration userRegistration, @PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.updatingUserPhoneNumber(userRegistration, userId);
	}
	
	/******************************************************************************
	 * Method - getUserAddress 
	 * Description - fetch user address.
	 * @return Address - user address.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@GetMapping("/address/{userId}")
	public Address getUserAddress(@PathVariable("userId") String userId) throws HandleException {
	//	System.out.println("in controller");
		return this.userRegistrationService.getAddress(userId);
	}
	
	/******************************************************************************
	 * Method - updateUserAddress 
	 * Description - update user address.
	 * @return UserRegistration - user details.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PatchMapping("/user/address/{userId}")
	public UserRegistration updateUserAddress(@RequestBody Address address,@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.changeUserAddress(address,userId);
	}
	
	/******************************************************************************
	 * Method - verifyEmailAndSendOtp 
	 * Description - verifying email by sending otp on email for forgot password module.
	 * @return Integer - generated otp.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PostMapping("/user/email/{userId}")
	public Integer verifyEmailAndSendOtp(@RequestBody EmailDto emailDto, @PathVariable("userId") String userId) throws HandleException {
		//System.out.println("hii email dto");
		otp = this.userRegistrationService.sendOtpOnEmail(emailDto, userId);
		return otp;
	}
	
	/******************************************************************************
	 * Method - saveUpdatedPassword 
	 * Description - save update password of forgot password module.
	 * @return UserRegistration - user details.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PostMapping("/user/password/{userId}")
	public UserRegistration saveUpdatedPassword(@RequestBody Password password,@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.savePassword(password, userId);
	}
	
	/******************************************************************************
	 * Method - sendUserEmailForPin 
	 * Description - verify email for update card PIN.
	 * @return Integer - generated OTP.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/

	@GetMapping("/verify/user/pin/{userId}")
	public Integer sendUserEmailForPin(@PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.sendUserEmailForUpdatePin(userId);
	}
	
	/******************************************************************************
	 * Method - updateUserCardPin 
	 * Description - after verification, update PIN.
	 * @return CardDetails - card details.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/
	
	@PatchMapping("/user/pin/{userId}")
	public CardDetails updateUserCardPin(@RequestBody CardDetails cardDetails, @PathVariable("userId") String userId) throws HandleException {
		return this.userRegistrationService.changeUserCardPin(cardDetails,userId);
	}
	
	/******************************************************************************
	 * Method - fetchingUserToStoreInSessionAtLoginTime 
	 * Description - fetch user detail to store user in session to track user onto the portal.
	 * @return UserRegistration - user details.
	 * Created by Krati Varshney
	 * Created Date 12-Sept-2023
	 ******************************************************************************/

	@PostMapping("/user/session/")
	public UserRegistration fetchingUserToStoreInSessionAtLoginTime(@RequestBody UserLogin userLogin) throws HandleException {
		return this.userRegistrationService.fetchingUser(userLogin);
	}
}
