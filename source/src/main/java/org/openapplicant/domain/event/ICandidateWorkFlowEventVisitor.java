package org.openapplicant.domain.event;

public interface ICandidateWorkFlowEventVisitor {
	
	void visit(UserAttachedResumeEvent event);
	
	void visit(UserAttachedCoverLetterEvent event);
	
	void visit(AddNoteToCandidateEvent event);
	
	void visit(CandidateCreatedEvent event);
	
	void visit(CandidateCreatedByUserEvent event);
	
	void visit(CandidateStatusChangedEvent event);
	
	void visit(CreateExamLinkForCandidateEvent event);
	
	void visit(SittingGradedEvent event);
	
	void visit(SittingCreatedEvent event);
	
	void visit(SittingCompletedEvent event);
	
	void visit(FacilitatorReceivedEmailEvent event);
	
	void visit(FacilitatorRejectedResumeEvent event);
	
	void visit(FacilitatorRequestedResumeEvent event);
	
	void visit(FacilitatorSentExamLinkEvent event);
	
	void visit(UserSentExamLinkEvent event);
	
}
