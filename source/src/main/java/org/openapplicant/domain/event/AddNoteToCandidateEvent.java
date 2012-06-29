package org.openapplicant.domain.event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Note;
import org.springframework.util.Assert;


/**
 * Event raised when a user adds a note to a candidate
 */
@Entity
public class AddNoteToCandidateEvent extends CandidateWorkFlowEvent {
	
	private Note note;
	
	/**
	 * Constructs a new event
	 * @param candidate the candidate who was noted
	 * @param user the user who added the note
	 * @param note the note added
	 */
	public AddNoteToCandidateEvent(Candidate candidate, Note note) {
		super(candidate);
		Assert.notNull(note);
		this.note = note;
	}
	
	private AddNoteToCandidateEvent(){}
	
	/**
	 * @return the note added to the candidate.
	 */
	@OneToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Note getNote() {
		return note;
	}
	
	private void setNote(Note note) {
		this.note = note;
	}
	
	@Override
	public void accept(ICandidateWorkFlowEventVisitor visitor) {
		visitor.visit(this);
	}

}
