package org.openapplicant.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openapplicant.domain.FileAttachment;

public class FileAttachmentTest {
	
	@Test
	public void fileType() {
		FileAttachment f = new ResumeBuilder()
									.withFileType("txt")
									.build();
		
		assertEquals("txt", f.getFileType());
		
		f = new ResumeBuilder()
					.withFileType(".txt")
					.build();
		assertEquals("txt", f.getFileType());
		
		f = new ResumeBuilder()
					.withFileType(".TXT")
					.build();
		assertEquals("TXT", f.getFileType());
	}

}
