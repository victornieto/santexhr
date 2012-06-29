package org.openapplicant.domain.question;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotEmpty;
import org.openapplicant.domain.Category;
import org.openapplicant.domain.DomainObject;
import org.openapplicant.policy.AssertNotFrozen;
import org.openapplicant.policy.IFreezable;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="kind")
public abstract class Question extends DomainObject implements IFreezable {
	
	private Integer timeAllowed;
	
	private String name = "Untitled";
	
	private String prompt = "New Question";
	
	private String artifactId = UUID.randomUUID().toString();
	
	private Question nextVersion;
	
	private Question previousVersion;
	
	private boolean isFrozen;
	
	private Category category;

	@Column(nullable=false) 
	public String getArtifactId() {
		return artifactId;
	}
	
	public void setArtifactId(String value) {
		artifactId = value;
	}
	
	/**
	 * @return this question's name
	 */
	@NotEmpty
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	
	@AssertNotFrozen
	public void setName(String name) {
		this.name = StringUtils.trimToEmpty(name);
	}
	
	/**
	 * @return this question's prompt
	 */
	@NotEmpty
	@Column(nullable=false,columnDefinition="longtext")
	public String getPrompt() {
		return prompt;
	}
	
	@AssertNotFrozen
	public void setPrompt(String prompt) {
		this.prompt = StringUtils.trimToEmpty(prompt);
	}
	
	/**
	 * @return the time allowed for this question in seconds.
	 * Returns null if this question is untimed.
	 */
	@Min(value=1)
	@Column(nullable=true)
	public Integer getTimeAllowed() {
		return timeAllowed;
	}

	@AssertNotFrozen
	public void setTimeAllowed(Integer timeAllowed) {
		this.timeAllowed = timeAllowed;
	}
	
	/**
	 * @return true if this question is untimed.
	 */
	@Transient
	public boolean isUntimed() {
		return timeAllowed == null;
	}

	/**
	 * @return true if this question is frozen and must not be edited.
	 */
	@Column(nullable=false) 
	public boolean isFrozen() {
		return isFrozen;
	}
	
	@Transient
	public boolean isNotFrozen() {
		return !isFrozen;
	}
	
	private void setFrozen(boolean value) {
		isFrozen = value;
	}
	
	/**
	 * Freeze this question and prevent it from being further edited.
	 */
	public void freeze() {
		isFrozen = true;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=true)
	public Question getNextVersion() {
		return nextVersion;
	}
	
	public void setNextVersion(Question question) {
		nextVersion = question;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=true)
	public Question getPreviousVersion() {
		return previousVersion;
	}
	
	public void setPreviousVersion(Question question) {
		previousVersion = question;
	}
	
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Transient
	public boolean isLatestVersion() {
		return nextVersion == null;
	}
	
	@Transient
	public boolean isOldestVersion() {
		return previousVersion == null;
	}
	
	@Transient
	public boolean isSnapshot() {
		return isLatestVersion() && isNotFrozen();
	}
	
	/**
	 * Creates a snapshot of the latest question version, 
	 * freezing the latest question.
	 * @return the created snapshot
	 * @throws IllegalStateException if this question is not the latest 
	 * version
	 */
	public Question createSnapshot() {
		Question snapshot = createNewInstance();
		Assert.state(this.isLatestVersion(), "snapshot may only be created from the latest version");
		freeze();
		
		nextVersion = snapshot;
		snapshot.setPreviousVersion(this);
		snapshot.artifactId = getArtifactId();
		snapshot.merge(this);
		return snapshot;
	}
	
	protected abstract Question createNewInstance();
	
	/**
	 * Replace properties of this question with properties of the other question,
	 * preserving the identity and artifactId of this question.
	 * 
	 * @param other the questions who's properties to merge with this question.
	 * @throws IllegalStateException if the question is not of the
	 * same type.
	 */
	@AssertNotFrozen
	public void merge(Question other) {
		setPrompt(other.getPrompt());
		setTimeAllowed(other.getTimeAllowed());
		setName(other.getName());
		setCategory(other.getCategory());
		doMerge(other);
	}
	
	protected abstract void doMerge(Question question);
	
	/**
	 * Accepts the given visitor
	 * @param visitor the visitor to accept
	 */
	public abstract void accept(IQuestionVisitor visitor);

}
