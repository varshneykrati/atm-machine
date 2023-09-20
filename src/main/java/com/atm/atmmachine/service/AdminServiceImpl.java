package com.atm.atmmachine.service;

import java.util.List;
import java.util.Optional;

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
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.entity.UserRequest.RequestStatus;
import com.atm.atmmachine.exceptions.AdminException;
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
	public List<UserRequest> displayAllRequest() {
		return this.userRequestRepository.findAll();
	}

	/************************************************************************************
	 * Method: - displayRequestByStatus Description: to see all pending requests
	 * 
	 *@returns returns all pending requests
	 * 
	 *Created By Shivam 
	 *Created Date 12th sept, 2023
	 * 
	 ************************************************************************************/
	@Override
	public List<UserRequest> displayRequestByStatus() {

		return this.userRequestRepository.findByRequestStatus(RequestStatus.Pending);
	}

	/************************************************************************************
	 * Method: - displayByRequest Description: - to see all pending card lost, card
	 * replcament and increment card type.
	 * 
	 *@param request - type of request
	 * 
	 *Created By -Shivam 
	 *Created Date -12th sept, 2023
	 * 
	 ************************************************************************************/
	@Override
	public List<UserRequest> displayByRequest(String request) {

		return this.userRequestRepository.findByRequestAndRequestStatus(request, RequestStatus.Pending);
	}

	/************************************************************************************
	 * Method: - updateUserRequestStatus Description: - to approve all pending card
	 * lost, card replacement and increment card type request and sending sms on
	 * appropriate action.
	 * 
	 *@param requestId - requestId to approve
	 * 
	 *Created By -Shivam 
	 *Created Date -12th sept, 2023
	 * 
	 ************************************************************************************/
	@Override
	public UserRequest updateUserRequestStatus(String requestId) throws AdminException {
		Optional<UserRequest> foundUserRequest = this.userRequestRepository.findById(requestId);
		if (!foundUserRequest.isPresent())
			throw new AdminException("Request doesn't exist");

		CardDetails cardDetailsOfRequestedUser = foundUserRequest.get().getUserRegistration().getCardDetails();

		String accountNumber = cardDetailsOfRequestedUser.getAccountNumber().toString();
		int lastFourDigitOfAccountNumber = Integer.parseInt(accountNumber.substring(8, 12));

		if (foundUserRequest.get().getRequest().equals("Card Block")) {

			cardDetailsOfRequestedUser.setCardstatus(CardStatus.Inactive);

			foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
			Optional<UserRegistration> foundUser = this.userRegistrationRepository.findById(requestId);
			this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
			this.userRequestRepository.save(foundUserRequest.get());

			smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
			smspojo.setMessage("Your Card is Inactivated as per your card Lost Request on Account Number " + "XXXXXXXX"
					+ lastFourDigitOfAccountNumber);

			smsController.smsSubmit(smspojo);

		} else if (foundUserRequest.get().getRequest().equals("Card Replacement")) {

			if (cardDetailsOfRequestedUser.getAmount() < 250.0) {
				smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
				smspojo.setMessage("An amount of INR " + 250.0
						+ " should be available in your account for card replacement on your Account Number "
						+ "XXXXXXXX" + lastFourDigitOfAccountNumber + " on " + LocalDateTime.now()
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
				smspojo.setMessage("An amount of INR " + 250.0 + " has been debited from your Account " + "XXXXXXXX"
						+ lastFourDigitOfAccountNumber + " on " + org.joda.time.LocalDate.now()
						+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

				smsController.smsSubmit(smspojo);

				foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
				this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
				this.userRequestRepository.save(foundUserRequest.get());
			}

		} else if (foundUserRequest.get().getRequest().equals("Upgrade Card Type")) {

			if (cardDetailsOfRequestedUser.getCardType().equals(CardType.Silver)) {

				if (cardDetailsOfRequestedUser.getAmount() < 250.0) {
					smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
					smspojo.setMessage("An amount of INR " + 250.0
							+ " should be available in your account for increment card type in your Account Number "
							+ "XXXXXXXX" + lastFourDigitOfAccountNumber + " on " + LocalDateTime.now()
							+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

					smsController.smsSubmit(smspojo);
				} else {
					TransactionDetails transaction = new TransactionDetails();
					
					cardDetailsOfRequestedUser.setCardType(CardType.Gold);
					cardDetailsOfRequestedUser.setCardLimit(50000.0);
					Double changedAmount = (cardDetailsOfRequestedUser.getAmount() - 250);
					cardDetailsOfRequestedUser.setAmount(changedAmount);
					this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
					foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
					this.userRequestRepository.save(foundUserRequest.get());

					smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
					smspojo.setMessage("An amount of INR " + 250.0 + "  has been debited from your Account  "
							+ "XXXXXXXX" + lastFourDigitOfAccountNumber + " on " + LocalDateTime.now()
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

			if (cardDetailsOfRequestedUser.getCardType().equals(CardType.Gold)) {
				if (cardDetailsOfRequestedUser.getAmount() < 350.0) {
					smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
					smspojo.setMessage("An amount of INR " + 350.0
							+ " should be available in your account for increment card type in your Account Number "
							+ "XXXXXXXX" + lastFourDigitOfAccountNumber + " on " + LocalDateTime.now()
							+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

					smsController.smsSubmit(smspojo);
				} else {
					TransactionDetails transaction = new TransactionDetails();
					
					cardDetailsOfRequestedUser.setCardType(CardType.Platinum);
					cardDetailsOfRequestedUser.setCardLimit(75000.0);
					Double changedAmount = (cardDetailsOfRequestedUser.getAmount() - 350);
					cardDetailsOfRequestedUser.setAmount(changedAmount);
					this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
					foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
					this.userRequestRepository.save(foundUserRequest.get());

					smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
					smspojo.setMessage("An amount of INR " + 350.0 + "  has been debited from your Account  "
							+ "XXXXXXXX" + lastFourDigitOfAccountNumber + " on " + org.joda.time.LocalDate.now()
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
	
	
	/************************************************************************************
	 * Method: - setAdminRemark 
	 * Description: - to set the admin remark of specific requestId
	 * 
	 *@param requestId - requestId to approve
	 *@param remark -  description set by admin to a specific request
	 * 
	 *Created By -Shivam 
	 *Created Date -12th sept, 2023
	 * 
	 ************************************************************************************/
	@Override
	public String setAdminRemark(String reqId, String remark) throws AdminException {
		Optional<UserRequest> foundUser = this.userRequestRepository.findById(reqId);
		if (!foundUser.isPresent())
			throw new AdminException("Request doesn't exist");

		foundUser.get().setAdminRemark(remark);
		this.userRequestRepository.save(foundUser.get());
		return foundUser.get().getAdminRemark();
	}

	/************************************************************************************
	 * Method: - changeCardLimit 
	 * Description: - to change card limit of specific card type
	 * 
	 *@param cardlimit - cardlimit to set for specific card type
	 * 
	 *Created By -Shivam 
	 *Created Date -12th sept, 2023
	 * 
	 ************************************************************************************/
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

	/************************************************************************************
	 * Method: -  sumOfTodayTransaction
	 * Description: - to get sum of transaction datewise
	 * 
	 *Created By -Shivam 
	 *Created Date -12th sept, 2023
	 * 
	 ************************************************************************************/
	@Override
	public List<TransactionDateInfo> sumOfTodayTransaction() {
		return this.transactionRepository.getSumOfTransaction();
	}

}
