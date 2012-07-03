package org.openapplicant.service.facilitator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Helper class for reading messages.
 */
class MessageReader {
	
	private final Collection<Attachment> attachments = new ArrayList<Attachment>();
	
	private final StringBuilder bodyText = new StringBuilder();
	
	private final Message message;
	
	public MessageReader(Message message) throws MessagingException, IOException {
		Assert.notNull(message);
		this.message = message;
		read();
	}
	
	/**
	 * @return the original message
	 */
	public Message getMessage() {
		return message;
	}
	
	public Collection<Attachment> getAttachments() {
		return Collections.unmodifiableCollection(attachments);
	}
	
	public String getBodyText() {
		return bodyText.toString();
	}
	
	public boolean doesNotHaveToRecipient() throws MessagingException {
		return ArrayUtils.isEmpty(message.getRecipients(RecipientType.TO));
	}
	
	/**
	 * @return the message's first "To" email address of null if none exists
	 */
	public String getTo() throws MessagingException {
		if(doesNotHaveToRecipient()) {
			return null;
		}
		Address address = message.getRecipients(RecipientType.TO)[0];
		if(address instanceof InternetAddress) {
			return ((InternetAddress)address).getAddress();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Check if this message does not have senders ("from" addresses)
	 */
	public boolean doesNotHaveSenders() throws MessagingException {
		return ArrayUtils.isEmpty(message.getFrom());
	}
	
	/**
	 * @return the message's first "from" email address or null if none 
	 * exists
	 */
	public String getSender() throws MessagingException {
		if(doesNotHaveSenders()) {
			return null;
		}
		Address address = message.getFrom()[0];
		if(address instanceof InternetAddress) {
			return ((InternetAddress)address).getAddress();
		}
		else {
			return null;
		}
	}
	
	public String getSenderPersonalName() throws MessagingException { 
		if(doesNotHaveSenders()) {
			return null;
		}
		if(!(message.getFrom()[0] instanceof InternetAddress)) {
			return null;
		}
		Address address = message.getFrom()[0];
		return ((InternetAddress) address).getPersonal();
	}
	
	private void read() throws MessagingException, IOException {
		Object body = message.getContent();
		if(body instanceof Multipart) {
			processMultipart((Multipart)body);
		}
		else {
			processPart(message);
		}
	}
	
	private void processMultipart(Multipart multipart) throws MessagingException,
		IOException {
		for(int i=0; i<multipart.getCount(); i++) {
			processPart(multipart.getBodyPart(i));
		}
	}
	
	private void processPart(Part part) throws MessagingException, IOException {
		if(part.getContent() instanceof Multipart) {
			processMultipart((Multipart)part.getContent());
		}
		if(isAttachment(part)) {
			attachments.add(new Attachment(part));
		}
		else if(isBodyText(part)) {
			bodyText.append(IOUtils.toString(part.getInputStream()));
		}
	}
	
	private boolean isAttachment(Part part) throws MessagingException {
		return Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition());
	}
	
	private boolean isBodyText(Part part) throws MessagingException {
		return part.getDisposition() == null && part.isMimeType("text/plain");
	}
}
