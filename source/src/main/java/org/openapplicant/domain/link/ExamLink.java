package org.openapplicant.domain.link;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.NotNull;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.DomainObject;
import org.openapplicant.domain.Exam;
import org.openapplicant.policy.NeverCall;
import org.springframework.util.Assert;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="kind")
public abstract class ExamLink extends DomainObject {

	private Company company;
		
	private String description = "";
	
	private boolean used;

	private boolean multiUse;
	
	private ExamsStrategy strategy;
	
	public ExamLink(Company company, ExamsStrategy strategy) {
		Assert.notNull(company);
		Assert.notNull(strategy);
		
		this.company = company;
		this.strategy = strategy;
	}
	
	@NeverCall
	protected ExamLink(){}
	
	@Column(nullable=false,columnDefinition="bit(1) default 0")
	public boolean isUsed() {
	    return used;
	}
	public void setUsed(boolean used) {
	    this.used = used;
	}
	
	@Column(nullable=false,columnDefinition="bit(1) default 0")
	public boolean isMultiUse() {
	    return multiUse;
	}
	private void setMultiUse(boolean multiUse) {
	    this.multiUse = multiUse;
	}
	
	@NotNull
	@Column
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String value) {
		description = StringUtils.trimToEmpty(value);
	}
	
	@Transient
	public List<Exam> getExams() {
		return Collections.unmodifiableList(strategy.fetchExams(this));
	}
	
	@ManyToOne(
			optional=true, // FIXME: should be optional=false, but saving company.linkToAllExams seems to fail
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	//@JoinColumn(nullable=true)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Transient
	public URL getUrl() {
		return company.urlFor(this);
	}
	
	@OneToOne(
			cascade=CascadeType.ALL, 
			optional=false,
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private ExamsStrategy getExamsStrategy() {
		return strategy;
	}
	
	private void setExamsStrategy(ExamsStrategy value) {
		strategy = value;
	}

	/**
	 * @return a name describing the exams bound to this exam link.
	 */
	@Transient
	public String getName() {
		return getExams().size() == 1 ? getExams().get(0).getName() : "exams";
	}
}
