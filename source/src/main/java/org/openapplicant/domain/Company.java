package org.openapplicant.domain;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;
import org.openapplicant.domain.email.AutoInviteEmailTemplate;
import org.openapplicant.domain.email.ExamInviteEmailTemplate;
import org.openapplicant.domain.email.RejectCandidateEmailTemplate;
import org.openapplicant.domain.email.RequestResumeEmailTemplate;
import org.openapplicant.domain.link.DynamicExamsStrategy;
import org.openapplicant.domain.link.ExamLink;
import org.openapplicant.domain.link.OpenExamLink;
import org.openapplicant.domain.setting.Facilitator;
import org.openapplicant.domain.setting.Smtp;
import org.openapplicant.util.Messages;
import org.openapplicant.validation.HostName;
import org.openapplicant.validation.Unique;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Required;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Configurable
@Entity
public class Company extends DomainObject {

	private String contextRoot; // FIXME: should be resolvable at runtime
	
	private String 	name = "";
	private String 	hostName = "";
	private int 	hostPort = 80;
	
	private String 	emailAlias = ""; // accept resumes via e-mail too
	
	// Are these to be in the facilitator inner class?
	private String proxyName = "";
	private int proxyPort = 80;
	
	private Profile profile;
	
	private Facilitator facilitator;
	private Smtp smtp;
	
	private Set<User> users = new HashSet<User>();

	private AutoInviteEmailTemplate autoInviteEmailTemplate;
	
	private RejectCandidateEmailTemplate rejectCandidateEmailTemplate;
	
	private RequestResumeEmailTemplate requestResumeEmailTemplate;
	
	private ExamInviteEmailTemplate examInviteEmailTemplate;
	
	private ExamLink linkToAllExams;
	
	private String imageUrl;
	
	private String welcomeText = Messages.getDefaultCompanyWelcomeText();
	
	private String completionText = Messages.getDefaultCompanyCompletionText();
	
	public Company() {
		profile = new Profile();
		facilitator = new Facilitator();
		smtp = new Smtp();
		autoInviteEmailTemplate = new AutoInviteEmailTemplate();
		rejectCandidateEmailTemplate = new RejectCandidateEmailTemplate();
		requestResumeEmailTemplate = new RequestResumeEmailTemplate();
		examInviteEmailTemplate = new ExamInviteEmailTemplate();
		linkToAllExams = new OpenExamLink(this, new DynamicExamsStrategy());
	}
	
	@OneToOne(
			cascade=CascadeType.ALL, 
			optional=false,
			fetch=FetchType.LAZY
	)
	@JoinColumn(nullable=false)
	public Profile getProfile() {
		return profile;
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	@Valid
	@NotNull
	@Embedded
	public Facilitator getFacilitator() {
		return facilitator;
	}

	public void setFacilitator(Facilitator value) {
		this.facilitator = value;
	}
	
	@Valid
	@NotNull
	@Embedded
	public Smtp getSmtp() {
		return smtp;
	}

	public void setSmtp(Smtp value) {
		this.smtp = value;
	}
	
	@OneToOne(
			cascade=CascadeType.ALL,
			optional=false,
			fetch=FetchType.LAZY
	)
	@JoinColumn(nullable=false)
	public ExamLink getLinkToAllExams() {
		return linkToAllExams;
	}
	
	private void setLinkToAllExams(ExamLink value) {
		linkToAllExams = value;
	}
	
	@NotEmpty
	@Column(nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = StringUtils.trim(name);
	}

	/**
	 * @return the company's exam link host name (eg. www.originatelabs.com )
	 */
	@Unique
	@HostName
	@NotEmpty
	@Column(nullable=false, unique=true)
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String value) {
		hostName = cleanHost(value);
	}
	
