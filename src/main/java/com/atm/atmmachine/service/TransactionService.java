package com.atm.atmmachine.service;

import java.util.List;

import com.atm.atmmachine.dto.CheckBalance;
import com.atm.atmmachine.dto.FundTransfer;
import com.atm.atmmachine.dto.WithdrawAmount;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.exceptions.TransactionException;

public interface TransactionService {

	List<TransactionDetails> getAllTransactions();
	Double withdrawFunds(WithdrawAmount withdraw) throws TransactionException;
	Double fundTransfer(FundTransfer fundTransfer)  throws TransactionException;
	Double checkBalance(CheckBalance checkBalance) throws TransactionException;

}
