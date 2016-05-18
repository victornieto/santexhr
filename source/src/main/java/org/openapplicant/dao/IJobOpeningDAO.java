package org.openapplicant.dao;

import org.openapplicant.domain.Company;
import org.openapplicant.domain.JobOpening;

import java.util.List;

/**
 * User: Gian Franco Zabarino
 * Date: 16/07/12
 * Time: 14:22
 */
public interface IJobOpeningDAO extends IDomainObjectDAO<JobOpening> {

    /**
     * Returns all job openings from the given company
     * @param company
     * @return
     */
    List<JobOpening> findAllByCompany(Company company);

    /**
     * Returns active job openings from the given company
     * @param company
     * @return
     */
    List<JobOpening> findActiveByCompany(Company company);

    /**
     * Returns archived job openings from the given company
     * @param company
     * @return
     */
    List<JobOpening> findArchivedByCompany(Company company);

    /**
     * Returns job openings from the given company with the given status
     * @param company
     * @return
     */
    List<JobOpening> findByCompanyAndStatus(Company company, JobOpening.Status status);
}
