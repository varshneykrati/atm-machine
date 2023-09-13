package com.atm.atmmachine.controller;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.atmmachine.dto.UserInfo;
import com.atm.atmmachine.dto.FundTransferInfo;
import com.atm.atmmachine.dto.SelfTransferInfo;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.exceptions.TransactionException;
import com.atm.atmmachine.service.TransactionService;
import com.atm.atmmachine.sms.SMSController;
import com.atm.atmmachine.sms.SmsPojo;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@GetMapping("transactions/{userId}")
	public List<TransactionDetails> getAllTransactionById(@PathVariable String userId) {
		List<TransactionDetails> transactioncollection;
		transactioncollection = this.transactionService.getAllTransactionsById(userId);
		return transactioncollection;
	}
	@GetMapping("/transactions")
    public List<TransactionDetails>getAllTransactions()
	{
		return this.transactionService.getAllTransactions();

	}

	@PostMapping("/selfwithdraw/funds/")
	public Double withdrawFunds(@RequestBody SelfTransferInfo withdraw) throws TransactionException {

		return this.transactionService.withdrawFunds(withdraw);

	}

	@PostMapping("/fund/deposit")
	public Double addFunds(@RequestBody SelfTransferInfo addFund) throws TransactionException {

		return this.transactionService.addFunds(addFund);

	}

	@PostMapping("/funds/transfer/")
	public Double fundsTransfer(@RequestBody FundTransferInfo fundTransfer) throws TransactionException {

		return this.transactionService.fundTransfer(fundTransfer);

	}
	
	@PostMapping("/account/balance/")
	public Double checkBalances(@RequestBody UserInfo checkBalance) throws TransactionException {

		return this.transactionService.checkBalance(checkBalance);

	}

}
