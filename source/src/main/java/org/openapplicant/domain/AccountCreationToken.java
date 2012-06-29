package org.openapplicant.domain;

import java.net.URL;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.springframework.util.Assert;

@Entity
public class AccountCreationToken extends DomainObject {

	private String companyName = "";
	private String contactName = "";
	private String contactPhone = "";
	private String contactEmail = "";
	private String street = "";
	private String city = "";
	private String state = "";
	private String zipCode = "";
	private Company company = null;
	
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
	@OneToOne(
			optional=true
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}	
	

}
