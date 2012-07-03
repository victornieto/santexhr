package org.openapplicant.service;

import org.openapplicant.dao.*;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.setting.Smtp;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * Provides common methods for service layer subclasses.
 */
@Service
public abstract class ApplicationService {
	
	private IFileAttachmentDAO fileAttachmentDao;
	
	private ICandidateDAO candidateDao;
	
	private ICandidateExamLinkDAO candidateExamLinkDao;
	
	private ICandidateSearchDAO candidateSearchDao;
	
	private ICandidateWorkFlowEventDAO candidateWorkFlowEventDao;
	
	private ICategoryDAO categoryDao;
	
	private ICompanyDAO companyDao;
	
	private IEmailTemplateDAO emailTemplateDao;
	
	private IExamDAO examDao;
	
	private IExamDefinitionDAO examDefinitionDao;
	
	private IExamLinkDAO examLinkDao;
	
	private IGradeDAO gradeDao;
	
	private IPasswordRecoveryTokenDAO passwordRecoveryTokenDao;
	
	private IProfileDAO profileDao;
	
	private IQuestionDAO questionDao;
	
	private IResponseDAO responseDao;
	
	private ISittingDAO sittingDao;
	
	private IUserDAO userDao;
	
	private IAccountCreationTokenDAO accountCreationTokenDao;
	
	private MailSender mailSender;

    private ExecutorService taskExecutor;
	
	//========================================================================
	// PROPERTIES
	//========================================================================
	protected IFileAttachmentDAO getFileAttachmentDao() {
		return fileAttachmentDao;
	}
	
	@Required
	public void setFileAttachmentDao(IFileAttachmentDAO value) {
		fileAttachmentDao = value;
	}
	
	protected ICandidateDAO getCandidateDao() {
		return candidateDao;
	}
	
	@Required
	public void setCandidateDao(ICandidateDAO value) {
		candidateDao = value;
	}
	
	protected ICandidateExamLinkDAO getCandidateExamLinkDao() {
		return candidateExamLinkDao;
	}
	
	@Required
	public void setCandidateExamLinkDao(ICandidateExamLinkDAO value) {
		candidateExamLinkDao = value;
	}
	
	ICandidateSearchDAO getCandidateSearchDao() {
		return candidateSearchDao;
	}
	
	@Required
	public void setCandidateSearchDao(ICandidateSearchDAO value) {
		candidateSearchDao = value;
	}
	
	protected ICandidateWorkFlowEventDAO getCandidateWorkFlowEventDao() {
		return candidateWorkFlowEventDao;
	}
	
	@Required
	public void setCandidateWorkFlowEventDao(ICandidateWorkFlowEventDAO value) {
		candidateWorkFlowEventDao = value;
	}
	
	protected ICompanyDAO getCompanyDao() {
		return companyDao;
	}
	
	@Required
	public void setCompanyDao(ICompanyDAO value) {
		companyDao = value;
	}
	
	ICategoryDAO getCategoryDao() {
		return categoryDao;
	}
	
	@Required
	public void setCategoryDao(ICategoryDAO value) {
		categoryDao = value;
	}

	IEmailTemplateDAO getEmailTemplateDao() {
		return emailTemplateDao;
	}
	
	@Required
	public void setEmailTemplateDao(IEmailTemplateDAO value) {
		emailTemplateDao = value;
	}
	
	IExamDefinitionDAO getExamDefinitionDao() {
		return examDefinitionDao;
	}
	
	@Required
	public void setExamDefinitionDao(IExamDefinitionDAO value) {
		examDefinitionDao = value;
	}

	IExamDAO getExamDao() {
		return examDao;
	}
	
	@Required
	public void setExamDao(IExamDAO value) {
		examDao = value;
	}

	IExamLinkDAO getExamLinkDao() {
		return examLinkDao;
	}
	
	@Required
	public void setExamLinkDao(IExamLinkDAO value) {
		examLinkDao = value;
	}

	protected IGradeDAO getGradeDao() {
		return gradeDao;
	}
	
	@Required
	public void setGradeDao(IGradeDAO value) {
		gradeDao = value;
	}
	
	IPasswordRecoveryTokenDAO getPasswordRecoveryTokenDao() {
		return passwordRecoveryTokenDao;
	}
	
	@Required
	public void setPasswordRecoveryTokenDao(IPasswordRecoveryTokenDAO value) {
		passwordRecoveryTokenDao = value;
	}
	
	IProfileDAO getProfileDao() {
		return profileDao;
	}
	
	@Required
	public void setProfileDao(IProfileDAO value) {
		profileDao = value;
	}

	IQuestionDAO getQuestionDao() {
		return questionDao;
	}
	
	@Required
	public void setQuestionDao(IQuestionDAO value) {
		questionDao = value;
	}

	IResponseDAO getResponseDao() {
		return responseDao;
	}
	
	@Required
	public void setResponseDao(IResponseDAO value) {
		responseDao = value;
	}

	ISittingDAO getSittingDao() {
		return sittingDao;
	}
	
	@Required
	public void setSittingDao(ISittingDAO value) {
		sittingDao = value;
	}

	IUserDAO getUserDao() {
		return userDao;
	}
	
	@Required
	public void setUserDao(IUserDAO value) {
		userDao = value;
	}
	
	public void setAccountCreationTokenDao(IAccountCreationTokenDAO accountCreationTokenDao) {
		this.accountCreationTokenDao = accountCreationTokenDao;
	}
	
	IAccountCreationTokenDAO getAccountCreationTokenDao() {
		return accountCreationTokenDao;
	}

    @Required
    public void setTaskExecutor(ExecutorService taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
	
	/**
	 * Finds an exam with the given id.
	 * 
	 * @param examId the id of the exam to find
	 * @return the exam with the given id
	 * @throws org.springframework.dao.DataRetrievalFailureException if the exam cannot be found.
	 */
	public Exam findExamById(Long examId) {
		return examDao.find(examId);
	}
	
	/**
	 * Retrieves the latest versions of each exam artifact for the given company
	 * 
	 * @param company the company who's exams to retrieve
	 * @return a list of the latest exam versions.
	 */
	public List<Exam> findExamByCompany(Company company) {
		return getExamDao().findByCompanyId(company.getId());
	}
	
	/**
	 * Retrieves an exam with the given artifact id
	 * 
	 * @param artifactId the artifact id of the exam to retrieve.
	 * @return the exam
	 */
    Exam findExamByArtifactId(String artifactId) {
		return getExamDao().findByArtifactId(artifactId);
	}

    private JavaMailSenderImpl createJavaMailSenderImpl(Smtp smtp) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(smtp.getUrl());
        javaMailSender.setPort(smtp.getPort());
        javaMailSender.setUsername(smtp.getUser());
        javaMailSender.setPassword(smtp.getPass());
        return javaMailSender;
    }

    protected void sendEmailAsynchronously(final SimpleMailMessage simpleMailMessage, final Smtp smtp) {
        taskExecutor.execute(new Runnable() {
            public void run() {
                createJavaMailSenderImpl(smtp).send(simpleMailMessage);
            }
        });
    }

    protected void sendEmailAsynchronously(final MimeMessage simpleMailMessage, final Smtp smtp) {
        taskExecutor.execute(new Runnable() {
            public void run() {
                sendEmailSynchronously(simpleMailMessage, smtp);
            }
        });
    }

    protected void sendEmailSynchronously(final MimeMessage simpleMailMessage, final Smtp smtp) {
        createJavaMailSenderImpl(smtp).send(simpleMailMessage);
    }
}
