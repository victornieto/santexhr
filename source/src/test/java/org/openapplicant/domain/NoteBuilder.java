package org.openapplicant.domain;

import org.openapplicant.domain.Note;
import org.openapplicant.domain.User;

/**
 * Builds note Test objects.
 */
public class NoteBuilder {
	
	private User user = new UserBuilder().build();
	
	private String body = "foo";
	
	public NoteBuilder withUser(User user) {
		this.user = user;
		return this;
	}
	
	public NoteBuilder withBody(String value) {
		body = value;
		return this;
	}
	
	public Note build() {
		Note note = new Note(user, body);
		return note;
	}

}
