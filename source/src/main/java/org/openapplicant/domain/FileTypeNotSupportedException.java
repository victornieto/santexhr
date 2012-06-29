package org.openapplicant.domain;

/**
 * Exception thrown when a document's file type is not supported
 */
public class FileTypeNotSupportedException extends Exception {
	
	public FileTypeNotSupportedException(String msg) {
		super(msg);
	}
	
	public FileTypeNotSupportedException(String msg, Throwable t) {
		super(msg, t);
	}

}
