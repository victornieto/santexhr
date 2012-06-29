package org.openapplicant.domain;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.Sitting;
import org.springframework.test.util.ReflectionTestUtils;

public class SittingBuilder {
	
	private Candidate candidate = new CandidateBuilder().build();
	
	private Exam exam = new ExamBuilder().build();
	
	private Long id = null;
	
	public SittingBuilder withCandidate(Candidate value) {
		candidate = value;
		return this;
	}
	
	public SittingBuilder withExam(Exam value) {
		exam = value;
		return this;
	}
	
	public SittingBuilder withId(Long value) {
		id = value;
		return this;
	}
	
	public Sitting build() {
		if(exam != null) {
			//exam.freeze();
		}
		Sitting sitting = new Sitting(exam);
		if(null != candidate) {
			candidate.addSitting(sitting);
		}
		ReflectionTestUtils.invokeSetterMethod(sitting, "id", id);
		return sitting;
	}
}
