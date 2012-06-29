package org.openapplicant.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.NotNull;
import org.openapplicant.domain.question.Question;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Category extends DomainObject {
	private String name = "Untitled";
	private Company company;
	private List<Question> questions = new ArrayList<Question>();
	
	private Category parent;
	private List<Category> children = new ArrayList<Category>();
	
	/**
	 * @return the category's name
	 */
	@NotNull
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the company owning the category.
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
	 * @return the parent category owning this category.
	 */
	@ManyToOne
	@JoinColumn(nullable=true)
	public Category getParent() {
		return parent;
	}
	
	public void setParent(Category parent) {
		this.parent = parent;
	}

	/**
	 * @return an unmodifiable list of qustion objects.
	 */
	@Transient
	public List<Question> getQuestions() {
		return Collections.unmodifiableList(questions);
	}
	
	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="category")
	private List<Question> getQuestionsInternal() {
		return questions;
	}
	
	private void setQuestionsInternal(List<Question> questions) {
		if (questions == null) {
			questions = new ArrayList<Question>();
		}
		this.questions = questions;
	}
	
	/**
	 * @return an unmodifiable list of category objects.
	 */
	@Transient
	public List<Category> getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="parent")
	private List<Category> getChildrenInternal() {
		return children;
	}
	
	private void setChildrenInternal(List<Category> children) {
		if (children == null) {
			children = new ArrayList<Category>();
		}
		this.children = children;
	}

	/**
	 * Adds a question to the category.
	 * @param question the question to add.
	 */
	public void addQuestion(Question question) {
		questions.add(question);
        question.setCategory(this);
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
	
	/**
	 * Removes this question from this category
	 * @param question the question to remove
	 */
	public void removeQuestion(Question question) {
		questions.remove(question);
        question.setCategory(null);
	}
	
	/**
	 * Adds a subcategory to the category list.
	 * @param category the subcategory to add.
	 */
	public void addChild(Category category) {
		children.add(category);
	}
	
	@Transient
	public List<Category> getAllSubcategories() {
		List<Category> categoriesList = new ArrayList<Category>();
		addCategorySubcategoriesTo(categoriesList);
		return categoriesList;
	}
	
	private void addCategorySubcategoriesTo(List<Category> categoriesList) {
		categoriesList.addAll(children);
		for (Category child : children) {
			child.addCategorySubcategoriesTo(categoriesList);
		}
	}
	
	@Transient
	public List<Question> getAllQuestions() {
		List<Question> questionsList = new ArrayList<Question>();
		addCategoryQuestionsTo(questionsList);
		return questionsList;
	}
	
	private void addCategoryQuestionsTo(List<Question> questionsList) {
		questionsList.addAll(questions);
		for (Category child : children) {
			child.addCategoryQuestionsTo(questionsList);
		}
	}
}
