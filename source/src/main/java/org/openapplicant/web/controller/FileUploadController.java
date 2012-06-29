package org.openapplicant.web.controller;

import org.apache.commons.io.FilenameUtils;
import org.openapplicant.domain.*;
import org.openapplicant.service.facilitator.FacilitatorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;


@Controller
public class FileUploadController extends AdminController {
	
	private FacilitatorService facilitatorService;
	
	public void setFacilitatorService(FacilitatorService value) {
		facilitatorService = value;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView processSubmit(
			@RequestParam("id") long candidateId,
			@RequestParam("type") String attachmentType,
			@RequestParam("file") MultipartFile file) throws IOException, FileTypeNotSupportedException {
		
	        if(file.isEmpty()){
	        	return new ModelAndView("redirect:candidates/detail").addObject("id", candidateId);
	        }
	     
	        if (attachmentType.equals("resume")){
	        	Resume resume = new Resume(file.getBytes(), getExtension(file));
	        	
	        	getAdminService().attachResumeToCandidate(currentUser(), candidateId, resume);
	        	
	        	// FIXME Jesse will move this monstrous if statement into attachResumeToCandidate
	        	/* Fill in empty candidate fields from extracted resume info. */
	        	Candidate extractedCandidate = facilitatorService.extractCandidate(resume.getStringContent());
	          	Candidate existingCandidate = getAdminService().findCandidateById(candidateId);
	     
	          	Company company = currentUser().getCompany();
	          	
	          	//existingCandidate.setScreeningScore(profile.screenResume(resume.getStringContent()));
	          	if ((existingCandidate.getEmail() == null) || existingCandidate.getEmail().equals("")) {
	          		existingCandidate.setEmail(extractedCandidate.getEmail());
	          	}
	          	if (existingCandidate.getCellPhoneNumber() == null || existingCandidate.getCellPhoneNumber().equals("")) {
	          		existingCandidate.setCellPhoneNumber(extractedCandidate.getCellPhoneNumber());
	          	}
	          	if (existingCandidate.getHomePhoneNumber() == null || existingCandidate.getHomePhoneNumber().equals("")) {
	          		existingCandidate.setHomePhoneNumber(extractedCandidate.getHomePhoneNumber());
	          	}
	          	if (existingCandidate.getWorkPhoneNumber() == null || existingCandidate.getWorkPhoneNumber().equals("")) {
	          		existingCandidate.setWorkPhoneNumber(extractedCandidate.getWorkPhoneNumber());
	          	}
	          	if (existingCandidate.getAddress() == null || existingCandidate.getAddress().equals("")) {
	          		existingCandidate.setAddress(extractedCandidate.getAddress());
	          	}
	          	if (existingCandidate.getName() == null || existingCandidate.getName().getFullName().equals("")) {
	          		existingCandidate.setName(extractedCandidate.getName());
	          	}
	          	
	          	
	          	getAdminService().saveCandidate(existingCandidate, currentUser());
	        }
	        else if (attachmentType.equals("coverletter")){
	        	getAdminService().attachCoverLetterToCandidate(
	        			currentUser(),
	        			candidateId, 
	        			new CoverLetter(file.getBytes(), getExtension(file))
				);
	        }
	        
	        return new ModelAndView("redirect:candidates/detail").addObject("id", candidateId);
	}
	
	private String getExtension(MultipartFile file) {
		return FilenameUtils.getExtension(file.getOriginalFilename());
	}
}
