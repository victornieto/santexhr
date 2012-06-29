package org.openapplicant.domain;

import org.openapplicant.domain.Name;

/**
 * Builds test name objects
 */
public class NameBuilder {

	private String first = "Joe";
	
	private String middle = "Bob";
	
	private String last = "Briggs";
	
	public NameBuilder() {}
	
	public NameBuilder(String first, String last) {
		this.first = first;
		this.last = last;
		this.middle = "";
	}
	
	public NameBuilder withFirst(String value) {
		first = value;
		return this;
	}
	
	public NameBuilder withMiddle(String value) {
		middle = value;
		return this;
	}
	
	public NameBuilder withLast(String value) {
		last = value;
		return this;
	}
	
	public Name build() {
		Name name = new Name();
		name.setFirst(first);
		name.setMiddle(middle);
		name.setLast(last);
		return name;
	}
}
