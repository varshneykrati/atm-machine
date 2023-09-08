package com.atm.atmmachine.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.dto.UserInfo;
import com.atm.atmmachine.dto.FundTransferInfo;
import com.atm.atmmachine.dto.SelfTransferInfo;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.CardDetails.CardStatus;
import com.atm.atmmachine.entity.CardDetails.UserTotallyRegister;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.entity.TransactionDetails.TransactionType;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;
import com.atm.atmmachine.exceptions.TransactionException;
import com.atm.atmmachine.repository.CardDetailsRepository;
import com.atm.atmmachine.repository.TransactionRepository;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.sms.SMSController;
import com.atm.atmmachine.sms.SmsPojo;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transRepository;

	@Autowired
	private CardDetailsRepository cardDetailsRepository;

	@Autowired
	private UserRegistrationRepository userRegistrationRepository;

	@Autowired
	SMSController smsController;

	SmsPojo smspojo = new SmsPojo();

	String wrongPin = "Wrong Card Pin,Please enter again";
	String userPresent = "User not present";

	@Override
	public List<TransactionDetails> getAllTransactionsById(String userId) {
		Optional<UserRegistration> userOpt = this.userRegistrationRepository.findById(userId);
		CardDetails foundCard = userOpt.get().getCardDetails();
		return this.transRepository.findByCardDetailsOrderByTransactionDateDesc(foundCard);
	}

	public List<TransactionDetails> getAllTransactions() {
		return this.transRepository.findAll();

	}

	@Override
	public Double withdrawFunds(SelfTransferInfo withdraw) throws TransactionException {
		Double total = 0.0;
		String userId = withdraw.getUserId();
		Double withdrawAmount = withdraw.getTransactionAmount();
		Optional<UserRegistration> userOpt = this.userRegistrationRepository.findById(userId);
		if (!userOpt.isPresent()) {
			throw new TransactionException(userPresent);
		}
		UserRegistration foundUser = userOpt.get();
		if (foundUser.getUserRegistrationApproval() == UserRegistrationApproval.Inactive) {
			throw new TransactionException("User Registration is not yet approved");
		}
		CardDetails foundCard = foundUser.getCardDetails();
		if (foundCard.getUserTotallyRegister() == UserTotallyRegister.False) {
			throw new TransactionException("User is not totally Registered,Complete the user profile");
		}
		if (foundCard.getCardstatus() == CardStatus.Inactive) {

			throw new TransactionException("Activate your Card");
		}

		LocalDate oldCurrentDate = LocalDate.now();
		LocalDate newCurrentDate = oldCurrentDate.plusDays(1);
		if (foundCard.getValidThrough().equals(newCurrentDate)) {
			throw new TransactionException("Card Validity is Expired");
		}

		if (!foundCard.getCardPin().equals(withdraw.getCardPin())) {
			throw new TransactionException(wrongPin);
		}
		if (foundCard.getAmount() < withdrawAmount) {

			throw new TransactionException("Insufficient Balance");
		}
		List<TransactionDetails> cardLimitCheck = this.transRepository
				.findByTransactionDateAndCardDetails(LocalDate.now(), foundCard);
		if (cardLimitCheck.size() > 0) {

			for (TransactionDetails transaction : cardLimitCheck)
				total += transaction.getBalance();

			if (foundCard.getCardLimit() < withdrawAmount + total) {

				throw new TransactionException("Exceeds CardLimit");
			}
		}
		foundCard.setAmount(foundCard.getAmount() - withdrawAmount);
		this.cardDetailsRepository.save(foundCard);
		TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.setCardDetails(foundCard);
		transactionDetails.setFromAccountNumber(foundCard.getAccountNumber());
		transactionDetails.setParticulars(foundCard.getUserRegistration().getUserName());
		transactionDetails.setTransactionDate(LocalDate.now());
		transactionDetails.setBalance(withdrawAmount);
		transactionDetails.setTransactionType(TransactionType.Withdrawal);
		this.transRepository.save(transactionDetails);

		smspojo.setTo(foundUser.getPhoneNo());
		smspojo.setMessage("An amount of INR " + withdrawAmount + " has been debited to your Account "
				+ foundCard.getAccountNumber() + " on " + org.joda.time.LocalDate.now() + ".Total Avail.bal INR "
				+ foundCard.getAmount());

		smsController.smsSubmit(smspojo);
		return foundCard.getAmount();
	}

	@Override
	public Double addFunds(SelfTransferInfo addFund) throws TransactionException {
		String userId = addFund.getUserId();
		Double addAmount = addFund.getTransactionAmount();
		Optional<UserRegistration> userOpt = this.userRegistrationRepository.findById(userId);
		if (!userOpt.isPresent()) {
			throw new TransactionException(userPresent);
		}
		UserRegistration foundUser = userOpt.get();
		if (foundUser.getUserRegistrationApproval() == UserRegistrationApproval.Inactive) {
			throw new TransactionException("User Registration is not yet approved");
		}
		smspojo.setTo(foundUser.getPhoneNo());
		CardDetails foundCard = foundUser.getCardDetails();
		if (foundCard.getUserTotallyRegister() == UserTotallyRegister.False) {
			throw new TransactionException("User is not totally Registered,Complete the user profile");
		}
		if (foundCard.getCardstatus() == CardStatus.Inactive) {

			throw new TransactionException("Activate your Card");
		}
		LocalDate oldCurrentDate = LocalDate.now();
		LocalDate newCurrentDate=oldCurrentDate.plusDays(1);
		if(foundCard.getValidThrough().equals(newCurrentDate)){
			throw new TransactionException("Card Validity is Expired");
		}
		if (!foundCard.getCardPin().equals(addFund.getCardPin())) {
			throw new TransactionException(wrongPin);
		}

		foundCard.setAmount(foundCard.getAmount() + addAmount);
		this.cardDetailsRepository.save(foundCard);
		TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.setCardDetails(foundCard);
		transactionDetails.setToAccountNumber(foundCard.getAccountNumber());
		transactionDetails.setParticulars(foundCard.getUserRegistration().getUserName());
		transactionDetails.setTransactionDate(LocalDate.now());
		transactionDetails.setBalance(addAmount);
		transactionDetails.setTransactionType(TransactionType.Deposit);
		this.transRepository.save(transactionDetails);

		smspojo.setMessage(
				"An amount of INR " + addAmount + " has been credited to your Account " + foundCard.getAccountNumber()
						+ " on " + org.joda.time.LocalDate.now() + ".Total Avail.bal INR " + foundCard.getAmount());

		smsController.smsSubmit(smspojo);
		return foundCard.getAmount();
	}

	@Override
	public Double fundTransfer(FundTransferInfo fundTransfer) throws TransactionException {

		String userId = fundTransfer.getUserId();
		BigInteger toAccountNumber = fundTransfer.getToAccountNumber();
		Double fundTransactionAmount = fundTransfer.getTransactionAmount();
		Double total = 0.0;
		// RazorPay
		Optional<UserRegistration> userOpt = this.userRegistrationRepository.findById(userId);
		if (!userOpt.isPresent()) {
			throw new TransactionException(userPresent);
		}
		UserRegistration foundUser = userOpt.get();
		if (foundUser.getUserRegistrationApproval() == UserRegistrationApproval.Inactive) {
			throw new TransactionException("User Registration is not yet approved");
		}
		CardDetails foundCard = foundUser.getCardDetails();
		if (foundCard.getUserTotallyRegister() == UserTotallyRegister.False) {
			throw new TransactionException("User is not totally Registered, please complete the user profile first.");
		}
		if (foundCard.getCardstatus() == CardStatus.Inactive) {

			throw new TransactionException("Activate your Card");
		}
		LocalDate oldCurrentDate = LocalDate.now();
		LocalDate newCurrentDate=oldCurrentDate.plusDays(1);
		if(foundCard.getValidThrough().equals(newCurrentDate)){
			throw new TransactionException("Card Validity is Expired");
		}
		if (!foundCard.getCardPin().equals(fundTransfer.getCardPin())) {
			throw new TransactionException(wrongPin);
		}
		if (foundCard.getAmount() < fundTransactionAmount) {

			throw new TransactionException("Insufficient Balance");
		}
		List<TransactionDetails> cardLimitCheck = this.transRepository
				.findByTransactionDateAndCardDetails(LocalDate.now(), foundCard);
		if (cardLimitCheck.size() > 0) {

			for (TransactionDetails transaction : cardLimitCheck)
				total += transaction.getBalance();
			if (foundCard.getCardLimit() < fundTransactionAmount + total) {

				throw new TransactionException("Exceeds CardLimit");
			}
		}

		// check length of account no and patttern and null
		Optional<CardDetails> cardOpt = this.cardDetailsRepository.findByAccountNumber(toAccountNumber);
		if (cardOpt.isEmpty()) {
			throw new TransactionException("Account no. dosen't exists,Re-enter the correct account number");
		}
		CardDetails tofoundCard = cardOpt.get();
		if (tofoundCard.getUserTotallyRegister() == UserTotallyRegister.False) {
			throw new TransactionException("Receiver is not totally Registered");
		}
//		if (tofoundCard.getCardstatus() == CardStatus.Inactive) {
//
//			throw new TransactionException("Receiver's card is inactive");
//		}
		
		foundCard.setAmount(foundCard.getAmount() - fundTransactionAmount);
		this.cardDetailsRepository.save(foundCard);
		smspojo.setTo(foundUser.getPhoneNo());
		smspojo.setMessage("An amount of INR " + fundTransactionAmount + " has been debited to your Account "
				+ foundCard.getAccountNumber() + " on " + LocalDate.now() + ".Total Avail.bal INR "
				+ foundCard.getAmount());

		smsController.smsSubmit(smspojo);
		TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.setCardDetails(foundCard);
		transactionDetails.setToAccountNumber(toAccountNumber);
		transactionDetails.setParticulars(cardOpt.get().getUserRegistration().getUserName());
		transactionDetails.setTransactionDate(LocalDate.now());
		transactionDetails.setBalance(fundTransactionAmount);
		transactionDetails.setTransactionType(TransactionType.Withdrawal);
		this.transRepository.save(transactionDetails);

		tofoundCard.setAmount(tofoundCard.getAmount() + fundTransactionAmount);
		this.cardDetailsRepository.save(tofoundCard);
		smspojo.setTo(tofoundCard.getUserRegistration().getPhoneNo());
		smspojo.setMessage("An amount of INR " + fundTransactionAmount + " has been credited to your Account "
				+ tofoundCard.getAccountNumber() + " on " + LocalDate.now() + ".Total Avail.bal INR "
				+ tofoundCard.getAmount());

		smsController.smsSubmit(smspojo);
		TransactionDetails toTransactionDetails = new TransactionDetails();
		toTransactionDetails.setCardDetails(tofoundCard);
		toTransactionDetails.setFromAccountNumber(foundCard.getAccountNumber());
		toTransactionDetails.setParticulars(tofoundCard.getUserRegistration().getUserName());
		toTransactionDetails.setTransactionDate(LocalDate.now());
		toTransactionDetails.setBalance(fundTransactionAmount);
		toTransactionDetails.setTransactionType(TransactionType.Deposit);
		this.transRepository.save(toTransactionDetails);
		return foundCard.getAmount();
	}

	@Override
	public Double checkBalance(UserInfo checkBalance) throws TransactionException {

		String userId = checkBalance.getUserId();
		Optional<UserRegistration> userOpt = this.userRegistrationRepository.findById(userId);
		if (!userOpt.isPresent()) {
			throw new TransactionException(userPresent);
		}
		UserRegistration foundUser = userOpt.get();
		CardDetails foundCard = foundUser.getCardDetails();
		if (foundCard.getUserTotallyRegister() == UserTotallyRegister.False) {
			throw new TransactionException("User is not totally Registered, please complete the user profile first.");
		}
		if (!foundCard.getCardPin().equals(checkBalance.getCardPin())) {
			throw new TransactionException(wrongPin);
		}
		return foundCard.getAmount();
	}

	@Override
	public String getUsername(BigInteger accountNumber) {
		Optional<CardDetails> cardOpt = this.cardDetailsRepository.findByAccountNumber(accountNumber);
		CardDetails foundCard = cardOpt.get();
		UserRegistration foundUser = foundCard.getUserRegistration();
		return foundUser.getUserName();
	}

}
