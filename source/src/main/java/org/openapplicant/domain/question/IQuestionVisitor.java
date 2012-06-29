package org.openapplicant.domain.question;

public interface IQuestionVisitor {

	void visit(CodeQuestion question);
	
	void visit(EssayQuestion question);
	
	void visit(MultipleChoiceQuestion question);
	
}
