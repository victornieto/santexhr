package org.openapplicant.service.facilitator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.mail.MessagingException;
import javax.mail.Part;
import java.io.IOException;

/**
 * Helper class for normalizing a message attachment.
 */
class Attachment {
	
	private final String fileName;
	
	private final byte[] content;
	
	public Attachment(Part part) throws IOException, MessagingException {
		this(IOUtils.toByteArray(part.getInputStream()), part.getFileName());
	}
	
	private Attachment(byte[] content, String fileName) {
		this.fileName = StringUtils.trimToEmpty(fileName);
		if(ArrayUtils.isEmpty(content)) {
			this.content = ArrayUtils.EMPTY_BYTE_ARRAY;
		} else {
            this.content = content;
        }
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public byte[] getContent() {
		return content;
	}

}
