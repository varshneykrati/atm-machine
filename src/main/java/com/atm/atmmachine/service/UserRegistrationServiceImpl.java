package com.atm.atmmachine.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.dto.EmailDto;
import com.atm.atmmachine.dto.Password;
import com.atm.atmmachine.dto.UserLogin;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.CardDetails.CardStatus;
import com.atm.atmmachine.entity.CardDetails.CardType;
import com.atm.atmmachine.entity.CardDetails.UserTotallyRegister;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;
import com.atm.atmmachine.exception.HandleException;
import com.atm.atmmachine.repository.CardDetailsRepository;
import com.atm.atmmachine.repository.UserRegistrationRepository;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService{

	@Autowired
	UserRegistrationRepository userRegistrationRepository;
	@Autowired
	EmailService emailService;
	@Autowired
	CardDetailsRepository cardDetailsRepository;
	
	Integer generateOtp = null;
	
	@Override
	public Integer userRegistrationDetails(UserRegistration userRegisteration) throws HandleException {
		
		System.out.println("Hello->"+userRegisteration.getUserDOB()+"-----"+userRegisteration.getPhoneNo()+"----"+userRegisteration.getAadharNumber()+"-----"+userRegisteration.getAddress().getStreet());
		Optional<UserRegistration> userEmailOpt = this.userRegistrationRepository.findByEmailId(userRegisteration.getEmailId());
		Optional<UserRegistration> userPhoneOpt = this.userRegistrationRepository.findByPhoneNo(userRegisteration.getPhoneNo());
		Optional<UserRegistration> userAadharOpt = this.userRegistrationRepository.findByAadharNumber(userRegisteration.getAadharNumber());
		if(userAadharOpt.isPresent()) {
			throw new HandleException("Aadhar number is there already.");
		}else if(userEmailOpt.isPresent()) {
			throw new HandleException("EmailId already exist");
		}else if(userPhoneOpt.isPresent()) {
			throw new HandleException("Phone number already exist");
		}
		else if(!userRegisteration.getPassword().equals(userRegisteration.getConfirmPassword())) {
			throw new HandleException("Passwords are not matching");
		}
		else {
			Random random = new Random();
			//System.out.println("sending email now");
			generateOtp = random.nextInt(999999);
			String message =""+"<h3>"+"Hey "+userRegisteration.getUserName()+ "!"+"</h3>"+"<p>"+"A sign in attempt requires further verification beacause we want to verify this email to create safe account for you in our bank"+"</p>"+"<strong>"+"Your OTP for verification : "+generateOtp+"</strong>";
			String subject = "Verify your Email";
			String to = userRegisteration.getEmailId();
			String from = "krativarshne@gmail.com";
			this.emailService.sendEmail(message,subject,to,from);
			return generateOtp;
		}
		
	}

	@Override
	public UserRegistration saveUserDetail(UserRegistration userRegistration) throws HandleException {
	//	System.out.println("service : "+otp+" global otp: "+generateOtp+" -> user name : "+userRegistration.getUserName());
		if(userRegistration!=null) {
			userRegistration.setUserRegistrationApproval(UserRegistrationApproval.Inactive);
			userRegistration.setPassword(BCrypt.hashpw(userRegistration.getPassword(), BCrypt.gensalt()));
			
			UserRegistration user = this.userRegistrationRepository.save(userRegistration);
			
////--------------------Generate User Card----------------------------------			
			//generate account number
			Random random = new Random();
			BigInteger accountNumber;
			Optional<CardDetails> getUserAccountNo;
			do {
				StringBuilder sb = new StringBuilder(12);
				for (int i = 0; i < 12; i++) {
		            int randomDigit = random.nextInt(10); 
		            sb.append(randomDigit);
		        }
				accountNumber = new BigInteger(sb.toString());
				getUserAccountNo = this.cardDetailsRepository.findByAccountNumber(accountNumber);
			}while(getUserAccountNo.isPresent());
			
			
			
			//generate card number
			Optional<CardDetails> getUserCardNumber;
			BigInteger cardNumber;
			do {
				
				StringBuilder sb1 = new StringBuilder(16);
				for (int i = 0; i < 16; i++) {
					int randomDigit = random.nextInt(10); 
					sb1.append(randomDigit);
				}
				cardNumber =new BigInteger(sb1.toString());
				getUserCardNumber = this.cardDetailsRepository.findByCardNumber(cardNumber);
			}while(getUserCardNumber.isPresent());
			
			//generate cvv
			StringBuilder sb2 = new StringBuilder(3);
			for (int i = 0; i < 3; i++) {
	            int randomDigit = random.nextInt(10); 
	            sb2.append(randomDigit);
	        }
			Integer cvv = Integer.parseInt(sb2.toString());
			//System.out.println(accountNumber+" "+cardNumber+" "+cvv);
			
			//expiryDate of Card
			LocalDate currentCardDate = LocalDate.now();
			LocalDate expiryDate = currentCardDate.plusYears(5);
			
			//sending email
			String message =""+"<h3>"+"Dear "+userRegistration.getUserName()+ ","+"</h3>"+
			"<p>"+"We are thrilled to welcome you to our portal and are excited to have you as a part of our community! Your registration process has been successfully completed, and we sincerely thank you for choosing us."+"</p>"
					+ ""+"<p>"+"To ensure smooth and secure transactions, we have a quick step for you to complete: registering your access card. This card will serve as your key to unlock the full potential of our portal's features."+"</p>"
							+ ""+"<strong>"+"Your Card Details are  : "+"</strong>"
									+ "<p>"+"Account Number : " +accountNumber+ ""+"</p>"
											+ "<p>"+"Card Number : "+cardNumber+ ""+"</p>"
													+ "<p>"+"CVV : "+cvv+ ""+"</p>"
															+ "<p>"+"Expiry Date Of Card : "+expiryDate+ ""+"</p>"
																	+ "<p>"+"Please ensure to add this given card on Portal to complete your profile as well to make payments or to move inside portal."+"</p>";
										
			String subject = "Card Details - Complete Your Registration for Transaction Access";
			String to = userRegistration.getEmailId();
			String from = "krativarshne@gmail.com";
			this.emailService.sendEmail(message,subject,to,from);
			
		//saving these details to card table	
			CardDetails userCard = new CardDetails(accountNumber, cardNumber, cvv, expiryDate, CardType.Silver,25000.0,CardStatus.Inactive, 0.0, null, UserTotallyRegister.False, user);
			CardDetails getCard = this.cardDetailsRepository.save(userCard);
			userRegistration.setCardDetails(userCard);
			this.userRegistrationRepository.save(userRegistration);
		//	System.out.println("Card Details : "+getCard);
			return user;
		}
		else {
			throw new  HandleException("we dont have user");
		}
	}

	@Override
	public String checkLoginDetails(UserLogin userLogin) throws HandleException {
		Optional<UserRegistration> userEmailOpt = this.userRegistrationRepository.findByEmailId(userLogin.getEmailId());
		if(!userEmailOpt.isPresent()) {
			throw new  HandleException("You are not registered to this ATM, Please register first.");

		}else if(userEmailOpt.get().getCardDetails().getUserTotallyRegister().equals(UserTotallyRegister.False)) {
			throw new  HandleException("Please  add your card first for login.");
		}else {
			//userEmailOpt.get().getEmailId().equals(userLogin.getEmailId()) && userEmailOpt.get().getPassword().equals(userLogin.getPassword())
			if(BCrypt.checkpw(userLogin.getPassword(), userEmailOpt.get().getPassword()) && userEmailOpt.get().getEmailId().equals(userLogin.getEmailId())) {
				
				return "Successfully loggedin";
			}else {
				throw new HandleException("Credentials are wrong.");
			}
		}

	}

	@Override
	public CardDetails addUserCard(UserRegistration userRegisterDetails, CardDetails cardDetails) throws HandleException {
		if(userRegisterDetails==null) {
			throw new HandleException("First you need to Signup");
		}
		else if(userRegisterDetails.getCardDetails().getUserTotallyRegister().equals(UserTotallyRegister.False)) {
			Optional<CardDetails> getCardOpt = this.cardDetailsRepository.findByAccountNumberAndCardNumber(cardDetails.getAccountNumber(),cardDetails.getCardNumber());
			if(getCardOpt.isPresent()) {
				CardDetails getUserCard = getCardOpt.get();
				getUserCard.setCardstatus(CardStatus.Active); 
				getUserCard.setUserTotallyRegister(UserTotallyRegister.True);
				getUserCard.setUserRegistration(userRegisterDetails);
				getUserCard.setAmount(cardDetails.getAmount());
				
				//Generate Pin number
				Random random = new Random();
				StringBuilder sb = new StringBuilder(6);
				for (int i = 0; i < 6; i++) {
		            int randomDigit = random.nextInt(10); 
		            sb.append(randomDigit);
		        }
				Integer pin = Integer.parseInt(sb.toString()); 
				String message =""+"<h3>"+"Dear "+userRegisterDetails.getUserName()+ ","+"</h3>"+
						"<p>"+"Congrats! Your registration process is done, your card get registered. Now for doing further transaction or for using further ATM features we are providing you your Card Pin."+"</p>"
										+ ""+"<strong>"+"Your Card PIN Number is : "+"</strong>"+"" +pin+ ""
													+ "<p>"+" Please ensure to keep it safe with you, Dont't share it with anyone."+"</p>";
													
						String subject = "Card Details - Complete Your Registration for Transaction Access";
						String to = userRegisterDetails.getEmailId();
						String from = "krativarshne@gmail.com";
						this.emailService.sendEmail(message,subject,to,from);
						
				getUserCard.setCardPin(pin);
				return this.cardDetailsRepository.save(getUserCard);
			}
			else {
				throw new HandleException("Credentials are wrong");
			}
		}
			else {
				throw new HandleException("This Card is already Registered");
			}
		
	}

	@Override
	public UserRegistration viewUserProfile(String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findByUserId(userId);
		if(getUserOpt.isPresent() && getUserOpt.get().getCardDetails().getUserTotallyRegister().equals(UserTotallyRegister.True)) {
			return getUserOpt.get();
		}
		else {
			throw new HandleException("You have to register first or you have to register your card first");
		}
	}

	@Override
	public UserRegistration updateUserAddress(UserRegistration userRegistration, String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findByUserId(userId);
		if(getUserOpt.isPresent()) {
			UserRegistration getUser = getUserOpt.get();
			getUser.setPhoneNo(userRegistration.getPhoneNo()); 
			getUser.setAddress(userRegistration.getAddress());
			return this.userRegistrationRepository.save(getUser);
		}
		else {
			throw new HandleException("user not exist, so no update option is there");
		}
	}

	@Override
	public Integer sendOtpOnEmail(EmailDto emailDto, String string) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findByUserIdAndEmailId(string, emailDto.getEmailId());
		if(getUserOpt.isPresent()) {
			Random random = new Random();
		//	System.out.println("sending email now");
			generateOtp = random.nextInt(999999);
			String message =""+"<h3>"+"Hey "+getUserOpt.get().getUserName()+ "!"+"</h3>"+"<p>"+"A sign in attempt requires further verification beacause we want to verify this email to create safe account for you in our bank"+"</p>"+"<strong>"+"Your OTP for verification : "+generateOtp+"</strong>";
			String subject = "Verify your Email";
			String to = getUserOpt.get().getEmailId();
			String from = "krativarshne@gmail.com";
			this.emailService.sendEmail(message,subject,to,from);
			return generateOtp;
		}
		else {
			throw new HandleException("Email id is not present");
		}
	}

	@Override
	public UserRegistration savePassword(Password password, String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findByUserId(userId);
		if(getUserOpt.isPresent()) {
			getUserOpt.get().setPassword(password.getPassword());
			return this.userRegistrationRepository.save(getUserOpt.get());
		}
		else {
			throw new HandleException("User not exist");
		}
	}
	
	
	
}
