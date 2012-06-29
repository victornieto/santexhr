package org.openapplicant.service.facilitator;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openapplicant.domain.CoverLetter;
import org.openapplicant.domain.FileAttachment;
import org.openapplicant.domain.FileTypeNotSupportedException;
import org.openapplicant.domain.Resume;

import java.io.IOException;
import java.util.Collection;

/**
 * Strategy for resolving a resume/cover letter from 
 * an email message
 */
public class DocumentResolver {
	
	private static final Log log = LogFactory.getLog(DocumentResolver.class);
	
	private Resume resume;
	
	private CoverLetter coverLetter;
	
	public DocumentResolver(MessageReader reader) {
		this(reader.getBodyText(), reader.getAttachments());
	}
	
	/**
	 * Package visible for testing
	 */
	DocumentResolver(String messageBody, Collection<Attachment> attachments) {
		if(containsResume(attachments)) {
			resume = findResume(attachments);
		}
		if(containsCoverLetter(attachments)) {
			coverLetter= findCoverLetter(attachments);
		}
		
		// default missing document to message body, give resume priority
		try {
			if(resume == null && StringUtils.isNotBlank(messageBody)) {
				resume = new Resume(messageBody.getBytes(),FileAttachment.TXT);
			}
			else if(coverLetter == null && StringUtils.isNotBlank(messageBody)) {
				coverLetter = new CoverLetter(messageBody.getBytes(), FileAttachment.TXT);
			}
		} 
		catch(FileTypeNotSupportedException e) {
			log.error(e);
		}
		catch(IOException e) {
			log.error(e);
		}
	}
	
	/**
	 * @return the email's resume or null if none was provided
	 */
	public Resume getResume() {
		return resume;
	}
	
	/**
	 * @return true if a resume was provided
	 */
	public boolean hasResume() {
		return resume != null;
	}
	
	/**
	 * @return the email's cover letter or null if none was provided
	 */
	public CoverLetter getCoverLetter() {
		return coverLetter;
	}
	
	/**
	 * @return true if a cover letter was provided
	 */
	public boolean hasCoverLetter() {
		return coverLetter != null;
	}
	
	private boolean containsResume(Collection<Attachment> attachments) {
		for(Attachment each : attachments) {
			if(isResume(each)) {
				return true;
			}
		}
		return false;
	}
	
	private Resume findResume(Collection<Attachment> attachments) {
		for(Attachment each : attachments) {
			if(isResume(each)) {
				try {
					return new Resume(each.getContent(),FilenameUtils.getExtension(each.getFileName()));
				} 
				catch(FileTypeNotSupportedException e) {
					log.error(e);
					return null;
				}
				catch(IOException e) {
					log.error(e);
					return null;
				}
			}
		}
		return null;
	}
	
	private boolean isResume(Attachment attachment) {
		return StringUtils.containsIgnoreCase(attachment.getFileName(), "resume"); // FIXME: weak algorithm
	}
	
	private boolean containsCoverLetter(Collection<Attachment> attachments) {
		for(Attachment each : attachments) {
			if(isCoverLetter(each)) {
				return true;
			}
		}
		return false;
	}
	
	private CoverLetter findCoverLetter(Collection<Attachment> attachments) {
		for(Attachment each : attachments) {
			if(isCoverLetter(each)) {
				try {
					return new CoverLetter(each.getContent(), FilenameUtils.getExtension(each.getFileName()));
				} 
				catch(FileTypeNotSupportedException e) {
					log.error(e);
					return null;
				}
				catch(IOException e) {
					log.error(e);
					return null;
				}
			}
		}
		return null;
	}
	
	private boolean isCoverLetter(Attachment attachment) {
		return StringUtils.containsIgnoreCase(attachment.getFileName(), "cover") &&
			StringUtils.containsIgnoreCase(attachment.getFileName(), "letter"); // FIXME: weak algorithm
	}

}
