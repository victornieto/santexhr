package org.openapplicant.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

@Entity
public class Response extends DomainObject {

	private long loadTimestamp = 0;
    
	private String content = "";
	
	private String keypressEvents = "";
	private String focusEvents = "";
	private String pasteEvents = "";
	
	private final ResponseMeasurements measurements = new ResponseMeasurements();
	
	/**
	 * Tracking cut and copy events is misleading because we can't record
	 * events while the candidate is away from focus.  If the candidate has
	 * pasted something without a cut/copy inside the window then we know
	 * they cut/copied something from outside and are thus a cheater.
	 */
	private boolean cutCopy = false;
	
	private String browserType = "";
	private String browserVersion = "";
	
	private Grade grade = new Grade();
	
	/**
	 * Getters and Setters for all the members above
	 */
	@OneToOne(
		cascade=CascadeType.ALL, 
		optional=false, 
		fetch=FetchType.LAZY
	)
	@JoinColumn(nullable=false)
	public Grade getGrade() {
		return grade;
	}
	
	private void setGrade(Grade grade) {
		this.grade = grade;
	}
	
	@Transient
	public boolean isNotGraded() {
		return grade.isBlank();
	}

	@NotNull
	@Column(columnDefinition="longtext", nullable=false) 
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = StringUtils.defaultString(content);
	}
	
	@NotNull
	@Column(columnDefinition="longtext", nullable=false) 
	public String getKeypressEvents() {
		return keypressEvents;
	}
	
	public void setKeypressEvents(String keypressEvents) {
		this.keypressEvents = StringUtils.defaultString(keypressEvents);
	}
	
	@NotNull
	@Column(columnDefinition="text", nullable=false) 
	public String getFocusEvents() {
		return focusEvents;
	}
	
	public void setFocusEvents(String focusEvents) {
		this.focusEvents = StringUtils.defaultString(focusEvents);
	}
	
	@NotNull
	@Column(columnDefinition="text", nullable=false) 
	public String getPasteEvents() {
		return pasteEvents;
	}
	
	public void setPasteEvents(String pasteEvents) {
		this.pasteEvents = StringUtils.defaultString(pasteEvents);
	}
	
	@Column(nullable=false)       
	public long getLoadTimestamp() {
		return loadTimestamp;
	}
	
	public void setLoadTimestamp(long loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
	}	
	
	// FIXME: make measurements embedded
	@Transient
	public ResponseMeasurements getMeasurements() {
		return measurements;
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getHesitationTime() {
		return measurements.getHesitationTime();
	}
	public void setHesitationTime(long hesitationTime) {
		measurements.setHesitationTime(hesitationTime);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getTypingTime() {
		return measurements.getTypingTime();
	}
	public void setTypingTime(long typingTime) {
		measurements.setTypingTime(typingTime);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getReviewingTime() {
		return measurements.getReviewingTime();
	}
	public void setReviewingTime(long reviewingTime) {
		measurements.setReviewingTime(reviewingTime);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getFocusTime() {
		return measurements.getFocusTime();
	}
	public void setFocusTime(long focusTime) {
		measurements.setFocusTime(focusTime);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getAwayTime() {
		return measurements.getAwayTime();
	}
	public void setAwayTime(long awayTime) {
		measurements.setAwayTime(awayTime);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getTotalTime() {
		return measurements.getTotalTime();
	}
	public void setTotalTime(long totalTime) {
		measurements.setTotalTime(totalTime);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getKeyPresses() {
		return measurements.getKeyPresses();
	}
	public void setKeyPresses(long keyPresses) {
		measurements.setKeyPresses(keyPresses);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getErasePresses() {
		return measurements.getErasePresses();
	}
	public void setErasePresses(long erasePresses) {
		measurements.setErasePresses(erasePresses);
	}

	@Min(0)
	@Column(nullable=false)   
	public long getPastePresses() {
		return measurements.getPastePresses();
	}
	public void setPastePresses(long pastePresses) {
		measurements.setPastePresses(pastePresses);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getKeyChars() {
		return measurements.getKeyChars();
	}
	public void setKeyChars(long keyChars) {
		measurements.setKeyChars(keyChars);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getEraseChars() {
		return measurements.getEraseChars();
	}
	public void setEraseChars(long eraseChars) {
		measurements.setEraseChars(eraseChars);
	}

	@Min(0)
	@Column(nullable=false)   
	public long getPasteChars() {
		return measurements.getPasteChars();
	}
	public void setPasteChars(long pasteChars) {
		measurements.setPasteChars(pasteChars);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getFocusChanges() {
		return measurements.getFocusChanges();
	}
	public void setFocusChanges(long focusChanges) {
		measurements.setFocusChanges(focusChanges);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getWordCount() {
		return measurements.getWordCount();
	}
	public void setWordCount(long wordCount) {
		measurements.setWordCount(wordCount);
	}
	
	@Min(0)
	@Column(nullable=false)
	public double getWordsPerMinute() {
	    return measurements.getWordsPerMinute();
	}
	public void setWordsPerMinute(double wordsPerMinute) {
	    measurements.setWordsPerMinute(wordsPerMinute);
	}
	
	@Min(0)
	@Column(nullable=false)   
	public long getLineCount() {
	    return measurements.getLineCount();
	}
	public void setLineCount(long lineCount) {
	    measurements.setLineCount(lineCount);
	}
	
	@Min(0)
	@Column(nullable=false)
	public double getLinesPerHour() {
	    return measurements.getLinesPerHour();
	}
	public void setLinesPerHour(double linesPerHour) {
	    measurements.setLinesPerHour(linesPerHour);
	}
	
	@Column(nullable=false)   
	public boolean isCutCopy() {
		return cutCopy;
	}
	public void setCutCopy(boolean cutCopy) {
		this.cutCopy = cutCopy;
	}
	
	@NotNull
	@Column(nullable=false)   
	public String getBrowserType() {
	    return browserType;
	}
	public void setBrowserType(String browserType) {
	    this.browserType = browserType;
	}
	
	@NotNull
	@Column(nullable=false)
	public String getBrowserVersion() {
	    return browserVersion;
	}
	public void setBrowserVersion(String browserVersion) {
	    this.browserVersion = browserVersion;
	}
}