package org.openapplicant.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.openapplicant.util.Pagination;
import org.springframework.util.Assert;


/**
 * Base class for modeling a user's candidate search.
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type")
public abstract class CandidateSearch extends DomainObject {

	private User user;
	
	protected CandidateSearch(User user) {
		Assert.notNull(user);
		this.user = user;
	}
	
	/**
	 * for orm tools only.  Subclasses should use the single argument
	 * constructor.
	 */
	protected CandidateSearch(){}
	
	/**
	 * @return the user who issued the candidate search.
	 */
	@ManyToOne(
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(nullable=false, updatable=false) 
	public User getUser() {
		return user;
	}
	
	private void setUser(User value) {
		user = value;
	}

	/**
	 * Retrieves a list of candidates.
	 * 
	 * @param pagination the pagination to apply
	 * @return the list of candidates.
	 */
	public abstract List<Candidate> execute(Pagination pagination);
	
	/**
	 * @return a readable string representing the candidate search.
	 */
	@Transient
	public abstract String getSearchString();
}
