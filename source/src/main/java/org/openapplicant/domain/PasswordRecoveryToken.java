package org.openapplicant.domain;

import java.net.URL;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.springframework.util.Assert;

@Entity
public class PasswordRecoveryToken extends DomainObject {

	private User user;
	
	public PasswordRecoveryToken(User user) {
		Assert.notNull(user);
		this.user = user;
	}
	
	private PasswordRecoveryToken() {}
	
	@ManyToOne(
			optional=false, 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@JoinColumn
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public User getUser() {
		return user;
	}
	
	private void setUser(User user) {
		this.user = user;
	}
	
	@Transient
	public URL getUrl() {
		return user.getCompany().urlFor(this);
	}
}
