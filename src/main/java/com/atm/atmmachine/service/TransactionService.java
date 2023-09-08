package com.atm.atmmachine.service;

import java.util.List;

import com.atm.atmmachine.dto.UserInfo;
import com.atm.atmmachine.dto.FundTransferDto;
import com.atm.atmmachine.dto.SelfTransferInfo;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.exceptions.TransactionException;

public interface TransactionService {

	List<TransactionDetails> getAllTransactions(String userId);
	Double withdrawFunds(SelfTransferInfo withdraw) throws TransactionException;
	Double fundTransfer(FundTransferDto fundTransfer)  throws TransactionException;
	Double checkBalance(UserInfo checkBalance) throws TransactionException;
	Double addFunds(SelfTransferInfo addFund) throws TransactionException;
	

}
