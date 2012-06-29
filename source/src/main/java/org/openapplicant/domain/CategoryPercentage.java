package org.openapplicant.domain;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;

@Entity
public class CategoryPercentage extends DomainObject {
	private Category category;
	private Double percentage = 1.0;
	private String artifactId = UUID.randomUUID().toString();
	
	/**
	 * @return the category of this categoryPercentage
	 */
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(nullable=false)
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Column(nullable=false)
	@Min(value=1)
	@Max(value=100)
	public Double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the artifactId
	 */
	@Column(nullable=false)
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * @param artifactId the artifactId to set
	 */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	/**
	 * Replace properties of this  with properties of the other
	 * category percentage, preserving the identity and artifactId of this 
	 * category percentage.
	 * 
	 * @param other the category percentage who's properties to merge with this 
	 * category percentage.
	 */
	public void merge(CategoryPercentage other) {
		setCategory(other.getCategory());
		setPercentage(other.getPercentage());
	}
}
