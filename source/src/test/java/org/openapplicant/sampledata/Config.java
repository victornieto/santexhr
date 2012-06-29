package org.openapplicant.sampledata;

import java.util.Properties;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.NamingStrategy;

/**
 * Test configuration holder
 */
class Config {
	
	private static final String DATABASE_PROPERTIES = "/database.properties";
	
	private static final String PROP_NAMING_STRATEGY = "hibernate.namingStrategyClassName";
	
	private static final String HIBERNATE_MAPPINGS = "/hibernate.cfg.xml";
	
	private Configuration configuration;
	
	public Config() {
		try {
			Properties props = new Properties();
			props.load(getClass().getResourceAsStream(DATABASE_PROPERTIES));
			configuration = new AnnotationConfiguration()
									.setNamingStrategy((NamingStrategy)Class.forName(props.getProperty(PROP_NAMING_STRATEGY)).newInstance())
									.addProperties(props)
									.configure(HIBERNATE_MAPPINGS);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
}
