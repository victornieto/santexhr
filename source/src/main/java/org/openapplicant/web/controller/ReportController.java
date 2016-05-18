package org.openapplicant.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Report;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.event.CandidateWorkFlowEvent;
import org.openapplicant.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportController extends AdminController{
	
	@Autowired
	private ReportService reportService;	
	
	@RequestMapping(value="/report.pdf")
	public ModelAndView find(@RequestParam("candidate") long candidateId) {
		Candidate candidate = getAdminService().findCandidateById(candidateId);
		
		Map<String,Object> xmap = new HashMap<String,Object>();
		xmap.put("candidate",  candidate);
		Collection<Sitting> sittings = candidate.getSittings();
		xmap.put("sittings", sittings);
		List<CandidateWorkFlowEvent> events = getAdminService().findAllCandidateWorkFlowEventsByCandidateId(candidate.getId());
		xmap.put("events",events);
		
		return new ModelAndView("pdf", xmap);
	}
	
	@RequestMapping(value="reports/reportCountCandidateByStatus")
	public String reportCountCandidateByStatus(@ModelAttribute("report") Report report, BindingResult binding, Map<String, Object> model) {
		
		if(binding.hasErrors()) {
			return "reports/view";		
		}
		
		//Star Date
		GregorianCalendar startDate  = new GregorianCalendar();
		startDate.setTime(report.getPeriodFrom());
		
		//End Date
		GregorianCalendar endDate  = new GregorianCalendar();
		endDate.setTime(report.getPeriodTo());
		
		//Adding 23:59
		endDate.add(Calendar.MINUTE, 1439);
		System.out.println(endDate.toString());
		
		List<Company> companies = reportService.findNightlyReportCompanies();
		List<String> reportList =  new ArrayList<String>();
		
		for(Company company : companies) {
			String reportFound = reportService.getDeltaReport(company.getId(),startDate,endDate);
			
			reportList.add(reportFound);
		}
		
		model.put("result",reportList);			

		return "reports/view";
	}
	
	@RequestMapping(value="reports/index")
	public String index() {
		return "reports/index";
	}
	
	@RequestMapping(value="reports/view")
	public String view(Map<String, Object> model) {
		Report report = new Report();
		model.put("report",report);
		return "reports/view";
	}
	
	@InitBinder(value="report")
	protected void initBinder(WebDataBinder binder, Locale locale) throws Exception {
			binder.registerCustomEditor(Date.class, null, 
					new CustomDateEditor(DateFormat.getDateInstance(DateFormat.SHORT, locale), false));
			binder.setRequiredFields(new String[]{"periodFrom", "periodTo"});
	}

}
