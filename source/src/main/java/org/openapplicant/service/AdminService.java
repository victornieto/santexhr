package org.openapplicant.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.domain.*;
import org.openapplicant.domain.email.EmailTemplate;
import org.openapplicant.domain.event.*;
import org.openapplicant.domain.link.CandidateExamLink;
import org.openapplicant.domain.link.ExamLink;
import org.openapplicant.domain.link.StaticExamsStrategy;
import org.openapplicant.domain.question.MultipleChoiceQuestion;
import org.openapplicant.domain.question.Question;
import org.openapplicant.util.CalendarUtils;
import org.openapplicant.util.Pagination;
import org.openapplicant.util.Verify;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class AdminService extends ApplicationService {

	private static final Log log = LogFactory.getLog(AdminService.class);

	/**
	 * Retrieves a user with the given email.
	 * 
	 * @param email
	 *            the user's email address
	 * @return the user with the given email
	 * @throws DataRetrievalFailureException
	 *             if no user exists for the given email.
	 */
	public User findUserByEmail(String email) {
		return getUserDao().findByEmail(email);
	}

	/**
	 * Retrieves a user with the given email, returning null if that user does
	 * not exist.
	 * 
	 * @param email
	 *            the user's email address
	 * @return the retrieved user or null
	 */
	public User findUserByEmailOrNull(String email) {
		return getUserDao().findByEmailOrNull(email);
	}

	/**
	 * Retrieves a user with the given id.
	 * 
	 * @param id
	 *            the user's id
	 * @return the user with the given id
	 * @throws DataRetrievalFailureException
	 *             if no user exists for the given id.
	 */
	public User findUserById(Long id) {
		return getUserDao().find(id);
	}

	/*
	 * public Company findCompanyById(Long id){ return getCompanyDao().find(id);
	 * }
	 */

	public CandidateExamLink findCandidateExamLinkById(Long id) {
		return getCandidateExamLinkDao().find(id);
	}

	/**
	 * Retrieves a candidate with the given id
	 * 
	 * @param candidateId
	 *            the candidate's database identifier
	 * @return a candidate with id equal to candidateId
	 * @throws DataRetrievalFailureException
	 *             if no candidate matches candidateId.
	 */
	public Candidate findCandidateById(Long candidateId) {
		return getCandidateDao().find(candidateId);
	}

	/**
	 * Finds all candidates for a given company
	 * 
	 * @param company
	 *            the company who's candidates to find
	 * @param pagination
	 *            the pagination to apply
	 * @return candidates
	 */
	public List<Candidate> findAllCandidatesByCompany(Company company,
			Pagination pagination) {
		return getCandidateDao()
				.findAllByCompanyId(company.getId(), pagination);
	}

	/**
	 * Finds all active candidates for a given company
	 */
	public List<Candidate> findAllActiveCandidatesByCompany(Company company,
			Pagination pagination) {
		return getCandidateDao().findAllActiveCandidatesByCompanyId(
				company.getId(), pagination);
	}

	/**
	 * Finds all archived candidates for a given company
	 */
	public List<Candidate> findAllArchivedCandidatesByCompany(Company company,
			Pagination pagination) {
		return getCandidateDao().findAllArchivedCandidatesByCompanyId(
				company.getId(), pagination);
	}

	/**
	 * Finds all candidates in a company with the given status.
	 * 
	 * @param company
	 *            the company who's candidates to find
	 * @param status status to search for
	 * @param pagination
	 *            the pagination to apply
	 * @return candidates
	 */
	public List<Candidate> findAllCandidatesByCompanyAndStatus(Company company,
			Candidate.Status status, Pagination pagination) {

		return getCandidateDao().findAllByCompanyIdAndStatus(company.getId(),
				status, pagination);
	}

	/**
	 * Finds all candidates for a given user's company who match the given
	 * search string.
	 * 
	 * @param user
	 *            the user issuing the search
	 * @param searchString
	 *            a string specifying the candidates to find.
	 * @return all candidates matching the search string.
	 */
	public CandidateSearch createTextCandidateSearch(User user,
			String searchString) {
		CandidateSearch search = new SimpleStringCandidateSearch(searchString,
				user);
		return getCandidateSearchDao().save(search);
	}

	/**
	 * Finds all candidate searches performed by the given user.
	 * 
	 * @param user
	 *            the user who's searches to find.
	 * @param pagination
	 *            the pagination to apply
	 * @return a list of recent searches.
	 */
	public List<CandidateSearch> findAllCandidateSearchesByUser(User user,
			Pagination pagination) {

		return getCandidateSearchDao()
				.findAllByUserId(user.getId(), pagination);
	}

	/**
	 * Finds all candidates for a user's company who match the given criteria.
	 * 
	 * @param user
	 *            the user issuing the search.
	 * @param name
	 *            a string representing the candidates name. (eg.
	 *            "joe bob briggs")
	 * @param skills
	 *            a string representing candidate skills (eg.
	 *            "java javascript ruby")
	 * @param dates
	 *            a string representing a candidate date range. (eg.
	 *            "12/21/2001 1/3/2004")
	 * @return all candidates matching the given criteria.
	 */
	public CandidateSearch createPropertyCandidateSearch(User user,
			String name, String skills, String dates) {

		CandidateSearch search = new PropertyCandidateSearch.Builder(user)
				.name(name).skills(skills).dateRange(dates).build();
		return getCandidateSearchDao().save(search);
	}

	/**
	 * Finds all candidates by executing a persisted search with the given id.
	 * 
	 * @param candidateSearchId
	 *            the id of the candidate search to replay.
	 * @param pagination
	 *            the pagination to apply
	 * @return a list of candidates matching the search.
	 */
	public List<Candidate> findAllCandidatesBySearchId(Long candidateSearchId,
			Pagination pagination) {
		CandidateSearch search = getCandidateSearchDao()
				.find(candidateSearchId);
		return search.execute(pagination);
	}

	/**
	 * Saves the given candidate.
	 * 
	 * @param candidate
	 *            the candidate to save.
	 * @return the saved candidate
	 */
	public Candidate saveCandidate(Candidate candidate, User user) {
        boolean createEvent = false;
        if (candidate.getId() == null) {
            createEvent = true;
        }
		Candidate attachedCandidate = getCandidateDao().save(candidate);
        if (createEvent) {
            getCandidateWorkFlowEventDao().save(
                    new CandidateCreatedByUserEvent(candidate, user));
        }
        return attachedCandidate;
	}

	/**
	 * Adds a note to the given candidate.
	 * 
	 * @param candidateId
	 *            the id of the candidate to add a note to.
	 * @param note
	 *            the note to add
	 * @return the updated candidate
	 */
    @Transactional(isolation = Isolation.SERIALIZABLE)
	public Candidate addNoteToCandidate(Long candidateId, Note note) {
		Candidate candidate = getCandidateDao().find(candidateId);
		candidate.addNote(note);
		candidate = getCandidateDao().save(candidate);
		getCandidateWorkFlowEventDao().save(
				new AddNoteToCandidateEvent(candidate, note));
		return candidate;
	}

	/**
	 * Attaches a resume to a candidate.
	 * 
	 * @param user
	 *            the user who attached the resume
	 * @param candidateId
	 *            the id of the candidate to attach a resume to
	 * @param resume
	 *            the resume to attach
	 * @return the updated candidate.
	 */
	public Candidate attachResumeToCandidate(User user, long candidateId,
			Resume resume) {
		Assert.notNull(user);
		Assert.notNull(resume);

		Candidate candidate = getCandidateDao().find(candidateId);
		candidate.setResume(resume);
		candidate = getCandidateDao().save(candidate);
		getCandidateWorkFlowEventDao().save(
				new UserAttachedResumeEvent(user, candidate, resume));
		return candidate;
	}

	/**
	 * Attaches a cover letter to a candidate
	 * 
	 * @param user
	 *            the use who attached the cover letter
	 * @param candidateId
	 *            the id of the candidate to attach a cover letter to
	 * @param coverLetter
	 *            the cover letter to attach
	 * @return the updated candidate.
	 */
	public Candidate attachCoverLetterToCandidate(User user, long candidateId,
			CoverLetter coverLetter) {
		Assert.notNull(user);
		Assert.notNull(coverLetter);

		Candidate candidate = getCandidateDao().find(candidateId);
		candidate.setCoverLetter(coverLetter);
		candidate = getCandidateDao().save(candidate);
		getCandidateWorkFlowEventDao().save(
				new UserAttachedCoverLetterEvent(user, candidate, coverLetter));
		return candidate;
	}

	/**
	 * Reverts a candidate to their last active state.
	 * 
	 * @param candidateId
	 *            the id of the candidate to unarchve
	 * @param user
	 *            the user who unarchived the candidate.
	 * @return the unarchived candidate
	 */
	public Candidate unarchiveCandidate(long candidateId, User user) {
		Candidate candidate = getCandidateDao().find(candidateId);
		candidate.unarchive();
		getCandidateDao().save(candidate);
		getCandidateWorkFlowEventDao().save(
				new CandidateStatusChangedEvent(candidate, user));
		return candidate;
	}

	/**
	 * Updates a candidate's status
	 * 
	 * @param candidateId
	 *            the id of the candidate to update
	 * @param status
	 *            the candidate's status
	 * @param user
	 *            the user who performed the update
	 * @return the updated candidate
	 */
	public Candidate updateCandidateStatus(Long candidateId,
			Candidate.Status status, User user) {
		Candidate candidate = getCandidateDao().find(candidateId);
		candidate.setStatus(status);
		candidate = getCandidateDao().save(candidate);
		getCandidateWorkFlowEventDao().save(
				new CandidateStatusChangedEvent(candidate, user));
		return candidate;
	}

	/**
	 * Finds all work flow events for a candidate with the given id.
	 * 
	 * @param candidateId
	 *            the id of the candidate who's events to find.
	 */
	public List<CandidateWorkFlowEvent> findAllCandidateWorkFlowEventsByCandidateId(
			Long candidateId) {
		return getCandidateWorkFlowEventDao().findAllByCandidateId(candidateId);
	}

	/**
	 * Creates a new candidate
	 * 
	 * @param user
	 *            the user who created the candidate
	 * @return the created candidate
	 */
	public Candidate createCandidate(User user) {
		Candidate candidate = new Candidate();
		candidate.setCompany(user.getCompany());
		candidate.setStatus(Candidate.Status.INCOMPLETE);

		return candidate;
	}

	/**
	 * Saves the given sitting.
	 * 
	 * @param sitting
	 *            the sitting to save
	 * @return the saved sitting
	 */
	/*
	 * public Sitting saveSitting(Sitting sitting) { return
	 * getSittingDao().save(sitting); }
	 */

	/**
	 * @param company
	 *            the company to add the user to
	 * @param user
	 *            the user to create
	 * @return the created user
	 */
	public User createUser(Company company, User user) {
		company.addUser(user);
		getCompanyDao().save(company);
		return user;
	}

	/**
	 * Updates or Saves an exam's editable information
	 * 
	 * @param exam the exam who's info to save
	 * @return the updated exam
	 */
	public Exam saveExam(Exam exam) {
		return getExamDao().save(exam);
	}

	/**
	 * Retrieves a sitting with the given id
	 * 
	 * @param sittingId
	 * @return Sitting
	 * @throws DataRetrievalFailureException
	 *             if no sitting has sittingId
	 */
	public Sitting findSittingById(Long sittingId) {
		return getSittingDao().find(sittingId);
	}

	/**
	 * @param question
	 * @return QuestionStatistics
	 */
	public QuestionStatistics getTotalTimeStatistics(Question question) {
		return getQuestionDao().getTotalTimeStatistics(question);
	}

	public QuestionStatistics getWordsPerMinuteStatistics(Question question) {
		return getQuestionDao().getWordsPerMinuteStatistics(question);
	}

	public QuestionStatistics getKeyCharsStatistics(Question question) {
		return getQuestionDao().getKeyCharsStatistics(question);
	}

	public QuestionStatistics getCorrectnessStatistics(Question question) {
		return getQuestionDao().getCorrectnessStatistics(question);
	}

	public QuestionStatistics getStyleStatistics(Question question) {
		return getQuestionDao().getStyleStatistics(question);
	}

	/**
	 * Grades a candidate's response, if the candidate has grades for all it's
	 * questions it sets the candidate to graded.
	 * 
	 * @param user
	 *            the response's grader
	 * @param sittingId
	 *            the id of the response's sitting
	 * @param responseId
	 *            the id of the response to grade
	 * @param grade the grade to assign
	 * @return the graded sitting
	 */
	public Sitting updateResponseGrade(User user, long sittingId,
			long responseId, Grade grade) {
		Sitting sitting = getSittingDao().find(sittingId);

		sitting.gradeResponse(responseId, grade);
		getSittingDao().save(sitting);

		if (sitting.isEachResponseGraded()) {
			getCandidateWorkFlowEventDao().save(
					new SittingGradedEvent(user, sitting));

			computeMatchScore(sitting.getCandidate());
			getCandidateDao().save(sitting.getCandidate());

		}

		return sitting;
	}

	/**
	 * @param companyId
	 *            the id of the company who's users to find.
	 * @return a set of users
	 */
	public Set<User> findAllUsersByCompanyId(Long companyId) {
		return getCompanyDao().find(companyId).getUsers();
	}

	/**
	 * Saves the company profile.
	 * 
	 * @param profile
	 *            the profile to save
	 * @return the saved profile.
	 * @throws IllegalArgumentException
	 *             if profile is new
	 */
	public Profile saveProfile(Profile profile) {
		Assert.isTrue(!profile.isNew());
		return getProfileDao().save(profile);
	}

	/**
	 * @param id
	 *            the id of the EmailTemplate to find
	 * @return the template with the given id
	 */
	public EmailTemplate findEmailTemplateById(long id) {
		return getEmailTemplateDao().find(id);
	}

	public EmailTemplate saveEmailTemplate(EmailTemplate template) {
		return getEmailTemplateDao().save(template);
	}

	/**
	 * @param emailTemplateId
	 *            the id of the email template to revert
	 * @return the reverted email template
	 */
	public EmailTemplate revertEmailTemplate(long emailTemplateId) {
		EmailTemplate template = getEmailTemplateDao().find(emailTemplateId);
		template.revert();
		return getEmailTemplateDao().save(template);
	}

	public User saveUser(User user) {
		return getUserDao().save(user);
	}

	public List<CandidateExamLink> getExamLinksForCandidate(Candidate candidate) {
		return getCandidateExamLinkDao()
				.findAllByCandidateId(candidate.getId());
	}

	/**
	 * @param guid the of the file attachment to find
	 * @return the file attachment with the given guid.
	 */
	public FileAttachment findFileAttachmentByGuid(String guid) {
		return getFileAttachmentDao().findByGuid(guid);
	}

	/**
	 * Updates a user's last login time to the current time.
	 * 
	 * @param userId
	 *            the id of the user who's last login time to update
	 */
	public void updateUserLastLoginTime(long userId) {
		User user = getUserDao().find(userId);
		user.setLastLoginTime(CalendarUtils.now());
		getUserDao().save(user);
	}

	/**
	 * Sends a confirmation email allowing a user to recover their password.
	 * 
	 * @param token
	 *            a token used to validate email confirmation.
	 */
	public void sendPasswordRecoveryEmail(PasswordRecoveryToken token) {
		Assert.isTrue(token.isNew());

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(token.getUser().getEmail());
		msg.setFrom("notify@openapplicant.com");
		msg.setSubject("Your Account");
		msg.setText("You have requested to change your Santex HR password.  "
				+ "To reset your password, please visit\n" + token.getUrl());
        sendEmailAsynchronously(msg, token.getUser().getCompany().getSmtp());
		getPasswordRecoveryTokenDao().save(token);
	}

	/**
	 * @param guid token's guid
	 */
	public PasswordRecoveryToken findPasswordRecoveryTokenByGuid(String guid) {
		return getPasswordRecoveryTokenDao().findByGuid(guid);
	}

	/**
	 * Delete's all password recovery tokens for a given user
	 * 
	 * @param user
	 *            the user who's tokens to delete
	 */
	public void deleteAllPasswordRecoveryTokensForUser(User user) {
		getPasswordRecoveryTokenDao().deleteAllQuietlyByUserId(user.getId());
	}

	/**
	 * Updates an exam with the given artifactId
	 * 
	 * @param examArtifactId
	 * @param name
	 * @param genre
	 * @param description
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void updateExamInfo(String examArtifactId, String name,
			String genre, String description) {
		Exam exam = findExamByArtifactId(examArtifactId);
		exam.setName(name);
		exam.setGenre(genre);
		exam.setDescription(description);
		getExamDao().save(exam);
	}

	/**
	 * Adds a coding question to an exam with the given artifactId
	 * 
	 * @param examArtifactId
	 *            the artifactId of the exam to add a question to
	 * @return the added question
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Question addQuestionToExam(String examArtifactId, Question question) {
		Exam exam = findExamByArtifactId(examArtifactId);
		exam.addQuestion(question);
		getExamDao().save(exam);
		return question;
	}

	/**
	 * Adds a coding question to an exam with the given artifactId
	 * 
	 * @param examArtifactId
	 *            the artifactId of the exam to add a question to
	 * @return the added question
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Question addMultipleChoiceQuestionToExam(String examArtifactId) {
		Exam exam = findExamByArtifactId(examArtifactId);
		Question question = new MultipleChoiceQuestion();
		exam.addQuestion(question);
		getExamDao().save(exam);
		return question;
	}

	/**
	 * @param examArtifactId
	 * @param question
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void updateExamQuestion(String examArtifactId, Question question) {
		Exam exam = findExamByArtifactId(examArtifactId);
		exam.updateQuestion(question);
		getExamDao().save(exam);
	}

	/**
	 * @param user
	 *            the user who wishes to send an email
	 * @param examLinkId
	 *            id the id of the exam link
	 * @param message
	 *            the message to send
	 */
	public void sendExamInviteEmail(User user, long examLinkId,
			SimpleMailMessage message) {
		CandidateExamLink examLink = getCandidateExamLinkDao().find(examLinkId);

		Verify.contains(message.getTo(), examLink.getCandidate().getEmail(),
				"candidate should be the recipient of message");
        sendEmailAsynchronously(message, user.getCompany().getSmtp());
		examLink.getCandidate().setStatus(Candidate.Status.SENT_EXAM);
		getCandidateDao().save(examLink.getCandidate());
		getCandidateWorkFlowEventDao().save(
				new UserSentExamLinkEvent(user, examLink));
	}

	/**
	 * @param accountCreationToken
	 *            account creation token
	 */
	public void saveAccountCreationToken(
			AccountCreationToken accountCreationToken) {
		getAccountCreationTokenDao().save(accountCreationToken);
	}

	/**
	 * 
	 * @param message The message to send
     * @param company The company to get the smtp config from
	 */
	public void sendAccountCreationEmail(SimpleMailMessage message, Company company) {
		sendEmailAsynchronously(message, company.getSmtp());
	}

	public AccountCreationToken findAccountCreationTokenByGuid(String guid) {
		return getAccountCreationTokenDao().findByGuid(guid);
	}

	public void computeMatchScore(Candidate candidate) {

		try {

			// get the most recent sitting
			Sitting sitting = candidate.getLastSitting();
			if (sitting == null) {
				candidate.setMatchScore(null);
				// can't compute a matching score if we have no sitting.
				return;
			}

			ResponseSummary summary = sitting.getResponseSummary();

			Map<String, Integer> elements = new HashMap<String, Integer>();
			Map<String, Double> values = new HashMap<String, Double>();

			elements.put("total_time", 1);
			values.put("total_time", (double) summary.getTotalTime());

			elements.put("key_chars", 1);
			values.put("key_chars", (double) summary.getKeyChars());

			elements.put("hesitation_time", 1);
			values.put("hesitation_time",
                    (double) summary.getHesitationTime());

			elements.put("focus_changes", -1);
			values.put("focus_changes", (double) summary.getFocusChanges());

			elements.put("paste_presses", -1);
			values.put("paste_presses", (double) summary.getPastePresses());

			QuestionStatistics qs_style = getExamDao()
					.findSittingStatisticsBySittingId(sitting.getId(), "style");
			elements.put("style", 1);
			values.put("style", qs_style.getMean().doubleValue());

			QuestionStatistics qs_correct = getExamDao()
					.findSittingStatisticsBySittingId(sitting.getId(),
							"correctness");
			elements.put("correctness", 1);
			values.put("correctness", qs_correct.getMean()
                    .doubleValue());

			// resume screening score

			double score = 0.0;
			double max_score = 0.0;
			for (String item : elements.keySet()) {
				// FIXME: refactor this so the column doesn't leak
				QuestionStatistics qs = getExamDao()
						.findExamStatisticsByArtifactIdAndColumn(
								sitting.getExam().getArtifactId(), item);
				double item_score = simpleScoreFunction(values.get(item), qs
						.getHiredMean().doubleValue(), qs.getStddev()
						.doubleValue());
				score += (elements.get(item) * item_score);
				if (elements.get(item) > 0)
					max_score += (elements.get(item) * 5.0);
			}

			double computed_score = (100.0 * score) / max_score;
			candidate.setMatchScore(new BigDecimal(computed_score));

		} catch (Exception e) {
			// FIXME maybe set the score to 0 instead of null
			candidate.setMatchScore(null);
		}

	}

	public Company findCompanyByEmailAlias(String alias) {
		// TODO Auto-generated method stub
		return getCompanyDao().findByEmailAlias(alias);
	}

	public void saveCompany(Company company) {
		getCompanyDao().save(company);
	}

	private double simpleScoreFunction(double value, double mean, double stddev) {
		double total_diff = value - mean;
		if (total_diff < (0.0 - stddev))
			return 0;
		else if (total_diff > stddev)
			return 4;
		else if (total_diff < 0)
			return 1;
		else if (total_diff > 0)
			return 3;
		else
			return 2;
	}

	/**
	 * Retrieves all the categories from the given company
	 * 
	 * @param company
	 * @param pagination
	 * @return
	 */
	public List<Category> findAllCategoriesByCompany(Company company,
			Pagination pagination) {
		List<Category> mainCategories = findMainCategoriesByCompany(company, pagination);
		List<Category> allCategories = new ArrayList<Category>();
		for (Category category : mainCategories) {
			allCategories.add(category);
			allCategories.addAll(category.getAllSubcategories());
		}
		return allCategories;
	}
	
	/**
	 * Retrieves the main categories from the given company
	 * 
	 * @param company
	 * @param pagination
	 * @return
	 */
	public List<Category> findMainCategoriesByCompany(Company company, 
			Pagination pagination) {
		return getCategoryDao().findMainCategoriesByCompany(company, pagination);
	}

	/**
	 * Returns the category for the given id
	 * 
	 * @param categoryId
	 * @return
	 */
	public Category findCategoryById(Long categoryId) {
		return getCategoryDao().findCategoryById(categoryId);
	}

	/**
	 * Updates the category given by the specified categoryId
	 * 
	 * @param categoryId
	 * @param name
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void updateCategoryInfo(Long categoryId, String name) {
		Category category = findCategoryById(categoryId);
		category.setName(name);
		getCategoryDao().save(category);
	}

	/**
	 * Updates or Saves an category's editable information
	 * 
	 * @param category
	 *            the category who's info to save
	 * @return the updated category
	 */
	public Category saveCategory(Category category) {
		return getCategoryDao().save(category);
	}

	/**
	 * Adds a question to a category with the given id
	 * 
	 * @param categoryId
	 *            the categoryId of the category to add a question to
	 * @return the added question
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Question addQuestionToCategory(Long categoryId, Question question) {
		Category category = findCategoryById(categoryId);
		category.addQuestion(question);
		getCategoryDao().save(category);
		return question;
	}

	/**
	 * Updates a category's question with the given id
	 * @param categoryId the category's Id
	 * @param question the question to update
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void updateCategoryQuestion(Long categoryId, Question question) {
		Category category = findCategoryById(categoryId);
		category.updateQuestion(question);
		getCategoryDao().save(category);
	}
	/**
	 * Finds all exam definitions for the given company
	 * @param company the company who's exam definitions to find
	 * @return the exam definitions of the given company
	 */
	public List<ExamDefinition> findAllExamDefinitionsByCompany(Company company) {
		return getExamDefinitionDao().findAllExamDefinitionsByCompany(company);
	}
	
	/**
	 * Adds a category percentage to the exam definition given its artifactId
	 * @param examDefinitionArtifactId the exam definition's artifactId
	 * @param categoryPercentage the category percentage to add
	 * @return the persisted category percentage
	 */
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public CategoryPercentage addCategoryPercentageToExamDefinition(
			String examDefinitionArtifactId,
			CategoryPercentage categoryPercentage) {
		ExamDefinition examDefinition = getExamDefinitionDao().findExamDefinitionByArtifactId(examDefinitionArtifactId);
		examDefinition.addCategoryPercentage(categoryPercentage);
		getExamDefinitionDao().save(examDefinition);
		return categoryPercentage;
	}
	
	/**
	 * Finds the exam definition for this artifactId
	 * @param examDefinitionArtifactId the exam definition's artifactId
	 * @return the exam definition for this artifactId
	 */
	public ExamDefinition findExamDefinitionByArtifactId(String examDefinitionArtifactId) {
		return getExamDefinitionDao().findExamDefinitionByArtifactId(examDefinitionArtifactId);
	}

	/**
	 * Saves an exam definition instance
	 * @param examDefinition the exam definition to save
	 */
	public void saveExamDefinition(ExamDefinition examDefinition) {
		getExamDefinitionDao().save(examDefinition);
	}

	/***
	 * Updates a category percentage of the exam definition given its
	 * artifactId
	 * @param examDefinitionArtifactId the artifactId of the exam definition
	 * @param categoryPercentage the updated category percentage
	 */
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public void updateExamDefinitionCategoryPercentage(
			String examDefinitionArtifactId,
			CategoryPercentage categoryPercentage) {
		ExamDefinition examDefinition = findExamDefinitionByArtifactId(examDefinitionArtifactId);
		examDefinition.updateCategoryPercentage(categoryPercentage);
		getExamDefinitionDao().save(examDefinition);
	}

	/**
	 * Updates the info of an exam definition given its artifactId
	 * @param examDefinitionArtifactId the exam definition's artifactId
	 * @param name the updated name
	 * @param genre the updated genre
	 * @param description the updated description
     * @param numberOfQuestionsWanted number of questions that the generated exam will have
     * @param isActive the updated active status
     * @param categoriesPercentage updated list of categories percentage
     */
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public void updateExamDefinitionInfo(String examDefinitionArtifactId, String name,
			String genre, String description, Integer numberOfQuestionsWanted, boolean isActive,
            List<CategoryPercentage> categoriesPercentage, JobPosition jobPosition) {
		ExamDefinition examDefinition = findExamDefinitionByArtifactId(examDefinitionArtifactId);
		examDefinition.setName(name);
		examDefinition.setGenre(genre);
		examDefinition.setDescription(description);
		examDefinition.setNumberOfQuestionsWanted(numberOfQuestionsWanted);
		examDefinition.setActive(isActive);
        examDefinition.setJobPosition(jobPosition);
        // Delete not present categories percentage
        List<CategoryPercentage> currentCategoriesPercentage = examDefinition.getCategoriesPercentage();
        for (int i = 0; i < currentCategoriesPercentage.size(); i++) {
            CategoryPercentage categoryPercentage = currentCategoriesPercentage.get(i);
            if (!isCategoryPercentageIn(categoryPercentage, categoriesPercentage)) {
                examDefinition.removeCategoryPercentage(categoryPercentage.getArtifactId());
                i--;
            }
        }
        // Update existing categories percentage or add it if does not exist
        for (CategoryPercentage categoryPercentage : categoriesPercentage) {
            if (isCategoryPercentageIn(categoryPercentage, examDefinition.getCategoriesPercentage())) {
                examDefinition.updateCategoryPercentage(categoryPercentage);
            } else {
                examDefinition.addCategoryPercentage(categoryPercentage);
            }
        }
		getExamDefinitionDao().save(examDefinition);
	}

    private boolean isCategoryPercentageIn(CategoryPercentage categoryPercentage, List<CategoryPercentage> categoriesPercentage) {
        boolean isPresent = false;
        for (CategoryPercentage listCategoryPercentage : categoriesPercentage) {
            if (listCategoryPercentage.getArtifactId().equals(categoryPercentage.getArtifactId())) {
                isPresent = true;
                break;
            }
        }
        return isPresent;
    }

	/**
	 * Return an exam definition by its id
	 * @param examDefinitionId the id of the exam definition to return
	 * @return the exam definition with the specified id
	 */
	public ExamDefinition findExamDefinitionById(Long examDefinitionId) {
		return getExamDefinitionDao().find(examDefinitionId);
	}

	/**
	 * Creates an exam link for a candidate
	 * 
	 * @param user
	 *            the user who created the exam link
	 * @param candidate
	 *            the candidate who is being linked to
	 * @param examDefinitions
	 *            a list of exam definitions to make exams to link to
	 * @return
	 */
	/**
	 * Creates an exam link for a candidate
	 * 
	 * @param user the user who created the exam link
	 * @param candidate the candidate who is being linked to
	 * @param examDefinitions a list of exam definitions to make exams to link to
	 * @return an ExamLink including all the Exams to be done by the candidate
	 * @throws IllegalStateException when there is an invalid state when building 
	 * the exams. This can happen either when the exam definition hasn't 100% for its
	 * sum of category percentages, or when there's no enough questions assigned to
	 * a category to fulfill the exam definition requirements
	 */
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public ExamLink createExamLink(User user, Candidate candidate,
			List<ExamDefinition> examDefinitions) throws IllegalStateException {
		List<Exam> exams = createExamsFromExamDefinitions(examDefinitions);
		CandidateExamLink examLink = new CandidateExamLink(user.getCompany(),
				candidate, new StaticExamsStrategy(exams));

        for (Exam exam : exams) {
            for (Question question : exam.getQuestions()) {
                question.freeze();
                getQuestionDao().save(question);
            }
        }

		getCandidateExamLinkDao().save(examLink);

		getCandidateWorkFlowEventDao().save(
				new CreateExamLinkForCandidateEvent(candidate, examLink, user));
		return examLink;
	}

	/**
	 * Given a list of exam definitions, create an exam for each of them, 
	 * using random questions in the proportion stated in the definition, 
	 * and return them as a list
	 * @param examDefinitions the definitions of the exams to create the exams
	 * @return a list of exams, with random question in them
	 */
	private List<Exam> createExamsFromExamDefinitions(
			List<ExamDefinition> examDefinitions) throws IllegalStateException {
		List<Exam> exams = new ArrayList<Exam>();
		for (ExamDefinition examDefinition : examDefinitions) {
			if (!examDefinition.isComplete()) {
				throw new IllegalStateException(
						"One or more exam definitions selected were not completed." + 
						" Please, refer to the Exam Definitions section.");
			}
			Integer totalNumberOfQuestionsWanted = examDefinition.getNumberOfQuestionsWanted();
			Exam exam = new Exam();
			exam.setDescription(examDefinition.getDescription());
			exam.setGenre(examDefinition.getGenre());
			exam.setName(examDefinition.getName());
			Random rnd = new Random();
			Double percentLeft = 100.0;
			for (CategoryPercentage categoryPercentage : examDefinition.getCategoriesPercentage()) {
				Category category = categoryPercentage.getCategory();
				percentLeft -= categoryPercentage.getPercentage();
				Integer numberOfQuestionRequired = (int) (totalNumberOfQuestionsWanted - exam.getQuestions().size() - Math.round((percentLeft / 100.0) * totalNumberOfQuestionsWanted));
				List<Question> categoryAllQuestions = category.getAllQuestions();
				if (categoryAllQuestions.size() < numberOfQuestionRequired) {
					throw new IllegalStateException(
							"The category " + category.getName() + " should contain at least " +
							numberOfQuestionRequired + " questions in order to meet the " + categoryPercentage.getPercentage() + 
							"% of total questions of the exam definition " + examDefinition.getName());
				}
				if (numberOfQuestionRequired.equals(categoryAllQuestions.size())) {
					for (Question question : categoryAllQuestions) {
						exam.addQuestion(question);
					}
				} else {
					Integer numberOfQuestionAdded = 0;
					List<Question> remainingQuestions = new ArrayList<Question>(categoryAllQuestions);
					while (numberOfQuestionAdded < numberOfQuestionRequired) {
						Question question = remainingQuestions.get(rnd.nextInt(remainingQuestions.size()));
						exam.addQuestion(question);
						numberOfQuestionAdded++;
						remainingQuestions.remove(question);
					}
				}
			}
			exams.add(exam);
		}
		return exams;
	}

	/**
	 * Finds a question by its primary key
	 * @param questionId the question id
	 * @return the Question with the specified primary key
	 */
    Question findQuestionById(Long questionId) {
		return getQuestionDao().find(questionId);
	}

	public List<Question> findQuestionsWithoutCategory() {
		return getQuestionDao().getQuestionsWithoutCategory();
	}

	public Question findQuestionByArtifactId(String questionArtifactId) {
		return getQuestionDao().findByArtifactId(questionArtifactId);
	}

    @Transactional(isolation = Isolation.SERIALIZABLE)
	public void saveQuestion(Question question) {
		getQuestionDao().save(question);
	}

    @Transactional(isolation = Isolation.SERIALIZABLE)
	public void updateQuestion(Question question) {
		Question attachedQuestion = getQuestionDao().findByArtifactId(question.getArtifactId());
		attachedQuestion.merge(question);
		saveQuestion(attachedQuestion);
	}

    /**
     * Deletes a question from the database.
     * @param questionArtifactId The question's artifactId to remove.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteQuestion(String questionArtifactId) {
        deleteQuestion(findQuestionByArtifactId(questionArtifactId));
    }

    private void deleteQuestion(Question question) {
        Category category = question.getCategory();
        if (category != null) {
            category.removeQuestion(question);
            getCategoryDao().save(category);
        }
        if (question.isNotFrozen()) {
            final Question previousVersion = question.getPreviousVersion();
            if (previousVersion != null) {
                previousVersion.setNextVersion(null);
                getQuestionDao().save(previousVersion);
            }
            getQuestionDao().delete(question.getId());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteCategory(Long categoryId, Boolean deleteQuestions) {
        Category category = findCategoryById(categoryId);
        if (!deleteQuestions) {
            final List<Question> allQuestions = category.getAllQuestions();
            for (Question question : allQuestions) {
                moveQuestionToCategory(question, null);
            }
        }
        getCategoryDao().deleteAllCategoryPercentagesWith(category);
        getCategoryDao().delete(category.getId());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void moveQuestionToCategory(Long questionId, Long categoryId) {
        Category category = null;
        if (categoryId != null) {
            category = findCategoryById(categoryId);
        }
        moveQuestionToCategory(findQuestionById(questionId), category);
    }

    private void moveQuestionToCategory(Question question, Category category) {
        Category sourceCategory = question.getCategory();
        if (sourceCategory != null) {
            sourceCategory.removeQuestion(question);
            saveCategory(sourceCategory);
        }
        if (question.isFrozen()) {
            question = question.createSnapshot();
            if (category == null) {
                saveQuestion(question);
            }
        }
        if (category != null) {
            category.addQuestion(question);
            saveCategory(category);
        }
    }

    /**
     * Deletes the exam definition with the specified artifact id
     * @param examDefinitionArtifactId
     */
    public void deleteExamDefinition(String examDefinitionArtifactId) {
        ExamDefinition examDefinition = findExamDefinitionByArtifactId(examDefinitionArtifactId);
        getExamDefinitionDao().delete(examDefinition.getId());
    }

    /**
     * Finds all candidates registered by the given user
     * @param user
     */
    public List<Candidate> findAllCandidatesByUser(User user) {
        return getCandidateDao().findByUser(user);
    }

    /**
     * Finds all jobs positions for the given company
     * @param company
     * @return
     */
    public Collection<JobPosition> findJobPositionsByCompany(Company company) {
        return getJobPositionDao().findAllByCompany(company);
    }

    /**
     * Updates a job position info
     * @param id
     * @param name
     */
    public void updateJobPositionInfo(Long id, String name, Set<Seniority> seniorities) {
        JobPosition jobPosition = getJobPositionDao().find(id);
        jobPosition.setName(name);
        jobPosition.setSeniorities(seniorities);
        getJobPositionDao().save(jobPosition);
    }

    /**
     * Returns a JobPosition instance given by its id
     * @param id
     * @return
     */
    public JobPosition findJobPositionById(Long id) {
        return getJobPositionDao().find(id);
    }

    /**
     * Saves a job position
     * @param jobPosition
     * @return
     */
    public JobPosition saveJobPosition(JobPosition jobPosition) {
        return getJobPositionDao().save(jobPosition);
    }

    /**
     * Deletes a job positions with the given id
     * @param id
     */
    public void deleteJobPositionWithId(Long id) {
        getJobPositionDao().delete(id);
    }

    /**
     * Returns all job openings from the given company
     * @param company
     * @return
     */
    public List<JobOpening> findAllJobOpeningsByCompany(Company company) {
        return getJobOpeningDao().findAllByCompany(company);
    }

    /**
     * Returns active job openings from the given company
     * @param company
     * @return
     */
    public List<JobOpening> findActiveJobOpeningsByCompany(Company company) {
        return getJobOpeningDao().findActiveByCompany(company);
    }

    /**
     * Returns archived job openings from the given company
     * @param company
     * @return
     */
    public List<JobOpening> findArchivedJobOpeningsByCompany(Company company) {
        return getJobOpeningDao().findArchivedByCompany(company);
    }

    /**
     * Returns job openings from the given company with the given status
     * @param company
     * @return
     */
    public List<JobOpening> findJobOpeningsByCompanyAndStatus(Company company, JobOpening.Status status) {
        return getJobOpeningDao().findByCompanyAndStatus(company, status);
    }

    /**
     * Returns the job opening with the given id
     * @param jobOpeningId
     * @return
     */
    public JobOpening findJobOpeningById(Long jobOpeningId) {
        return getJobOpeningDao().find(jobOpeningId);
    }

    /**
     * Updates a job opening information
     * @param id
     * @param jobPosition
     * @param finishDate
     * @param client
     * @param description
     * @param applicants
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateJobOpeningInfo(Long id, JobPosition jobPosition, Date finishDate, String client, String description, Set<Candidate> applicants) {
        JobOpening jobOpening = getJobOpeningDao().find(id);
        jobOpening.setJobPosition(jobPosition);
        jobOpening.setFinishDate(finishDate);
        jobOpening.setClient(client);
        jobOpening.setDescription(description);
        jobOpening.setApplicants(applicants);
        getJobOpeningDao().save(jobOpening);
    }

    /**
     * Saves a job opening
     * @param jobOpening
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveJobOpening(JobOpening jobOpening) {
        getJobOpeningDao().save(jobOpening);
    }
}
