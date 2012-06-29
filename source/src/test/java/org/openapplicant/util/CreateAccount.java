package org.openapplicant.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.dao.hibernate.CompanyDAO;
import org.openapplicant.dao.hibernate.ExamDAO;
import org.openapplicant.dao.hibernate.UserDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.User;
import org.openapplicant.domain.User.Role;
import org.openapplicant.domain.question.CodeQuestionBuilder;
import org.openapplicant.domain.question.EssayQuestionBuilder;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class CreateAccount {

	private static Log logger = LogFactory.getLog(CreateAccount.class);
	
	public static void main(String args[]) throws IOException {
		
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("applicationContext-test.xml"));
		
		logger.info("Created the factory.");
		
		CompanyDAO companyDao = (CompanyDAO)factory.getBean("companyDao");
		UserDAO userDao = (UserDAO)factory.getBean("userDao");
		ExamDAO examDao = (ExamDAO)factory.getBean("examDao");

		
		logger.info("Have the company DAO.");
		
		File infile = new File(args[0]);
		BufferedReader reader = new BufferedReader(new FileReader(infile));
		
		String companyName = reader.readLine();
		String hostName    = reader.readLine();
		
		Company company = new Company();
		company.setEmailAlias(hostName);
		company.setHostName(hostName + ".openapplicant.org");
		company.setHostPort(80);
		company.setName(companyName);
		company.setProxyName(hostName + ".localhost");
		company.setProxyPort(8080);
		
		String userLine;
		while( (userLine = reader.readLine()) != null) {
			String bits[] = userLine.split(" ");
			User user = new User();
			user.setName(new Name(bits[0] + " " + bits[1]));
			user.setEmail(bits[2]);
			user.setEnabled(true);
			user.setPassword(bits[0].toLowerCase());
			user.setRole(Role.ROLE_ADMIN);
			company.addUser(user);
			userDao.save(user);	
		}
		

		companyDao.save(company);
		
		
		String longQuestion = "What does the following C code do?:\n"
			+"\n"
			+"<pre>int w = 10, x = 0, z = 0;\n"
			+"int y = x++;\n"
			+"\n"
			+"for ( ; w >= 0; w-- )\n"
			+"{\n"
			+"z = x + y;\n"
			+"printf( \"%i, \", y = x );\n"
			+"x = z;\n"
			+"}</pre>\n";
		
		Exam exam = new ExamBuilder()
    	.withName("Programming")
    	.withGenre("Programming")
    	.withDescription("Basic Programming Test")
    	.withCompany(company)
    	.withQuestions(
    			new CodeQuestionBuilder()
                            .withPrompt("Write a Java or C/C++ function that takes in a 1-byte character and returns it as a string.")
                            .withTimeAllowed(null)
                            .withName("Byte to String")
                            .build(),
	            new CodeQuestionBuilder()
	                            .withPrompt("In Java or C/C++, swap the value of two integer variables.")
	                            .withTimeAllowed(null)
	                            .withName("Integer Swap")
	                            .build(),
	            new CodeQuestionBuilder()
	                            .withPrompt("Write a Java or C/C++ function that takes a string parameter and returns a string with all non-letters removed.")
	                            .withTimeAllowed(null)
	                            .withName("Remove Non-Letters")
	                            .build(),
	            new CodeQuestionBuilder()
	                            .withPrompt("In Java or C/C++, implement a stack for integers.")
	                            .withTimeAllowed(180)
	                            .withName("3-Minute Stack")
	                            .build(),
	            new EssayQuestionBuilder()
	                            .withPrompt(longQuestion)
	                            .withTimeAllowed(null)
	                            .withName("Fibonacci - Read")
	                            .build(),
	            new CodeQuestionBuilder()
	                            .withPrompt("In Java or C/C++, write a function to print out the first n integers of the Fibonacci sequence.")
	                            .withTimeAllowed(null)
	                            .withName("Fibonacci - Write")
	                            .build(),                                    
	            new CodeQuestionBuilder()
	                            .withPrompt("Write 60 seconds of interesting code.")
	                            .withTimeAllowed(60)
	                            .withName("Interesting Code")
	                            .build()                    
	    	)
	    .build();
		
		
		examDao.save(exam);
		
	}
	
}
