package org.openapplicant.domain.email;

public enum PlaceHolder {
	
	CANDIDATE_FIRST_NAME ("Candidate's first name", "<<First Name>>"),
	
	CANDIDATE_LAST_NAME("Candidate's last name", "<<Last Name>>"),
	
	CANDIDATE_FULL_NAME("Candidate's full name", "<<Full Name>>"),
	
	TODAY("Today's date", "<<Today>>"),
	
	EXAM_LINK("Exam link", "<<Exam Link>>"),
	
	COMPANY_NAME("Company name", "<<Company Name>>");
	
	private final String description;
	
	private final String symbol;
	
	private PlaceHolder(String description, String symbol) {
		this.description = description;
		this.symbol = symbol;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * @return the placeholder's symbol
	 */
	public String toString() {
		return symbol;
	}
}
