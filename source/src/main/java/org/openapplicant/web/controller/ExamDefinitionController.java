package org.openapplicant.web.controller;

import org.openapplicant.domain.Category;
import org.openapplicant.domain.CategoryPercentage;
import org.openapplicant.domain.ExamDefinition;
import org.openapplicant.util.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.PropertyEditorSupport;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ExamDefinitionController extends AdminController {
	
	@ModelAttribute("examDefinitions")
	public Collection<ExamDefinition> populateExamDefinitions() {
		return getAdminService().findAllExamDefinitionsByCompany(currentUser().getCompany());
	}
	
	@RequestMapping(method=GET)
	public String view(
			@RequestParam("ed") String examDefinitionArtifactId, 
			Map<String,Object> model) {
		
		ExamDefinition examDefinition = getAdminService().findExamDefinitionByArtifactId(examDefinitionArtifactId);
		model.put("examDefinition", examDefinition);
        model.put("categories", getAdminService().findAllCategoriesByCompany(
                currentUser().getCompany(),
                Pagination.oneBased()));
		return "examDefinition/view";
	}
	
	@RequestMapping(method=POST)
	public String update(
			@ModelAttribute("examDefinition") ExamDefinition cmd, 
			BindingResult binding,
			Map<String, Object> model) {

        boolean examDefinitionErrors = false;

		Errors errors = cmd.validate();
		if(errors.hasErrors()) {
			binding.addAllErrors(errors);
            examDefinitionErrors = true;
		}

        // Categories Percentage Error map, doesn't matter if it gets empty.
        // Used to show errors in the view on categories percentage rows.
        Map<String, Map<String, String>> categoriesPercentageErrorMap = new HashMap<String, Map<String, String>>();
        model.put("categoriesPercentageError", categoriesPercentageErrorMap);

        // Default category error.
        final String categoryError = "*";  // Just a red asterisk is enough
        final HashMap<String, String> categoryErrorMapValue = new HashMap<String, String>();
        categoryErrorMapValue.put("category", categoryError);

        CategoryPercentage firstOffendingCategoryPercentage = null;
        CategoryPercentage secondOffendingCategoryPercentage = null;

        boolean duplicatedCategoryError = false;

        // Duplicated category validation
        for (int i = 0; i < cmd.getCategoriesPercentage().size() && !duplicatedCategoryError; i++) {
            CategoryPercentage currentCategoryPercentage = cmd.getCategoriesPercentage().get(i);
            for (int j = i + 1; j < cmd.getCategoriesPercentage().size() && !duplicatedCategoryError; j++) {
                CategoryPercentage categoryPercentage = cmd.getCategoriesPercentage().get(j);
                if (currentCategoryPercentage.getCategory().equals(categoryPercentage.getCategory())) {
                    firstOffendingCategoryPercentage = currentCategoryPercentage;
                    secondOffendingCategoryPercentage = categoryPercentage;
                    duplicatedCategoryError = true;
                    break;
                }
            }
        }

        // Hierarchy validation
        boolean hierarchyCategoryError = false;

        for (int i = 0; i < cmd.getCategoriesPercentage().size() && !hierarchyCategoryError; i++) {
            final CategoryPercentage currentCategoryPercentage = cmd.getCategoriesPercentage().get(i);
            Category category = currentCategoryPercentage.getCategory();
            List<Category> hierarchyCategories = getHierarchyCategoryForCategory(category);
            for (int j = i + 1; j < cmd.getCategoriesPercentage().size() && !hierarchyCategoryError; j++) {
                CategoryPercentage categoryPercentage = cmd.getCategoriesPercentage().get(j);
                for (Category currentCategory : hierarchyCategories) {
                    if (currentCategory.equals(categoryPercentage.getCategory())) {
                        firstOffendingCategoryPercentage = currentCategoryPercentage;
                        secondOffendingCategoryPercentage = categoryPercentage;
                        hierarchyCategoryError = true;
                        break;
                    }
                }
            }
        }

        errors = null;
        int i = 0;
        for (CategoryPercentage categoryPercentage : cmd.getCategoriesPercentage()) {
            if (errors == null) {
                errors = populateErrorsCategoryPercentage(model, categoryPercentage, i);
            } else {
                errors.addAllErrors(populateErrorsCategoryPercentage(model, categoryPercentage, i));
            }
            i++;
        }

        if (hierarchyCategoryError) {
            model.put("categoryError", "Parent categories or subcategories found");
            categoriesPercentageErrorMap.put(firstOffendingCategoryPercentage.getArtifactId(),
                    categoryErrorMapValue);
            categoriesPercentageErrorMap.put(secondOffendingCategoryPercentage.getArtifactId(),
                    categoryErrorMapValue);
        } else if (duplicatedCategoryError) {
            model.put("categoryError", "Duplicated categories found");
            categoriesPercentageErrorMap.put(firstOffendingCategoryPercentage.getArtifactId(),
                    categoryErrorMapValue);
            categoriesPercentageErrorMap.put(secondOffendingCategoryPercentage.getArtifactId(),
                    categoryErrorMapValue);
        }

        if(examDefinitionErrors || duplicatedCategoryError || hierarchyCategoryError || errors.hasErrors()) {
            model.put("categories", getAdminService().findAllCategoriesByCompany(currentUser().getCompany(), Pagination.oneBased().forPage(1)));
            return "examDefinition/view";
        }

		getAdminService().updateExamDefinitionInfo(
				cmd.getArtifactId(), 
				cmd.getName(), 
				cmd.getGenre(), 
				cmd.getDescription(),
				cmd.getNumberOfQuestionsWanted(),
				cmd.isActive(),
                cmd.getCategoriesPercentage()
		);
		return "redirect:view?ed="+cmd.getArtifactId();
	}
	
	@RequestMapping(method=GET)
	public String add(Map<String, Object> model) {
		model.put("examDefinition", new ExamDefinition());
		return "examDefinition/add";
	}
	
	@RequestMapping(method=POST)
	public String doAdd(
			@ModelAttribute("examDefinition") ExamDefinition examDefinition,
			BindingResult binding) {
		
		Errors errors = examDefinition.validate();
		if(errors.hasErrors()) {
			binding.addAllErrors(errors);
			return "examDefinition/add";
		} else {
			examDefinition.setCompany(currentUser().getCompany());
			getAdminService().saveExamDefinition(examDefinition);
			return "redirect:view?ed="+examDefinition.getArtifactId();
		}
	}
	
	@RequestMapping(method = POST)
	public String addCategoryPercentage(
            @ModelAttribute("examDefinition") ExamDefinition cmd,
			Map<String, Object> model) {
        cmd.addCategoryPercentage(new CategoryPercentage());
		model.put("examDefinition", cmd);
        model.put("categories", getAdminService().findAllCategoriesByCompany(
                currentUser().getCompany(),
                Pagination.oneBased()));
		return "examDefinition/view";
	}

    @RequestMapping(method = POST)
    public String deleteCategoryPercentage(
            @ModelAttribute("examDefinition") ExamDefinition cmd,
            @RequestParam("cp") String categoryPercentageArtifactId,
            Map<String, Object> model) {
        cmd.removeCategoryPercentage(categoryPercentageArtifactId);
        model.put("examDefinition", cmd);
        model.put("categories", getAdminService().findAllCategoriesByCompany(
                currentUser().getCompany(),
                Pagination.oneBased()));
        return "examDefinition/view";
    }
	
	private Errors populateErrorsCategoryPercentage(Map<String, Object> model, CategoryPercentage categoryPercentage, Integer index) {
		Errors errors = categoryPercentage.validate();
        HashMap<String, HashMap<String, String>> map = (HashMap<String, HashMap<String, String>>) model.get("categoriesPercentageError");
        HashMap<String, String> value;
        if(errors.hasFieldErrors("category")) {
            value = new HashMap<String, String>();
            value.put("category", errors.getFieldError("category").getDefaultMessage());
            map.put(categoryPercentage.getArtifactId(), value);
		}
		if(errors.hasFieldErrors("percentage")) {
            value = new HashMap<String, String>();
            value.put("percentage", errors.getFieldError("percentage").getDefaultMessage());
            map.put(categoryPercentage.getArtifactId(), value);
		}
		return errors;
	}
	
	private List<Category> getHierarchyCategoryForCategory(Category category) {
		List<Category> categories = new ArrayList<Category>();
		Category parent = category.getParent();
		while (parent != null) {
			categories.add(parent);
			parent = parent.getParent();
		}
        categories.addAll(category.getAllSubcategories());
		return categories;
	}

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Category.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                if (getValue() != null) {
                    return ((Category)getValue()).getId().toString();
                }
                return null;
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(getAdminService().findCategoryById(Long.parseLong(text)));
            }
        });
    }
}
