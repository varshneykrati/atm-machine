package com.atm.atmmachine.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.exceptions.RequestException;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.repository.UserRequestRepository;

import java.util.List;

@Service
public class UserRequestServiceImpl implements UserRequestService {
	@Autowired
	UserRequestRepository requestRepository;
	@Autowired
	UserRegistrationRepository userRegistrationRepository;

	@Override
	public UserRequest getUserRequestById(String request_id) throws RequestException {
		Optional<UserRequest> userRequestOpt = this.requestRepository.findById(request_id);
		if(userRequestOpt.isEmpty())
		{
			throw new RequestException("In get user the user id is not present");
		}
		
		return userRequestOpt.get();
	}

	@Override
	public UserRequest addRequest(UserRequest newRequest)throws RequestException {
		newRequest.setDateOfRequest(LocalDate.now());// for storing current date
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findByUserId("user1");
		if(getUserOpt.isEmpty())
		{
			throw new RequestException(" Can't add  as User id is  not present");
		}
			UserRegistration getUser = getUserOpt.get();
			newRequest.setUserRegistration(getUser);
			newRequest.setAccountNumber(getUser.getCardDetails().getAccountNumber());
		
		boolean condition = true;
		List<UserRequest> allUser = this.requestRepository.findAll();

		for (UserRequest user : allUser) {
			if ((user.getRequest().equals(newRequest.getRequest())) && user.getUserRegistration().equals(getUser) &&  ((user.getDateOfRequest().equals(LocalDate.now()))
						|| ((user.getDateOfRequest().isBefore(LocalDate.now()))
								&& (user.getDateOfRequest().plusDays(3).isAfter(LocalDate.now()))))) {
					condition = false;
				
			}
		}
		
		if (condition) {
			return this.requestRepository.save(newRequest);
		}
		return null;
	}

	@Override
	public UserRequest deleteRequest(String request_id) throws RequestException {
		if(request_id.isEmpty())
		{
			throw new RequestException("User not present");
		}
		this.requestRepository.deleteById(request_id);
		return null;
	}

	@Override
	public List<UserRequest> getAllUserRequest()throws RequestException {

		return this.requestRepository.findAll();
	}

	@Override
	public List<UserRequest> getRequestByUserId(String userId)throws RequestException {
		Optional<UserRegistration> userRegistrationOpt = this.userRegistrationRepository.findByUserId(userId);
		if(userRegistrationOpt.isPresent())
		{
			UserRegistration getUser = userRegistrationOpt.get();
			List<UserRequest> RequestOpt ;
			RequestOpt = this.requestRepository.findAllByUserRegistration(getUser);
			return RequestOpt;
		}
		else
			throw new RequestException("User not present");
		
		
		
	}

	@Override
	public UserRequest UpdateRequest(UserRequest newRequest) throws RequestException {
		Optional<UserRequest> getRequest = requestRepository.findById(newRequest.getRequestId());
		if (getRequest.isPresent()) {
			UserRequest updatedrequest = requestRepository.save(newRequest);
			return updatedrequest;
		}
		return null;
	}
}
