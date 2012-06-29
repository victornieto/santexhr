package org.openapplicant.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openapplicant.dao.ICategoryDAO;
import org.openapplicant.domain.Category;
import org.openapplicant.domain.Company;
import org.openapplicant.util.Pagination;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryDAO extends DomainObjectDAO<Category> implements ICategoryDAO {

	public CategoryDAO() {
		super(Category.class);
	}

	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public List<Category> findMainCategoriesByCompany(final Company company,
			Pagination pagination) {
		return (List<Category>)getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session s) {
				return s.createCriteria(Category.class)
						.add(Restrictions.eq("company", company))
						.add(Restrictions.isNull("parent"))
						.list();
			}
		});
	}

	@Transactional(readOnly=true)
	public Category findCategoryById(final Long categoryId) {
		return (Category)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session s) {
				return s.get(Category.class, categoryId);
			}
		});
	}

    public void deleteAllCategoryPercentagesWith(final Category category) {
        final List<Category> categories = category.getAllSubcategories();
        categories.add(category);
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return session.createQuery("delete from CategoryPercentage cp where cp.category IN (:categories)")
                        .setParameterList("categories", categories)
                        .executeUpdate();
            }
        });
    }

}
