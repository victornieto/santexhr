package org.openapplicant.domain;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.openapplicant.domain.question.Question;
import org.openapplicant.policy.NeverCall;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.*;


@Entity
public class Exam extends DomainObject {
	
	private String name = "";
	
	private String description = "";
	
	private String genre = "";
	
	private List<Question> questions = new ArrayList<Question>();
	
	private String artifactId = UUID.randomUUID().toString();

    private JobPosition jobPosition;

	/**
	 * @return the artifactId of this exam.
	 */
	@Column(nullable=false, name="artifact_id")
	public String getArtifactId() {
		return artifactId;
	}
	
	@NeverCall
	public void setArtifactId(String value) {
		artifactId = value;
	}
	
	/**
	 * @return the exam's description
	 */
	@NotNull
	@Column(nullable=false)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String value) {
		description = StringUtils.trimToEmpty(value);
	}
	
	/**
	 * @return the exam's genre
	 */
	@NotNull
	@Column(nullable = false)
	public String getGenre() {
		return genre;
	}

	public void setGenre(String value) {
		genre = StringUtils.trimToEmpty(value);
	}
	
	@NotEmpty
	@Column(nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = StringUtils.trim(name);
	}
	
	/**
	 * @return an unmodifiable list of qustion objects.
	 */
	@Transient
	public List<Question> getQuestions() {
		return Collections.unmodifiableList(questions);
	}
	
	/**
	 * @param questionId the id of the question to find.
	 * @return a question with the given id
	 * @throws IllegalArgumentException if no question exists for the given
	 * id, or if id is null.
	 */
	public Question getQuestionById(Long questionId) {
		Assert.notNull(questionId);
		for(Question each : questions) {
			if(ObjectUtils.equals(each.getId(), questionId)) {
				return each;
			}
		}
		throw new IllegalArgumentException("No question for id " + questionId);
	}
	
	/**
	 * Retrieves a question with the given artifactId
	 * @return the question with the given artifactId or null if no question
	 * can be found
	 */
	public Question getQuestionByArtifactId(String artifactId) {
		Assert.notNull(artifactId);
		for(Question each : questions) {
			if(artifactId.equals(each.getArtifactId())) {
				return each;
			}
		}
		return null;
	}
	
	/**
	 * Adds a question to the exam.
	 * @param question the question to add.
	 * @throws IllegalStateException if a question exists with the same 
	 * artifactId.
	 */
	public void addQuestion(Question question) {
		assertUniqueQuestionArtifact(question.getArtifactId());
		questions.add(question);
	}
	
	private void assertUniqueQuestionArtifact(String artifactId) {
		Assert.state(getQuestionByArtifactId(artifactId) == null);
	}
	
	/**
	 * Updates a question by it's artifactId
	 * 
	 * @param updatedValue the updated question
	 */
	public void updateQuestion(Question updatedValue) {
		Question question = getQuestionByArtifactId(updatedValue.getArtifactId());
		if(question.isFrozen()) {
			Question snapshot = question.createSnapshot();
			snapshot.merge(updatedValue);
			int index = questions.indexOf(question);
			questions.set(index, snapshot);
		} else {
			question.merge(updatedValue);
		}
	}
	
	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable
	@IndexColumn(name="ordinal",base=1, nullable=false)
	private List<Question> getQuestionsInternal() {
		return questions;
	}
	
	private void setQuestionsInternal(List<Question> value) {
		if(value == null) {
			value = new ArrayList<Question>();
		}
		questions = value;
	}

    @ManyToOne
    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(JobPosition jobPosition) {
        this.jobPosition = jobPosition;
    }
}
