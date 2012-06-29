package org.openapplicant.dao;

import org.openapplicant.domain.QuestionStatistics;
import org.openapplicant.domain.question.Question;

import java.util.List;


//============================================================================
// INTERFACE QUESTION DAO
//============================================================================
public interface IQuestionDAO extends IDomainObjectDAO<Question> {
	
	/**
	 * Returns line count statistics for the given question.
	 * @param question the question who's statistics to calculate.
	 */
	public QuestionStatistics getTotalTimeStatistics(Question question);
	public QuestionStatistics getWordsPerMinuteStatistics(Question question);
	public QuestionStatistics getKeyCharsStatistics(Question question);
	
	public QuestionStatistics getCorrectnessStatistics(Question question);
	public QuestionStatistics getStyleStatistics(Question question);
	public List<Question> getQuestionsWithoutCategory();
	public Question findByArtifactId(String questionArtifactId);

}
