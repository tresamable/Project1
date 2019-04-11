package com.tresa.framewok.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailMailboxReader {
	static final String host = "pop.gmail.com";
	static final String mailStoreType = "pop3";
	public static List<EmailMessage> getRecentEmails(String user, String password, int count) {
		
		List<EmailMessage> inboxEmails = new ArrayList<EmailMessage>();
		try {

			// create properties field
			Properties properties = new Properties();

			properties.put("mail.pop3.host", host);
			properties.put("mail.pop3.port", "995");
			properties.put("mail.pop3.starttls.enable", "true");
			Session emailSession = Session.getDefaultInstance(properties);
			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");
			store.connect(host, user, password);
			// create the folder object and open it
			
			Folder emailFolder = store.getFolder("Inbox");
			emailFolder.open(Folder.READ_ONLY);
			Message[] messages = emailFolder.getMessages();
			
			for (int i = messages.length-1,k=0; i >= 0  && k < count; i--,k++) {
				Message message = messages[i];
				EmailMessage emailMessage = new EmailMessage();
				emailMessage.setSubject(message.getSubject());
				emailMessage.setFrom(message.getFrom()[0].toString());
				List<String> attachmentFileNames = new ArrayList<String>();
				Object msgContent = message.getContent();
				/* Check if content is pure text/html or in parts */
				if (msgContent instanceof Multipart) {
					Multipart multipart = (Multipart) message.getContent();
					StringBuilder content = new StringBuilder();
					for (int j = 0; j < multipart.getCount(); j++) {
						BodyPart bodyPart = multipart.getBodyPart(j);
						String disposition = bodyPart.getDisposition();
						if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) {
							DataHandler handler = bodyPart.getDataHandler();
							attachmentFileNames.add(handler.getName());
						} else {
							content.append(bodyPart.getContent().toString());
						}
					}
					emailMessage.setBody(content.toString());
				} else {
					emailMessage.setBody(messages[i].getContent().toString());
				}
				//System.out.println(emailMessage);
				inboxEmails.add(emailMessage);
			}
			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inboxEmails;
	}

	

	public static boolean sendEmail(String to, final String username, final String password,String subject,String body) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);
			return true;
		}

		catch (MessagingException e) {
			System.out.println("Username or Password are incorrect ... exiting !");
			return false;
		}
	}
}