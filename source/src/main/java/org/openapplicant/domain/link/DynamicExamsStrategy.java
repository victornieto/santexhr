package org.openapplicant.domain.link;

import java.util.List;

import javax.persistence.Entity;

import org.openapplicant.dao.IExamDAO;
import org.openapplicant.domain.Exam;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Required;


/**
 * Strategy used to associate a company's current exams with an exam link.
 */
@Configurable
@Entity
public class DynamicExamsStrategy extends ExamsStrategy {

	private IExamDAO examDao;
	
	@Required
	public void setExamDao(IExamDAO value) {
		examDao = value;
	}
	
	@Override
	public List<Exam> fetchExams(ExamLink ctx) {
		return examDao.findByCompanyId(ctx.getCompany().getId());
	}
}
