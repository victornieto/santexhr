package org.openapplicant.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.validator.Email;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;
import org.openapplicant.util.Strings;
import org.openapplicant.validation.UniqueWith;


@Entity
@Table(
		uniqueConstraints={@UniqueConstraint(columnNames={"email", "company"})}
)
public class Candidate extends DomainObject {
	
	private static final String PHONE_HOME = "home";
	
	private static final String PHONE_WORK = "work";
	
	private static final String PHONE_CELL = "cell";
	
	private Map<String, PhoneNumber> phoneNumbers = new HashMap<String,PhoneNumber>();
	
	private String email;
	
	private Name name = new Name();
	
	private Resume resume; 
	
	private CoverLetter coverletter; 
	
	private Status status = Status.INCOMPLETE;
	
	private Status lastActiveStatus = Status.INCOMPLETE;

	public String address;

	private Company company;
	
	private BigDecimal matchScore;
	
	private PhoneNumber homePhoneNumber = PhoneNumber.createEmptyPhoneNumber();
	
	private PhoneNumber workPhoneNumber = PhoneNumber.createEmptyPhoneNumber();
	
	private PhoneNumber cellPhoneNumber = PhoneNumber.createEmptyPhoneNumber();
	
	private List<Sitting> sittings = new ArrayList<Sitting>();
	
	private List<Note> notes = new ArrayList<Note>();
	
	private Sitting lastSitting;
	

	@Override
	public void beforeSave() {
		checkCompleteness();
	}
	
	private void checkCompleteness() {
		if(hasEnoughInformation() && isIncomplete()) {
			status = Status.NOT_TESTED;
		}
		// FIXME: if candidate does not have enough information, set the status to 
		// incomplete?  What happens if a test link has been sent and a 
		// candidate is regressed to INCOMPLETE?
	}
	
	private boolean hasEnoughInformation() {
		return name.hasFirst() || name.hasLast();
	}
	
