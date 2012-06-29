package org.openapplicant.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

@Embeddable
public class Name {
	
	private String first = "";
	
	private String middle = "";

	private String last = "";
	
	public Name() {} 

	public Name(String arg) {
		if (arg == null) {
			return;
		}
		
		// change the name from "Meadows, Rob" to "Rob Meadows"
		Pattern commaPattern = Pattern.compile("^(\\w+),\\s?");
		Matcher matcher = commaPattern.matcher(arg);
		String toParse = "";
		if (matcher.find()) {
			String lastName = matcher.group(1);
			String firstName = arg.replace(lastName+",", "");
			toParse = firstName + " " + lastName;
		} else {
			toParse = arg;
		}
		
		// clean up garbage characters from the string
		toParse = toParse.replaceAll("[^a-zA-Z0-9 ]", "");


		String[] names = toParse.trim().split(" ");
		
		if (names.length == 0)
			return;
		
		switch (names.length) {
		case 1:
			setFirst(names[0]); 
			break;
		case 2:
			setFirst(names[0]);
			setLast(names[1]);
			break;
		case 3:
			setFirst(names[0]);
			setMiddle(names[1]);
			setLast(names[2]);
			break;
		default:
			setFirst(names[0]);
			setLast(names[names.length-1]);
			setMiddle(StringUtils.join(ArrayUtils.subarray(names, 1, names.length-2)," "));
		}
	}
	
	/**
	 * @return true if all name fields are blank
	 */
	@Transient
	public boolean isBlank() {
		return StringUtils.isBlank(first) && 
				StringUtils.isBlank(middle) && 
				StringUtils.isBlank(last);
	}
	
	/**
	 * @return true if any name field is not blank.
	 */
	@Transient
	public boolean isNotBlank() {
		return !isBlank();
	}
	
	@Transient
	public String getFullName() {
		return (first + " " + middle + " " + last).trim();
	}
	
	@Transient
	public String getSortName() {
		return (last + ", " + first + " " + middle).trim();
	}
	
	@NotNull
	@Column(name="firstname", nullable=false)
    @NotEmpty
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = StringUtils.trimToEmpty(first);
	}
	
	/**
	 * @return true if first name is blank.
	 */
	public boolean hasFirst() {
		return StringUtils.isNotBlank(first);
	}

	@NotNull
	@Column(name="middlename", nullable=false)
	public String getMiddle() {
		return middle;
	}
	public void setMiddle(String middle) {
		this.middle = StringUtils.trimToEmpty(middle);
	}
	
	/**
	 * @return true if middle name is blank
	 */
	public boolean hasMiddle() {
		return StringUtils.isNotBlank(middle);
	}
	
	@NotNull
	@Column(name="lastname", nullable=false)
    @NotEmpty
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = StringUtils.trimToEmpty(last);
	}
	
	/**
	 * @return true if last name is blank.
	 */
	public boolean hasLast() {
		return StringUtils.isNotBlank(last);
	}
	
	@Override
	public String toString() {
		return getFirst() + " " + getLast();
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Name)) {
			return false;
		}
		if(this == other) {
			return true;
		}
		Name rhs = (Name)other;
		return new EqualsBuilder()
			.append(first, rhs.getFirst())
			.append(middle, rhs.getMiddle())
			.append(last, rhs.getLast())
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 29)
			.append(first)
			.append(middle)
			.append(last)
			.toHashCode();
	}
}
