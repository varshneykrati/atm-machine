package com.atm.atmmachine.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.dto.EmailDto;
import com.atm.atmmachine.dto.OtpGeneration;
import com.atm.atmmachine.dto.Password;
import com.atm.atmmachine.dto.UserLogin;
import com.atm.atmmachine.entity.Address;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.CardDetails.CardStatus;
import com.atm.atmmachine.entity.CardDetails.CardType;
import com.atm.atmmachine.entity.CardDetails.UserTotallyRegister;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.entity.TransactionDetails.TransactionType;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;
import com.atm.atmmachine.exception.HandleException;
import com.atm.atmmachine.idGenerator.VerhoeffAlgorithm;
import com.atm.atmmachine.repository.CardDetailsRepository;
import com.atm.atmmachine.repository.TransactionRepository;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.sms.SMSController;
import com.atm.atmmachine.sms.SmsPojo;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService{

	@Autowired
	UserRegistrationRepository userRegistrationRepository;
	@Autowired
	EmailService emailService;
	@Autowired
	CardDetailsRepository cardDetailsRepository;
	@Autowired
	SMSController smsController;
	@Autowired
	TransactionRepository transactionRepository;
	
	Integer generateOtp = null;
	Integer generateSmsOtp = null;
	SmsPojo smspojo = new SmsPojo();
	
	@Override
	public OtpGeneration userRegistrationDetails(UserRegistration userRegisteration) throws HandleException {
		
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
			System.out.println("Inside else if everything right.");
			
			Pattern aadharPattern = Pattern.compile("\\d{12}");
			String aadharNumber = userRegisteration.getAadharNumber().toString();
			System.out.println("inside pattern->"+aadharNumber);
			 boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
			 if(isValidAadhar){
		            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
		            if(isValidAadhar==true) {
		            	Random random = new Random();
		    			//System.out.println("sending email now");
		    			generateOtp = random.nextInt(999999);
		    			generateSmsOtp = random.nextInt(999999);
		    			String message =""+"<h3>"+"Hey "+userRegisteration.getUserName()+ "!"+"</h3>"+"<p>"+"A sign in attempt requires further verification beacause we want to verify this email to create safe account for you in our bank"+"</p>"+"<strong>"+"Your OTP for verification : "+generateOtp+"</strong>";
		    			String subject = "Verify your Email";
		    			String to = userRegisteration.getEmailId();
		    			String from = "krativarshne@gmail.com";
		    			this.emailService.sendEmail(message,subject,to,from);
		    			
		    			//sending OTP to SMS
		    			smspojo.setTo(userRegisteration.getPhoneNo());
		    			smspojo.setMessage("Hey " + userRegisteration.getUserName() + " A sign in attempt requires further verification beacause we want to verify this phone number to create safe account for you in our bank "
		    					+  " Your One Time Password [OTP] is : " +generateSmsOtp);

		    			smsController.smsSubmit(smspojo);
		    			
		            }
		            else {
		            	throw new HandleException("Aadhar Number is not valid.");
		            }
		        }
			 else {
				 throw new HandleException("Aadhar Number is not valid.");
			 }
			 OtpGeneration otpGeneration = new OtpGeneration(generateOtp,generateSmsOtp);
			 return otpGeneration;
		}
		
	}

	@Override
	public UserRegistration saveUserDetail(UserRegistration userRegistration) throws HandleException {
	//	System.out.println("service : "+otp+" global otp: "+generateOtp+" -> user name : "+userRegistration.getUserName());
		if(userRegistration!=null) {
			userRegistration.setUserRegistrationApproval(UserRegistrationApproval.Active);
			userRegistration.setPassword(BCrypt.hashpw(userRegistration.getPassword(), BCrypt.gensalt()));
			
			UserRegistration user = this.userRegistrationRepository.save(userRegistration);
			
////--------------------Generate User Card----------------------------------			
			//generate account number
			Random random = new Random();
			BigInteger accountNumber;  String str;
			Optional<CardDetails> getUserAccountNo;
			do {
				StringBuilder sb = new StringBuilder(12);
				for (int i = 0; i < 12; i++) {
		            int randomDigit = random.nextInt(10); 
		            sb.append(randomDigit);
		        }
				accountNumber = new BigInteger(sb.toString());
				
				//length must be 12 digit
				str = accountNumber.toString();
				getUserAccountNo = this.cardDetailsRepository.findByAccountNumber(accountNumber);
			}while(getUserAccountNo.isPresent() || str.length()!=12);
			
			
			
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
				str = cardNumber.toString();
				getUserCardNumber = this.cardDetailsRepository.findByCardNumber(cardNumber);
			}while(getUserCardNumber.isPresent() || str.length()!=16);
			
			//generate cvv
			Integer cvv;
			do {
				StringBuilder sb2 = new StringBuilder(3);
				for (int i = 0; i < 3; i++) {
		            int randomDigit = random.nextInt(10); 
		            sb2.append(randomDigit);
		        }
				cvv = Integer.parseInt(sb2.toString());
				str = cvv.toString();
			}while(str.length()!=3);
			
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
			UserRegistration userData = this.userRegistrationRepository.save(userRegistration);
		//	System.out.println("Card Details : "+getCard);
			return userData;
		}
		else {
			throw new  HandleException("we dont have user");
		}
	}

	@Override
	public UserRegistration checkLoginDetails(UserLogin userLogin) throws HandleException {
		Optional<UserRegistration> userEmailOpt = this.userRegistrationRepository.findByEmailId(userLogin.getEmailId());
		if(!userEmailOpt.isPresent()) {
			throw new  HandleException("You dont have an Account, Please register first.");

		}else if(userEmailOpt.get().getCardDetails().getUserTotallyRegister().equals(UserTotallyRegister.False)) {
			throw new  HandleException("Please  add your card first for login.");
		}else {
			//userEmailOpt.get().getEmailId().equals(userLogin.getEmailId()) && userEmailOpt.get().getPassword().equals(userLogin.getPassword())
			if(BCrypt.checkpw(userLogin.getPassword(), userEmailOpt.get().getPassword()) && userEmailOpt.get().getEmailId().equals(userLogin.getEmailId())) {
				System.out.println("Done login");
				return userEmailOpt.get();
			}else {
				throw new HandleException("Credentials are wrong.");
			}
		}

	}

	@Override
	public CardDetails addUserCard(String userId, CardDetails cardDetails) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		UserRegistration userRegisterDetails = getUserOpt.get();
		if(userRegisterDetails==null) {
			throw new HandleException("Please sign in first");
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
				Integer pin;  String str;
				do {
					StringBuilder sb = new StringBuilder(6);
					for (int i = 0; i < 6; i++) {
			            int randomDigit = random.nextInt(10); 
			            sb.append(randomDigit);
			        }
					pin = Integer.parseInt(sb.toString()); 
					str = pin.toString();
				}while(str.length()!=6);
				
				String message =""+"<h3>"+"Dear "+userRegisterDetails.getUserName()+ ","+"</h3>"+
						"<p>"+"Congrats! Your registration process is done, your card get registered. Now for doing further transaction or for using further ATM features we are providing you your Card Pin."+"</p>"
										+ ""+"<strong>"+"Your Card PIN Number is : "+"</strong>"+"" +pin+ ""
													+ "<p>"+" Please ensure to keep it safe with you, Dont't share it with anyone."+"</p>";
													
						String subject = "Card Details - Complete Your Registration for Transaction Access";
						String to = userRegisterDetails.getEmailId();
						String from = "krativarshne@gmail.com";
						this.emailService.sendEmail(message,subject,to,from);
						
				getUserCard.setCardPin(pin);
				CardDetails userCard = this.cardDetailsRepository.save(getUserCard);
				
				TransactionDetails transaction = new TransactionDetails();
				transaction.setCardDetails(userCard);
				transaction.setToAccountNumber(userCard.getAccountNumber());
				transaction.setTransactionDate(LocalDateTime.now());
				transaction.setBalance(2000.0);
				transaction.setTransactionType(TransactionType.Deposit);
				transaction.setParticulars("Created account with Rs.2000.");
				this.transactionRepository.save(transaction);
				
				String accountNo = userCard.getAccountNumber().toString();
				String lastFourDigitOfAccNo = accountNo.substring(8,12);
				smspojo.setTo(userRegisterDetails.getPhoneNo());
				smspojo.setMessage("An amount of INR " + 2000.0 + " is debited to account No "+ "XXXXXXXX"+lastFourDigitOfAccNo + " on " + LocalDate.now()
										+ ".Total Avail.bal INR " + userCard.getAmount());

				smsController.smsSubmit(smspojo);
				
				
				return userCard;
			}
			else {
				throw new HandleException("Please check your credentials first");
			}
		}
			else {
				throw new HandleException("This Card is already Registered");
			}
		
	}

	@Override
	public CardDetails viewUserProfile(String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(getUserOpt.isPresent()) {
			//System.out.println(getUserOpt.get().getCardDetails().getCardPin());
			return getUserOpt.get().getCardDetails();
		}
		else {
			throw new HandleException("You have to register first or you have to register your card first");
		}
	}

	@Override
	public Integer otpForUpdatingPhoneNumber(UserRegistration userRegistration, String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(!getUserOpt.isPresent()) {
			throw new HandleException("user not present, Please Register");
		}
		Optional<UserRegistration> userPhoneOpt = this.userRegistrationRepository.findByPhoneNo(userRegistration.getPhoneNo());
		if(userPhoneOpt.isPresent() && !(userPhoneOpt.get().getUserId().equals(userId))){
			throw new HandleException("Phone number already exist.");
		}
		else if((userPhoneOpt.isPresent() && userPhoneOpt.get().getUserId().equals(userId)) || (!userPhoneOpt.isPresent())){
			Random random = new Random();
			generateSmsOtp = random.nextInt(999999);
			smspojo.setTo(userRegistration.getPhoneNo());
			smspojo.setMessage("Hey " + userRegistration.getUserName() + " Your One Time Password [OTP] is : " +generateSmsOtp);

			smsController.smsSubmit(smspojo);
			

		}
		return generateSmsOtp;
		
	}
	
	@Override
	public UserRegistration updatingUserPhoneNumber(UserRegistration userRegistration, String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(getUserOpt.isPresent()) {
			UserRegistration user = getUserOpt.get();
			user.setPhoneNo(userRegistration.getPhoneNo());
			return this.userRegistrationRepository.save(user);
		}
		else {
			throw new HandleException("User not exist, Please Register");
		}
	}
	
	@Override
	public Address getAddress(String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(getUserOpt.isPresent()) {
			return getUserOpt.get().getAddress();
		}
		else {
			throw new HandleException("User not exist, Please Register");
		}
	}
	
	
	@Override
	public UserRegistration changeUserAddress(Address address, String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(getUserOpt.isPresent()) {
			UserRegistration user = getUserOpt.get();
			user.setAddress(address);
			return this.userRegistrationRepository.save(user);
		}
		else {
			throw new HandleException("User not exist, Please Register");
		}
	}

	@Override
	public Integer sendOtpOnEmail(EmailDto emailDto, String string) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findByUserIdAndEmailId(string, emailDto.getEmailId());
		if(getUserOpt.isPresent()) {
			Random random = new Random();
		//	System.out.println("sending email now");
			generateOtp = random.nextInt(999999);
			String message =""+"<h3>"+"Hey "+getUserOpt.get().getUserName()+ "!"+"</h3>"+"<p>"+"You have requested a password reset for your account. Please use this OTP to verify your email address and reset your password."+"</p>"+"<strong>"+"Your One-Time Password (OTP) for verification is: "+generateOtp+"</strong>";
			String subject = "Password Reset OTP";
			String to = getUserOpt.get().getEmailId();
			String from = "krativarshne@gmail.com";
			this.emailService.sendEmail(message,subject,to,from);
			return generateOtp;
		}
		else {
			throw new HandleException("Email id is not present. Please register first");
		}
	}

	@Override
	public UserRegistration savePassword(Password password, String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(getUserOpt.isPresent()) {
			//userRegistration.setPassword(BCrypt.hashpw(userRegistration.getPassword(), BCrypt.gensalt()));
			getUserOpt.get().setPassword(BCrypt.hashpw(password.getPassword(), BCrypt.gensalt()));
			return this.userRegistrationRepository.save(getUserOpt.get());
		}
		else {
			throw new HandleException("User not exist");
		}
	}
	
	@Override
	public Integer sendUserEmailForUpdatePin(String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(getUserOpt.isPresent()) {
			UserRegistration user = getUserOpt.get();
			Random random = new Random();
			generateOtp = random.nextInt(999999);
			String message =""+"<h3>"+"Hey "+user.getUserName()+ ","+"</h3>"+
					"<p>"+"As you are requested for changing card PIN"+"</p>"
									+ ""+"<strong>"+"Your OTP is : "+"</strong>"+"" +generateOtp+ ""
												+ "<p>"+" Please ensure to keep it safe with you, Dont't share it with anyone."+"</p>";
												
					String subject = "Verify User Email";
					String to = user.getEmailId();
					String from = "krativarshne@gmail.com";
					this.emailService.sendEmail(message,subject,to,from);
					return generateOtp;
			
		}else {
			throw new HandleException("User nor present, Please Register.");
		}
	}
	
	@Override
	public CardDetails changeUserCardPin(CardDetails cardDetails, String userId) throws HandleException {
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(getUserOpt.isPresent()) {
			UserRegistration user = getUserOpt.get();
			CardDetails userCard = user.getCardDetails();
			userCard.setCardPin(cardDetails.getCardPin());
			return this.cardDetailsRepository.save(userCard);
			
		}
		else {
			throw new HandleException("User not exist, Register first.");
		}
		
	}


	@Override
	public UserRegistration fetchingUser(UserLogin userLogin) throws HandleException {
		Optional<UserRegistration> userEmailOpt = this.userRegistrationRepository.findByEmailId(userLogin.getEmailId());
		if(userEmailOpt.isPresent()) {
			return userEmailOpt.get();
		}
		else {
			throw new HandleException("User with this Email not exist.");
		}
	}

}
