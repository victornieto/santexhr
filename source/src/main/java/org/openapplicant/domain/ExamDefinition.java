package org.openapplicant.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.openapplicant.policy.NeverCall;
import org.springframework.util.Assert;

@Entity
public class ExamDefinition extends DomainObject {

	private String artifactId = UUID.randomUUID().toString();
	private List<CategoryPercentage> categoriesPercentage = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(CategoryPercentage.class));
	private Company company;
	private String description = "";
	private boolean facilitateEmail = false;
	private String genre = "";
	private String name = "";
	private boolean active = false;
	private Integer numberOfQuestionsWanted = 1;
	
	@Transient
	public boolean isComplete() {
		Double acum = 0.0;
		for (CategoryPercentage categoryPercentage : categoriesPercentage) {
			acum += categoryPercentage.getPercentage();
		}
		return acum == 100;
	}
	
	@Transient
	public Double getTotalPercentage() {
		Double sum = 0.0;
		for (CategoryPercentage categoryPercentage : categoriesPercentage) {
			sum += categoryPercentage.getPercentage();
		}
		return sum;
	}

	public CategoryPercentage getCategoryPercentageByArtifactId(
			String categoryPercentageArtifactId) {
		Assert.notNull(categoryPercentageArtifactId);
		for (CategoryPercentage categoryPercentage : categoriesPercentage) {
			if (categoryPercentage.getArtifactId().equals(categoryPercentageArtifactId)) {
				return categoryPercentage;
			}
		}
		return null;
	}
	
	/**
	 * @return the artifactId of this exam.
	 */
	@Column(nullable=false, name="artifact_id")
	public String getArtifactId() {
		return artifactId;
	}
	
	@NeverCall
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	@Transient
	public List<CategoryPercentage> getCategoriesPercentage() {
		return Collections.unmodifiableList(categoriesPercentage);
	}
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="examDefinition", nullable=false)
	private List<CategoryPercentage> getCategoriesPercentageInternal() {
		return categoriesPercentage;
	}
	
	private void setCategoriesPercentageInternal(
			List<CategoryPercentage> categoriesPercentage) {
		if (categoriesPercentage == null) {
			categoriesPercentage = new ArrayList<CategoryPercentage>();
		}
		this.categoriesPercentage = categoriesPercentage;
	}
	
	/**
	 * @return the company of this {@link ExamDefinition}
	 */
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(nullable=false)
	public Company getCompany() {
		return company;
	}
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	/**
	 * @return the exam definition's description
	 */
	@NotNull
	@Column(nullable=false)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isFacilitateEmail() {
		return facilitateEmail;
	}
	
	public void setFacilitateEmail(boolean facilitateEmail) {
		this.facilitateEmail = facilitateEmail;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * @return the exam definition's genre
	 */
	@NotNull
	@Column(nullable = false)
	public String getGenre() {
		return genre;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	/**
	 * @return the exam definition's name
	 */
	@NotEmpty
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(nullable=false)
	@Min(value=1)
	@Max(value=200)
	public Integer getNumberOfQuestionsWanted() {
		return numberOfQuestionsWanted;
	}
	
	public void setNumberOfQuestionsWanted(Integer numberOfQuestionsWanted) {
		this.numberOfQuestionsWanted = numberOfQuestionsWanted;
	}
	
	/**
	 * Adds a categoryPercentage to this exam definition.
	 * @param categoryPercentage categoryPercentages to add.
	 */
	public void addCategoryPercentage(CategoryPercentage categoryPercentage) {
		Assert.notNull(categoryPercentage);
		assertUniqueCategoryPercentageArtifact(categoryPercentage.getArtifactId());
        getCategoriesPercentageInternal().add(categoryPercentage);
	}
	
	private void assertUniqueCategoryPercentageArtifact(String artifactId) {
		Assert.state(getCategoryPercentageByArtifactId(artifactId) == null);
	}

	public void updateCategoryPercentage(CategoryPercentage categoryPercentage) {
		Assert.notNull(categoryPercentage);
		CategoryPercentage originalCategoryPercentage = getCategoryPercentageByArtifactId(categoryPercentage.getArtifactId());
		Assert.notNull(originalCategoryPercentage, "The specified category percentage was not found");
		originalCategoryPercentage.merge(categoryPercentage);
	}

    public void removeCategoryPercentage(String categoryPercentageArtifactId) {
        Assert.notNull(categoryPercentageArtifactId);
        for (CategoryPercentage categoryPercentage : getCategoriesPercentageInternal()) {
            if (categoryPercentage.getArtifactId().equals(categoryPercentageArtifactId)) {
                getCategoriesPercentageInternal().remove(categoryPercentage);
                return;
            }
        }
        Assert.state(false, "The specified category percentage was not found");
    }
}
