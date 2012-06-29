package org.openapplicant.domain.question;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.NotEmpty;
import org.openapplicant.policy.AssertNotFrozen;


@Entity
public class CodeQuestion extends Question {
	
	private String answer = "";

	public CodeQuestion() {}
	
	/**
	 * @return this question's answer
	 */
	@Column(columnDefinition="longtext")
	public String getAnswer() {
		return answer;
	}
	
	@AssertNotFrozen
	public void setAnswer(String answer) {
		this.answer = StringUtils.trimToEmpty(answer);
	}
	
	/**
	 * @see Question#createSnapshot()
	 */
	@Override
	protected Question createNewInstance() {
		return new CodeQuestion();
	}
	
	/**
	 * @see Question#merge(Question)
	 */
	@Override
	protected void doMerge(Question other) {
		
		if(!(other instanceof CodeQuestion))
			throw new IllegalStateException();
		
		CodeQuestion code = (CodeQuestion) other;
		
		answer = code.getAnswer();
	}
	
	@Override
	public void accept(IQuestionVisitor visitor) {
		visitor.visit(this);
	}
}
