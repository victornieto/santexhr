package org.openapplicant.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.dao.hibernate.CompanyDAO;
import org.openapplicant.dao.hibernate.ExamDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.question.CodeQuestionBuilder;
import org.openapplicant.domain.question.EssayQuestionBuilder;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class CreateExam {

	private static Log logger = LogFactory.getLog(CreateExam.class);
	
	public static void main(String args[]) {
		
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("applicationContext-test.xml"));
		
		logger.info("Created the factory.");
		
		CompanyDAO companyDao = (CompanyDAO)factory.getBean("companyDao");
		ExamDAO examDao = (ExamDAO)factory.getBean("examDao");
		
		Company company = companyDao.find(new Long(907));
		
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
