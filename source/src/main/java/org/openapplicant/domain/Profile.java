package org.openapplicant.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.validator.Email;
import org.hibernate.validator.Min;
import org.openapplicant.policy.NeverCall;
import org.openapplicant.util.Strings;


@Entity
public class Profile extends DomainObject {

	public enum Priority {
		OPTIONAL, REQUIRED, PREFERRED;
				
		/**
		 * @return the human readable string representing this priority.
		 */
		public String getHumanString() {
			return Strings.humanize(name());
		}
	};

	private boolean acceptResumesByEmail = false;

	private boolean forwardDailyReports = true;

	private String dailyReportsRecipient = "";

	private boolean forwardCandidateEmails = false;

	private String candidateEmailsRecipient = "";

	private boolean forwardJobBoardEmails = true;
	
	private String jobBoardEmailsRecipient = "";

	private Integer minInviteScore;

	private Integer maxRejectScore;

	private boolean solicitResumes = false;

	private Map<String, Priority> keywords = new HashMap<String, Priority>();

	private List<String> jobBoards = new ArrayList<String>();
	
	public Profile() {
		jobBoards.add("www.craigslist.org");
		jobBoards.add("www.dice.com");
		jobBoards.add("www.monster.com");
	}

	/**
	 * @return an unmodifiable map of keywords to priority.
	 */
	@Transient
	public Map<String, Priority> getKeywords() {
		return Collections.unmodifiableMap(keywords);
	}

	public void addKeyword(String keyword, Priority priority) {
		if (StringUtils.isBlank(keyword)) {
			return;
		}
		if (null == priority) {
			priority = Priority.OPTIONAL;
		}
		keywords.put(keyword, priority);
	}
	
	public void removeKeywords(List<String> removeKeywordList) {
		if (null != removeKeywordList) {
			for (String keyword : removeKeywordList) {
				keywords.remove(keyword);
			}
		}
	}

	@Column
	@JoinTable
	@CollectionOfElements
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Map<String, Priority> getKeywordsInternal() {
		return keywords;
	}

	private void setKeywordsInternal(Map<String, Priority> value) {
		if(value == null) {
			value = new HashMap<String, Priority>();
		}
		keywords = value;
	}

	@Column(nullable=false, columnDefinition = "bit(1) default 0")
	public boolean isAcceptResumesByEmail() {
		return acceptResumesByEmail;
	}

	public void setAcceptResumesByEmail(boolean acceptResumesViaEmail) {
		this.acceptResumesByEmail = acceptResumesViaEmail;
	}

	@Column(nullable=false, columnDefinition = "bit(1) default 0")
	public boolean isForwardDailyReports() {
		return forwardDailyReports;
	}

	public void setForwardDailyReports(boolean dailyReports) {
		this.forwardDailyReports = dailyReports;
	}

	@Email
	@Column
	public String getDailyReportsRecipient() {
		return dailyReportsRecipient;
	}

	public void setDailyReportsRecipient(String dailyReportRecipient) {
		this.dailyReportsRecipient = StringUtils.trim(dailyReportRecipient);
	}

	@Column(nullable=false, columnDefinition = "bit(1) default 0")
	public boolean isForwardCandidateEmails() {
		return forwardCandidateEmails;
	}

	public void setForwardCandidateEmails(boolean value) {
		this.forwardCandidateEmails = value;
	}

	@Email
	@Column
	public String getCandidateEmailsRecipient() {
		return candidateEmailsRecipient;
	}

	public void setCandidateEmailsRecipient(String value) {
		this.candidateEmailsRecipient = StringUtils.trim(value);
	}

	/**
	 * ForwardLimit is true if e-mail should only be forwarded from a specific
	 * list of domains.
	 * 
	 * @see getForwardLimitList
	 */
	@Column(nullable=false, columnDefinition = "bit(1) default 0")
	public boolean isForwardJobBoardEmails() {
		return forwardJobBoardEmails;
	}

	public void setForwardJobBoardEmails(boolean value) {
		this.forwardJobBoardEmails = value;
	}
	
	@Email
	@Column
	public String getJobBoardEmailsRecipient() {
		return jobBoardEmailsRecipient;
	}
	
	public void setJobBoardEmailsRecipient(String value) {
		jobBoardEmailsRecipient = StringUtils.trim(value);
	}

