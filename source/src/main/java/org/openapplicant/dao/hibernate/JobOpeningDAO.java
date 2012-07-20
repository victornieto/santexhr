package org.openapplicant.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.IJobOpeningDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.JobOpening;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * User: Gian Franco Zabarino
 * Date: 16/07/12
 * Time: 14:21
 */
@Repository
public class JobOpeningDAO extends DomainObjectDAO<JobOpening> implements IJobOpeningDAO {

    public JobOpeningDAO() {
        super(JobOpening.class);
    }

    public List<JobOpening> findAllByCompany(final Company company) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(JobOpening.class)
                        .add(Restrictions.eq("company", company))
                        .list();
            }
        });
    }

    public List<JobOpening> findActiveByCompany(final Company company) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(JobOpening.class)
                        .add(Restrictions.eq("company", company))
                        .add(Restrictions.in("status", JobOpening.Status.getActiveStatus()))
                        .list();
            }
        });
    }

    public List<JobOpening> findArchivedByCompany(final Company company) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(JobOpening.class)
                        .add(Restrictions.eq("company", company))
                        .add(Restrictions.in("status", JobOpening.Status.getArchivedStatus()))
                        .list();
            }
        });
    }

    public List<JobOpening> findByCompanyAndStatus(final Company company, final JobOpening.Status status) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(JobOpening.class)
                        .add(Restrictions.eq("company", company))
                        .add(Restrictions.eq("status", status))
                        .list();
            }
        });
    }
}
