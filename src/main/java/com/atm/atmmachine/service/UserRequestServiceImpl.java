package com.atm.atmmachine.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRequest;
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
	public UserRequest getUserRequestById(String request_id) {
		// TODO Auto-generated method stub
		System.out.println("helo i am unside");
		Optional<UserRequest> userRequestOpt = this.requestRepository.findById(request_id);
		return userRequestOpt.get();
	}

	@Override
	public UserRequest addRequest(UserRequest newRequest) {
		newRequest.setDateOfRequest(LocalDate.now());// for storing current date
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findByUserId("user2");
		UserRegistration getUser = getUserOpt.get();
		newRequest.setUserRegistration(getUser);
		newRequest.setAccountNumber(getUser.getCardDetails().getAccountNumber());// for fetching account no
		boolean condition = true;
		List<UserRequest> allUser = this.requestRepository.findAll();

		for (UserRequest user : allUser) {
			if ((user.getRequest().equals(newRequest.getRequest())) && user.getUserRegistration().equals(getUser)) {
				if ((user.getDateOfRequest().equals(LocalDate.now()))
						|| ((user.getDateOfRequest().isBefore(LocalDate.now()))
								&& (user.getDateOfRequest().plusDays(3).isAfter(LocalDate.now())))) {
					condition = false;
				}
			}
		}
		if (condition != false) {
			return this.requestRepository.save(newRequest);
		}
		return null;
	}

	@Override
	public UserRequest deleteRequest(String request_id) {
		// TODO Auto-generated method stub
		this.requestRepository.deleteById(request_id);
		return null;
	}

	@Override
	public List<UserRequest> getAllUserRequest() {

		return this.requestRepository.findAll();
	}

	@Override
	public List<UserRequest> getRequestByUserId(String userId) {
		Optional<UserRegistration> userRegistrationOpt = this.userRegistrationRepository.findByUserId(userId);
		UserRegistration getUser = userRegistrationOpt.get();
		System.out.println(getUser.getUserId());
		List<UserRequest> RequestOpt = this.requestRepository.findAllByUserRegistration(getUser);
		return RequestOpt;
	}

	@Override
	public UserRequest UpdateRequest(UserRequest newRequest) {
		Optional<UserRequest> getRequest = requestRepository.findById(newRequest.getRequestId());
		if (getRequest.isPresent()) {
			UserRequest updatedrequest = requestRepository.save(newRequest);
			return updatedrequest;
		}
		return null;
	}
}
