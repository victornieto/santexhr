package org.openapplicant.util;


import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
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
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class Importer {

	private static Log logger = LogFactory.getLog(Importer.class);
	
	public static void main(String args[]) throws FileNotFoundException {
		
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("applicationContext-test.xml"));
		
		FileInputStream input = new FileInputStream("tmp/1.xml");
		
		XMLDecoder decoder = new XMLDecoder(input);
		
		Company company = (Company) decoder.readObject();
		logger.info("Retrieved company "+company.getName()+" from the file.");
	}
		
}
