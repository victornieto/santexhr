package org.openapplicant.web.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.event.CandidateWorkFlowEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ReportController extends AdminController{
	
	@SuppressWarnings("unchecked")
	@RequestMapping
	public ModelAndView find(@RequestParam("candidate") long candidateId) {
		Candidate candidate = getAdminService().findCandidateById(candidateId);
		
		Map xmap = new HashMap();
		xmap.put("candidate",  candidate);
		Collection<Sitting> sittings = candidate.getSittings();
		xmap.put("sittings", sittings);
		List<CandidateWorkFlowEvent> events = getAdminService().findAllCandidateWorkFlowEventsByCandidateId(candidate.getId());
		xmap.put("events",events);
		
		return new ModelAndView("pdf", xmap);
	}
}
