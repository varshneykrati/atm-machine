package com.atm.atmmachine.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.dto.CardLimit;
import com.atm.atmmachine.dto.TransactionDateInfo;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.entity.TransactionDetails.TransactionType;
import com.atm.atmmachine.entity.CardDetails.CardStatus;
import com.atm.atmmachine.entity.CardDetails.CardType;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.entity.UserRequest.RequestStatus;
import com.atm.atmmachine.exceptions.AdminException;
import com.atm.atmmachine.idGenerator.VerhoeffAlgorithm;
import com.atm.atmmachine.repository.CardDetailsRepository;
import com.atm.atmmachine.repository.TransactionRepository;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.repository.UserRequestRepository;
import com.atm.atmmachine.sms.SMSController;
import com.atm.atmmachine.sms.SmsPojo;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRequestRepository userRequestRepository;

	@Autowired
	private UserRegistrationRepository userRegistrationRepository;

	@Autowired
	private CardDetailsRepository cardDetailsRepository;

	@Autowired
	private TransactionRepository transactionRepository;
	
	

	@Autowired
	SMSController smsController;

	SmsPojo smspojo = new SmsPojo();
	

	@Override
	public Optional<UserRegistration> findByUserId(String userId) {
		return this.userRegistrationRepository.findById(userId);

	}

	@Override
	public List<UserRequest> displayAllRequest() {
		return this.userRequestRepository.findAll();
	}

	@Override
	public List<UserRequest> displayRequestByStatus() {

		return this.userRequestRepository.findByRequestStatus(RequestStatus.Pending);
	}

	@Override
	public List<UserRequest> displayByRequest(String request) {
		
		return this.userRequestRepository.findByRequestAndRequestStatus(request,RequestStatus.Pending);
	}

	

	@Override
	public UserRequest updateUserRequestStatus(String requestId) throws AdminException {
		Optional<UserRequest> foundUserRequest = this.userRequestRepository.findById(requestId);
		if (!foundUserRequest.isPresent())
			throw new AdminException("Request doesn't exist");
		
		CardDetails cardDetailsOfRequestedUser = foundUserRequest.get().getUserRegistration().getCardDetails();
		
		String accountNumber = cardDetailsOfRequestedUser.getAccountNumber().toString();
		int lastFourDigitOfAccountNumber = Integer.parseInt(accountNumber.substring(8,12));

		if (foundUserRequest.get().getRequest().equals("Card Block")) {

			
			cardDetailsOfRequestedUser.setCardstatus(CardStatus.Inactive);

			foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
			Optional<UserRegistration> foundUser = this.userRegistrationRepository.findById(requestId);
			this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
			this.userRequestRepository.save(foundUserRequest.get());
			
			smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
			smspojo.setMessage("Your Card is Inactivated as per your card Lost Request on Account Number " + "XXXXXXXX"+lastFourDigitOfAccountNumber);

			smsController.smsSubmit(smspojo);

		} else if (foundUserRequest.get().getRequest().equals("Card Replacement")) {
			
			

			
			if (cardDetailsOfRequestedUser.getAmount() < 250.0) {
				smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
				smspojo.setMessage(
						"An amount of INR " + 250.0 + " should be available in your account for card replacement on your Account Number "
								+ "XXXXXXXX"+lastFourDigitOfAccountNumber + " on " + LocalDateTime.now()
								+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

				smsController.smsSubmit(smspojo);
			} else {

				TransactionDetails transaction = new TransactionDetails();
				Double changedAmount = (cardDetailsOfRequestedUser.getAmount() - 250);
				LocalDate oldValidThroughDate = cardDetailsOfRequestedUser.getValidThrough();
				LocalDate newValidThroughDate = oldValidThroughDate.plusYears(5);

				cardDetailsOfRequestedUser.setValidThrough(newValidThroughDate);
				cardDetailsOfRequestedUser.setCardstatus(CardStatus.Active);
				cardDetailsOfRequestedUser.setAmount(changedAmount);
				this.cardDetailsRepository.save(cardDetailsOfRequestedUser);

				transaction.setCardDetails(cardDetailsOfRequestedUser);
				transaction.setFromAccountNumber(cardDetailsOfRequestedUser.getAccountNumber());
				transaction.setTransactionDate(LocalDateTime.now());
				transaction.setParticulars("Card Replacement Request is Approved");
				transaction.setBalance(250.0);
				transaction.setTransactionType(TransactionType.Withdrawal);		

				this.transactionRepository.save(transaction);
				
				

				smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
				smspojo.setMessage("An amount of INR " + 250.0 + " has been debited from your Account "
						+ "XXXXXXXX"+lastFourDigitOfAccountNumber + " on " + org.joda.time.LocalDate.now()
						+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

				smsController.smsSubmit(smspojo);

				foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
				this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
				this.userRequestRepository.save(foundUserRequest.get());
			}

		}
		else if(foundUserRequest.get().getRequest().equals("Upgrade Card Type")) {
			
			if(cardDetailsOfRequestedUser.getCardType().equals(CardType.Silver)) {
				
				
				if (cardDetailsOfRequestedUser.getAmount() < 250.0) {
					smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
					smspojo.setMessage(
							"An amount of INR " + 250.0 + " should be available in your account for increment card type in your Account Number "
									+ "XXXXXXXX"+lastFourDigitOfAccountNumber +" on " + LocalDateTime.now()
									+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

					smsController.smsSubmit(smspojo);
				}else {
					TransactionDetails transaction = new TransactionDetails();
					//Double cardLimitOfGoldUsers = cardDetailsOfRequestedUser.getCardLimit();
					cardDetailsOfRequestedUser.setCardType(CardType.Gold);
					cardDetailsOfRequestedUser.setCardLimit(50000.0);
					Double changedAmount = (cardDetailsOfRequestedUser.getAmount() - 250);
					cardDetailsOfRequestedUser.setAmount(changedAmount);
					this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
					foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
					this.userRequestRepository.save(foundUserRequest.get());
					
					smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
					smspojo.setMessage(
							"An amount of INR " + 250.0 + "  has been debited from your Account  "
									+ "XXXXXXXX"+lastFourDigitOfAccountNumber + " on " + LocalDateTime.now()
									+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

					smsController.smsSubmit(smspojo);
					
					transaction.setCardDetails(cardDetailsOfRequestedUser);
					transaction.setFromAccountNumber(cardDetailsOfRequestedUser.getAccountNumber());
					transaction.setTransactionDate(LocalDateTime.now());
					transaction.setParticulars("Card Increment Request is Approved");
					transaction.setBalance(250.0);
					transaction.setTransactionType(TransactionType.Withdrawal);				
					this.transactionRepository.save(transaction);
					
					
				}
				
				
			}
			
			if(cardDetailsOfRequestedUser.getCardType().equals(CardType.Gold)) {
				if (cardDetailsOfRequestedUser.getAmount() < 350.0) {
					smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
					smspojo.setMessage(
							"An amount of INR " + 350.0 + " should be available in your account for increment card type in your Account Number "
									+ "XXXXXXXX"+lastFourDigitOfAccountNumber + " on " + LocalDateTime.now()
									+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

					smsController.smsSubmit(smspojo);
				}else {
					TransactionDetails transaction = new TransactionDetails();
					//Double cardLimitOfPlatinumUsers = cardDetailsOfRequestedUser.getCardLimit();
					cardDetailsOfRequestedUser.setCardType(CardType.Platinum);
					cardDetailsOfRequestedUser.setCardLimit(75000.0);
					Double changedAmount = (cardDetailsOfRequestedUser.getAmount() - 350);
					cardDetailsOfRequestedUser.setAmount(changedAmount);
					this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
					foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
					this.userRequestRepository.save(foundUserRequest.get());
					
					smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
					smspojo.setMessage(
							"An amount of INR " + 350.0 + "  has been debited from your Account  "
									+ "XXXXXXXX"+lastFourDigitOfAccountNumber + " on " + org.joda.time.LocalDate.now()
									+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

					smsController.smsSubmit(smspojo);
					
					transaction.setCardDetails(cardDetailsOfRequestedUser);
					transaction.setFromAccountNumber(cardDetailsOfRequestedUser.getAccountNumber());
					transaction.setTransactionDate(LocalDateTime.now());
					transaction.setParticulars("Card Increment Request is Approved");
					transaction.setBalance(350.0);
					transaction.setTransactionType(TransactionType.Withdrawal);		
					this.transactionRepository.save(transaction);
					
				}

			}
			
			this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
			
			
		}

		return foundUserRequest.get();
	}

	@Override
	public Optional<UserRequest> getRequestById(String requestId) throws AdminException {
		Optional<UserRequest> foundUserRequest = this.userRequestRepository.findById(requestId);
		if (!foundUserRequest.isPresent())
			throw new AdminException("Request doesn't exist");

		return foundUserRequest;
	}

	@Override
	public List<UserRegistration> displayUsersWithStatusInactive() {
		return this.userRegistrationRepository.findByUserRegistrationApproval(UserRegistrationApproval.Inactive);

	}

	@Override
	public UserRegistrationApproval updateUserRegistrationApproval(String userId) throws AdminException {
		Optional<UserRegistration> foundUser = this.userRegistrationRepository.findById(userId);
		if (!foundUser.isPresent())
			throw new AdminException("User doesn't exist");

		foundUser.get().setUserRegistrationApproval(UserRegistrationApproval.Active);
		this.userRegistrationRepository.save(foundUser.get());
		return foundUser.get().getUserRegistrationApproval();
	}

	@Override
	public Optional<UserRegistration> getUsersById(String userId) throws AdminException {
		Optional<UserRegistration> foundUserRegistration = this.userRegistrationRepository.findById(userId);
		if (!foundUserRegistration.isPresent())
			throw new AdminException("User doesn't exist");

		return foundUserRegistration;
	}

	@Override
	public String setAdminRemark(String reqId, String remark) throws AdminException {
		Optional<UserRequest> foundUser = this.userRequestRepository.findById(reqId);
		if (!foundUser.isPresent())
			throw new AdminException("Request doesn't exist");

		foundUser.get().setAdminRemark(remark);
		this.userRequestRepository.save(foundUser.get());
		return foundUser.get().getAdminRemark();
	}

	@Override
	public Double changeCardLimit(CardLimit cardLimit) throws AdminException {
		if (cardLimit.getCardType().equals("") && cardLimit.getChangedCardLimit().equals(0))
			throw new AdminException("Check inputs properly");
		List<CardDetails> foundCardDetails = this.cardDetailsRepository
				.findByCardType(CardType.valueOf(cardLimit.getCardType()));

		for (CardDetails c : foundCardDetails) {
			c.setCardLimit(cardLimit.getChangedCardLimit());
			this.cardDetailsRepository.save(c);
		}
		return cardLimit.getChangedCardLimit();

	}

	@Override
	public Boolean validAadharCard(String aadharNumber) {
		Pattern aadharPattern = Pattern.compile("\\d{12}");
		boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
		if (isValidAadhar) {
			isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
		}
		return isValidAadhar;

	}

	@Override
	public List<TransactionDateInfo> sumOfTodayTransaction() {
		return this.transactionRepository.getSumOfTransaction();
	}

	
	
	

}
