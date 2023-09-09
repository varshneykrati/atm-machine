package com.atm.atmmachine.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.atm.atmmachine.exception.BillPaymentsException;

@RestControllerAdvice
public class BillPaymentsControllerAdvice {
	@ExceptionHandler(BillPaymentsException.class)
	public ResponseEntity<String> handleBillPaymentsException(Exception exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
