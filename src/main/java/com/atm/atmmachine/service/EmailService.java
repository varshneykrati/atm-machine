package com.atm.atmmachine.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	public void sendEmail(String message, String subject, String to, String from) {
		String host = "smtp.gmail.com";
		
		Properties properties = System.getProperties();
		//System.out.println("Properties : "+properties);
		
		//setting important information
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth", "true");
		
		//step1 - to get session object
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("krativarshne@gmail.com", "dfjhdkjfhkjxhk");
			}
			
		});
		session.setDebug(true);
		
		
		MimeMessage m = new MimeMessage(session);
		
		try {
			m.setFrom(from);
			
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			m.setSubject(subject);
			
			//m.setText(message);
			m.setContent(message, "text/html");
			
			//send
			
			
			Transport.send(m);
			System.out.println("Sent successfully....");
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		
	}

}
