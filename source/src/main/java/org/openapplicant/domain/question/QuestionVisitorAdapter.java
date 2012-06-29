package org.openapplicant.domain.question;

import org.openapplicant.domain.question.CodeQuestion;
import org.openapplicant.domain.question.EssayQuestion;
import org.openapplicant.domain.question.IQuestionVisitor;
import org.openapplicant.domain.question.MultipleChoiceQuestion;

public class QuestionVisitorAdapter implements IQuestionVisitor {

	public void visit(CodeQuestion question) {}

	public void visit(EssayQuestion question) {}

	public void visit(MultipleChoiceQuestion question) {}

}
