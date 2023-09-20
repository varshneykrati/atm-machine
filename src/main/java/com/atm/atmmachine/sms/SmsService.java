package com.atm.atmmachine.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@Component
public class SmsService {

	    
	    private final String ACCOUNT_SID ="AC810cff1c9f29b4074f20ea6a20a423e5";

	    private final String AUTH_TOKEN = "4dffe961a507c6aad9caf6df1377747a";

	    private final String FROM_NUMBER = "+13346974670";

	    public void send(SmsPojo sms) {
	    	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

	        Message message = Message.creator(new PhoneNumber(sms.getTo()), new PhoneNumber(FROM_NUMBER), sms.getMessage())
	                .create();
	        
	    }
	
}
