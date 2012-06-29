package org.openapplicant.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openapplicant.domain.Profile;
import org.openapplicant.util.TestUtils;


/**
 * Builds test Profile objects
 */
public class ProfileBuilder {
	
	private boolean dailyReports = true;
	
	private boolean solicitResumes = false;
	
	private String dailyReportRecipient = TestUtils.uniqueEmail();
	
	private int maxRejectScore = 20;
	
	private int minInviteScore = 80;
	
	private String forwardCandidateEmailRecipient = TestUtils.uniqueEmail();
	
	private String forwardResumeRecipient = TestUtils.uniqueEmail();
	
	private String whiteListRecipient = TestUtils.uniqueEmail();
	
	private Map<String, Profile.Priority> keywords = new HashMap<String, Profile.Priority>();
	
	/**
	 * Constructs a new ProfileBuilder
	 */
	public ProfileBuilder() {
		keywords.put("java", Profile.Priority.OPTIONAL);
	}
	
	public ProfileBuilder withNoKeywords() {
		keywords = new HashMap<String, Profile.Priority>();
		return this;
	}
	
	public ProfileBuilder addKeyword(String word, Profile.Priority priority) {
		keywords.put(word, priority);
		return this;
	}
	
	public ProfileBuilder withDailyReports(boolean value) {
		dailyReports = value;
		return this;
	}
	
	public ProfileBuilder withDailyReportRecipient(String value) {
		dailyReportRecipient = value;
		return this;
	}
	
	public ProfileBuilder withMaxRejectScore(int value) {
		maxRejectScore = value;
		return this;
	}
	
	public ProfileBuilder withMinInviteScore(int value) {
		minInviteScore = value;
		return this;
	}
	
	public ProfileBuilder withForwardResumeRecipient(String value) {
		forwardResumeRecipient = value;
		return this;
	}
	
	public ProfileBuilder withWhiteListRecipient(String value) {
		whiteListRecipient = value;
		return this;
	}
	
	public ProfileBuilder withForwardCandidateEmailRecipient(String value) {
		forwardCandidateEmailRecipient = value;
		return this;
	}
	
	public Profile build() {
		Profile result = new Profile();
		result.setForwardDailyReports(dailyReports);
		result.setDailyReportsRecipient(dailyReportRecipient);
		result.setMaxRejectScore(maxRejectScore);
		result.setMinInviteScore(minInviteScore);
		for(Entry<String, Profile.Priority> each : keywords.entrySet()) {
			result.addKeyword(each.getKey(), each.getValue());
		}
		result.setSolicitResumes(solicitResumes);
		result.setCandidateEmailsRecipient(forwardResumeRecipient);
		result.setJobBoardEmailsRecipient(whiteListRecipient);
		result.setCandidateEmailsRecipient(forwardCandidateEmailRecipient);
		return result;
	}

	public ProfileBuilder withRequestResume(boolean b) {
		solicitResumes = b;
		return this;
	}

}
