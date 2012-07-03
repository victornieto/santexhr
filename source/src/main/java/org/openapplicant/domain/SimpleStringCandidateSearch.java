package org.openapplicant.domain;

import org.apache.commons.lang.StringUtils;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.util.CalendarUtils;
import org.openapplicant.util.Pagination;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.text.ParseException;
import java.util.*;


/**
 * Models a simple candidate search based on a single input string
 * with space separated search terms.
 */
@Configurable
@Entity
public class SimpleStringCandidateSearch extends CandidateSearch {

	private ICandidateDAO candidateDao;
	
	private String searchString;
	
	// these should be accessed using getters
	private CalendarRange dateRange;
	
	private List<String> searchTerms;
	
	/**
	 * Constructs a new SimpleCandidateSearch.
	 * 
	 * @param searchString a space separated search string.
	 * @param user the user who issued the search.
	 * @throws IllegalArgumentException if user is null
	 */
	public SimpleStringCandidateSearch(String searchString, User user) {
		super(user);
		this.searchString = StringUtils.trimToEmpty(searchString);
	}
	
	private SimpleStringCandidateSearch() {}
	
	/**
	 * Spring injected.
	 */
	public void setCandidateDao(ICandidateDAO value) {
		candidateDao = value;
	}
	
	/**
	 * @return the user's search string.
	 */
	@Column
	@Override
	public String getSearchString() {
		return searchString;
	}
	
	private void setSearchString(String value) {
		searchString = StringUtils.trimToEmpty(value);
	}
	
	/**
	 * @return a range of dates for the candidate search.
	 */
	@Transient
	public CalendarRange getDateRange() {
		if(null == dateRange) {
			dateRange = _parseDateRange(searchString);
		}
		return dateRange;
	}

	/**
	 * @return an unmodifiable collection of search terms.
	 */
	@Transient
	public List<String> getSearchTerms() {
		if(null == searchTerms) {
			searchTerms = _parseSearchTerms(searchString);
		}
		return Collections.unmodifiableList(searchTerms);
	}
	
	/**
	 * Executes the candidate search.
	 */
	@Override
	public List<Candidate> execute(Pagination pagination) {
		if(null == getUser().getCompany()) {
			return new ArrayList<Candidate>();
		}
		return candidateDao.performSearch(this, pagination); 
	}
	
	/**
	 * @param searchString space delimited string of search terms.
	 * @return a parsed date range.
	 */
	private CalendarRange _parseDateRange(String searchString) {
		Calendar startDate = null;
		Calendar endDate = null;
		
		for(String each : StringUtils.split(searchString)) {
			try {
				Calendar date = CalendarUtils.parse(each);
				if(null == startDate) {
					startDate = date;
				} else {
					endDate = date;
					break;
				}
			} catch(ParseException e) {
				// ignore
			}
		}
		return new CalendarRange(startDate, endDate);
	}
	
	/**
	 * @param searchString space delimited string of search terms.
	 * @return a list of search terms excluding any dates.
	 */
	private List<String> _parseSearchTerms(String searchString) {
		Set<String> included = new HashSet<String>();
		List<String> result = new ArrayList<String>();
		
		for(String each : StringUtils.split(searchString)) {
			try {
				CalendarUtils.parse(each);
			} catch(ParseException e) {
				each = each.replaceAll("\\W", "");
				if(StringUtils.isNotBlank(each) && !included.contains(each)) {
					included.add(each);
					result.add(each);
				}
			}
		}
		return result;
	}

}
