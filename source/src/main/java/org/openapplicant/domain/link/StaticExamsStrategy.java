package org.openapplicant.domain.link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Cascade;
import org.openapplicant.domain.Exam;
import org.openapplicant.policy.NeverCall;
import org.springframework.util.Assert;


/**
 * Strategy used to associate a static list of exams with an exam link.
 */
@Entity
public class StaticExamsStrategy extends ExamsStrategy {

	private List<Exam> exams = new ArrayList<Exam>();
	
	public StaticExamsStrategy(Collection<Exam> exams) {
		Assert.notEmpty(exams);
		
		for(Exam each : exams) {
			if(each == null) {
				continue;
			}
			this.exams.add(each);
		}
	}
	
	@NeverCall
	StaticExamsStrategy(){}
	
	@Override
	public List<Exam> fetchExams(ExamLink examLink) {
		return exams;
	}
	
	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinTable(
			inverseJoinColumns=@JoinColumn(name="exam_id")
	)
	public List<Exam> getExamsInternal() {
		return exams;
	}
	
	private void setExamsInternal(List<Exam> value) {
		exams = value;
	}
}
