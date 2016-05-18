package org.openapplicant.domain;

import java.util.Collection;
import java.util.Date;

public class Report extends DomainObject{
	
	private Date periodFrom;
	
	private Date periodTo;
	
	private Collection<String> result;

	public Date getPeriodFrom() {
		return periodFrom;
	}	
	
	public void setPeriodFrom(Date periodFrom) {
		this.periodFrom = periodFrom;
	}
	
	public Date getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(Date periodTo) {
		this.periodTo = periodTo;
	}

	public Collection<String> getResult() {
		return result;
	}

	public void setResult(Collection<String> result) {
		this.result = result;
	}
		
}