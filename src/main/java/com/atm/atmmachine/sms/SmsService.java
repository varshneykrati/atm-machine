package com.atm.atmmachine.sms;

import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class SmsService {
	private final String ACCOUNT_SID = "ACd769cc108b49bc5267df66cc0cc57861";

	private final String AUTH_TOKEN = "023a57351e574f0890c46c217e02c818";

	private final String FROM_NUMBER = "+16185528051";

	public void send(SmsPojo sms) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		Message message = Message.creator(new PhoneNumber(sms.getTo()), new PhoneNumber(FROM_NUMBER), sms.getMessage())
				.create();
		System.out.println("here is my id:" + message.getSid());// Unique resource ID created to manage this transaction
	}
	public void receive(MultiValueMap<String, String> smscallback) {
	}

}
