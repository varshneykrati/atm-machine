package com.atm.atmmachine.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.dto.CardLimit;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.TransactionDetails;
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
	TransactionDetails transaction = new TransactionDetails();

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
	public List<UserRequest> displayByRequest() {
		
		return this.userRequestRepository.findByRequestAndRequestStatus("Card Lost",RequestStatus.Pending);
	}

	@Override
	public List<UserRequest> displayAllCardReplacementRequests() {

		return this.userRequestRepository.findByRequestAndRequestStatus("Card Replacement",RequestStatus.Pending);
	}

	@Override
	public UserRequest updateUserRequestStatus(String requestId) throws AdminException {
		Optional<UserRequest> foundUserRequest = this.userRequestRepository.findById(requestId);
		if (!foundUserRequest.isPresent())
			throw new AdminException("Request doesn't exist");

		if (foundUserRequest.get().getRequest().equals("Card Lost")) {

			CardDetails cardDetailsOfRequestedUser = foundUserRequest.get().getUserRegistration().getCardDetails();
			cardDetailsOfRequestedUser.setCardstatus(CardStatus.Inactive);

			foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
			Optional<UserRegistration> foundUser = this.userRegistrationRepository.findById(requestId);
			this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
			this.userRequestRepository.save(foundUserRequest.get());

		} else if (foundUserRequest.get().getRequest().equals("Card Replacement")) {
			CardDetails cardDetailsOfRequestedUser = foundUserRequest.get().getUserRegistration().getCardDetails();
			

			if (cardDetailsOfRequestedUser.getAmount() < 250.0) {
				smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
				smspojo.setMessage(
						"An amount of INR " + 250.0 + " should be available in your account for card replacement on your Account Number"
								+ cardDetailsOfRequestedUser.getAccountNumber() + " on " + org.joda.time.LocalDate.now()
								+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

				smsController.smsSubmit(smspojo);
			} else {

				Double changedAmount = (cardDetailsOfRequestedUser.getAmount() - 250);
				LocalDate oldValidThroughDate = cardDetailsOfRequestedUser.getValidThrough();
				LocalDate newValidThroughDate = oldValidThroughDate.plusYears(5);

				cardDetailsOfRequestedUser.setValidThrough(newValidThroughDate);
				cardDetailsOfRequestedUser.setAmount(changedAmount);
				this.cardDetailsRepository.save(cardDetailsOfRequestedUser);

				transaction.setCardDetails(cardDetailsOfRequestedUser);
				transaction.setFromAccountNumber(cardDetailsOfRequestedUser.getAccountNumber());
				transaction.setTransactionDate(LocalDate.now());
				transaction.setParticulars("Card Replacement Request is Approved");
				transaction.setBalance(250.0);

				this.transactionRepository.save(transaction);

				smspojo.setTo(foundUserRequest.get().getUserRegistration().getPhoneNo());
				smspojo.setMessage("An amount of INR " + 250.0 + " has been debited from your Account "
						+ cardDetailsOfRequestedUser.getAccountNumber() + " on " + org.joda.time.LocalDate.now()
						+ ".Total Avail.bal INR " + cardDetailsOfRequestedUser.getAmount());

				smsController.smsSubmit(smspojo);

				foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
				this.cardDetailsRepository.save(cardDetailsOfRequestedUser);
				this.userRequestRepository.save(foundUserRequest.get());
			}

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

}
