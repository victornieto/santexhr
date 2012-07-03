package org.openapplicant.service;

import org.apache.commons.lang.time.DateFormatUtils;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Candidate.Status;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.User;
import org.openapplicant.domain.setting.Smtp;
import org.openapplicant.util.Pagination;
import org.openapplicant.util.Strings;
import org.springframework.mail.SimpleMailMessage;

import java.util.*;


public class ReportService extends ApplicationService  {

	/**
	 *
	 * @param companyId
	 * @param startDate
	 * @param endDate
	 * @return report text
	 */
	public String getDeltaReport(Long companyId, Calendar startDate, Calendar endDate) {
		List<Candidate> candidates = getCandidateDao().findByCompanyIDandDateRange(companyId, startDate, endDate);
		String returnValue = "";
		
		returnValue += "Santex HR Activity Report for "
				+DateFormatUtils.format(startDate.getTime(),"HH:mm MM/dd/yyyy", TimeZone.getDefault())+" - "
				+DateFormatUtils.format(endDate.getTime(),"HH:mm MM/dd/yyyy", TimeZone.getDefault())+"\n\n";

		returnValue += candidates.size() + " new candidates added to the database.\n\n";
		
		for(Candidate c : candidates) {
			returnValue += "\t" + c.getName().getFullName() + " - " + c.getEmail() + "\n";
			// screening score
			if (c.getResume() != null) {
				if (c.getResume().getScreeningScore() != null)
					returnValue += "\t\tScreening Score: "+ c.getResume().getScreeningScore().intValue() + "\n\n";
			}
			if (c.getMatchScore() != null) {
				returnValue += "\t\tMatching Score: " + c.getMatchScore() + "\n";
			}
			if (c.getLastSitting() != null) {
				returnValue += "\t\ttook exam "+c.getLastSitting().getExam().getName()+": "+c.getLastSitting().getScore() + "\n";
			}
		}
		
		returnValue += "\nCandidate Counts\n";
		Map<String,Integer> candidateCounts = getCandidateDao().findStatusCountsByCompanyId(companyId);
		for(Status s : Candidate.Status.values()) {
			Integer count = candidateCounts.get(s.name());
			/*
			for(Status substatus : s.getSubCategories()) {
				count = new Integer( candidateCounts.get(substatus.name()).intValue() + count.intValue() );
			}	
			*/
			returnValue += "\t" + Strings.humanize(s.name()) + " - " + count + "\n";
		}
		
		return returnValue;
	}

	public String getDailyReportRecipient(Long companyId) {
		Company company = getCompanyDao().find(companyId);
		Profile profile = company.getProfile();
		return profile.getDailyReportsRecipient();
	}

	public List<Company> findNightlyReportCompanies() {
		return getProfileDao().findNightlyReportCompanies();
	}

	public void sendMail(SimpleMailMessage msg, Smtp smtp) {
		sendEmailAsynchronously(msg, smtp);
	}

	public List<String> findDailyReportsRecipient(Company company) {
		List<String> returnValue = new ArrayList<String>();
		if (company.getProfile().getDailyReportsRecipient() != null) {
			returnValue.add(company.getProfile().getDailyReportsRecipient());
		} else {
			Pagination foo = Pagination.zeroBased().forPage(0).perPage(1000);
			List<User> users = getUserDao().findAllActiveAdminsByCompanyId(company.getId(), foo);
			for(User user : users) {
				returnValue.add(user.getEmail());
			}
		}
		return returnValue;
		
	}
	
	
}
