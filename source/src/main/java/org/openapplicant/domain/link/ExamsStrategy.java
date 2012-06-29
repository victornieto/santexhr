package org.openapplicant.domain.link;

import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.openapplicant.domain.DomainObject;
import org.openapplicant.domain.Exam;


/**
 * Models a strategy used by an exam link to fetch exams associated with 
 * that link.
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="kind")
public abstract class ExamsStrategy extends DomainObject{
	
	public abstract List<Exam> fetchExams(ExamLink ctx);
	
}