	/**
	 * @return the candidate's resume or null if no resume has been set
	 */
	@OneToOne(
			cascade=CascadeType.ALL,
			fetch=FetchType.LAZY
	)
	@JoinColumn
	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}
		
	/**
	 * @return the candidate's cover letter or null if no cover letter
	 * has been set.
	 */
	@OneToOne(
			cascade=CascadeType.ALL,
			fetch=FetchType.LAZY
	)
	@JoinColumn
	public CoverLetter getCoverLetter() {
		return coverletter;
	}

	public void setCoverLetter(CoverLetter coverletter) {
		this.coverletter = coverletter;
	}

	/**
	 * @return the company applied to
	 */
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(nullable=false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Transient
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
		if(status.isActive()) {
			lastActiveStatus = status;
		}
	}
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private Status getStatusInternal() {
		return status;
	}
	
	private void setStatusInternal(Status value) {
		status = value;
	}
	
	public Status getLastActiveStatus() {
		return lastActiveStatus;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private void setLastActiveStatus(Status value) {
		lastActiveStatus = value;
	}
	
	/**
	 * Reverts a candidate to his last active state.
	 */
	public void unarchive() {
		status = lastActiveStatus;
	}
	
	/**
	 * @return the candidate's most recent sitting or null if no sittings
	 * have been taken.
	 */
	@OneToOne(
			cascade=CascadeType.ALL,
			fetch=FetchType.LAZY
	)
	public Sitting getLastSitting() {
		return lastSitting;
	}
	
	private void setLastSitting(Sitting value) {
		lastSitting = value;
	}
	
	/**
	 * @return an unmodifiable collection of Sitting Objects
	 */
	@Transient
	public Collection<Sitting> getSittings() {
		return Collections.unmodifiableList(sittings);
	}
	
	/**
	 * Adds a sitting to the given candidate.
	 * @param sitting
	 */
	public void addSitting(Sitting sitting) {
		if(null == sitting) {
			return;
		}
		sitting.setCandidate(this);
		sittings.add(sitting);
		lastSitting = sitting;
	}
	
	@OneToMany(mappedBy="candidate", cascade={CascadeType.ALL})
	private List<Sitting> getSittingsInternal() {
		return sittings;
	}
	
	private void setSittingsInternal(List<Sitting> value) {
		if(value == null) {
			value = new ArrayList<Sitting>();
		}
		sittings = value;
	}
	
	/**
	 * @return the candidate's contact email or null if none has been 
	 * set.
	 */
	@Email
	@UniqueWith("company.id")
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = StringUtils.trimToNull(email);
	}
	
	/**
	 * @return the candidate's name
	 */
	@NotNull
	@Valid
	@Embedded 
	public Name getName() {
		return name;
	}
	
	public void setName(Name name) {
		this.name = name;
	}
	
	/**
	 * @return an unmodifiable list of notes regarding the candidate.
	 */
	@Transient
	public List<Note> getNotes() {
		return Collections.unmodifiableList(notes);
	}
	
	/**
	 * Adds a note to the candidate
	 * @param note the note to add.
	 */
	public void addNote(Note note) {
		if(null == note) {
			return;
		}
		notes.add(note);
		
	}
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(
			name="candidate_note",
			inverseJoinColumns=@JoinColumn(name="note_id")
	)
	private List<Note> getNotesInternal() {
		return notes;
	}
	
	private void setNotesInternal(List<Note> value) {
		if(value == null) {
			value = new ArrayList<Note>();
		}
		notes = value;
	}
	
	/**
	 * @return the candidate's address or null if none has been set.
	 */
	@Column(columnDefinition="text")
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address=address;
	}
	
	@Transient
	public PhoneNumber getCellPhoneNumber() {
		return cellPhoneNumber;
	}

	public void setCellPhoneNumber(PhoneNumber n) {
		if(n == null) {
			n = PhoneNumber.createEmptyPhoneNumber();
		}
		cellPhoneNumber = n;
		phoneNumbers.put(PHONE_CELL, cellPhoneNumber);
	}
	
	@Transient
	public PhoneNumber getHomePhoneNumber() {
		return homePhoneNumber;
	}
	
	public void setHomePhoneNumber(PhoneNumber n) {
		if(n == null) {
			n = PhoneNumber.createEmptyPhoneNumber();
		}
		homePhoneNumber = n;
		phoneNumbers.put(PHONE_HOME, homePhoneNumber);
	}
	
	@Transient
	public PhoneNumber getWorkPhoneNumber() {
		return workPhoneNumber;
	}
	
	public void setWorkPhoneNumber(PhoneNumber n) {
		if(n == null) {
			n = PhoneNumber.createEmptyPhoneNumber();
		}
		workPhoneNumber = n;
		phoneNumbers.put(PHONE_WORK, workPhoneNumber);
	}
	
	/**
	 * Mapping method for hibernate
	 */
	@CollectionOfElements
	@JoinTable
	private Map<String,PhoneNumber> getPhoneNumbersInternal() {
		return phoneNumbers;
	}
	
	/**
	 * Mapping method for hibernate
	 */
	private void setPhoneNumbersInternal(Map<String,PhoneNumber> value) {
		if(value == null) {
			value = new HashMap<String,PhoneNumber>();
		}
		if(value.containsKey(PHONE_HOME)) {
			homePhoneNumber = value.get(PHONE_HOME);
		}
		if(value.containsKey(PHONE_WORK)) {
			workPhoneNumber = value.get(PHONE_WORK);
		}
		if(value.containsKey(PHONE_CELL)) {
			cellPhoneNumber = value.get(PHONE_CELL);
		}
		phoneNumbers = value;
	}
	
	@Transient
	public boolean isActive() {
		return status.isActive();
	}
	
	@Transient 
	public boolean isArchived() {
		return !isActive();
	}
	
	@Transient
	public boolean isIncomplete() {
		return status == Status.INCOMPLETE;
	}
	
	@Column
	public BigDecimal getMatchScore() {
		return matchScore;
	}
	
	public void setMatchScore(BigDecimal matchScore) {
		this.matchScore = matchScore;
	}

	//========================================================================
	// STATUS
	//========================================================================
	public enum Status {
		INCOMPLETE,
		NOT_TESTED,
		SENT_EXAM,
		EXAM_STARTED,
		READY_FOR_GRADING,
		GRADED,
		
		HIRED,
		RESUME_REJECTED,
		CANDIDATE_REJECTED,
		FUTURE_CANDIDATE,
		BENCHMARKED;
		
		public static List<Status> getActiveStatuses() {
			return Arrays.asList(
					INCOMPLETE,
					NOT_TESTED, 
					SENT_EXAM, 
					EXAM_STARTED, 
					READY_FOR_GRADING, 
					GRADED
			);
		}
		
		public static List<Status> getArchivedStatuses() {
			return Arrays.asList(
					HIRED, 
					RESUME_REJECTED, 
					CANDIDATE_REJECTED, 
					FUTURE_CANDIDATE,
					BENCHMARKED
			);
		}
		
		/**
		 * @return true if this status is not active
		 */
		public boolean isArchived() {
			return !isActive();
		}
		
		private boolean isActive() {
			return Status.getActiveStatuses().contains(this);
		}
	}
}
