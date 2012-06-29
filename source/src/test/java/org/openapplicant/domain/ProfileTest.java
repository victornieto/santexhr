package org.openapplicant.domain;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.zip.DataFormatException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.Resume;
import org.openapplicant.domain.Profile.Priority;
import org.springframework.core.io.ClassPathResource;


public class ProfileTest {
	
	private static Log logger = LogFactory.getLog(ProfileTest.class);
	
	@Test
	public void setDailyReportRecipient_empty() {
		Profile profile = new ProfileBuilder()
								.withDailyReportRecipient("")
								.build();
		
		assertFalse(profile.validate().hasErrors());
	}
	
	@Test
	public void screenResumeOne() throws Exception {
		Profile profile = new ProfileBuilder()
							.withNoKeywords()
							.build();
		profile.addKeyword("Java", Priority.REQUIRED);
		profile.addKeyword("HTML", Profile.Priority.PREFERRED);
		profile.addKeyword("CSS", Profile.Priority.OPTIONAL);
		profile.addKeyword("Cheese", Profile.Priority.OPTIONAL);
		profile.addKeyword("J2EE",Profile.Priority.OPTIONAL);
		
		File file = new ClassPathResource("rob_resume_2008.pdf").getFile();
		FileInputStream stream = new FileInputStream(file);
		byte[] content = new byte[(int) file.length()];
		stream.read(content);
		Resume resume = new Resume(content,"pdf");
		logger.debug("Resume read.");
		
		BigDecimal score = profile.screenResume(resume.getStringContent());
		// score 1 
		assertEquals(93,score.intValue());
	}

	@Test
	public void screenResumeTwo() throws Exception {
		Profile profile = new ProfileBuilder()
							.withNoKeywords()
							.build();
		profile.addKeyword("Java", Priority.REQUIRED);
		profile.addKeyword("HTML", Profile.Priority.REQUIRED);
		profile.addKeyword("Arizona", Profile.Priority.REQUIRED);
		profile.addKeyword("Cheese", Profile.Priority.REQUIRED);
		
		File file = new ClassPathResource("rob_resume_2008.pdf").getFile();
		FileInputStream stream = new FileInputStream(file);
		byte[] content = new byte[(int) file.length()];
		stream.read(content);
		Resume resume = new Resume(content,"pdf");
		logger.debug("Resume read.");
		
		BigDecimal score = profile.screenResume(resume.getStringContent());
		// score:  3/4 required = 90
		assertEquals(90,score.intValue());
	}
	
	
	@Test
	public void screenResumeThree() throws Exception {
		Profile profile = new ProfileBuilder()
							.withNoKeywords()
							.build();
		profile.addKeyword("Java", Priority.REQUIRED);
		profile.addKeyword("HTML", Profile.Priority.REQUIRED);
		profile.addKeyword("CSS", Profile.Priority.REQUIRED);
		profile.addKeyword("Cheese", Profile.Priority.REQUIRED);
		profile.addKeyword("J2EE",Profile.Priority.REQUIRED);
		
		File file = new ClassPathResource("rob_resume_2008.pdf").getFile();
		FileInputStream stream = new FileInputStream(file);
		byte[] content = new byte[(int) file.length()];
		stream.read(content);
		Resume resume = new Resume(content,"pdf");
		logger.debug("Resume read.");
		
		BigDecimal score = profile.screenResume(resume.getStringContent());
		// score:  3/5 required = 78
		assertEquals(78,score.intValue());
	}
	
	@Test
	public void screenResumeFour() throws Exception {
		Profile profile = new ProfileBuilder()
							.withNoKeywords()
							.build();
		profile.addKeyword("Java", Priority.REQUIRED);
		profile.addKeyword("HTML", Profile.Priority.REQUIRED);
		profile.addKeyword("CSS", Profile.Priority.REQUIRED);
		profile.addKeyword("Cheese", Profile.Priority.REQUIRED);

		profile.addKeyword("J2EE",Profile.Priority.PREFERRED);
		profile.addKeyword("Pirhana", Profile.Priority.PREFERRED);
		
		profile.addKeyword("UML",Profile.Priority.OPTIONAL);
		profile.addKeyword("Wookie",Profile.Priority.OPTIONAL);
		
		File file = new ClassPathResource("rob_resume_2008.pdf").getFile();
		FileInputStream stream = new FileInputStream(file);
		byte[] content = new byte[(int) file.length()];
		stream.read(content);
		Resume resume = new Resume(content,"pdf");
		logger.debug("Resume read.");
		
		BigDecimal score = profile.screenResume(resume.getStringContent());
		// score:  2/4 required, 1/2 preferred, 1/2 optional = 55
		assertEquals(55,score.intValue());
	}
	
}
