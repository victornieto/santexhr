package org.openapplicant.domain;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.hibernate.validator.InvalidValue;
import org.junit.Test;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.PasswordRecoveryToken;
import org.openapplicant.domain.link.CandidateExamLinkBuilder;
import org.openapplicant.domain.link.ExamLink;
import org.openapplicant.validation.ClassValidator;
import org.openapplicant.validation.HostName;
import org.springframework.util.ReflectionUtils;


public class CompanyTest {
	
	@Test
	public void setHostName() {
		Company company = new CompanyBuilder().build();
		
		company.setHostName("  www.openapplicant.com ");
		assertEquals("www.openapplicant.com", company.getHostName());
		
		company.setHostName("www.openapplicant.com/");
		assertEquals("www.openapplicant.com", company.getHostName());
	}
	
	@Test
	public void setHostName_invalid() {
		Company company = new CompanyBuilder()
								.withHostName("www.openapplicant.com:8080")
								.build();
		
		InvalidValue[] errors = new ClassValidator<Company>(Company.class)
										.getPotentialInvalidValues("hostName", company.getHostName());
		assertEquals(1, errors.length);
		
		Method method = ReflectionUtils.findMethod(Company.class, "getHostName");
		assertEquals(method.getAnnotation(HostName.class).message(), errors[0].getMessage());
	}
	
	@Test
	public void setProxyName() {
		Company company = new CompanyBuilder().build();
		
		company.setProxyName("   www.proxy.openapplicant.com  ");
		assertEquals("www.proxy.openapplicant.com", company.getProxyName());
		
		company.setProxyName("www.proxy.openapplicant.com/");
		assertEquals("www.proxy.openapplicant.com", company.getProxyName());
	}
	
	@Test
	public void setProxyName_invalid() {
		Company company = new CompanyBuilder()
								.withProxyName("www.openapplicant.com:8080")
								.build();
		
		InvalidValue[] errors  = new ClassValidator<Company>(Company.class)
											.getPotentialInvalidValues("proxyName", company.getProxyName());
		
		assertEquals(1, errors.length);
		
		Method method = ReflectionUtils.findMethod(Company.class, "getProxyName");
		assertEquals(method.getAnnotation(HostName.class).message(), errors[0].getMessage());
	}
	
	@Test
	public void urlFor_ExamLinkPort80() {
		Company company = new CompanyBuilder()
								.withHostName("www.openapplicant.com")
								.withHostPort(80)
								.withContextRoot("openapplicant")
								.build();
		
		ExamLink examLink = new CandidateExamLinkBuilder()
									.withCompany(company)
									.build();
		
		assertEquals(
				"http://www.openapplicant.com/openapplicant/quiz/index?exam="+examLink.getGuid(),
				company.urlFor(examLink).toString()
		);
	}
	
	@Test
	public void urlFor_ExamLinkPort8080() {
		Company company = new CompanyBuilder()
									.withHostName("www.openapplicant.com")
									.withContextRoot("openapplicant")
									.withHostPort(8080)
									.build();
		
		ExamLink examLink = new CandidateExamLinkBuilder()
									.withCompany(company)
									.build();
		
		assertEquals(
				"http://www.openapplicant.com:8080/openapplicant/quiz/index?exam="+examLink.getGuid(),
				company.urlFor(examLink).toString()
		);
	}
	
	@Test
	public void urlFor_PasswordRecoveryToken() {
		Company company = new CompanyBuilder()
								.withHostName("localhost")
								.withHostPort(80)
								.withContextRoot("openapplicant")
								.build();
		
		PasswordRecoveryToken token = new PasswordRecoveryTokenBuilder().build();
		
		assertEquals(
				"http://localhost/openapplicant/admin/forgotPassword/confirm?id=" + token.getGuid(),
				company.urlFor(token).toString()
		);
	}
}
