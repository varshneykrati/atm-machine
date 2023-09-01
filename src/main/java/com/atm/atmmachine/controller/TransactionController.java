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

import com.atm.atmmachine.dto.CheckBalance;
import com.atm.atmmachine.dto.FundTransfer;
import com.atm.atmmachine.dto.WithdrawAmount;
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
	
	
	
	@GetMapping("/user/")
		public String Vaishnav() {
			return "Hi Users!";
		}
	
	@GetMapping("transactions")
	public List<TransactionDetails> getAllTransaction(){
		List<TransactionDetails> transactioncollection=this.transactionService.getAllTransactions();
		return transactioncollection;
	}
	
	@PostMapping("/withdraw/")
	public Double withdrawfunds(@RequestBody WithdrawAmount withdraw) throws TransactionException { 
		try {
			return this.transactionService.withdrawFunds(withdraw);
		} catch (TransactionException e) {
			throw e;
		}
	}
	

	@PostMapping("/fundTransfer/")
	public Double withdrawfunds(@RequestBody FundTransfer fundTransfer) throws TransactionException { 
			try {
				return this.transactionService.fundTransfer(fundTransfer);
			} catch (TransactionException e) {
				throw e;
			}
		
	}
	
	@PostMapping("/checkBalance/")
	public Double checkBalance(@RequestBody CheckBalance checkBalance)throws TransactionException{
		try {
			return this.transactionService.checkBalance(checkBalance);
		}catch (TransactionException e) {
			throw e;
		}
		
	}
	
}

 