package com.atm.atmmachine.service;


import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.dto.CardLimit;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.CardDetails.CardType;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.entity.UserRequest.RequestStatus;
import com.atm.atmmachine.exceptions.AdminException;
import com.atm.atmmachine.idGenerator.VerhoeffAlgorithm;
import com.atm.atmmachine.repository.CardDetailsRepository;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.repository.UserRequestRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	UserRequestRepository userRequestRepository;

	@Autowired
	UserRegistrationRepository userRegistrationRepository;

	@Autowired
	CardDetailsRepository cardDetailsRepository;
	
	@Override
	public Optional<UserRegistration> findByUserId(String userId) {
		return this.userRegistrationRepository.findById(userId);
		
	}

	@Override
	public List<UserRequest> displayAllRequest() {
		return this.userRequestRepository.findAll();
	}
	
	

	@Override
	public List<UserRequest> displayRequestByStatus() {

		return this.userRequestRepository.findByRequestStatus(RequestStatus.Pending);
	}

	@Override
	public List<UserRequest> displayByRequest() {

		return this.userRequestRepository.findByRequest("Card Lost");
	}

	@Override
	public List<UserRequest> displayAllCardReplacementRequests() {

		return this.userRequestRepository.findByRequest("Card Replacement");
	}

	@Override
	public UserRequest updateUserRequestStatus(String requestId) throws AdminException{
		Optional<UserRequest> foundUserRequest = this.getRequestById(requestId);
		if(!foundUserRequest.isPresent())
			throw new AdminException("Request doesn't exist");
			
		foundUserRequest.get().setRequestStatus(RequestStatus.Approved);
		this.userRequestRepository.save(foundUserRequest.get());
		return foundUserRequest.get();
	}

	@Override
	public Optional<UserRequest> getRequestById(String requestId) throws AdminException{
		Optional<UserRequest> foundUserRequest = this.userRequestRepository.findByRequestId(requestId);
		if(!foundUserRequest.isPresent())
			throw new AdminException("Request doesn't exist");
		
		return foundUserRequest;
	}

	@Override
	public List<UserRegistration> displayUsersWithStatusInactive() {
		return this.userRegistrationRepository.findByUserRegistrationApproval(UserRegistrationApproval.Inactive);

	}

	@Override
	public UserRegistrationApproval updateUserRegistrationApproval(String id)throws AdminException {
		Optional<UserRegistration> foundUser = this.userRegistrationRepository.findByUserId(id);
		if(!foundUser.isPresent())
			throw new AdminException("User doesn't exist");
		
		foundUser.get().setUserRegistrationApproval(UserRegistrationApproval.Active);
		this.userRegistrationRepository.save(foundUser.get());
		return foundUser.get().getUserRegistrationApproval();
	}

	@Override
	public Optional<UserRegistration> getUsersById(String userId) throws AdminException {
		Optional<UserRegistration> foundUserRegistration = this.userRegistrationRepository.findByUserId(userId);
		if(!foundUserRegistration.isPresent())
			throw new AdminException("User doesn't exist");
		
		return foundUserRegistration;
	}

	@Override
	public String setAdminRemark(String reqId, String remark) throws AdminException{
		Optional<UserRequest> foundUser = this.userRequestRepository.findById(reqId);
		if(!foundUser.isPresent())
			throw new AdminException("Request doesn't exist");
		
		foundUser.get().setAdminRemark(remark);
		this.userRequestRepository.save(foundUser.get());
		return foundUser.get().getAdminRemark();
	}

	@Override
	public Double changeCardLimit(CardLimit cardLimit) {
		List<CardDetails> foundCardDetails = this.cardDetailsRepository.findByCardType(CardType.valueOf(cardLimit.getCardType()));
		for(CardDetails c:foundCardDetails) {
			c.setCardLimit(cardLimit.getChangedCardLimit());
			this.cardDetailsRepository.save(c);
		}
		return cardLimit.getChangedCardLimit();
		
	}

	@Override
	public Boolean validAadharCard(String aadharNumber) {
		Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
		
	}


}
