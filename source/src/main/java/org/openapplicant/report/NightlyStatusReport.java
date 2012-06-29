package org.openapplicant.report;

import org.apache.commons.lang.time.DateFormatUtils;
import org.openapplicant.domain.Company;
import org.openapplicant.service.ReportService;
import org.springframework.mail.SimpleMailMessage;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;




public class NightlyStatusReport {

	private ReportService reportService;
	public void setReportService(ReportService arg) {
		reportService = arg;
	}
	
	protected void execute() {
		GregorianCalendar startDate          = new GregorianCalendar();
		startDate.add(Calendar.DAY_OF_MONTH,-1);
		GregorianCalendar endDate            = new GregorianCalendar();

		List<Company> companies = reportService.findNightlyReportCompanies();
		for(Company company : companies) {
			String report = reportService.getDeltaReport(company.getId(),startDate,endDate);
			SimpleMailMessage msg = new SimpleMailMessage();
			if (company.getProfile().getDailyReportsRecipient() == null) 
				continue;
			List<String> recipients = reportService.findDailyReportsRecipient(company);
			msg.setTo(recipients.toArray(new String[recipients.size()]));
			msg.setFrom("reports@admin.openapplicant.org");
			msg.setSubject(company.getName() + " Santex HR Activity Report for "+DateFormatUtils.format(startDate.getTime(),"MM/dd/yyyy", TimeZone.getDefault()));
			msg.setText(report);
			reportService.sendMail(msg, company.getSmtp());
		}
	}
	
}




/*
implements Job {


	public void execute(JobExecutionContext context) throws JobExecutionException {
		ReportService reportService = (ReportService)context.get("reportService");
		Long companyId              = (Long)context.get("companyId");
		Calendar startDate          = (Calendar)context.get("startDate");
		Calendar endDate            = (Calendar)context.get("endDate");
		
		String report = reportService.getDeltaReport(companyId, startDate, endDate);
		String recip  = reportService.getDailyReportRecipient(companyId);
	}

}
*/