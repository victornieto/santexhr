package org.openapplicant.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Collection;
import java.util.Map;

import org.openapplicant.domain.Category;
import org.openapplicant.util.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CategoriesController extends AdminController {
	
	private static final Pagination pagination = Pagination.oneBased().perPage(10000); // FIXME: we need real pagination!!!
	
	@RequestMapping(method=GET)
	public String index(Map<String, Object> model) {
		Collection<Category> categories = getAdminService().findMainCategoriesByCompany(currentUser().getCompany(),
				pagination.forPage(1));
		model.put("categories", categories);
	    model.put("categoriesSidebar", true);
	    model.put("questions", getAdminService().findQuestionsWithoutCategory());
		return "categories/index";
	}
}
