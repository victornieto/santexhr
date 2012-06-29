package org.openapplicant.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Map;

import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Grade;
import org.openapplicant.domain.QuestionAndResponse;
import org.openapplicant.domain.Response;
import org.openapplicant.domain.Score;
import org.openapplicant.domain.Sitting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ResultsController extends AdminController {
	
	@RequestMapping(method=GET)
	public String exam( @RequestParam("s") long sittingId,
			    		Map<String,Object> model) {
	    
	    Sitting sitting = getAdminService().findSittingById(sittingId);
	    
	    model.put("sitting", sitting);
	    model.put("candidate", sitting.getCandidate());
    
	    model.put("questionAndResponses", sitting.getQuestionsAndResponses());

	    int questions = sitting.getQuestionsAndResponses().size();
	    int untimedQuestions = 0;
	    model.put("questions", questions);
	    for(int i=0; i<questions; i++) {
	    	if(sitting.getQuestionsAndResponses().get(i).getQuestion().isUntimed())
	    		untimedQuestions++;
	    }
	    model.put("untimedQuestions", untimedQuestions);
	    
	    model.put("activeStatuses", Candidate.Status.getActiveStatuses());
	    model.put("archivedStatuses", Candidate.Status.getArchivedStatuses());
	    
	    return "results/exam";
	}
    
	@RequestMapping(method=GET)
	public String question(	@RequestParam("s") long sittingId,
							@RequestParam(value="i", required=false) Integer index,
							Map<String,Object> model) {
	    		
	    Sitting sitting = getAdminService().findSittingById(sittingId);
	    model.put("sitting", sitting);
		
	    if(index == null || index >= sitting.getExam().getQuestions().size()) {
	    	index = 0;
	    }
		
	    model.put("index", index);
	
	    populateModelForQuestion(model, sitting.getCandidate(), sitting.getQuestionsAndResponses().get(index));	
		
	    return "results/question";
	}

	@RequestMapping(method=POST)
	public String setGrade(	@RequestParam("s") long sittingId,
							@RequestParam("i") int index,
							@RequestParam("responseId") long responseId,
							@RequestParam("correctness")	Double correctness,
							@RequestParam("style") 		Double style,
							@RequestParam(value="save", required=false) String save,
							@RequestParam(value="next", required=false) String next,
							Map<String,Object> model) {
		
	    Grade grade = new Grade();
		
	    if(correctness != null) {
	    	grade.putScore("function", new Score(correctness));
	    }
		
	    if(style != null) {
	    	grade.putScore("form", new Score(style));
	    }
		
	    getAdminService().updateResponseGrade(currentUser(), sittingId, responseId, grade);

	    if(next != null) {
	    	index++;
	    }
	    
	    return "redirect:question?s="+sittingId+"&i="+index;
	}
	
	private void populateModelForQuestion(Map<String,Object> model, Candidate candidate, QuestionAndResponse qr) {
	    
	    model.put("candidate", candidate);
	    
	    model.put("question", qr.getQuestion());
	    model.put("response", qr.getResponse());

	    model.put("totalTimeStat", getAdminService().getTotalTimeStatistics(qr.getQuestion()));
	    model.put("wordsPerMinuteStat", getAdminService().getWordsPerMinuteStatistics(qr.getQuestion()));
	    model.put("keyCharsStat", getAdminService().getKeyCharsStatistics(qr.getQuestion()));

	    model.put("correctnessStat", getAdminService().getCorrectnessStatistics(qr.getQuestion()));
	    model.put("styleStat", getAdminService().getStyleStatistics(qr.getQuestion()));

	    model.put("activeStatuses", Candidate.Status.getActiveStatuses());
	    model.put("archivedStatuses", Candidate.Status.getArchivedStatuses());
	}
}
