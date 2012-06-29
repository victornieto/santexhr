package org.openapplicant.util;


import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.dao.hibernate.CompanyDAO;
import org.openapplicant.dao.hibernate.UserDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.User;
import org.openapplicant.domain.User.Role;
import org.openapplicant.util.Pagination;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class Exporter {

	private static Log logger = LogFactory.getLog(Exporter.class);
	
	public static void main(String args[]) throws FileNotFoundException {
		
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("applicationContext-test.xml"));
		
		logger.info("Created the factory.");
		
		CompanyDAO companyDao = (CompanyDAO)factory.getBean("companyDao");
		UserDAO userDao       = (UserDAO)factory.getBean("userDao");
		
		Company company = companyDao.findOrNull(new Long(1));
		FileOutputStream output = new FileOutputStream("tmp/1.xml");
		XMLEncoder encoder = new XMLEncoder(output);
		
		encoder.writeObject(company);
		
		List<User> users = userDao.findAllActiveUsersByCompanyId(company.getId(), Pagination.zeroBased());
		
		for(User user : users) {
			encoder.writeObject(user);
		}
		
		encoder.close();
		
	}
	
}
