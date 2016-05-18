package org.openapplicant.domain;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.digest.StringDigester;
import org.openapplicant.util.Strings;
import org.openapplicant.validation.Unique;


@Entity
public class User extends DomainObject {

	private static final StandardStringDigester stringDigester = new StandardStringDigester();
	
	static {
		stringDigester.setAlgorithm("SHA-1");
		stringDigester.setIterations(1000);
	}
	
	/**
	 * @return the string digester used internally to hash a user's password
	 */
	public static StringDigester getStringDigester() {
		return stringDigester;
	}
	
	/**
	 * The maximum number of characters allowed in a password
	 */
	public static final int MAX_PASSWORD_LENGTH = 255;
	
	/**
	 * The minimum number of characters allowed in a password
	 */
	public static final int MIN_PASSWORD_LENGTH = 3;
	
	public enum Role {
		ROLE_HR,
		ROLE_GRADER,
		ROLE_HR_MANAGER,
		ROLE_SETTINGS,
		ROLE_ADMIN;
		
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
			String result = StringUtils.removeStart(name(), "ROLE_");
			return Strings.humanize(result);
		}
	}
	
	private String email = ""; 

	private String hashPassword = "";
	
	private Company company;

	private Name name;
	
	private boolean enabled;
	
	private Role role;
	
	private Calendar lastLoginTime;
	
	public User() {
		name = new Name();
		enabled = true;
	}
	
	@Unique
	@Email
	@NotEmpty
	@Column(nullable=false, unique=true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = StringUtils.trimToEmpty(email); 
	}
	
	@Column(nullable=false, columnDefinition="bit(1) default 1")
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean status) {
		this.enabled = status;
	}
	
	/**
	 * Toggles the user's current enabled status.
	 */
	public void toggleEnabled() {
		this.enabled = !this.enabled;
	}
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	/**
	 * @return the user's hashed password.
	 */
	@NotEmpty
	@Length(min=MIN_PASSWORD_LENGTH, max=MAX_PASSWORD_LENGTH)
	@Transient
	public String getPassword() {
		return hashPassword;
	}

	/**
	 * Sets and hashes a user's password
	 * @param password the user's text password
	 */
	public void setPassword(String password) {
		password = StringUtils.trimToEmpty(password);
		if(password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
			hashPassword = password; // don't hash password so we can report validation errors
		} else {
			this.hashPassword = stringDigester.digest(password);
		}
	}
	
	/**
	 * Check if the given text password matches this user's password.
	 * @return
	 */
	public boolean passwordMatches(String text) {
		text = StringUtils.trimToEmpty(text);
		return stringDigester.matches(text, hashPassword);
	}
	
	/**
	 * @return the user's digest password
	 */
	@Column(nullable=false, length=MAX_PASSWORD_LENGTH)
	private String getHashedPassword() {
		return hashPassword;
	}
	
	private void setHashedPassword(String value) {
		hashPassword = value;
	}
	
	@ManyToOne(
			optional=false,
			cascade ={CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(nullable=false)
	public Company getCompany() {
		return company;
	}

	void setCompany(Company company) {
		this.company = company;
	}

	@Valid
	@NotNull
	@Embedded
	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}
	
	/**
	 * @return the user's last login time, may be null if a user has 
	 * never logged in
	 */
	@Column
	public Calendar getLastLoginTime() {
		return lastLoginTime;
	}
	
	public void setLastLoginTime(Calendar lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
}
