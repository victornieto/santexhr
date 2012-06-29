package org.openapplicant.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.question.Question;
import org.springframework.util.Assert;


@Entity
public class QuestionAndResponse extends DomainObject {
	
	private Question question;
	
	private Response response;
	
	/**
	 * Creates a QuestionAndResponse object with the given question.
	 */
	public QuestionAndResponse(Question question) {
		Assert.notNull(question);
		
		this.question = question;
	}
	
	private QuestionAndResponse() {}
	
	@ManyToOne(
			optional=false, 
			cascade={CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(nullable=false)
	public Question getQuestion() {
		return question;
	}
	
	private void setQuestion(Question question) {
		this.question = question;
	}
	
	/**
	 * May return null if no response was given.
	 */
	@OneToOne(
			optional=true, 
			cascade=CascadeType.ALL,
			fetch=FetchType.LAZY
	)
	@JoinColumn(nullable=true)
	public Response getResponse() {
		return response;
	}
	
	void setResponse(Response response) {
		this.response= response;
	}
}
