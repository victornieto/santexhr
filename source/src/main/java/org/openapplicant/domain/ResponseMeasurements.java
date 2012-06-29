package org.openapplicant.domain;

// TODO: make this class immutable and embedded
public class ResponseMeasurements {

	private long totalTime = 0;
	
	private long hesitationTime = 0;
	private long reviewingTime = 0;
	private long typingTime = 0;
	//hesitation + reviewing + typing = total
	
	private long focusTime = 0;
	private long awayTime = 0;
	//focus + away = total
	
	private long keyPresses = 0;
	private long erasePresses = 0;
	private long pastePresses = 0;
	
	private long keyChars = 0;
	private long eraseChars = 0;
	private long pasteChars = 0;
	
	private long focusChanges = 0;
	
	private long wordCount = 0;
	private double wordsPerMinute = 0;
	
	private long lineCount = 0;
	private double linesPerHour = 0;
	
	public long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	
	public long getHesitationTime() {
		return hesitationTime;
	}
	public void setHesitationTime(long hesitationTime) {
		this.hesitationTime = hesitationTime;
	}
	
	public long getReviewingTime() {
		return reviewingTime;
	}
	public void setReviewingTime(long reviewingTime) {
		this.reviewingTime = reviewingTime;
	}
	
	public long getTypingTime() {
		return typingTime;
	}
	public void setTypingTime(long typingTime) {
		this.typingTime = typingTime;
	}
	
	public long getFocusTime() {
		return focusTime;
	}
	public void setFocusTime(long focusTime) {
		this.focusTime = focusTime;
	}
	
	public long getAwayTime() {
		return awayTime;
	}
	public void setAwayTime(long awayTime) {
		this.awayTime = awayTime;
	}
	
	public long getKeyPresses() {
		return keyPresses;
	}
	public void setKeyPresses(long keyPresses) {
		this.keyPresses = keyPresses;
	}
	
	public long getErasePresses() {
		return erasePresses;
	}
	public void setErasePresses(long erasePresses) {
		this.erasePresses = erasePresses;
	}
	
	public long getPastePresses() {
		return pastePresses;
	}
	public void setPastePresses(long pastePresses) {
		this.pastePresses = pastePresses;
	}
	
	public long getKeyChars() {
		return keyChars;
	}
	public void setKeyChars(long keyChars) {
		this.keyChars = keyChars;
	}
	
	public long getEraseChars() {
		return eraseChars;
	}
	public void setEraseChars(long eraseChars) {
		this.eraseChars = eraseChars;
	}
	
	public long getPasteChars() {
		return pasteChars;
	}
	public void setPasteChars(long pasteChars) {
		this.pasteChars = pasteChars;
	}
	
	public long getFocusChanges() {
		return focusChanges;
	}
	public void setFocusChanges(long focusChanges) {
		this.focusChanges = focusChanges;
	}
	
	public long getWordCount() {
		return wordCount;
	}
	public void setWordCount(long wordCount) {
		this.wordCount = wordCount;
	}
	
	public double getWordsPerMinute() {
		return wordsPerMinute;
	}
	public void setWordsPerMinute(double wordsPerMinute) {
		this.wordsPerMinute = wordsPerMinute;
	}
	
	public long getLineCount() {
		return lineCount;
	}
	public void setLineCount(long lineCount) {
		this.lineCount = lineCount;
	}
	
	public double getLinesPerHour() {
		return linesPerHour;
	}
	public void setLinesPerHour(double linesPerHour) {
		this.linesPerHour = linesPerHour;
	}
	
	/**
	 * @param measurement the measurement to add
	 * @return a new ResponseMeasurements representing the summation of 
	 * this measurement and another
	 */
	public ResponseMeasurements add(ResponseMeasurements response) {
			ResponseMeasurements result = new ResponseMeasurements();
			result.totalTime = this.totalTime + response.getTotalTime();
		    
		    result.hesitationTime = this.hesitationTime + response.getHesitationTime();
		    result.reviewingTime = this.reviewingTime + response.getReviewingTime();
		    result.typingTime = this.typingTime + response.getTypingTime();
		    
		    result.focusTime = this.focusTime + response.getFocusTime();
		    result.awayTime = this.awayTime + response.getAwayTime();
		    
		    result.keyPresses = this.keyPresses + response.getKeyPresses();
		    result.erasePresses = this.erasePresses + response.getErasePresses();
		    result.pastePresses = this.pastePresses + response.getPastePresses();
		    
		    result.keyChars = this.keyChars + response.getKeyChars();
		    result.eraseChars = this.eraseChars + response.getEraseChars();
		    result.pasteChars = this.pasteChars + response.getPasteChars();
		    
		    result.focusChanges = this.focusChanges + response.getFocusChanges();
		    
		    result.wordCount = this.wordCount + response.getWordCount();
		    if(result.totalTime > 0) {
		    	result.wordsPerMinute = (result.wordCount*1000*60)/result.totalTime; // FIXME: this calculation could be done in getWordsPerMinute
		    } else {
		    	result.wordsPerMinute = 0;
		    }
		    
		    result.lineCount = this.lineCount + response.getLineCount();
		    if(result.totalTime > 0) {
		    	result.linesPerHour = (result.lineCount*1000*60*60)/result.totalTime;  // FIXME: this calculation could be done in getLinesPerHour
		    } else {
		    	result.linesPerHour = 0;
		    }
		    return result;
	}
	
	
}
