package com.atm.atmmachine.service;

import java.math.BigInteger;
import java.util.List;

import com.atm.atmmachine.dto.UserInfo;
import com.atm.atmmachine.dto.FundTransferInfo;
import com.atm.atmmachine.dto.SelfTransferInfo;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.exceptions.TransactionException;

public interface TransactionService {

	List<TransactionDetails> getAllTransactionsById(String userId);
	List<TransactionDetails> getAllTransactions();
	Double withdrawFunds(SelfTransferInfo withdraw) throws TransactionException;
	Double fundTransfer(FundTransferInfo fundTransfer)  throws TransactionException;
	Double checkBalance(UserInfo checkBalance) throws TransactionException;
	Double addFunds(SelfTransferInfo addFund) throws TransactionException;
	String getUsername(BigInteger accountNumber);
	

}
