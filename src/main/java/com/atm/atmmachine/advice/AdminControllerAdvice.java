package com.atm.atmmachine.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.atm.atmmachine.exceptions.AdminException;

@RestController
public class AdminControllerAdvice {
	
	public ResponseEntity<String> handleProductException(AdminException adminException){
		return new ResponseEntity<>(adminException.getMessage(),HttpStatus.BAD_REQUEST);
	}

}
