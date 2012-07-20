package org.openapplicant.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.IJobPositionDAO;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.JobPosition;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.List;

/**
 * User: Gian Franco Zabarino
 * Date: 11/07/12
 * Time: 11:52
 */
public class JobPositionDAO extends DomainObjectDAO<JobPosition> implements IJobPositionDAO {

    public JobPositionDAO() {
        super(JobPosition.class);
    }

    public List<JobPosition> findAllByCompany(final Company company) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createCriteria(JobPosition.class)
                        .add(Restrictions.eq("company", company))
                        .list();
            }
        });
    }
}
