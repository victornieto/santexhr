package org.openapplicant.web.view;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Note;
import org.openapplicant.policy.NeverCall;

/**
 * User: franco
 * Date: 6/29/12
 * Time: 12:57 PM
 */
public class CandidateNoteView {
    private Candidate candidate;
    private Note note;

    @NeverCall
    public CandidateNoteView() {}

    public CandidateNoteView(Candidate candidate, Note note) {
        this.candidate = candidate;
        this.note = note;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public Note getNote() {
        return note;
    }
}
