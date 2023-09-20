package com.atm.atmmachine.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.CardDetails.CardType;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.entity.UserRequest.RequestStatus;
import com.atm.atmmachine.exceptions.RequestException;
import com.atm.atmmachine.repository.CardDetailsRepository;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.repository.UserRequestRepository;

import java.util.List;

/********************************************************************************************************
 * @author :Sidhi Jain
 * Description: It is  a service implementation class that provide the service for raising a request, view all the request
 *              ,for updating and deleting the request.
 * Version: 1.0
 * Created date: 04-09-2023
 *********************************************************************************************************/
 

@Service
public class UserRequestServiceImpl implements UserRequestService {
	@Autowired
	 private UserRequestRepository requestRepository;
	@Autowired
	 private UserRegistrationRepository userRegistrationRepository;
	@Autowired
	private  CardDetailsRepository cardDetailsRepository;

	/************************************************************************************
	 * Method:getUserRequestById
	 * Description :It will show the request of the user which is logged in 
	 */
	@Override
	public UserRequest getUserRequestById(String requestId) throws RequestException {
		Optional<UserRequest> userRequestOpt = this.requestRepository.findById(requestId);
		if(!userRequestOpt.isPresent())
		{
			throw new RequestException("In get user the user id is not present");
		}
		
		return userRequestOpt.get();
	}
	/************************************************************************************
	 * Method:addRequest
	 * Description :It will add the request
	 */

	@Override
	public UserRequest addRequest(UserRequest newRequest,String userId)throws RequestException {
		newRequest.setDateOfRequest(LocalDate.now());// for storing current date
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(!getUserOpt.isPresent())
		{
			throw new RequestException(" Can't add  as User id is  not present");
		}
			UserRegistration getUser = getUserOpt.get();
			newRequest.setUserRegistration(getUser);
			newRequest.setAccountNumber(getUser.getCardDetails().getAccountNumber());
			newRequest.setRequestStatus(RequestStatus.Pending);
		    
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
		else
		{
			throw new RequestException("You make same request earlier");
		}
		
	}
	/************************************************************************************
	 * Method:deleteRequest
	 * Description :Method for deleting the request
	 */
	@Override
	public UserRequest deleteRequest(String requestId) throws RequestException {
		if(requestId.isEmpty())
		{
			throw new RequestException("User not present");
		}
		this.requestRepository.deleteById(requestId);
		return null;
	}
	/************************************************************************************
	 * Method:getAllUserRequest
	 * Description :Method for getting all the request
	 */
	@Override
	public List<UserRequest> getAllUserRequest()throws RequestException {

		return this.requestRepository.findAll();
	}
	/**********************************************************************************************************
	 * Method:getRequestByUserId
	 * Description :Method for getting request of particular user(the user who is login to the system) 
	 *******************************************************************************************/
	@Override
	public List<UserRequest> getRequestByUserId(String userId)throws RequestException {
		Optional<UserRegistration> userRegistrationOpt = this.userRegistrationRepository.findById(userId);
		if(userRegistrationOpt.isPresent())
		{
			UserRegistration getUser = userRegistrationOpt.get();
			List<UserRequest> requestOpt ;
			requestOpt = this.requestRepository.findAllByUserRegistration(getUser);
			return requestOpt;
		}
		else
			throw new RequestException("User not present");	
	}
	/**********************************************************************************************************
	 * Method:updateRequest
	 * Description :Method for updating the already raised request
	 *******************************************************************************************/
	@Override
	public UserRequest updateRequest(UserRequest newRequest, String userId) throws RequestException {
		Optional<UserRequest> getRequest = requestRepository.findById(newRequest.getRequestId());
		newRequest.setDateOfRequest(LocalDate.now());
		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById(userId);
		if(!getUserOpt.isPresent())
		{
			throw new RequestException(" Can't add  as User id is  not present");
		}
			UserRegistration getUser = getUserOpt.get();
			newRequest.setUserRegistration(getUser);
			newRequest.setAccountNumber(getUser.getCardDetails().getAccountNumber());
		
		if (getRequest.isPresent()) {
			UserRequest updatedrequest = this.requestRepository.save(newRequest);
			return updatedrequest;
		}
		return null;
	}
	/**********************************************************************************************************
	 * Method:getCardType
	 * Description :Method for getting the card type(Silver,platinum,gold) for the user who is login
	 *******************************************************************************************/
	@Override
	public CardType getCardType(String userId) throws RequestException {
		Optional<UserRegistration> userRegistrationOpt = this.userRegistrationRepository.findById(userId);
		if(userRegistrationOpt.isPresent())
		{
			UserRegistration getUser = userRegistrationOpt.get();
			CardDetails foundCard=getUser.getCardDetails();
			
			return foundCard.getCardType();
		}
		else
			throw new RequestException("User not present");	
	}
}
