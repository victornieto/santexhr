package org.openapplicant.domain;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.openapplicant.policy.NeverCall;
import org.springframework.util.Assert;

import javax.persistence.*;


/**
 * Represents a user's comment.
 */
@Entity
public class Note extends DomainObject {

	private String body;
	
	private User author;
	
	/**
	 * Constructs a new note
	 * 
	 * @param author the note's author
	 * @param body the note's content
	 */
	public Note(User author, String body) {
		Assert.notNull(author);
		this.body = StringUtils.trimToEmpty(body);
		this.author = author;
	}
	
	@NeverCall
	public Note() {}
	
	/**
	 * @return the user's comment
	 */
	@NotEmpty
    @Length(max = 3000)
	@Column(nullable=false, columnDefinition="longtext")
	public String getBody() {
		return body;
	}
	public void setBody(String value) {
		body = StringUtils.trimToEmpty(value);
	}
	
	/**
	 * @return the note's author
	 */
	@NotNull
	@ManyToOne(
			optional=false, 
			cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
			fetch=FetchType.LAZY
	)
	@JoinColumn(nullable=false, updatable=false)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public User getAuthor() {
		return author;
	}
	private void setAuthor(User value) {
		author = value;
	}
}
