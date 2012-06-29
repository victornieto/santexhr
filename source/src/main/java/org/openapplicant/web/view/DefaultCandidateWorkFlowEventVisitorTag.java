package org.openapplicant.web.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.event.AddNoteToCandidateEvent;
import org.openapplicant.domain.event.CandidateCreatedByUserEvent;
import org.openapplicant.domain.event.CandidateCreatedEvent;
import org.openapplicant.domain.event.CandidateStatusChangedEvent;
import org.openapplicant.domain.event.CandidateWorkFlowEvent;
import org.openapplicant.domain.event.CreateExamLinkForCandidateEvent;
import org.openapplicant.domain.event.FacilitatorReceivedEmailEvent;
import org.openapplicant.domain.event.FacilitatorRejectedResumeEvent;
import org.openapplicant.domain.event.FacilitatorRequestedResumeEvent;
import org.openapplicant.domain.event.FacilitatorSentExamLinkEvent;
import org.openapplicant.domain.event.ICandidateWorkFlowEventVisitor;
import org.openapplicant.domain.event.SittingCompletedEvent;
import org.openapplicant.domain.event.SittingCreatedEvent;
import org.openapplicant.domain.event.SittingGradedEvent;
import org.openapplicant.domain.event.UserAttachedCoverLetterEvent;
import org.openapplicant.domain.event.UserAttachedResumeEvent;
import org.openapplicant.domain.event.UserSentExamLinkEvent;
import org.openapplicant.domain.link.ExamLink;
import org.openapplicant.util.Strings;
import org.springframework.web.util.HtmlUtils;


/**
 * Default helper class for rendering a collection of CandidateWorkFlowEvents.
 */
