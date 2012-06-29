package org.openapplicant.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.InvalidValue;
import org.openapplicant.policy.NeverCall;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import javax.persistence.*;


/**
 * Layer super type for domain objects.
 */
@MappedSuperclass
public abstract class DomainObject {
	
	private Long id;
	
	private EntityInfo entityInfo = new EntityInfo();
	
	/**
	 * @return the domain object's database identifier.
	 */
	@Id
	@GeneratedValue(generator="foreign")
	@GenericGenerator(
			name="foreign",
			strategy="foreign",
			parameters={@Parameter(name="property", value="entityInfo")}
	)
	public Long getId() {
		return id;
	}
	
	@NeverCall
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the domain object's meta-data
	 */
	@OneToOne(cascade=CascadeType.ALL, optional=false)
	@PrimaryKeyJoinColumn
	public EntityInfo getEntityInfo() {
		return entityInfo;
	}
	
	private void setEntityInfo(EntityInfo value) {
		entityInfo = value;
	}

	/**
	 * @return true if the domain object has not been persisted.  More
	 * specifically true if null == id.
	 */
	@Transient
	public boolean isNew() {
		return null == id;
	}
	
	/**
	 * @return the domain object's business key
	 */
	@Transient
	public String getGuid() {
		return entityInfo.getBusinessGuid();
	}
	
	/**
	 * <p>
	 * Validate the state of this domain object.  This method returns an 
	 * Errors instance who's object name is the uncapitalized version of 
	 * the current class's simple name.  
	 * </p>
	 * <b>example:</b>
	 * <pre>
	 *new User().validate().getObjectName()       => 'user'
	 *new CoverLetter.validate().getObjectName()  => 'coverLetter'
	 * </pre>
	 * <p>
	 * Field errors can be accessed by their bean property names.
	 * </p>
	 * <b>example:</b>
	 * <pre>
	 *new User().validate().getField("name.first")
	 * </pre>
	 * <p>
	 * This convention requires that Errors are represented using a spring
	 * form like following:
	 * </p>
	 * <pre>
	 * &lt;form:form commandName="user"&gt;
	 *   &lt;table&gt;
	 *     &lt;tr&gt;
	 *       &lt;td&gt;First Name:&lt;/td&gt;
	 *       &lt;td&gt;&lt;form:input path="name.first"/&gt;&lt;/td&gt;
	 *       &lt;td&gt;&lt;form:errors path="name.first"/&gt;&lt;/td&gt;
	 *     &lt;tr&gt;
	 *   &lt;/table&gt;
	 * &lt;/form:form&gt;
	 * </pre>
	 * @return this object's validation errors
	 */
	@SuppressWarnings("unchecked")
	public Errors validate() {
		Errors result = new BeanPropertyBindingResult(this, StringUtils.uncapitalize(getClass().getSimpleName()));
		
		// In order to validate @Unique constraints, it's necessary to use 
		// custom version of ClassValidator rather than the standard
		// org.hibernate.validator.ClassValidator
		InvalidValue[] values = new org.openapplicant.validation.ClassValidator(getClass()).getInvalidValues(this);
		for(InvalidValue each : values) {
			if(each.getPropertyName() == null) {
				result.reject(null, each.getMessage());
			} else {
				result.rejectValue(each.getPropertyPath(), null, each.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * Called by the persistence framework before this domain object is 
	 * saved.  Can be overridden by subclasses to hook into persistence 
	 * life cycle events.
	 */
	public void beforeSave() { }

    /**
     * Called by the persistence framework before this domain object is
     * deleted.  Can be overridden by subclasses to hook into persistence
     * life cycle events.
     */
    public void beforeDelete() { }
	
	@Override 
	public boolean equals(Object other) {
		if(!(other instanceof DomainObject)) {
			return false;
		}
		if(other == this){
			return true;
		}
		DomainObject rhs = (DomainObject) other;
		return entityInfo.equals(rhs.getEntityInfo());
	}
	
	@Override
	public int hashCode() {
		return entityInfo.hashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
						.append("id", id)
						.append("entityInfo", entityInfo)
						.toString();
	}
}
