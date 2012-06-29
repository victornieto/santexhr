package org.openapplicant.domain;

import org.openapplicant.domain.FileAttachment;
import org.openapplicant.domain.Resume;

/**
 * Builds test resume objects.
 */
public class ResumeBuilder {
	
	private String fileType = FileAttachment.TXT;
	
	public ResumeBuilder withFileType(String value) {
		fileType = value; 
		return this;
	}
	
	public Resume build() {
		try {
			Resume resume = new Resume("foo".getBytes(), fileType);
			return resume;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
