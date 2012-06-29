package org.openapplicant.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.util.CalendarUtils;
import org.openapplicant.util.Pagination;
import org.springframework.beans.factory.annotation.Configurable;


@Configurable
@Entity
public class PropertyCandidateSearch extends CandidateSearch {

	private ICandidateDAO candidateDao;
	
	private String nameString;
	
	private String skillsString;
	
	private String datesString;
	
	// these should be accessed using getters
	private Name name;
	
	private List<String> skills;
	
	private CalendarRange dateRange;
	
	private PropertyCandidateSearch(Builder builder) {
		super(builder.user);
		nameString = StringUtils.trimToEmpty(builder.nameString);
		skillsString = StringUtils.trimToEmpty(builder.skillsString);
		datesString = StringUtils.trimToEmpty(builder.datesString);
	}
	
	private PropertyCandidateSearch() {}
	
	/**
	 * Injected by spring
	 */
	public void setCandidateDao(ICandidateDAO value) {
		candidateDao = value;
	}
	
	/**
	 * @return a string parsable to a candidate name
	 */
	@Column
	public String getNameString() {
		return nameString;
	}
	
	private void setNameString(String value) {
		nameString = StringUtils.trimToEmpty(value);
	}
	
	/**
	 * @return a string parsable to a list of candidate skills. 
	 */
	@Column
	public String getSkillsString() {
		return skillsString;
	}
	
	private void setSkillsString(String value) {
		skillsString = StringUtils.trimToEmpty(value);
	}
	
	/**
	 * @return a string parsable to a date range.
	 */
	@Column
	public String getDatesString() {
		return datesString;
	}
	
	private void setDatesString(String value) {
		datesString = StringUtils.trimToEmpty(value);
	}
	
	/**
	 * @return the name of the candidate to search for.
	 */
	@Transient
	public Name getName() {
		if(null == name) {
			name = new Name(nameString);
		}
		return name;
	}
	
	/**
	 * @return an unmodifiable list of candidate skills to search for.
	 */
	@Transient
	public List<String> getSkills() {
		if(null == skills) {
			skills = _parseSkills(skillsString);
		}
		return Collections.unmodifiableList(skills);
	}
	
	/**
	 * @return a range of dates to search within.
	 */
	@Transient
	public CalendarRange getDateRange() {
		if(null == dateRange) {
			dateRange = CalendarUtils.parseRange(datesString);
		}
		return dateRange;
	}
	
	@Override
	public List<Candidate> execute(Pagination pagination) {
		if(null == getUser().getCompany()) {
			return new ArrayList<Candidate>();
		}
		return candidateDao.performSearch(this, pagination);
	}

	@Transient
	@Override
	public String getSearchString() {
		return new StringBuilder()
					.append(nameString)	
					.append(" ")
					.append(skillsString)
					.append(" ")
					.append(datesString)
					.toString();
	}
	

	/**
	 * @param skills delimited list of skills.  (delimiter can be commas or 
	 * whtiespace)
	 * @return a list of skill terms to search for.
	 */
	private List<String> _parseSkills(String skills) {
		Set<String> included = new  HashSet<String>();
		List<String> result = new ArrayList<String>();
		
		String[] skillList = StringUtils.contains(skills, ",") ? 
				StringUtils.split(skills, ",") : StringUtils.split(skills);
		
		for(String each : skillList) {
			each = StringUtils.trimToEmpty(each);
			if(StringUtils.isNotBlank(each) && !included.contains(each)) {
				included.add(each);
				result.add(each);
			}
		}
		return result;
	}
	
	//========================================================================
	// BUILDER
	//========================================================================
	/**
	 * Builds a PropertyCandidateSearch
	 */
	public static class Builder {
		private User user;
		private String nameString = "";
		private String skillsString = "";
		private String datesString = "";
		
		/**
		 * Constructs a new builder
		 * @param user the user who issued the search.
		 * @throws IllegalArgumentException if user is null
		 */
		public Builder(User user) {
			Validate.notNull(user);
			this.user = user;
		}
		
		public Builder name(String value) {
			nameString = value;
			return this;
		}
		
		public Builder skills(String value) {
			skillsString = value;
			return this;
		}
		
		public Builder dateRange(String value) {
			datesString = value;
			return this;
		}
		
		public PropertyCandidateSearch build() {
			return new PropertyCandidateSearch(this);
		}
	}
}
