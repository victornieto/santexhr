package org.openapplicant.dao;

import org.openapplicant.domain.Category;
import org.openapplicant.domain.Company;
import org.openapplicant.util.Pagination;

import java.util.List;

public interface ICategoryDAO extends IDomainObjectDAO<Category>{

	List<Category> findMainCategoriesByCompany(Company company,
			Pagination pagination);

	Category findCategoryById(Long categoryId);

    void deleteAllCategoryPercentagesWith(Category category);
}
