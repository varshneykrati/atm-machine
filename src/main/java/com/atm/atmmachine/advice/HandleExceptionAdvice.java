package com.atm.atmmachine.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.atm.atmmachine.exception.HandleException;

@RestControllerAdvice
public class HandleExceptionAdvice {

	@ExceptionHandler(HandleException.class)
	public ResponseEntity<String> handlerProductException(HandleException handleException){
		return new ResponseEntity<String>(handleException.getMessage(),HttpStatus.NOT_ACCEPTABLE);
	}
}
import com.atm.atmmachine.exceptions.RequestException;

@RestControllerAdvice

	public class HandleExceptionAdvice {

	 

	    @ExceptionHandler(RequestException.class)

	    public ResponseEntity<String> handlerRequestException(RequestException requestException){

	        return new ResponseEntity<String>(requestException.getMessage(),HttpStatus.NOT_ACCEPTABLE);

	    }
	}
