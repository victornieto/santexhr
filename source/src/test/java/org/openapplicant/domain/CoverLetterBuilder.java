package org.openapplicant.domain;

import org.openapplicant.domain.CoverLetter;
import org.openapplicant.domain.FileAttachment;

public class CoverLetterBuilder {
	
	public CoverLetter build() {
		try {
			CoverLetter result = new CoverLetter("foo".getBytes(), FileAttachment.TXT);
			return result;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
