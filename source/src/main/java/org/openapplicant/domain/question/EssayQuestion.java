package org.openapplicant.domain.question;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.NotEmpty;
import org.openapplicant.policy.AssertNotFrozen;
import org.openapplicant.policy.NeverCall;
import org.springframework.util.Assert;


@Entity
public class EssayQuestion extends Question {
	
	private String answer = "";

	public EssayQuestion() {}
	
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
		return new EssayQuestion();
	}
	
	/**
	 * @see Question#merge(Question)
	 */
	@Override
	protected void doMerge(Question other) {
		
		if(!(other instanceof EssayQuestion))
			throw new IllegalStateException();
		
		EssayQuestion code = (EssayQuestion) other;
		
		answer = code.getAnswer();
	}
	
	@Override
	public void accept(IQuestionVisitor visitor) {
		visitor.visit(this);
	}
}