	@Unique
	@HostName
	@NotEmpty
	@Column(nullable=false, unique=true)
	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String value) {
		proxyName = cleanHost(value);
	}
	
	private String cleanHost(String host) {
		host = StringUtils.trimToEmpty(host);
		return StringUtils.removeEnd(host, "/");
	}
	
	@Min(0)
	@Column(nullable=false)
	public int getHostPort() {
		return hostPort;
	}
	
	public void setHostPort(int value) {
		hostPort = value;
	}
	
	@Min(0)
	@Column(nullable=false)
	public int getProxyPort() {
		return proxyPort;
	}
	
	public void setProxyPort(int value) {
		proxyPort = value;
	}
	
	@Required
	public void setContextRoot(String value) {
		value = StringUtils.trimToEmpty(value);
		if(!value.startsWith("/")) {
			value = "/" + value;
		}
		if(value.endsWith("/")) {
			value = StringUtils.removeEnd(value, "/");
		}
		contextRoot = value;
	}

	/**
	 * Creates a url to an exam for the given exam link
	 * @param examLink
	 * @return
	 */
	public URL urlFor(ExamLink examLink) {
		return doUrlFor(hostName, hostPort, "/quiz/index", "?exam=" + examLink.getGuid());
	}
	
	/**
	 * Creates a url to recover a user's password.
	 * @param token the token identifying the url
	 */
	public URL urlFor(PasswordRecoveryToken token) {
		return doUrlFor(
				hostName,
				hostPort, 
				"/admin/forgotPassword/confirm", 
				"?id=" + token.getGuid()
		);
	}
	
	private URL doUrlFor(String hostName, int port, String path, String queryString) {
		String s = "http://" + hostName + (port != 80 ? ":" + port : "") + contextRoot + path + queryString;
		try {
			return new URL(s);
		} catch(MalformedURLException e) {
			throw new IllegalStateException(e);
		}				
	}
	
	/**
	 * @return an unmodifiableSet of User objects.
	 */
	@Transient
	public Set<User> getUsers() {
		return Collections.unmodifiableSet(users);
	}
	
	/**
	 * Adds a user to the company
	 * @param user the user to add
	 */
	public void addUser(User user) {
		if(null == user) {
			return;
		}
		user.setCompany(this);
		users.add(user);
	}
	
	/**
	 * Removes a user with the given id.
	 * @param userId the id of the user to remove
	 */
	public void removeUser(Long userId) {
		if(null == userId) {
			return;
		}
		for(Iterator<User> it = users.iterator(); it.hasNext();) {
			User user = it.next();
			if(userId.equals(user.getId())) {
				user.setCompany(null);
				it.remove();
				break;
			}
		}
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="company")
	private Set<User> getUsersInternal() {
		return users;
	}
	
	private void setUsersInternal(Set<User> users) {
		if(users == null) {
			users = new HashSet<User>();
		}
		this.users = users;
	}

	/**
	 * @return the company's email alias (eg. the local part before '@')
	 */
	@Unique
	@NotEmpty
	@Column(nullable=false, unique=true)
	public String getEmailAlias() {
		return emailAlias;
	}

	public void setEmailAlias(String emailAlias) {
		this.emailAlias = StringUtils.trim(emailAlias);
	}
	
	@Transient
	public InternetAddress getFacilitatorEmailAddress() {
		try {
			return new InternetAddress(emailAlias + "@" + hostName); // TODO: user@host.domain.  where is domain?
		} catch(AddressException e) {
			throw new IllegalStateException(e);
		}
	}
	
	@OneToOne(
			optional=false, 
			cascade=CascadeType.ALL,
			fetch=FetchType.LAZY
	)
	public AutoInviteEmailTemplate getAutoInviteEmailTemplate() {
		return autoInviteEmailTemplate;
	}
	
	private void setAutoInviteEmailTemplate(AutoInviteEmailTemplate value) {
		autoInviteEmailTemplate = value;
	}
	
	@OneToOne(
			optional=false, 
			cascade=CascadeType.ALL,
			fetch=FetchType.LAZY
	)
	public ExamInviteEmailTemplate getExamInviteEmailTemplate() {
		return examInviteEmailTemplate;
	}
	
	private void setExamInviteEmailTemplate(ExamInviteEmailTemplate value) {
		examInviteEmailTemplate = value;
	}
	
	@OneToOne(
			optional=false, 
			cascade=CascadeType.ALL,
			fetch=FetchType.LAZY
	)
	public RejectCandidateEmailTemplate getRejectCandidateEmailTemplate() {
		return rejectCandidateEmailTemplate;
	}
	
	private void setRejectCandidateEmailTemplate(RejectCandidateEmailTemplate value) {
		rejectCandidateEmailTemplate = value;
	}
	
	@OneToOne(
			optional=false,
			cascade=CascadeType.ALL,
			fetch=FetchType.LAZY
	)
	public RequestResumeEmailTemplate getRequestResumeEmailTemplate() {
		return requestResumeEmailTemplate;
	}
	
	private void setRequestResumeEmailTemplate(RequestResumeEmailTemplate value) {
		requestResumeEmailTemplate = value;
	}

	@Column
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	/**
	 * @return the welcome text to display on the quiz portal
	 */
	@Column(nullable=false, columnDefinition="longtext")
	public String getWelcomeText() {
		return welcomeText;
	}
	
	public void setWelcomeText(String value) {
		welcomeText = StringUtils.trimToEmpty(value);
	}
	
	/**
	 * @return the completion text to display on the quiz portal
	 */
	@Column(nullable=false, columnDefinition="longtext")
	public String getCompletionText() {
		return completionText;
	}
	
	public void setCompletionText(String value) {
		completionText = StringUtils.trimToEmpty(value);
	}
}