public class DefaultCandidateWorkFlowEventVisitorTag  extends TagSupport 
		implements ICandidateWorkFlowEventVisitor {
	
	private Collection<CandidateWorkFlowEvent> events = 
		new ArrayList<CandidateWorkFlowEvent>();
	
	private boolean showIcon = true;
	
	private Integer maxEvents;
	
	private boolean abbreviateNotes = false;
	
	private boolean showExamLinkUrl = true;
	
	public void setEvents(Collection<CandidateWorkFlowEvent> events) {
		if(null == events) {
			return;
		}
		this.events = events;
	}
	
	/**
	 * @param value true if this tag should render an icon for each event
	 */
	public void setShowIcon(boolean value) {
		showIcon = value;
	}
	
	/**
	 * @param value the maximum number of events to show
	 */
	public void setMaxEvents(Integer value) {
		maxEvents = value;
	}
	
	/**
	 * @param true if the notes body should be abbreviated
	 */
	public void setAbbreviateNotes(boolean value) {
		abbreviateNotes = value;
	}
	
	/**
	 * @param true if the exam link url should be shown
	 */
	public void setShowExamLinkUrl(boolean value) {
		showExamLinkUrl = value;
	}
	
	@Override
	public int doStartTag() throws JspException {
		int i=0;
		for(CandidateWorkFlowEvent each : events) {
			if(maxEvents != null && i == maxEvents.intValue()) {
				break;
			}
			each.accept(this);
			i++;
		}
		return Tag.SKIP_BODY;
	}

	public void visit(UserAttachedResumeEvent event) {
		new HtmlTemplate()
				.icon(img("resume.jpg"))
				.date(event.getEntityInfo().getCreatedDate())
				.user(HtmlUtils.htmlEscape(event.getUser().getName().getFullName()))
				.action("uploaded resume")
				.write();
	}
	
	public void visit(UserAttachedCoverLetterEvent event) {
		new HtmlTemplate()
				.icon(img("coverletter.jpg"))
				.date(event.getEntityInfo().getCreatedDate())
				.user(HtmlUtils.htmlEscape(event.getUser().getName().getFullName()))
				.action("uploaded cover letter")
				.write();
	}

	public void visit(AddNoteToCandidateEvent event) {
		new HtmlTemplate()
				.icon(img("note.jpg"))
				.date(event.getEntityInfo().getCreatedDate())
				.user(HtmlUtils.htmlEscape(event.getNote().getAuthor().getName().getFullName()))
				.action("added note:")
				.description(
						HtmlUtils.htmlEscape(
							abbreviateNotes? 
								StringUtils.abbreviate(event.getNote().getBody(), 20) :
								event.getNote().getBody()
							
						)
				)
				.write();
	}

	public void visit(CandidateCreatedEvent event) {
		new HtmlTemplate()
				.icon(img("candidate_added.jpg"))
				.date(event.getEntityInfo().getCreatedDate())
				.action("Santex HR created candidate")
				.write();
	}
	
	public void visit(CandidateCreatedByUserEvent event) {
		new HtmlTemplate()
				.icon(img("candidate_added.jpg"))
				.date(event.getEntityInfo().getCreatedDate())
				.user(HtmlUtils.htmlEscape(event.getUser().getName().getFullName()))
				.action("created candidate")
				.write();
	}

	public void visit(CandidateStatusChangedEvent event) {
		new HtmlTemplate()
				.icon(img("activated.jpg")) 
				.date(event.getEntityInfo().getCreatedDate())
				.user(HtmlUtils.htmlEscape(event.getUser().getName().getFullName()))
				.action(event.getStatus().isArchived() ? "Archived:" : "Activated:")
				.description("[",HtmlUtils.htmlEscape(Strings.humanize(event.getStatus().toString())),"]")
				.write();
	}
	
	public void visit(CreateExamLinkForCandidateEvent event) {
		new HtmlTemplate()
				.icon(linkToExamPortal(event.getExamLink(),img("exam_link.jpg")))
				.date(event.getEntityInfo().getCreatedDate())
				.user(event.getUser().getName().getFullName())
				.action("created link:")
				.description(showExamLinkUrl ? linkToExamPortal(event.getExamLink()) : "")
				.write();
	}

	public void visit(SittingGradedEvent event) {
		new HtmlTemplate()
				.icon(linkToSitting(event.getSitting(), img("exam.jpg")))
				.date(event.getEntityInfo().getCreatedDate())
				.user(HtmlUtils.htmlEscape(event.getUser().getName().getFullName()))
				.action("graded exam:")
				.description(
						linkToSitting(event.getSitting()) + 
						". Score: " + event.getSittingScore()
				)
				.write();
	}

	public void visit(SittingCreatedEvent event) {
		new HtmlTemplate()
				.icon(linkToSitting(event.getSitting(), img("exam.jpg")))
				.date(event.getEntityInfo().getCreatedDate())
				.action("Candidate started exam:")
				.description(linkToSitting(event.getSitting()))
				.write();
	}
	
	public void visit(SittingCompletedEvent event) {
		new HtmlTemplate()
				.icon(linkToSitting(event.getSitting(), img("exam.jpg")))
				.date(event.getEntityInfo().getCreatedDate())
				.action("Candidate finished exam:")
				.description(linkToSitting(event.getSitting()))
				.write();
	}

	public void visit(FacilitatorReceivedEmailEvent event) {
		new HtmlTemplate()
				.icon(img("fac_rec_email.jpg"))
				.date(event.getEntityInfo().getCreatedDate())
				.action("Santex HR received email")
				.write();
	}

	public void visit(FacilitatorRejectedResumeEvent event) {
		new HtmlTemplate()
				.icon(img("fac_reject_resume.jpg"))
				.date(event.getEntityInfo().getCreatedDate())
				.action("Santex HR rejected resume")
				.write();
	}

	public void visit(FacilitatorRequestedResumeEvent event) {
		new HtmlTemplate()
				.icon(img("fac_rec_resume.jpg"))
				.date(event.getEntityInfo().getCreatedDate())
				.action("Santex HR sent a resume request")
				.write();
	}

	public void visit(FacilitatorSentExamLinkEvent event) {
		new HtmlTemplate()
				.icon(linkToExamPortal(event.getExamLink(),img("fac_sent_exam_link.jpg")))
				.date(event.getEntityInfo().getCreatedDate())
				.action("Santex HR sent exam link:")
				.description(showExamLinkUrl ? linkToExamPortal(event.getExamLink()) : "")
				.write();
	}
	
	public void visit(UserSentExamLinkEvent event) {
		new HtmlTemplate()
				.icon(linkToExamPortal(event.getExamLink(), img("fac_sent_exam_link.jpg"))) // TODO: need image
				.date(event.getEntityInfo().getCreatedDate())
				.user(HtmlUtils.htmlEscape(event.getUser().getName().getFullName()))
				.action(" sent exam link:")
				.description(showExamLinkUrl ? linkToExamPortal(event.getExamLink()) : "")
				.write();
	}
	
	private String linkToSitting(Sitting sitting) {
		return linkToSitting(sitting, HtmlUtils.htmlEscape(sitting.getExam().getName()));
	}
	
	private String linkToSitting(Sitting sitting, String innerHtml) {
		return new StringBuilder()
					.append("<a href='").append(getContextPath()).append("/admin/results/exam?s=").append(sitting.getId()).append("'>")
						.append(innerHtml)
					.append("</a>")
					.toString();
	}
	
	private String linkToExamPortal(ExamLink examLink) {
		return linkToExamPortal(examLink, examLink.getName());
	}
	
	private String linkToExamPortal(ExamLink examLink, String innerHtml) {
		return new StringBuilder()
						.append("<a href='").append(examLink.getUrl()).append("'>")
							.append(innerHtml)
						.append("</a>")
						.toString();
	}
	
	private String img(String file) {
		return new StringBuilder()
			.append("<img src='")
				.append(getContextPath()).append("/img/history/").append(file)
			.append("'/>")
			.toString();
	}
	
	private String getContextPath() {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		return request.getContextPath();
	}
	
	private class HtmlTemplate {
		private String icon;
		private Calendar date;
		private String action = "";
		private String description = "";
		private String user = "";
		
		public HtmlTemplate icon(String html) {
			icon = html;
			return this;
		}
		
		public HtmlTemplate date(Calendar date) {
			this.date = date;
			return this;
		}
		
		public HtmlTemplate action(String...values) {
			this.action = StringUtils.join(values);
			return this;
		}
		
		public HtmlTemplate description(String... values) {
			description = StringUtils.join(values);
			return this;
		}
		
		public HtmlTemplate user(String value) {
			user = value;
			return this;
		}

		private void write() {
			StringBuilder sb = new StringBuilder();
				sb.append("<div class='event'>");
					if(showIcon) {
						sb.append(icon);
					}
					sb.append("<div class='text'>");
						sb.append("<span class='date'>").append(format(date)).append("</span>");
						sb.append("<span class='user'>").append(user).append("</span>");
						sb.append("<span class='action'>").append(action).append("</span>");
						sb.append("<span class='description'>").append(description).append("</span>");
					sb.append("</div>");
				sb.append("</div>");
			try {
				pageContext.getOut().write(sb.toString());
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		private String format(Calendar cal) {
			TimeZone timezone = (TimeZone)Config.get(pageContext.getSession(), Config.FMT_TIME_ZONE);
			return DateFormatUtils.format(cal.getTime(),"MM/dd/yyyy h:mm a", timezone);
		}
	}
}
