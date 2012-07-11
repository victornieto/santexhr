package org.openapplicant.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.domain.Category;
import org.openapplicant.domain.question.*;
import org.openapplicant.util.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class CategoryController extends AdminController {
	
	Pagination pagination = Pagination.oneBased().perPage(10000); // FIXME see how can we get this work
	private static final Log logger = LogFactory.getLog(CategoryController.class);
	
	@ModelAttribute("categories")
	public Collection<Category> populateCategories() {
		return getAdminService().findMainCategoriesByCompany(currentUser().getCompany(), pagination.forPage(1));
	}
	
	@RequestMapping(method=GET)
	public String view(
			@RequestParam("c") Long id, 
			Map<String,Object> model) {
		
		Category category = getAdminService().findCategoryById(id);
		model.put("category", category);
		return "category/view";
	}
	
	@RequestMapping(method=POST)
	public String update(
			@ModelAttribute("category") Category cmd, 
			BindingResult binding,
			Map<String, Object> model) {
		
		Errors errors = cmd.validate();
		if(errors.hasErrors()) {
			binding.addAllErrors(errors);
			return "category/view";
		}
		getAdminService().updateCategoryInfo(
				cmd.getId(), 
				cmd.getName()
		);
		return "redirect:view?c="+cmd.getId();
	}

	@RequestMapping(method=GET)
	public String add(
			@RequestParam(value="c", required=false) Long parentCategoryId,
			Map<String, Object> model) {
		if (parentCategoryId != null) {
			model.put("parentCategoryId", parentCategoryId);
		}
		model.put("category", new Category());
		return "category/add";
	}
	
	@RequestMapping(method=POST)
	public String doAdd(
			@ModelAttribute("category") Category category,
			BindingResult binding,
			@RequestParam(value="c", required=false) Long parentCategoryId) {
		
		Errors errors = category.validate();
		if(errors.hasErrors()) {
			binding.addAllErrors(errors);
			return "category/add";
		} else {
			category.setCompany(currentUser().getCompany());
			if (parentCategoryId != null) {
				Category parentCategory = getAdminService().findCategoryById(parentCategoryId);
				parentCategory.addChild(category);
				getAdminService().saveCategory(parentCategory);
			} else {
				getAdminService().saveCategory(category);
			}
			return "redirect:view?c="+category.getId();
		}
	}

    @RequestMapping(method=GET)
    public String addEssayQuestion(
            @RequestParam(required=false, value="c") Long categoryId,
            Map<String, Object> model) {
        final Question question = new EssayQuestion();
        addNewQuestionToModel(question, categoryId, model);
        return new AdminQuestionViewVisitor(question).getView();
    }

	@RequestMapping(method=GET)
	public String addCodeQuestion(
			@RequestParam(required=false, value="c") Long categoryId,
            Map<String, Object> model) {
        final Question question = new CodeQuestion();
        addNewQuestionToModel(question, categoryId, model);
        return new AdminQuestionViewVisitor(question).getView();
	}
	
	@RequestMapping(method=GET)
	public String addMultipleChoiceQuestion(
			@RequestParam(required=false, value="c") Long categoryId,
            Map<String, Object> model) {
        final Question question = new MultipleChoiceQuestion();
        addNewQuestionToModel(question, categoryId, model);
        return new AdminQuestionViewVisitor(question).getView();
	}

    private void addNewQuestionToModel(Question question, Long categoryId, Map<String, Object> model) {
        if (categoryId != null && categoryId > 0) {
            final Category category = getAdminService().findCategoryById(categoryId);
            question.setCategory(category);
            model.put("category", category);
        }
        model.put("question", question);
        model.put("isNew", true);
    }

	@RequestMapping(method=GET)
	public String question(
			@RequestParam(required=false, value="c") Long categoryId, 
			@RequestParam("q") String questionArtifactId,
            @RequestParam(required = false, value = "success") Boolean success,
			Map<String, Object> model) {
		Question question = null;
		if (categoryId != null) {
			Category category = getAdminService().findCategoryById(categoryId);
			model.put("category", category);
			question = category.getQuestionByArtifactId(questionArtifactId);
		} else {
			question = getAdminService().findQuestionByArtifactId(questionArtifactId);
		}
		if (success != null && success) {
            model.put("success", success);
        }
		model.put("question", question);
				
		return new AdminQuestionViewVisitor(question).getView();
	}

	/**
	 * This method will be less empty when its strongly bound
	 * to the view and it shows errors
	 * 
	 * @param categoryId
	 * @param questionArtifactId
	 * @param timeAllowed
	 * @param name
	 * @param prompt
	 * @param answer
	 * @param model
	 * @return redirects to question
	 */
	@RequestMapping(method=POST)
	public String updateEssayQuestion(
			@RequestParam(required=false, value="c") Long categoryId,
			@RequestParam("q") String questionArtifactId,
			@RequestParam("timeAllowed") Integer timeAllowed,
			@RequestParam("name") String name,
			@RequestParam("prompt") String prompt,
			@RequestParam("answer") String answer,
            @RequestParam(required = false, value = "n") Boolean isNew,
            @RequestParam(required = false, value = "save") String save,
			Map<String, Object> model) {

        if (save == null) {
            if (categoryId == null) {
                return "redirect:/admin/categories/index";
            } else {
                return "redirect:view?c=" + categoryId;
            }
        }

		EssayQuestion essayQuestion = new EssayQuestion();
		essayQuestion.setArtifactId(questionArtifactId);
		essayQuestion.setTimeAllowed(timeAllowed);
		essayQuestion.setName(name);
		essayQuestion.setPrompt(prompt);
		essayQuestion.setAnswer(answer);

        if (categoryId != null) {
            essayQuestion.setCategory(getAdminService().findCategoryById(categoryId));
        }
		
		return updateQuestion(essayQuestion, categoryId, isNew, model);
	}
	
	/**
	 * This method will be less empty when its strongly bound
	 * to the view and it shows errors
	 * 
	 * @param categoryId
	 * @param questionArtifactId
	 * @param timeAllowed
	 * @param name
	 * @param prompt
	 * @param answer
	 * @param model
	 * @return redirects to question
	 */
	@RequestMapping(method=POST)
	public String updateCodeQuestion(
			@RequestParam(required=false, value="c") Long categoryId,
			@RequestParam("q") String questionArtifactId,
			@RequestParam("timeAllowed") Integer timeAllowed,
			@RequestParam("name") String name,
			@RequestParam("prompt") String prompt,
			@RequestParam("answer") String answer,
            @RequestParam(required = false, value = "n") Boolean isNew,
            @RequestParam(required = false, value = "save") String save,
			Map<String, Object> model) {

        if (save == null) {
            if (categoryId == null) {
                return "redirect:/admin/categories/index";
            } else {
                return "redirect:view?c=" + categoryId;
            }
        }

		CodeQuestion codeQuestion = new CodeQuestion();
		codeQuestion.setArtifactId(questionArtifactId);
		codeQuestion.setTimeAllowed(timeAllowed);
		codeQuestion.setName(name);
		codeQuestion.setPrompt(prompt);
		codeQuestion.setAnswer(answer);

        if (categoryId != null) {
            codeQuestion.setCategory(getAdminService().findCategoryById(categoryId));
        }

        return updateQuestion(codeQuestion, categoryId, isNew, model);
	}
	
	/**
	 * This method will be less empty when its tightly
	 * bound to the view and shows errors
	 * 
	 * @param categoryId
	 * @param questionArtifactId
	 * @param timeAllowed
	 * @param name
	 * @param prompt
	 * @param answerIndex
	 * @param choices
	 * @param model
	 * @return redirects to question
	 */
	@RequestMapping(method=POST)
	public String updateMultipleChoiceQuestion(
			@RequestParam(required=false, value="c") Long categoryId,
			@RequestParam("q") String questionArtifactId,
			@RequestParam("timeAllowed") Integer timeAllowed,
			@RequestParam("name") String name,
			@RequestParam("prompt") String prompt,
			@RequestParam(value="answerIndex", required=false) Integer answerIndex,
			@RequestParam("choices") List<String> choices,
            @RequestParam(required = false, value = "n") Boolean isNew,
            @RequestParam(required = false, value = "save") String save,
			Map<String, Object> model) {

        if (save == null) {
            if (categoryId == null) {
                return "redirect:/admin/categories/index";
            } else {
                return "redirect:view?c=" + categoryId;
            }
        }
		
		MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
		multipleChoiceQuestion.setArtifactId(questionArtifactId);
		multipleChoiceQuestion.setTimeAllowed(timeAllowed);
		multipleChoiceQuestion.setName(name);
		multipleChoiceQuestion.setPrompt(prompt);
		multipleChoiceQuestion.setAnswerIndex(answerIndex);
		multipleChoiceQuestion.setChoicesText(choices);

        if (categoryId != null) {
            multipleChoiceQuestion.setCategory(getAdminService().findCategoryById(categoryId));
        }

        return updateQuestion(multipleChoiceQuestion, categoryId, isNew, model);
	}

    private String updateQuestion(Question question, Long categoryId, Boolean isNew, Map<String, Object> model) {
        Errors errors = populateErrorsQuestion(model, question);
        if(errors.hasFieldErrors("answer")) {
            model.put("answerError", errors.getFieldError("answer").getDefaultMessage());
        }
        if(errors.hasErrors()) {
            if (categoryId != null) {
                Category category = getAdminService().findCategoryById(categoryId);
                model.put("category", category);
            }
            model.put("question", question);
            return new AdminQuestionViewVisitor(question).getView();
        }
        if (isNew == null) {
            isNew = false;
        }
        if (categoryId != null) {
            if (isNew != null && isNew) {
                getAdminService().addQuestionToCategory(categoryId, question);
                model.put("success", isNew);
            } else {
                getAdminService().updateCategoryQuestion(categoryId, question);
            }
        } else {
            if (isNew != null && isNew) {
                getAdminService().saveQuestion(question);
                model.put("success", isNew);
            } else {
                getAdminService().updateQuestion(question);
            }
        }
        if (categoryId != null) {
            return "redirect:question?c="+categoryId+"&q="+question.getArtifactId();
        } else {
            return "redirect:question?q="+question.getArtifactId();
        }
    }

    private Errors populateErrorsQuestion(Map<String, Object> model, Question question) {
        Errors errors = question.validate();
        if(errors.hasFieldErrors("timeAllowed")) {
            model.put("timeAllowedError", errors.getFieldError("timeAllowed").getDefaultMessage());
        }
        if(errors.hasFieldErrors("name")) {
            model.put("nameError", errors.getFieldError("name").getDefaultMessage());
        }
        if(errors.hasFieldErrors("prompt")) {
            model.put("promptError", errors.getFieldError("prompt").getDefaultMessage());
        }
        return errors;
    }

    @RequestMapping(method = POST)
    public String appendChoice(
            @RequestParam(required=false, value="c") Long categoryId,
            @RequestParam("q") String questionArtifactId,
            @RequestParam("name") String name,
            @RequestParam("prompt") String prompt,
            @RequestParam("answerIndex") Integer answerIndex,
            @RequestParam("timeAllowed") Integer timeAllowed,
            @RequestParam(required=false, value="choices") List<String> choices,
            @RequestParam(required = false, value = "n") Boolean isNew,
            Map<String, Object> model) {
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
        multipleChoiceQuestion.setArtifactId(questionArtifactId);
        multipleChoiceQuestion.setTimeAllowed(timeAllowed);
        multipleChoiceQuestion.setName(name);
        multipleChoiceQuestion.setPrompt(prompt);
        multipleChoiceQuestion.setChoicesText(choices);
        multipleChoiceQuestion.setAnswerIndex(answerIndex);
        multipleChoiceQuestion.appendChoice();
        model.put("question", multipleChoiceQuestion);
        if (categoryId != null) {
            Category category = getAdminService().findCategoryById(categoryId);
            model.put("category", category);
        }
        model.put("isNew", isNew);
        return "category/multipleChoiceQuestion";
    }

    @RequestMapping(method = POST)
    public String deleteChoice(
            @RequestParam(required=false, value="c") Long categoryId,
            @RequestParam("q") String questionArtifactId,
            @RequestParam("index") Integer choiceIndex,
            @RequestParam("name") String name,
            @RequestParam("prompt") String prompt,
            @RequestParam("answerIndex") Integer answerIndex,
            @RequestParam("timeAllowed") Integer timeAllowed,
            @RequestParam(required=false, value="choices") List<String> choices,
            @RequestParam(required = false, value = "n") Boolean isNew,
            Map<String, Object> model) {
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
        multipleChoiceQuestion.setArtifactId(questionArtifactId);
        multipleChoiceQuestion.setTimeAllowed(timeAllowed);
        multipleChoiceQuestion.setName(name);
        multipleChoiceQuestion.setPrompt(prompt);
        multipleChoiceQuestion.setChoicesText(choices);
        multipleChoiceQuestion.setAnswerIndex(answerIndex);
        multipleChoiceQuestion.removeChoiceAt(choiceIndex);
        model.put("question", multipleChoiceQuestion);
        if (categoryId != null) {
            Category category = getAdminService().findCategoryById(categoryId);
            model.put("category", category);
        }
        model.put("isNew", isNew);
        return "category/multipleChoiceQuestion";
    }

    @RequestMapping(method = POST)
    public String deleteQuestion(
            @RequestParam("q") String questionArtifactId,
            Map<String, Object> model) {
        Question question = getAdminService().findQuestionByArtifactId(questionArtifactId);
        boolean goToSpecificCategory = false;
        if (question.getCategory() != null) {
            goToSpecificCategory = true;
            model.put("category", question.getCategory());
        }
        getAdminService().deleteQuestion(questionArtifactId);
        if (goToSpecificCategory) {
            return "category/view";
        } else {
            return "redirect:/admin/categories/index";
        }
    }

    @RequestMapping
    public String deleteCategory(
            @RequestParam("c") Long categoryId,
            @RequestParam("q") Boolean deleteQuestions,
            Map<String, Object> model) {
        Category category = getAdminService().findCategoryById(categoryId);
        boolean goToSpecificCategory = false;
        if (category.getParent() != null) {
            goToSpecificCategory = true;
            model.put("category", category.getParent());
        }
        getAdminService().deleteCategory(categoryId, deleteQuestions);
        if (goToSpecificCategory) {
            return "category/view";
        } else {
            return "redirect:/admin/categories/index";
        }
    }
	
	// DWR method
	public Integer moveQuestionToCategory(Long questionId, Long categoryId) {
        getAdminService().moveQuestionToCategory(questionId, categoryId);
		return 0;
	}	
	
	private static class AdminQuestionViewVisitor implements IQuestionVisitor {
		private String view;

		public AdminQuestionViewVisitor(Question question) {
			question.accept(this);
		}
		
		public String getView() {
			return view;
		}
		
		public void visit(CodeQuestion question) {
			this.view = "category/codeQuestion";
		}

		public void visit(EssayQuestion question) {
			this.view = "category/essayQuestion";
		}

		public void visit(MultipleChoiceQuestion question) {
			this.view = "category/multipleChoiceQuestion";
		}
	}

}
