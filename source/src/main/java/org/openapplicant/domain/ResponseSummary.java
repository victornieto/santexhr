package org.openapplicant.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

@Embeddable
public class ResponseSummary {

	private long loadTimestamp = 0;
	
	private ResponseMeasurements measurements = new ResponseMeasurements();
	
	private	boolean browserChange = false;
	private String browserType = null;
	private String browserVersion =  null;
	
	public void addResponse(Response response) {
	    if(0 == loadTimestamp)
		loadTimestamp = response.getLoadTimestamp();
	    
	    measurements = measurements.add(response.getMeasurements());
	    
		if(!browserChange) {
			if(null != browserType ) {
				if(!browserType.equals(response.getBrowserType()) ||
				   !browserVersion.equals(response.getBrowserVersion())) 
					browserChange = true;
			} else {
				browserType = response.getBrowserType();
				browserVersion = response.getBrowserVersion();
			}
		}
	}
	
	@NotNull
	@Column(nullable=false)       
	public long getLoadTimestamp() {
		return loadTimestamp;
	}
	
	public void setLoadTimestamp(long loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
	}	

	// FIXME: make measurements embedded to avoid duplicate column definitions.
	@Transient
	public ResponseMeasurements getMeasurements() {
		return measurements;
	}
	
	@NotNull
	@Column(nullable=false, columnDefinition= "bigint default 0")   
	public long getHesitationTime() {
		return measurements.getHesitationTime();
	}
	public void setHesitationTime(long hesitationTime) {
		measurements.setHesitationTime(hesitationTime);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getTypingTime() {
		return measurements.getTypingTime();
	}
	public void setTypingTime(long typingTime) {
		measurements.setTypingTime(typingTime);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getReviewingTime() {
		return measurements.getReviewingTime();
	}
	public void setReviewingTime(long reviewingTime) {
		measurements.setReviewingTime(reviewingTime);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getFocusTime() {
		return measurements.getFocusTime();
	}
	public void setFocusTime(long focusTime) {
		measurements.setFocusTime(focusTime);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getAwayTime() {
		return measurements.getAwayTime();
	}
	public void setAwayTime(long awayTime) {
		measurements.setAwayTime(awayTime);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getTotalTime() {
		return measurements.getTotalTime();
	}
	public void setTotalTime(long totalTime) {
		measurements.setTotalTime(totalTime);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getKeyPresses() {
		return measurements.getKeyPresses();
	}
	public void setKeyPresses(long keyPresses) {
		measurements.setKeyPresses(keyPresses);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getErasePresses() {
		return measurements.getErasePresses();
	}
	public void setErasePresses(long erasePresses) {
		measurements.setErasePresses(erasePresses);
	}

	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getPastePresses() {
		return measurements.getPastePresses();
	}
	public void setPastePresses(long pastePresses) {
		measurements.setPastePresses(pastePresses);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getKeyChars() {
		return measurements.getKeyChars();
	}
	public void setKeyChars(long keyChars) {
		measurements.setKeyChars(keyChars);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getEraseChars() {
		return measurements.getEraseChars();
	}
	public void setEraseChars(long eraseChars) {
		measurements.setEraseChars(eraseChars);
	}

	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getPasteChars() {
		return measurements.getPasteChars();
	}
	public void setPasteChars(long pasteChars) {
		measurements.setPasteChars(pasteChars);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getFocusChanges() {
		return measurements.getFocusChanges();
	}
	public void setFocusChanges(long focusChanges) {
		measurements.setFocusChanges(focusChanges);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getWordCount() {
		return measurements.getWordCount();
	}
	public void setWordCount(long wordCount) {
		measurements.setWordCount(wordCount);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "double default 0")
	public double getWordsPerMinute() {
	    return measurements.getWordsPerMinute();
	}
	public void setWordsPerMinute(double wordsPerMinute) {
	    measurements.setWordsPerMinute(wordsPerMinute);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "bigint default 0")   
	public long getLineCount() {
		return measurements.getLineCount();
	}
	public void setLineCount(long lineCount) {
		measurements.setLineCount(lineCount);
	}
	
	@NotNull
	@Column(nullable=false,columnDefinition= "double default 0")
	public double getLinesPerHour() {
	    return measurements.getLinesPerHour();
	}
	public void setLinesPerHour(double linesPerHour) {
	    measurements.setLinesPerHour(linesPerHour);
	}
	
	@Column(nullable=false)   
	public boolean isBrowserChange() {
		return browserChange;
	}
	public void setBrowserChange(boolean browserChange) {
		this.browserChange = browserChange;
	}
	
	@Column   
	public String getBrowserType() {
	    return browserType;
	}
	public void setBrowserType(String browserType) {
	    this.browserType = browserType;
	}
	
	@Column
	public String getBrowserVersion() {
	    return browserVersion;
	}
	public void setBrowserVersion(String browserVersion) {
	    this.browserVersion = browserVersion;
	}
}