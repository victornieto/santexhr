package org.openapplicant.domain.setting;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.NotNull;
import org.openapplicant.util.Strings;

@Embeddable
public class Facilitator {
	
	private String			user = "";
	private String 			pass = "";
	private	String 			url = "";
	private Protocol 		protocol = Protocol.IMAP;
	private final String 	folder = "INBOX";
	private final long 		interval = 60000;
	
	public enum Protocol {
		POP, IMAP;
		
		/**
		 * Exposes Enum.name as a bean property
		 * @return
		 */
		public String getName() {
			return name();
		}
		
		/**
		 * @return the human readable string representing this role
		 */
		public String getHumanString() {
			return name();
		}
	};
	
	@NotNull
	@Column(name="facilitator_user")
	public String getUser() {
		return user;
	}
	public void setUser(String value) {
		this.user = value;
	}
	
	@NotNull
	@Column(name="facilitator_pass")
	public String getPass() {
		return pass;
	}
	public void setPass(String value) {
		this.pass = value;
	}
	
	@NotNull
	@Column(name="facilitator_url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String value) {
		this.url = value;
	}
	
	@NotNull
	@Column(name="protocol")
	public Protocol getProtocol() {
		return protocol;
	}
	public void setProtocol(Protocol value) {
		this.protocol = value;
	}
	
}

