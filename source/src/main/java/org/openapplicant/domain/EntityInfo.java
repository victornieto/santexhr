package org.openapplicant.domain;

import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.openapplicant.util.CalendarUtils;


/**
 * Meta-data holder for persistent entities. 
 */
@Entity
public class EntityInfo {
	
	private Long id;
	
	private Calendar createdDate;
	
	private String businessGuid = UUID.randomUUID().toString();
	
	/**
	 * @return this object's database identifier
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	private void setId(Long value) {
		id = value;
	}
	
	/**
	 * @return the time of database insertion
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition="timestamp") 
	@Generated(GenerationTime.INSERT)
	public Calendar getCreatedDate() {
		return createdDate;
	}

	private void setCreatedDate(Calendar ts) {
		this.createdDate = ts;
	}
	
	/**
	 * @return the entity's business uuid identifier.  This value is generated 
	 * independently from the persistence layer. 
	 */
	@Column(name="guid", nullable=false, updatable=false, length=36)
	public String getBusinessGuid() {
		return businessGuid;
	}
	
	private void setBusinessGuid(String value) {
		businessGuid = value;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof EntityInfo)) {
			return false;
		}
		if(other == this) {
			return true;
		}
		EntityInfo rhs = (EntityInfo)other;
		return businessGuid.equals(rhs.getBusinessGuid());
	}
	
	@Override
	public int hashCode() {
		return businessGuid.hashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
					.append("id", id)
					.append("businessGuid", businessGuid)
					.append("createdDate", CalendarUtils.toString(createdDate))
					.toString();
	}
}
