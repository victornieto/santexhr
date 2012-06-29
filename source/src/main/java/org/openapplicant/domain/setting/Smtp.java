package org.openapplicant.domain.setting;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.NotNull;

@Embeddable
public class Smtp {
	
	private String	url = "";
	private int		port = 80;
	private	String 	user = "";
	private String 	pass = "";
	
	@NotNull
	@Column(name="smtp_url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String value) {
		this.url = value;
	}
	
	@NotNull
	@Column(name="smtp_port")
	public int getPort() {
		return port;
	}
	public void setPort(int value) {
		this.port = value;
	}
	
	@NotNull
	@Column(name="smtp_user")
	public String getUser() {
		return user;
	}
	public void setUser(String value) {
		this.user = value;
	}
	
	@NotNull
	@Column(name="smtp_pass")
	public String getPass() {
		return pass;
	}
	public void setPass(String value) {
		this.pass = value;
	}
}