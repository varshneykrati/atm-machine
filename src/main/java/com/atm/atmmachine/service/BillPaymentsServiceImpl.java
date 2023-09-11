package com.atm.atmmachine.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.atm.atmmachine.dto.DthBill;
import com.atm.atmmachine.dto.ElectricityBillDto;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.CardDetails.CardStatus;
import com.atm.atmmachine.entity.CardDetails.UserTotallyRegister;
import com.atm.atmmachine.entity.DTH;
import com.atm.atmmachine.entity.ElectricityBill;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.entity.TransactionDetails.TransactionType;
import com.atm.atmmachine.dto.Transaction;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.Vendors;
import com.atm.atmmachine.entity.Vendors.TypeOfVendor;
import com.atm.atmmachine.exception.BillPaymentsException;
import com.atm.atmmachine.repository.CardDetailsRepository;
import com.atm.atmmachine.repository.DthRepository;
import com.atm.atmmachine.repository.ElectricityBillRepository;
import com.atm.atmmachine.repository.TransactionRepository;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.repository.VendorsRepository;
import com.atm.atmmachine.sms.SMSController;
import com.atm.atmmachine.sms.SmsPojo;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class BillPaymentsServiceImpl implements BillPaymentsService {

	@Autowired
	private VendorsRepository vendorRepository;

	@Autowired
	private DthRepository dthRepository;

	@Autowired
	private ElectricityBillRepository electricityBillRepository;

	@Autowired
	private UserRegistrationRepository userRegistrationRepository;

	@Autowired
	private CardDetailsRepository cardDetailsRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private SMSController smscontroller;

	private static final String KEY = "rzp_test_HI1uI0WgKFFo8h";
	private static final String KEY_SECRET = "3dJc1rbsXmEzxs6ysTx31ARq";
	private static final String CURRENCY = "INR";

	SmsPojo smspojo = new SmsPojo();

	@Override
	public Transaction createTransactionAmount(Integer amount) throws BillPaymentsException {

		try {
			Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById("user2");

			CardDetails getUserCard = getUserOpt.get().getCardDetails();

			if (getUserCard.getAmount() < amount) {

				throw new BillPaymentsException("Insufficient Balance");

			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("amount", (amount * 100));
			jsonObject.put("currency", CURRENCY);

			RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);

			Order order = razorpayClient.orders.create(jsonObject);
			Transaction transactionDetails = prepareTransactionDetails(order);

			return transactionDetails;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	private Transaction prepareTransactionDetails(Order order) throws BillPaymentsException {

		String orderId = order.get("id");
		String currency = order.get("currency");
		Integer amount = order.get("amount");

		Transaction transactionDetails = new Transaction(orderId, currency, amount, KEY);
		return transactionDetails;
	}

	@Override
	public List<Vendors> getVendorByName(TypeOfVendor typeOfVendor) {
		return this.vendorRepository.findByTypeOfVendor(typeOfVendor);
	}

	@Override
	public Double getAmountToBePaid(DthBill dthBill) throws BillPaymentsException {

		String userDthCardNumber = dthBill.getUserDthCardNumber();
		Optional<DTH> DthOpt = this.dthRepository.findByUserDthCardNumber(userDthCardNumber);

		if (!DthOpt.isPresent()) {
			throw new BillPaymentsException("Wrong account no.");
		}

		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById("user2");
		CardDetails getUserCard = getUserOpt.get().getCardDetails();

		if (getUserCard.getUserTotallyRegister() == UserTotallyRegister.False) {
			throw new BillPaymentsException("User is not totally registered, please complete the user profile first.");
		}

		if (getUserCard.getCardstatus() == CardStatus.Inactive) {
			throw new BillPaymentsException("Activate your card.");
		}
		if (!getUserCard.getCardPin().equals(dthBill.getCardPin())) {
			throw new BillPaymentsException("Wrong Card Pin,Please enter again");
		}

		Optional<DTH> getUserDthOpt = this.dthRepository.findByUserDthCardNumber(dthBill.getUserDthCardNumber());
		if (getUserCard.getAmount() < getUserDthOpt.get().getAmountToBePaid()) {
			throw new BillPaymentsException("You DTH recharge is " + getUserDthOpt.get().getAmountToBePaid()
					+ ". You have insufficient Balance. ");
		}

		return DthOpt.get().getAmountToBePaid();
	}

	@Override
	public Double getElectricityAmountToBePaid(ElectricityBillDto electricityBill) throws BillPaymentsException {

		String userElectricityId = electricityBill.getUserElectricityId();
		Optional<ElectricityBill> electricityOpt = this.electricityBillRepository
				.findByUserElectricityId(userElectricityId);

		if (!electricityOpt.isPresent()) {
			throw new BillPaymentsException("Wrong account no.");
		}

		Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById("user1");
		CardDetails getUserCard = getUserOpt.get().getCardDetails();

		if (getUserCard.getUserTotallyRegister() == UserTotallyRegister.False) {
			throw new BillPaymentsException("User is not totally registered, please complete the user profile first.");
		}

		if (getUserCard.getCardstatus() == CardStatus.Inactive) {
			throw new BillPaymentsException("Activate your card.");
		}
		if (!getUserCard.getCardPin().equals(electricityBill.getCardPin())) {
			throw new BillPaymentsException("Wrong Card Pin,Please enter again");
		}

		Optional<ElectricityBill> getUserElectrictyOpt = this.electricityBillRepository
				.findByUserElectricityId(electricityBill.getUserElectricityId());

		if (getUserCard.getAmount() < getUserElectrictyOpt.get().getAmountToBePaid()) {

			throw new BillPaymentsException("You ElectricityBill recharge is "
					+ getUserElectrictyOpt.get().getAmountToBePaid() + ". You have insufficient Balance. ");

		}

		return electricityOpt.get().getAmountToBePaid();
	}

	@Override
	public DTH payUserBill(DthBill dthBill) throws BillPaymentsException {

		// Optional<UserRegistration> userOpt
		// =this.userRegistrationRepository.findById(userId);
		Double total = 0.0;

		String vendorName = "Airtel", vendorType = "DTH";
		Optional<DTH> getUserDthOpt = this.dthRepository.findByUserDthCardNumber(dthBill.getUserDthCardNumber());
		if (getUserDthOpt.isPresent()) {
			Optional<Vendors> getVendorOpt = this.vendorRepository.findByVendorName(vendorName);
			Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById("user1");

			CardDetails getUserCard = getUserOpt.get().getCardDetails();

			if (getUserCard.getUserTotallyRegister() == UserTotallyRegister.False) {
				throw new BillPaymentsException(
						"user is not totally registered, please complete the user profile first.");
			}

			if (getUserCard.getCardstatus() == CardStatus.Inactive) {
				throw new BillPaymentsException("Activate your card.");
			}
			if (!getUserCard.getCardPin().equals(dthBill.getCardPin())) {

				throw new BillPaymentsException("Wrong Card Pin,Please enter again");

			}

			if (getUserCard.getAmount() < getUserDthOpt.get().getAmountToBePaid()) {

				throw new BillPaymentsException("Insufficient Balance");

			}

			List<TransactionDetails> cardLimitCheck = this.transactionRepository.findByTransactionDateAndCardDetails(
					LocalDate.now(),

					getUserCard);

			if (cardLimitCheck.size() > 0) {

				for (TransactionDetails transaction : cardLimitCheck)
					total += transaction.getBalance();
				
				if (getUserCard.getCardLimit() < getUserDthOpt.get().getAmountToBePaid()+ total) {

					throw new BillPaymentsException("Exceeds CardLimit");

				}

			}

			System.out.println("Amount that user have to Pay : " + getUserDthOpt.get().getAmountToBePaid()
					+ " which you cant change");
			getUserDthOpt.get().setCardDetails(getUserCard);
			getUserDthOpt.get().setVendors(getVendorOpt.get());
			this.dthRepository.save(getUserDthOpt.get());

			// do subtract amount from user card and do add amount to vendor card
			getUserCard.setAmount(getUserCard.getAmount() - getUserDthOpt.get().getAmountToBePaid());
			this.cardDetailsRepository.save(getUserCard);

			getVendorOpt.get().setVendorAccountAmount(
					getVendorOpt.get().getVendorAccountAmount() + getUserDthOpt.get().getAmountToBePaid());
			this.vendorRepository.save(getVendorOpt.get());

			// Now add this in transaction table
			TransactionDetails dthTransaction = new TransactionDetails(getUserCard, getVendorOpt.get().getVendorAccountNumber(),
					getUserCard.getAccountNumber(),LocalDate.now(), getUserDthOpt.get().getAmountToBePaid(), "DTH",null, getUserDthOpt.get(),TransactionType.Withdrawal);
			this.transactionRepository.save(dthTransaction);
			// Optional<UserRegistration> userOpt
			// =this.userRegistrationRepository.findById(userId)}
			smspojo.setTo(getUserOpt.get().getPhoneNo());
			smspojo.setMessage("An amount of INR " + getUserDthOpt.get().getAmountToBePaid()
					+ " has been debited from your Account "

					+ getUserCard.getAccountNumber() + " on " + org.joda.time.LocalDate.now() + ".Total Avail.bal INR "

					+ getUserCard.getAmount());

			smscontroller.smsSubmit(smspojo);
		} else {
			throw new BillPaymentsException("Please enter valid card number for paymenmt of DTH");
		}
		return getUserDthOpt.get();
	}

	@Override
	public ElectricityBill payElectricityUserBill(ElectricityBillDto electricityBill) throws BillPaymentsException {
		Double total = 0.0;
		String vendorName = "Delhi", vendorType = "ElectricityBill";
		Optional<ElectricityBill> getUserElectrictyOpt = this.electricityBillRepository
				.findByUserElectricityId(electricityBill.getUserElectricityId());
		if (getUserElectrictyOpt.isPresent()) {
			Optional<Vendors> getVendorOpt = this.vendorRepository.findByVendorName(vendorName);
			Optional<UserRegistration> getUserOpt = this.userRegistrationRepository.findById("user1");
			CardDetails getUserCard = getUserOpt.get().getCardDetails();

			if (getUserCard.getUserTotallyRegister() == UserTotallyRegister.False) {
				throw new BillPaymentsException(
						"user is not totally registered, please complete the user profile first.");
			}

			if (getUserCard.getCardstatus() == CardStatus.Inactive) {
				throw new BillPaymentsException("Activate your card.");
			}
			if (!getUserCard.getCardPin().equals(electricityBill.getCardPin())) {

				throw new BillPaymentsException("Wrong Card Pin,Please enter again");

			}

			if (getUserCard.getAmount() < getUserElectrictyOpt.get().getAmountToBePaid()) {

				throw new BillPaymentsException("Insufficient Balance");

			}

			List<TransactionDetails> cardLimitCheck = this.transactionRepository.findByTransactionDateAndCardDetails(
					LocalDate.now(),

					getUserCard);

			if (cardLimitCheck.size() > 0) {

				for (TransactionDetails transaction : cardLimitCheck)
					total += transaction.getBalance();

				if (getUserCard.getCardLimit() < getUserElectrictyOpt.get().getAmountToBePaid() + total) {

					throw new BillPaymentsException("Exceeds CardLimit");

				}

			}

			System.out.println("Amount that user have to Pay : " + getUserElectrictyOpt.get().getAmountToBePaid()
					+ " which you cant change");
			getUserElectrictyOpt.get().setCardDetails(getUserCard);
			getUserElectrictyOpt.get().setVendors(getVendorOpt.get());
			this.electricityBillRepository.save(getUserElectrictyOpt.get());

			// do subtract amount from user card and do add amount to vendor card
			getUserCard.setAmount(getUserCard.getAmount() - getUserElectrictyOpt.get().getAmountToBePaid());
			this.cardDetailsRepository.save(getUserCard);

			getVendorOpt.get().setVendorAccountAmount(
					getVendorOpt.get().getVendorAccountAmount() + getUserElectrictyOpt.get().getAmountToBePaid());
			this.vendorRepository.save(getVendorOpt.get());

			// Now add this in transaction table
			TransactionDetails dthTransaction = new TransactionDetails(getUserCard, getVendorOpt.get().getVendorAccountNumber(),
					getUserCard.getAccountNumber(),LocalDate.now(), getUserElectrictyOpt.get().getAmountToBePaid(),"Electricity" ,getUserElectrictyOpt.get(), null,TransactionType.Withdrawal);
			this.transactionRepository.save(dthTransaction);

			smspojo.setTo(getUserOpt.get().getPhoneNo());
			smspojo.setMessage("An amount of INR " + getUserElectrictyOpt.get().getAmountToBePaid()
					+ " has been debited from your Account "

					+ getUserCard.getAccountNumber() + " on " + org.joda.time.LocalDate.now() + ".Total Avail.bal INR "

					+ getUserCard.getAmount());

			smscontroller.smsSubmit(smspojo);
		} else {
			throw new BillPaymentsException("Please enter valid card number for paymenmt of Electricity Bill");
		}
		return getUserElectrictyOpt.get();

	}

}
