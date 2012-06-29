package org.openapplicant.domain;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CoverLetter;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.Note;
import org.openapplicant.domain.PhoneNumber;
import org.openapplicant.domain.Resume;
import org.openapplicant.util.TestUtils;


/**
 * Candidate test data builder.
 */
public class CandidateBuilder {

	private Company company = new CompanyBuilder().build();
	
	private Name name = new NameBuilder().build();
	
	private Note[] notes = new Note[]{};
	
	private PhoneNumber workPhoneNumber = new PhoneNumberBuilder().build();
	
	private PhoneNumber homePhoneNumber = new PhoneNumberBuilder().build();
	
	private PhoneNumber cellPhoneNumber = new PhoneNumberBuilder().build();
	
	private String email = TestUtils.uniqueEmail();
	
	private Candidate.Status status = Candidate.Status.SENT_EXAM;
	
	private Resume resume = new ResumeBuilder().build();
	
	private CoverLetter coverLetter = new CoverLetterBuilder().build();
	
	public CandidateBuilder withCompany(Company company) {
		this.company = company;
		return this;
	}
	
	public CandidateBuilder withName(Name name) {
		this.name = name;
		return this;
	}
	
	public CandidateBuilder withNotes(Note...notes) {
		if(null == notes) {
			notes = new Note[]{};
		}
		this.notes = notes;
		return this;
	}
	
	public CandidateBuilder withWorkPhoneNumber(PhoneNumber value) {
		workPhoneNumber = value;
		return this;
	}
	
	public CandidateBuilder withHomePhoneNumber(PhoneNumber value) {
		homePhoneNumber = value;
		return this;
	}
	
	public CandidateBuilder withCellPhoneNumber(PhoneNumber value) {
		cellPhoneNumber = value;
		return this;
	}
	
	public CandidateBuilder withEmail(String value) {
		email = value;
		return this;
	}
	
	public CandidateBuilder withStatus(Candidate.Status value) {
		status = value;
		return this;
	}
	
	public CandidateBuilder withResume(Resume value) {
		resume = value;
		return this;
	}
	
	public CandidateBuilder withCoverLetter(CoverLetter value) {
		coverLetter = value;
		return this;
	}
	
	public Candidate build() {
		Candidate candidate = new Candidate();
		candidate.setName(name);
		candidate.setCompany(company);
		for(Note each : notes) {
			candidate.addNote(each);
		}
		candidate.setWorkPhoneNumber(workPhoneNumber);
		candidate.setHomePhoneNumber(homePhoneNumber);
		candidate.setCellPhoneNumber(cellPhoneNumber);
		candidate.setEmail(email);
		candidate.setStatus(status);
		candidate.setResume(resume);
		candidate.setCoverLetter(coverLetter);
		return candidate;
	}
}
