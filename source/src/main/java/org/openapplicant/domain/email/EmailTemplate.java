package org.openapplicant.domain.email;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Email;
import org.openapplicant.domain.DomainObject;
import org.springframework.mail.SimpleMailMessage;

import javax.persistence.*;
import java.util.List;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
public abstract class EmailTemplate extends DomainObject {
	
	private String subject = "";
	
	private String fromAddress;
	
	private String body = "";
	
	protected EmailTemplate() {
		revert();
	}
	
	/**
	 * @return the template's subject
	 */
	@Column
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String value) {
		subject = StringUtils.trimToEmpty(value);
	}
	
	/**
	 * @return the template's from email address or null if none was set.
	 */
	@Email
	@Column
	public String getFromAddress() {
		return fromAddress;
	}
	
	public void setFromAddress(String value) {
		fromAddress = value;
	}
	
	@Transient
	public String getDefaultFromAddress() {
		return "noreply@beta.openapplicant.org";
	}
	
	/**
	 * @return the template's body content.
	 */
	@Column(columnDefinition="longtext")
	public String getBody() {
		return body;
	}
	
	public void setBody(String value) {
		body = StringUtils.trimToEmpty(value);
	}

	/**
	 * Revert this template to it's default text.
	 */
	public void revert() {
		setFromAddress(getDefaultFromAddress());
		setSubject(defaultSubject());
		setBody(defaultBody());
	}
	
	/**
	 * @return the template's default subject
	 */
	protected abstract String defaultSubject();
	
	/**
	 * @return the template's default body text
	 */
	protected abstract String defaultBody();
	
	/**
	 * @return the template's display name.
	 */
	@Transient
	public abstract String getName();
	
	/**
	 * @return a list of PlaceHolders available to this template
	 */
	@Transient
	public abstract List<PlaceHolder> getPlaceHolders();
	
	/**
	 * Compose a MailMessage with the given to address and body.
	 * @param to the to address
	 * @param body the template's body
	 */
    SimpleMailMessage createMailMessage(String to, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		if(StringUtils.isBlank(fromAddress)) {
			message.setFrom(getDefaultFromAddress());
		} else {
			message.setFrom(fromAddress);
		}
		message.setSubject(subject);
		message.setText(body);
		return message;
	}
}
