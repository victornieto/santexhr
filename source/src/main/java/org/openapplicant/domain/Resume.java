package org.openapplicant.domain;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.zip.DataFormatException;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.openapplicant.policy.NeverCall;


@Entity
public class Resume extends FileAttachment {
	
	private BigDecimal screeningScore;
	
	/**
	 * Creates a new resume
	 * @param content the resume's byte content
	 * @param fileType the file extension (eg. "pdf")
	 * @throws IOException if there is an error reading the content
	 * @throws FileTypeNotSupportedException if fileType is not supported
	 */
	public Resume(byte[] content, String fileType)
		throws IOException, FileTypeNotSupportedException {
		super(content, fileType);
	}
	
	@NeverCall
	Resume(){}
	
	@Column(nullable=true)
	public BigDecimal getScreeningScore() {
		return screeningScore;
	}
	
	public void setScreeningScore(BigDecimal value) {
		screeningScore = value;
	}

}
