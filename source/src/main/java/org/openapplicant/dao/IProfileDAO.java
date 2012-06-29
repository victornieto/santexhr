package org.openapplicant.dao;

import java.util.List;
import java.util.Map;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.Profile;


public interface IProfileDAO extends IDomainObjectDAO<Profile> {

	List<Company> findNightlyReportCompanies();

}
