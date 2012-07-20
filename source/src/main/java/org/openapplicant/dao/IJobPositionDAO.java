package org.openapplicant.dao;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.JobPosition;

import java.util.List;

/**
 * User: Gian Franco Zabarino
 * Date: 11/07/12
 * Time: 11:47
 */
public interface IJobPositionDAO extends IDomainObjectDAO<JobPosition> {
    List<JobPosition> findAllByCompany(Company company);
}
