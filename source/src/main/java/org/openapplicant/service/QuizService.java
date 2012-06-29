package org.openapplicant.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.Response;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.event.CandidateCreatedEvent;
import org.openapplicant.domain.event.SittingCompletedEvent;
import org.openapplicant.domain.event.SittingCreatedEvent;
import org.openapplicant.domain.link.ExamLink;
import org.openapplicant.domain.question.Question;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class QuizService extends ApplicationService {
	
	private static final Log log = LogFactory.getLog(QuizService.class);
	
	/**
	 * Retrieves an exam link by its guid, sets the exam link to be used.
	 * 
	 * @param guid the exam links guid
	 * @return examLink the exam link with the given guid.  Returns null if 
	 * no exam link exists with the given guid.
	 */
	public ExamLink getExamLinkByGuid(String guid) {
		try {
		    return getExamLinkDao().findByGuid(guid);
		} catch(DataRetrievalFailureException e) {
			log.error("getExamLinkByGuid", e);
			return null;
		}
	}
	
	/**
	 * Sets the examLink to used.
	 * @param examLink
	 */
	public void useExamLink(String guid) {
	    	ExamLink examLink = getExamLinkDao().findByGuidOrNull(guid);
	    	
	    	//FIXME: this should return a company exam link
	    	if(null == examLink)
	    	    return;

	    	examLink.setUsed(true);
	    	getExamLinkDao().save(examLink);
	}
	
	/**
	 * Retrieves a company by its request url
	 * 
	 * @param url the company's request url name
	 * @return company
	 * @throws DataRetrievalFailureException if no company exists.
	 */
	public Company findByUrl(String url) {
		return getCompanyDao().findByProxyname(getHostname(url));
	}
	
	private String getHostname(String url) {
		//host "http://localhost:8080/recruit-quiz-webclient/actions/submitCandidateLogin.jsp" 
		String[] splitURL = url.split("/");
		return splitURL[2];
	}
	
	/**
	 * This retruns 
	 * @param candidate a partially filled in candidate
	 * @param companyId the company to which the candidate will be associated
	 * @return
	 */
	public Candidate resolveCandidate(Candidate candidate, Company company) {	    
	    if(candidate.getId() != null) {
	    	return getCandidateDao().findOrNull(candidate.getId());
	    }
	    
	    Candidate existingCandidate = getCandidateDao().findByEmailAndCompanyIdOrNull(candidate.getEmail(), company.getId());
	    if(null == existingCandidate) {
	    	candidate.setCompany(company);
	    	candidate = getCandidateDao().save(candidate);
	    	getCandidateWorkFlowEventDao().save(new CandidateCreatedEvent(candidate));
	    } else {
	    	candidate = existingCandidate;
	    }
	    
	    return candidate;
	}
	
	/**
	 * Creates a for the given candidate, creating the candidate if it is 
	 * new.
	 * 
	 * @param candidate the candidate to a create a sitting for.
	 * @param companyId the id of the company the candidate will be added to.
	 * @param examArtifactId the artifactId of the exam to take
	 * @return the created sitting
	 */
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Sitting createSitting(Candidate candidate, String examArtifactId) {
		// FIXME: what if artifactId refers to a different exam version after the break?
		Exam exam = findExamByArtifactId(examArtifactId);

		Sitting sitting = null;
		for(Sitting s : candidate.getSittings()) {
		    if(s.getExam().getArtifactId().equals(examArtifactId)) {
		    	sitting = s;
		    	break;
		    }
		}
		
		if(null == sitting) {
		    sitting = new Sitting(exam);
		    candidate.addSitting(sitting);
		    candidate.setStatus(Candidate.Status.EXAM_STARTED);
		    getCandidateWorkFlowEventDao().save(new SittingCreatedEvent(sitting));
		    getCandidateDao().save(candidate);
		}
		return sitting;
	}
	
	/**
	 * Retrieves the next question and updates the sitting index.
	 * 
	 * @param sittingId
	 * @return
	 */
	public Question nextQuestion(Sitting sitting) {
        	Question result = sitting.advanceToNextQuestion();
        	if(!sitting.hasNextQuestion()) {
        		sitting.getCandidate().setStatus(Candidate.Status.READY_FOR_GRADING);
        		getCandidateDao().save(sitting.getCandidate());
        		getCandidateWorkFlowEventDao().save(new SittingCompletedEvent(sitting));
        	}
        	getSittingDao().save(sitting);
        	return result;
	}

	/**
	 * Retrieves a sitting with the given id
	 * 
	 * @param sittingId
	 * @return Sitting
	 * @throws DataRetrievalFailureException
	 *             if no sitting has sittingId
	 */
	public Sitting findSittingById(Long id) {
		return getSittingDao().find(id);
	}
	
	/**
	 * Retrieves a sitting with the given guid
	 * 
	 * @param guid
	 * @return Sitting
	 * @throws DataRetrievalFailureException
	 *             if no sitting has guid
	 */	
	public Sitting findSittingByGuid(String guid) {
		return getSittingDao().findByGuid(guid);
	}
	
	/**
	 * Persists a candidate's response to a question.
	 * 
	 * @param sittingId the id of the current sitting
	 * @param questionId the id of the question responded to
	 * @param response the candidate's response.
	 * @return the saved response.
	 */
	public Response submitResponse(Long sittingId, Long questionId, Response response) {
		Sitting sitting = getSittingDao().find(sittingId);
		sitting.assignResponse(questionId, response);
		getSittingDao().save(sitting);
		return response = getResponseDao().save(response);
	}
}
