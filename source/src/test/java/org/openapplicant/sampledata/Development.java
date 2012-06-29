package org.openapplicant.sampledata;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CompanyBuilder;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.NameBuilder;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.domain.question.CodeQuestionBuilder;
import org.openapplicant.domain.question.EssayQuestionBuilder;
import org.openapplicant.domain.question.MultipleChoiceQuestionBuilder;


/**
 * Adds sample data to the development database.
 */
public class Development {
	
	private static Configuration configuration;
	
	public static void main(String... args) throws Exception{
		buildConfiguration();
		exportSchema();
		addSampleData();
	}
	
	private static void buildConfiguration() throws Exception {
		configuration = new Config().getConfiguration();
	}
	
	private static void exportSchema() {
		new SchemaExport(configuration)
				.execute(true, true, false, false);
	}
	
	private static void addSampleData() throws Exception {
		Session session = configuration.buildSessionFactory().openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			Company company = new CompanyBuilder()
									.withHostName("localhost")
									.withHostPort(8080)
									.withProxyName("localhost")
									.withProxyPort(8080)
									.withName("Originate Dev")
									.withUsers(
											new UserBuilder()
													.withEmail("tommy@originatelabs.com")
													.withPassword("tommy")
													.withName(
															new NameBuilder("Tommy", "Sullivan")
																	.build()
													)
													.build(),
											new UserBuilder()
													.withEmail("mrw@originatelabs.com")
													.withPassword("matt")
													.withName(
															new NameBuilder("Matt", "Williams")
																	.build()
													)
													.build(),
											new UserBuilder()
													.withEmail("jesse@originatelabs.com")
													.withPassword("jesse")
													.withName(
															new NameBuilder("Jesse", "Berman")
																	.build()
													)
													.build(),
											new UserBuilder()
													.withEmail("rick@originatelabs.com")
													.withPassword("rick")
													.withName(
															new NameBuilder("Rick", "Cook")
																	.build()
													)
													.build(),
											new UserBuilder()
													.withEmail("tom@originatelabs.com")
													.withPassword("tom")
													.withName(
															new NameBuilder("Tom","Brow")
																	.build()
													)
													.build()
									)
									.build();
			session.save(company);

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
			
			String youtube = "<p>Explain the use of generics in the second puzzle, \"More Joy of Sets\", in this video:</p>" +
					"<object width=\"320\" height=\"265\">" +
					"	<param name=\"movie\" value=\"http://www.youtube.com/v/wDN_EYUvUq0&hl=en&fs=1&rel=0\"></param>"+
					"	<param name=\"allowFullScreen\" value=\"true\"></param>" +
					"	<param name=\"allowscriptaccess\" value=\"always\"></param>" +
					"	<embed src=\"http://www.youtube.com/v/wDN_EYUvUq0&hl=en&fs=1&rel=0\" type=\"application/x-shockwave-flash\" allowscriptaccess=\"always\" allowfullscreen=\"true\" width=\"320\" height=\"265\"></embed>" +
					"</object>";
			
			List<String> choices = Arrays.asList("O(logN)","O(N)","O(NlogN)","O(N^2)");
			
			Exam exam1 = new ExamBuilder()
				.withName("Every Type of Question")
				.withGenre("java")
				.withDescription("java test")
				.withCompany(company)
	        	.withQuestions(
	        		new EssayQuestionBuilder()
	                    .withTimeAllowed(15)
	                    .withName("Fibonacci - Read")
	                    .withPrompt(longQuestion)
	                    .build(),
	                new CodeQuestionBuilder()
						.withTimeAllowed(30)
						.withName("Byte to String")
						.withPrompt("Write a Java or C/C++ function that takes in a 1-byte character and returns it as a string.")
						.build(),
	        		new MultipleChoiceQuestionBuilder()
						.withChoices(choices)
						.withTimeAllowed(null)
						.withName("O of BinarySearch")
						.withPrompt("What is the worst-case time complexity of sorting a given set of N numbers by building a binary tree search?")
						.build()
            )
			.build();
			session.save(exam1);
			
			Exam exam2 = new ExamBuilder()
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
			session.save(exam2);
			
			List<NameBuilder> nameBuilders = Arrays.asList(
					new NameBuilder("Hank","Hill"),
					new NameBuilder("Phillip","Fry"),
					new NameBuilder("Eric", "Cartman"),
					new NameBuilder("Homer", "Simpson"),
					new NameBuilder("Peter","Griffin"),
					new NameBuilder("Jeff","Lebowski"),
					new NameBuilder("Adam","West"),
					new NameBuilder("John","Matrix"),
					new NameBuilder("Jay","Z"),
					new NameBuilder("Bruce","Wayne"),
					new NameBuilder("Notorious","BIG"),
					new NameBuilder("Nazir","Jones"),
					new NameBuilder("Anthony","Cruz"),
					new NameBuilder("Beastie","Boys"),
					new NameBuilder("Bob","Marley"),
					new NameBuilder("David","Bowie"),
					new NameBuilder("Dr.","Dre"),
					new NameBuilder("Fat","Joe"),
					new NameBuilder("The","Game"),
					new NameBuilder("Johnny","Cash"),
					new NameBuilder("Big","Pun"),
					new NameBuilder("Ghostface","Killah"),
					new NameBuilder("Lupe","Fiasco"),
					new NameBuilder("Snoop","Dogg"),
					new NameBuilder("Jam Master", "Jay"),
					new NameBuilder("MF","Doom"),
					new NameBuilder("Victor","Vaughn"),
					new NameBuilder("Tupac","Shakur"),
					new NameBuilder("YT","Cracker")
			);
			for(NameBuilder each : nameBuilders) {
				Name name = each.build();
				session.save(
						new CandidateBuilder()
								.withEmail(name.getLast().toLowerCase()+"@gmail.com")
								.withName(name)
								.withStatus(Candidate.Status.NOT_TESTED)
								.withCompany(company)
								.build()
								
				);
			}
			
			tx.commit();
		} catch(Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

}
