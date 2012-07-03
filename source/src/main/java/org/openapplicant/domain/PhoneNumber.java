package org.openapplicant.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.NotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class PhoneNumber {
	
	private String number = "";
	
	private PhoneNumber() {
		super();
	} 
	
	public PhoneNumber(String number) {
		super();
		setNumber(number);
	}
	
	/**
	 * Factory method for creating an empty phone number
	 */
	public static final PhoneNumber createEmptyPhoneNumber() {
		return new PhoneNumber("");
	}

	@NotNull
	@Column(nullable=false)
	public String getNumber() {
		return number;
	}

	void setNumber(String number) {
		this.number = StringUtils.trimToEmpty(number);
	}
	
	/**
	 * @return true if this number is blank.
	 */
	@Transient
	public boolean isBlank() {
		return StringUtils.isBlank(number);
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof PhoneNumber)) {
			return false;
		}
		if(other == this) {
			return true;
		}
		PhoneNumber rhs = (PhoneNumber)other;
		return new EqualsBuilder()
						.append(number, rhs.getNumber())
						.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(97, 1039)
						.append(number)
						.toHashCode();
	}
	
	@Override 
	public String toString() {
		return new ToStringBuilder(this)
						.append("number", number)
						.toString();
	}

}
