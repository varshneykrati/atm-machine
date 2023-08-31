package com.atm.atmmachine.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.dto.CheckBalance;
import com.atm.atmmachine.dto.FundTransfer;
import com.atm.atmmachine.dto.WithdrawAmount;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.CardDetails.CardStatus;
import com.atm.atmmachine.entity.CardDetails.UserTotallyRegister;
import com.atm.atmmachine.entity.TransactionDetails;
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
	TransactionRepository transRepo;

	@Autowired
	CardDetailsRepository cardRepo;

	@Autowired
	UserRegistrationRepository userRepo;

	@Autowired
	SMSController smsController;

	SmsPojo smspojo = new SmsPojo();
	TransactionDetails transaction = new TransactionDetails();

	@Override
	public List<TransactionDetails> getAllTransactions() {
		return this.transRepo.findByOrderByTransactionDateDesc();
	}

	@Override
	public Double withdrawFunds(WithdrawAmount withdraw) throws TransactionException {
		Double total = 0.0;
		String userId = withdraw.getUserId();
		Double withdrawAmount = withdraw.getTransactionAmount();
		Optional<UserRegistration> UserOpt = this.userRepo.findById(userId);
		if (UserOpt.isEmpty()) {
			throw new TransactionException("User not present");
		}
		UserRegistration foundUser = UserOpt.get();
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
		if (!foundCard.getCardPin().equals(withdraw.getCardPin())) {
			throw new TransactionException("Wrong Card Pin,Please enter again");
		}
		if (foundCard.getAmount() < withdrawAmount) {

			throw new TransactionException("Insufficient Balance");
		}
		List<TransactionDetails> cardLimitCheck = this.transRepo.findByTransactionDateAndCardDetails(LocalDate.now(),
				foundCard);
		if (cardLimitCheck.size() > 0) {

			for (TransactionDetails transaction : cardLimitCheck)
				total += transaction.getBalance();

			if (foundCard.getCardLimit() < withdrawAmount + total) {

				throw new TransactionException("Exceeds CardLimit");
			}
		}
		foundCard.setAmount(foundCard.getAmount() - withdrawAmount);
		this.cardRepo.save(foundCard);

		transaction.setCardDetails(foundCard);
		transaction.setToAccountNumber(foundCard.getAccountNumber());
		transaction.setTransactionDate(LocalDate.now());
		transaction.setBalance(withdrawAmount);
		this.transRepo.save(transaction);

		smspojo.setMessage("An amount of INR " + withdrawAmount + " has been debited to your Account "
				+ foundCard.getAccountNumber() + " on " + org.joda.time.LocalDate.now() + ".Total Avail.bal INR "
				+ foundCard.getAmount());

		smsController.smsSubmit(smspojo);
		return foundCard.getAmount();
	}

	@Override
	public Double fundTransfer(FundTransfer fundTransfer) throws TransactionException {

		String userId = fundTransfer.getUserId();
		BigInteger toAccountNumber = fundTransfer.getToAccountNumber();
		Double fundTransactionAmount = fundTransfer.getTransactionAmount();
		Double total = 0.0;
		//RazorPay
		Optional<UserRegistration> UserOpt = this.userRepo.findById(userId);
		if (UserOpt.isEmpty()) {
			throw new TransactionException("User not present");
		}
		UserRegistration foundUser = UserOpt.get();
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
		if (!foundCard.getCardPin().equals(fundTransfer.getCardPin())) {
			throw new TransactionException("Wrong Card Pin,Please enter again");
		}
		if (foundCard.getAmount() < fundTransactionAmount) {

			throw new TransactionException("Insufficient Balance");
		}
		List<TransactionDetails> cardLimitCheck = this.transRepo.findByTransactionDateAndCardDetails(LocalDate.now(),
				foundCard);
		if (cardLimitCheck.size() > 0) {

			for (TransactionDetails transaction : cardLimitCheck)
				total += transaction.getBalance();
			if (foundCard.getCardLimit() < fundTransactionAmount) {

				throw new TransactionException("Exceeds CardLimit");
			}
		}
		Optional<CardDetails> CardOpt = this.cardRepo.findByAccountNumber(toAccountNumber);
		if (CardOpt.isEmpty()) {
			throw new TransactionException("Account no. dosen't exists,Re-enter the correct account number");
		}
		CardDetails tofoundCard = CardOpt.get();
		if (tofoundCard.getUserTotallyRegister() == UserTotallyRegister.False) {
			throw new TransactionException("Receiver is not totally Registered");
		}
		if (tofoundCard.getCardstatus() == CardStatus.Inactive) {

			throw new TransactionException("Receiver's card is inactive");
		}
		foundCard.setAmount(foundCard.getAmount() - fundTransactionAmount);
		this.cardRepo.save(foundCard);
		smspojo.setTo(foundUser.getPhoneNo());
		smspojo.setMessage("An amount of INR " + fundTransactionAmount + " has been debited to your Account "
				+ foundCard.getAccountNumber() + " on " + LocalDate.now() + ".Total Avail.bal INR "
				+ foundCard.getAmount());

		smsController.smsSubmit(smspojo);

		tofoundCard.setAmount(tofoundCard.getAmount() + fundTransactionAmount);
		this.cardRepo.save(tofoundCard);
		smspojo.setTo(tofoundCard.getUserRegistration().getPhoneNo());
		smspojo.setMessage("An amount of INR " + fundTransactionAmount + " has been credited to your Account "
				+ tofoundCard.getAccountNumber() + " on " + LocalDate.now() + ".Total Avail.bal INR "
				+ tofoundCard.getAmount());

		smsController.smsSubmit(smspojo);
		TransactionDetails transaction = new TransactionDetails();
		transaction.setCardDetails(foundCard);
		transaction.setToAccountNumber(toAccountNumber);
		transaction.setTransactionDate(LocalDate.now());
		transaction.setBalance(fundTransactionAmount);
		this.transRepo.save(transaction);
		return foundCard.getAmount();
	}

	@Override
	public Double checkBalance(CheckBalance checkBalance) throws TransactionException {
		
		String userId=checkBalance.getUserId();
		Optional<UserRegistration> UserOpt = this.userRepo.findById(userId);
		UserRegistration foundUser = UserOpt.get();
		CardDetails foundCard=foundUser.getCardDetails();
		if(!foundCard.getCardPin().equals(checkBalance.getCardPin())) {
			throw new TransactionException("Wrong Card Pin,Please enter again");
		}
		return foundCard.getAmount();
	}

}
