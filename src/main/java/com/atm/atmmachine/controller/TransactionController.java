package com.atm.atmmachine.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.atmmachine.dto.UserInfo;
import com.atm.atmmachine.dto.FundTransferDto;
import com.atm.atmmachine.dto.SelfTransferInfo;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.exceptions.TransactionException;
import com.atm.atmmachine.service.TransactionService;
import com.atm.atmmachine.sms.SMSController;
import com.atm.atmmachine.sms.SmsPojo;

@RestController
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	@GetMapping("transactions/{userId}")
	public List<TransactionDetails> getAllTransaction(@PathVariable String userId) {
		List<TransactionDetails> transactioncollection;
		transactioncollection = this.transactionService.getAllTransactions(userId);
		return transactioncollection;
	}

	@PostMapping("/withdraw/funds")
	public Double withdrawfunds(@RequestBody SelfTransferInfo withdraw) throws TransactionException {

		return this.transactionService.withdrawFunds(withdraw);

	}
	@PostMapping("/add/funds")
	public Double addfunds(@RequestBody SelfTransferInfo addFund) throws TransactionException {

		return this.transactionService.addFunds(addFund);

	}

	@PostMapping("/funds/transfer/")
	public Double fundsTransfer(@RequestBody FundTransferDto fundTransfer) throws TransactionException {

		return this.transactionService.fundTransfer(fundTransfer);

	}

	@PostMapping("/account/balance/")
	public Double checkBalance(@RequestBody UserInfo checkBalance) throws TransactionException {

		return this.transactionService.checkBalance(checkBalance);

	}

}
