package com.atm.atmmachine;

import java.math.BigInteger;
import java.time.LocalDate;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.atm.atmmachine.entity.Address;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.CardDetails.CardStatus;
import com.atm.atmmachine.entity.CardDetails.CardType;
import com.atm.atmmachine.entity.CardDetails.UserTotallyRegister;
import com.atm.atmmachine.entity.DTH;
import com.atm.atmmachine.entity.ElectricityBill;
import com.atm.atmmachine.entity.TransactionDetails;
import com.atm.atmmachine.entity.TransactionDetails.TransactionType;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.entity.UserRequest.RequestStatus;
import com.atm.atmmachine.entity.Vendors;
import com.atm.atmmachine.entity.Vendors.TypeOfVendor;
import com.atm.atmmachine.repository.AddressRepository;
import com.atm.atmmachine.repository.CardDetailsRepository;
import com.atm.atmmachine.repository.DthRepository;
import com.atm.atmmachine.repository.ElectricityBillRepository;
import com.atm.atmmachine.repository.TransactionRepository;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.repository.UserRequestRepository;
import com.atm.atmmachine.repository.VendorsRepository;

@SpringBootApplication
@Configuration
@EnableScheduling
public class AtmMachineApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(AtmMachineApplication.class, args);
	}

	@Autowired
	UserRegistrationRepository userRegistrationRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	CardDetailsRepository cardDetailsRepository;
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	UserRequestRepository userRequestRepository;
	@Autowired
	ElectricityBillRepository electricityBillRepository;
	@Autowired
	DthRepository dthRepository;
	@Autowired
	VendorsRepository vendorsRepository;
	
	//LocalDate localDate;
	//LocalDateTime localDateTime;
	
	@Override
	public void run(String... args) throws Exception {
		
//Entering user registration detail form with the -> address  -----
		Address address1 = new Address("405/8 Nai Basti","Firozabad",283203,"Uttar Pradesh");
		UserRegistration userRegistration1 = new UserRegistration("Krati Varshney",LocalDate.now(),"9760286311",962239611757L,"krativarshne@gmail.com","KVar@7777","KVar@7777",false,UserRegistrationApproval.Inactive,address1,null);
		UserRegistration userRegistration2 = new UserRegistration("Shivam",LocalDate.now(),"+919307204212",301577650820L,"shivam@gmail.com","shivam@7777","shivam@7777",false,UserRegistrationApproval.Active,new Address("415/8 New Market","Agra",283203,"Uttar Pradesh"),null);
		UserRegistration userRegistration3 = new UserRegistration("Sidhi",LocalDate.now(),"+918178234554",767678355011L,"sidhi@gmail.com","sidhi@7777","sidhi@7777",false,UserRegistrationApproval.Inactive,new Address("415/8 Old Market","Agra",283203,"Madhya Pradesh"),null);
		UserRegistration userRegistration4 = new UserRegistration("Mansi",LocalDate.now(),"9760281156",679967896789L,"mansi@gmail.com","mansi@7777","mansi@7777",false,UserRegistrationApproval.Inactive,new Address("905/8 New Market","Agra",283203,"Uttar Pradesh"),null);
		UserRegistration userRegistration5 = new UserRegistration("Ram",LocalDate.now(),"+919760286311",967739611757L,"ram@gmail.com","ram@7700","ram@7700",false,UserRegistrationApproval.Inactive,new Address("415/8 Navi Mumbai","Mumbai",283203,"Maharastra"),null);
		UserRegistration userRegistration6 = new UserRegistration("Admin",LocalDate.now(),"+918178234554",967739981757L,"admin@gmail.com","admin","admin",true,UserRegistrationApproval.Active,new Address("415/8 Navi Mumbai","Mumbai",283203,"Maharastra"),null);
		userRegistrationRepository.save(userRegistration1);
		userRegistrationRepository.save(userRegistration2);
		userRegistrationRepository.save(userRegistration3);
		userRegistrationRepository.save(userRegistration4);
		userRegistrationRepository.save(userRegistration5);
		userRegistrationRepository.save(userRegistration6);
		
//Entering Card Detail of the user ->link with USER Registration
		CardDetails carddetail1 = new CardDetails(new BigInteger("123412341234"),new BigInteger("7890789078907890"),456,LocalDate.now(),CardType.Silver,25000.0,CardStatus.Active,20000.0,3456,UserTotallyRegister.True,userRegistration2);
		 userRegistration2.setCardDetails(carddetail1);cardDetailsRepository.save(carddetail1); userRegistrationRepository.save(userRegistration2);
		 CardDetails carddetail2 = new CardDetails(new BigInteger("123412344321"),new BigInteger("7890789078900965"),789,LocalDate.now(),CardType.Silver,25000.0,CardStatus.Active,2000.0,null,UserTotallyRegister.False,userRegistration1);
		 userRegistration1.setCardDetails(carddetail2);cardDetailsRepository.save(carddetail2); userRegistrationRepository.save(userRegistration1);
		 CardDetails carddetail3 = new CardDetails(new BigInteger("1234123456678"),new BigInteger("7890789078999890"),459,LocalDate.now(),CardType.Silver,25000.0,CardStatus.Active,2000.0,null,UserTotallyRegister.False,userRegistration3);
		 userRegistration3.setCardDetails(carddetail3);cardDetailsRepository.save(carddetail3); userRegistrationRepository.save(userRegistration3);
		 CardDetails carddetail4 = new CardDetails(new BigInteger("123424561234"),new BigInteger("7890789008907890"),956,LocalDate.now(),CardType.Silver,25000.0,CardStatus.Active,2000.0,null,UserTotallyRegister.False,userRegistration4);
		 userRegistration4.setCardDetails(carddetail4);cardDetailsRepository.save(carddetail4); userRegistrationRepository.save(userRegistration4);
		 CardDetails carddetail5 = new CardDetails(new BigInteger("678412341234"),new BigInteger("7891789078907890"),756,LocalDate.now(),CardType.Silver,25000.0,CardStatus.Active,2000.0,null,UserTotallyRegister.True,userRegistration5);
		 userRegistration5.setCardDetails(carddetail5);cardDetailsRepository.save(carddetail5); userRegistrationRepository.save(userRegistration5);
// Now Add Transaction which is link to -> Address
		 TransactionDetails transaction1 = new TransactionDetails(carddetail1,null,new BigInteger("123412344321"),LocalDateTime.now(),500.0,null,null,null,TransactionType.Deposit);
		 this.transactionRepository.save(transaction1);
		 TransactionDetails transaction2 = new TransactionDetails(carddetail1,null,new BigInteger("993412344321"),LocalDateTime.now(),1500.0,null,null,null,TransactionType.Deposit);
		 this.transactionRepository.save(transaction2);
		 TransactionDetails transaction3 = new TransactionDetails(carddetail1,null,new BigInteger("553412344321"),LocalDateTime.now(),200.0,null,null,null,TransactionType.Deposit);
		 this.transactionRepository.save(transaction3);
		 TransactionDetails transaction4 = new TransactionDetails(carddetail2,null,new BigInteger("123412344321"),LocalDateTime.now(),100.0,"krati",null,null,TransactionType.Deposit);
		 this.transactionRepository.save(transaction4);
		 TransactionDetails transaction5 = new TransactionDetails(carddetail3,null,new BigInteger("123412344321"),LocalDateTime.now(),1200.0,null,null,null,TransactionType.Deposit);
		 this.transactionRepository.save(transaction5);

		 
//create all vendors roughly

         Vendors vendor1 = new Vendors(TypeOfVendor.DTH,"Airtel",new BigInteger("126734582093"),22000.0);

         this.vendorsRepository.save(vendor1);

         Vendors vendor2 = new Vendors(TypeOfVendor.DTH,"Samsung",new BigInteger("126675423093"),22000.0);

         this.vendorsRepository.save(vendor2);

         Vendors vendor3 = new Vendors(TypeOfVendor.DTH,"Sony",new BigInteger("126734512345"),22000.0);

         this.vendorsRepository.save(vendor3);

         Vendors vendor4 = new Vendors(TypeOfVendor.DTH,"Personi",new BigInteger("999994582093"),22000.0);

         this.vendorsRepository.save(vendor4);

         Vendors vendor5 = new Vendors(TypeOfVendor.ElectricityBill,"Maharstra",new BigInteger("126734582999"),22000.0);

         this.vendorsRepository.save(vendor5);

         Vendors vendor6 = new Vendors(TypeOfVendor.ElectricityBill,"Delhi",new BigInteger("126774582093"),22000.0);

         this.vendorsRepository.save(vendor6);

         Vendors vendor7 = new Vendors(TypeOfVendor.ElectricityBill,"UttarPradesh",new BigInteger("106734582093"),22000.0);

         this.vendorsRepository.save(vendor7);

         Vendors vendor8 = new Vendors(TypeOfVendor.ElectricityBill,"MadhyaPradesh",new BigInteger("846734582093"),22000.0);

         this.vendorsRepository.save(vendor8);

         

//Now entering data in UserRequest which is link to our -> UserRegistration

         UserRequest userRequest1 = new UserRequest(userRegistration6.getCardDetails().getAccountNumber(),"Card Replacement","My card is not working",LocalDate.now(),RequestStatus.Pending,null,userRegistration6);

         this.userRequestRepository.save(userRequest1);

         UserRequest userRequest2 = new UserRequest(userRegistration6.getCardDetails().getAccountNumber(),"Card Lost","My card is not Lost",LocalDate.now(),RequestStatus.Pending,null,userRegistration6);

         this.userRequestRepository.save(userRequest2);

         UserRequest userRequest3 = new UserRequest(userRegistration2.getCardDetails().getAccountNumber(),"Card Replacement","My card is not working",LocalDate.now(),RequestStatus.Pending,null,userRegistration2);

         this.userRequestRepository.save(userRequest3);

         UserRequest userRequest4 = new UserRequest(userRegistration3.getCardDetails().getAccountNumber(),"Card Replacement","My card is not working",LocalDate.now(),RequestStatus.Pending,null,userRegistration3);

         this.userRequestRepository.save(userRequest4);

         UserRequest userRequest5 = new UserRequest(userRegistration3.getCardDetails().getAccountNumber(),"Card Lost","My card is not Lost",LocalDate.now(),RequestStatus.Pending,null,userRegistration3);

         this.userRequestRepository.save(userRequest5);

         UserRequest userRequest6 = new UserRequest(userRegistration4.getCardDetails().getAccountNumber(),"Card Lost","Please uopdate my Card",LocalDate.now(),RequestStatus.Pending,null,userRegistration6);

         this.userRequestRepository.save(userRequest6);
         
         UserRequest userRequest7 = new UserRequest(userRegistration3.getCardDetails().getAccountNumber(),"Increment Card Type","Please update my Card",LocalDate.now(),RequestStatus.Pending,null,userRegistration3);

         this.userRequestRepository.save(userRequest7);

         

         

///Now entering electricityBill which is link to -> CardDetail -> Transaction -> Vendors

         ElectricityBill electricityBill1 = new ElectricityBill(carddetail1,2000.0,vendor5);

         this.electricityBillRepository.save(electricityBill1);

         TransactionDetails transaction8 = new TransactionDetails(carddetail1,new BigInteger("126734582999"),null,LocalDateTime.now(),2000.0,null,electricityBill1,null,TransactionType.Withdrawal);

         this.transactionRepository.save(transaction8);

         ElectricityBill electricityBill2 = new ElectricityBill(carddetail1,3000.0,vendor6);

         this.electricityBillRepository.save(electricityBill2);

         TransactionDetails transaction9 = new TransactionDetails(carddetail1,new BigInteger("126774582093"),null,LocalDateTime.now(),3000.0,null,electricityBill2,null,TransactionType.Withdrawal);

         this.transactionRepository.save(transaction9);

         ElectricityBill electricityBill3 = new ElectricityBill(carddetail2,5000.0,vendor7);

         this.electricityBillRepository.save(electricityBill3);

         TransactionDetails transaction10 = new TransactionDetails(carddetail2,new BigInteger("106734582093"),null,LocalDateTime.now(),5000.0,null,electricityBill1,null,TransactionType.Withdrawal);

         this.transactionRepository.save(transaction10);

         

         

//Now entering DTHBill which is link to -> CardDetail -> Transaction -> Vendors

         DTH dthBill1 = new DTH(carddetail1,200.0,vendor1);

         this.dthRepository.save(dthBill1);

         TransactionDetails transaction11 = new TransactionDetails(carddetail1,new BigInteger("126734582093"),null,LocalDateTime.now(),200.0,null,null,dthBill1,TransactionType.Withdrawal);

         this.transactionRepository.save(transaction11);

         DTH dthBill2 = new DTH(carddetail1,300.0,vendor2);

         this.dthRepository.save(dthBill2);

         TransactionDetails transaction12 = new TransactionDetails(carddetail1,new BigInteger("126675423093"),null,LocalDateTime.now(),300.0,null,null,dthBill2,TransactionType.Withdrawal);

         this.transactionRepository.save(transaction12);

         DTH dthBill3 = new DTH(carddetail2,500.0,vendor3);

         this.dthRepository.save(dthBill3);

         TransactionDetails transaction13 = new TransactionDetails(carddetail2,new BigInteger("126734512345"),null,LocalDateTime.now(),500.0,null,null,dthBill3,TransactionType.Withdrawal);

         this.transactionRepository.save(transaction13);

		 
}

}
