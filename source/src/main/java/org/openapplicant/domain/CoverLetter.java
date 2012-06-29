package org.openapplicant.domain;

import java.io.IOException;
import java.util.zip.DataFormatException;

import javax.persistence.Entity;

import org.openapplicant.policy.NeverCall;


@Entity
public class CoverLetter extends FileAttachment {
	
	/**
	 * Creates a new cover letter
	 * 
	 * @param content the cover letter's byte content
	 * @param fileType the cover letters file extension (eg. "doc", "pdf", "txt")
	 * @throws IOException if there is an error reading the byte content
	 * @throws FileTypeNotSupportedException if fileType is not supported
	 */
	public CoverLetter(byte[] content, String fileType) 
		throws IOException, FileTypeNotSupportedException {
		super(content, fileType);
	}
	
	@NeverCall
	CoverLetter(){}

}
