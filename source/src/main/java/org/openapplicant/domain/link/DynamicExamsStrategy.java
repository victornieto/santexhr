package org.openapplicant.domain.link;

import org.openapplicant.dao.IExamDAO;
import org.openapplicant.domain.Exam;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;


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
        // FIXME Should return a List of ExamDefinition instances
      	// return examDao.findByCompanyId(ctx.getCompany().getId());
        return new ArrayList<Exam>();
	}
}
