package org.openapplicant.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openapplicant.util.CalendarUtils;


/**
 * Immutable object representing a date range.
 */
@Embeddable
public class CalendarRange {
	
	/**
	 * Null range object.
	 */
	public static final CalendarRange EMPTY = new CalendarRange(null, null);
	
	private Calendar startDate;
	
	private Calendar endDate;
	
	/**
	 * Constructs a new CalendarRange
	 * 
	 * @param startDate may be null
	 * @param endDate may be null
	 */
	public CalendarRange(Calendar startDate, Calendar endDate) {
		if(startDate != null) {
			this.startDate = (Calendar)startDate.clone();
		}
		if(endDate != null) {
			this.endDate = (Calendar)endDate.clone();
		}
	}
	
	private CalendarRange(){}
	
	/**
	 * @return the calendar's start date.  Returns null if no start date 
	 * was set.
	 */
	@Column
	public Calendar getStartDate() {
		if(null == startDate) {
			return null;
		}
		return (Calendar)startDate.clone();
	}
	
	private void setStartDate(Calendar value) {
		startDate = value;
	}
	
	/**
	 * @return the calendar's end date. Returns null if no end date was set.
	 */
	@Column
	public Calendar getEndDate() {
		if(null == endDate) {
			return null;
		}
		return (Calendar) endDate.clone();
	}
	
	private void setEndDate(Calendar value) {
		endDate = value;
	}
	
	/**
	 * @return true if the calendar has a start and end date.
	 */
	@Transient
	public boolean isBounded() {
		return null != startDate && null != endDate;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
					.append(getClass().getSimpleName())
					.append("[")
					.append("startDate=").append(CalendarUtils.toString(startDate))
					.append(", ")
					.append("endDate=").append(CalendarUtils.toString(endDate))
					.append("]")
					.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof CalendarRange)) {
			return false;
		}
		if(other == this) {
			return true;
		}
		CalendarRange rhs = (CalendarRange) other;
		
		return new EqualsBuilder()
					.append(startDate, rhs.startDate)
					.append(endDate, rhs.endDate)
					.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 41)
					.append(startDate)
					.append(endDate)
					.toHashCode();
	}

}
