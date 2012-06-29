package org.openapplicant.web.dwr;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Response;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.link.ExamLink;
import org.openapplicant.domain.question.Question;
import org.openapplicant.service.QuizService;
import org.springframework.test.util.ReflectionTestUtils;


public class DwrQuizService {
	
	private QuizService quizService;
	
	public void setQuizService(QuizService value) {
		quizService = value;
	}

	public void submitResponse(Long sittingId, Long questionId, Response response) {
		quizService.submitResponse(sittingId, questionId, response);
	}
}