	@Min(0)
	@Column
	public Integer getMinInviteScore() {
		return minInviteScore;
	}

	public void setMinInviteScore(Integer value) {
		this.minInviteScore = value;
	}

	@Min(0)
	@Column
	public Integer getMaxRejectScore() {
		return maxRejectScore;
	}

	public void setMaxRejectScore(Integer value) {
		this.maxRejectScore = value;
	}

	/**
	 * @return true if this profile's company should solicit missing resumes
	 *         from candidates.
	 */
	@Column(nullable=false, columnDefinition = "bit(1) default 0")
	public boolean isSolicitResumes() {
		return solicitResumes;
	}

	/**
	 * @param value
	 *            true if this profile's company should solicit missing resumes
	 *            from candidates.
	 */
	public void setSolicitResumes(boolean value) {
		solicitResumes = value;
	}

	@Transient
	public List<String> getJobBoards() {
		return Collections.unmodifiableList(jobBoards);
	}
	
	/**
	 * Add the given domain, ignoring blank values
	 * @param domain
	 */
	public void addJobBoard(String domain) {
		if(StringUtils.isBlank(domain)) {
			return;
		}
		jobBoards.add(domain.trim());
	}
	
	/**
	 * Remove the given job boards
	 * @param domainList the list of entries to remove.
	 */
	public void deleteJobBoards(List<String> domainList) {
		if(null != domainList) {
			for (String domain : domainList){
				if(domain == null) {
					continue;
				}
				jobBoards.remove(domain.trim());
			}
		}
	}
	
	@NeverCall
	public void setJobBoards(List<String> values) {
		setJobBoardsInternal(values);
	}

	@Column
	@CollectionOfElements
	@JoinTable
	private List<String> getJobBoardsInternal() {
		return jobBoards;
	}

	private void setJobBoardsInternal(List<String> value) {
		if(value == null) {
			value = new ArrayList<String>();
		}
		jobBoards = value;
	}

	/**
	 * screens the resume against this profile
	 * 
	 * @return BigDecimal score
	 */
	@Transient
	public BigDecimal screenResume(String resume) {
		
		String lowerResume = resume.toLowerCase();
		int requiredTotal = 0;
		int requiredMatch = 0;
		int preferredTotal = 0;
		int preferredMatch = 0;
		int optionalTotal = 0;
		int optionalMatch = 0;
		Map<String,Priority> myKeywords = getKeywords();
		if (myKeywords == null) {
			return new BigDecimal(0.0);
		}
		for(String keyword : myKeywords.keySet()) {
			int count = StringUtils.countMatches(lowerResume, keyword.toLowerCase());
			
			switch(myKeywords.get(keyword)) {
			case REQUIRED:
				requiredTotal++;
				if (count > 0)
					requiredMatch++;
				break;
			case PREFERRED:
				preferredTotal++;
				if (count > 0) {
					preferredMatch++;
				}
				break;
			case OPTIONAL:
				optionalTotal++;
				if (count > 0) {
					optionalMatch++;
				}
				break;
			}
		}
		
		double score = 70.0;
		double increment = 0.0;
		if (requiredTotal > 1) {
			increment  =   ( 10 - (60 / (requiredTotal - 1)) ) / (requiredTotal / 2);
			System.err.println("Increment is "+increment);
		}
		for (int i = 0; i < (requiredTotal - requiredMatch); i++) {
			double change = 10 - (i * increment);
			System.err.println("deducting "+change);
			score -= change;
		}
		System.err.println("Score from "+requiredTotal+" required is "+score);
		
		double preferred = 0.0;
		if (preferredTotal > 0) 
			preferred = ( (20.0 * preferredMatch) / preferredTotal);
		else
			preferred = 20.0;
		System.err.println("Score from "+preferredTotal+" preferred is "+preferred);
		score += preferred;
		
		double optional = 0.0;
		if (optionalTotal > 0) 
			optional = ( (10.0 * optionalMatch)  / optionalTotal);
		else
			optional = 10.0;
		System.err.println("Score from "+optionalTotal+" optional is "+optional);
		score += optional;
		
		if (score < 0) {
			System.err.println("Score is < 0!!!!!");
			score = 0;
		} else if (score > 100) {
			System.err.println("Score is > 100!!!!");
			score = 100;
		}
		
		return new BigDecimal(score);
	}
}
