package org.openapplicant.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;
import org.openapplicant.domain.question.Question;
import org.openapplicant.policy.NeverCall;
import org.springframework.util.Assert;


@Entity
public class Sitting extends DomainObject {
	
	private Candidate candidate;
	
	private Exam exam;
		
	private Score score = Score.ZERO;
	
	private ResponseSummary responseSummary = new ResponseSummary();
	
	private List<QuestionAndResponse> questionsAndResponses = 
		new ArrayList<QuestionAndResponse>();
	
	private int nextQuestionIndex = 0;
	
	/**
	 * @param exam the sitting's exam.
	 */
	public Sitting(Exam exam) {
		Assert.notNull(exam);
		
		this.exam = exam;
		for(Question each : exam.getQuestions()) {
			questionsAndResponses.add(new QuestionAndResponse(each));
		}
	}
	
	@NeverCall
	public Sitting() {}
	
	@ManyToOne(
		optional=false, 
		cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
		fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(nullable=false) 
	public Candidate getCandidate() {
		return candidate;
	}

	void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	@ManyToOne(
		optional=false, 
		cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
		fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(nullable=false)
	public Exam getExam() {
		return exam;
	}

	private void setExam(Exam exam) {
		this.exam = exam;
	}
	
	/**
	 * @return a list of questions and the candidate's response.
	 */
	@Transient
	public List<QuestionAndResponse> getQuestionsAndResponses() {
		return Collections.unmodifiableList(questionsAndResponses);
	}
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(nullable=false, name="sitting")
	@IndexColumn(name="ordinal")
	private List<QuestionAndResponse> getQuestionsAndResponsesInternal() {
		return questionsAndResponses;
	}
	
	private void setQuestionsAndResponsesInternal(List<QuestionAndResponse> value) {
		questionsAndResponses = value;
	}
	
	/**
	 * Assigns a response to a question with the given id.
	 * 
	 * @param questionId the id of the question responded to
	 * @param response the question's response
	 */
	public void assignResponse(long questionId, Response response) {
		Assert.notNull(response);
		
		QuestionAndResponse qar = findQuestionAndResponseByQuestionId(questionId);
		Assert.state(qar.getResponse() == null, "Prior response should not be replaced");
		responseSummary.addResponse(response);
		qar.setResponse(response); 
	}

	/**
	 * @return true if this sitting has more questions to advance to
	 */
	public boolean hasNextQuestion() {
		return nextQuestionIndex < questionsAndResponses.size();
	}
	
	/**
	 * @return true if the sitting is exhausted 
	 */
	@Transient
	public boolean isFinished() {
	    return !hasNextQuestion();
	}
	
	/**
	 * @return the next question
	 */
	public Question advanceToNextQuestion() {
		Assert.state(hasNextQuestion(), "All questions have been viewed.");
		
		Question result =  questionsAndResponses.get(nextQuestionIndex).getQuestion();
		nextQuestionIndex++;
		return result;
	}
	
	// FIXME: we shouldn't be using this method to display the index of the 
	// current question.  See QuestionController.js, question.ejs in 
	// src/main/js/openapplicant/quiz
	@Column(nullable=false) 
	public int getNextQuestionIndex() {
		return nextQuestionIndex;
	}
	
	private void setNextQuestionIndex(int value) {
		nextQuestionIndex = value;
	}
	
	@Valid
	@Embedded
	public Score getScore() {
	    return score;
	}
	
	private void setScore(Score value) {
	    score = value;
	}
	
	@NotNull
	@Valid
	@Embedded 
	public ResponseSummary getResponseSummary() {
		return responseSummary;
	}
	
	private void setResponseSummary(ResponseSummary responseSummary) {
		this.responseSummary = responseSummary;
	}
	
	/**
	 * Assigns a grade to a response with the given id.
	 * @param responseId the id of the response to grade
	 * @param grade the grade to assign.
	 */
	public void gradeResponse(long responseId, Grade grade) {
		assignGrade(responseId, grade);
		calculateScore();
		updateCandidateStatus();
	}
	
	private void assignGrade(long responseId, Grade grade) {
		Response response = findResponse(responseId);
		response.getGrade().updateScores(grade);
	}
	
	private Response findResponse(long responseId) {
		for(QuestionAndResponse each : questionsAndResponses) {
			if(each.getResponse() == null) {
				continue;
			}
			if(each.getResponse().getId() == null) {
				continue;
			}
			if(each.getResponse().getId().equals(responseId)) {
				return each.getResponse();
			}
		}
		throw new IllegalArgumentException("no response for id: " + responseId);
	}

	private void calculateScore() {	
		Score total = Score.ZERO;
		for(QuestionAndResponse each : questionsAndResponses ) {
			if(each.getResponse() == null) {
				continue;
			}
			if(each.getResponse().isNotGraded()) {
				continue;
			}
			total = total.add(each.getResponse().getGrade().getScore());
		}
		score = total.divide(questionsAndResponses.size()); 
	}
	
	private void updateCandidateStatus() {
		if(isEachResponseGraded()) {
			candidate.setStatus(Candidate.Status.GRADED);
		}
	}
	
	/**
	 * @return true if each response is graded.
	 */
	@Transient
	public boolean isEachResponseGraded() {
		for(QuestionAndResponse each : questionsAndResponses) {
			if(each.getResponse() == null) {
				continue;
			}
			if(each.getResponse().isNotGraded()) {
				return false;
			}
		}
		return true;
	}
	
	private QuestionAndResponse findQuestionAndResponseByQuestionId(long questionId) {
		for(QuestionAndResponse each: questionsAndResponses) {
			if(each.getQuestion().getId() == null) {
				continue;
			}
			if(each.getQuestion().getId().equals(questionId)) {
				return each;
			}
		}
		throw new IllegalArgumentException("question not found. id:" + questionId);
	}
}
