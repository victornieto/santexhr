package org.openapplicant.util;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Immutable helper class for calculating the limit and offset for a given
 * page number.
 */
public class Pagination {
	
	//========================================================================
	// MEMBERS
	//========================================================================
	private final int _perPage;
	
	private final int _pageNum;
	
	private final int _base;
	
	//========================================================================
	// FACTORY METHODS
	//========================================================================
	/**
	 * Creates a zero-based pagination object with a starting page number of 0
	 * and max results of 50 per page.
	 * 
	 * @return the zero-based pagination.
	 */
	public static Pagination zeroBased() {
		return new Pagination(0, 0, 50);
	}
	
	/**
	 * Creates a one-based pagination object with a starting page number of 1
	 * and max results of 50 per page.
	 * 
	 * @return the one-based pagination.
	 */
	public static Pagination oneBased() {
		return new Pagination(1, 1, 50);
	}
	
	//========================================================================
	// METHODS
	//========================================================================
	private Pagination(int base, int pageNum, int perPage) {
		Validate.isTrue(pageNum >= base, "Page numbers must not be less than " + base);
		Validate.isTrue(perPage >= 0, "Results per page must be >= 0");
		
		_base = base;
		_pageNum = pageNum;
		_perPage= perPage;
	}
	
	/**
	 * Constructs a new Pagination object using this object's page limit and 
	 * base.
	 * 
	 * @param pageNum the page number of the new Pagination object. 
	 * @throws IllegalArgumentException if pageNum < base
	 */
	public Pagination forPage(int pageNum) {
		return new Pagination(_base, pageNum, _perPage);
	}
	
	/**
	 * Constructs a new Pagination object using this object's page number and
	 * base.
	 * 
	 * @param perPage the max number of results per page.
	 * @return the new Pagination object.
	 * @throws IllegalArgumentException if perPage is less than 0
	 */
	public Pagination perPage(int perPage) {
		return new Pagination(_base, _pageNum, perPage);
	}
	
	/**
	 * @return the max number of items to retrieve per page.
	 */
	public int getLimit() {
		return _perPage;
	}
	
	/**
	 * @return the offset of the first result.
	 */
	public int getOffset() {
		return (_pageNum - _base) * _perPage;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Pagination)) {
			return false;
		}
		if(other == this) {
			return true;
		}
		Pagination rhs = (Pagination)other;
		return new EqualsBuilder()
						.append(_pageNum, rhs._pageNum)
						.append(_perPage, rhs._perPage)
						.append(_base, rhs._base)
						.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(83, 307)
						.append(_pageNum)
						.append(_perPage)
						.append(_base)
						.toHashCode();
	}
	
	@Override 
	public String toString() {
		return new ToStringBuilder(this)
						.append("base", _base)
						.append("pageNum",_pageNum)
						.append("perPage",_perPage)
						.toString();
	}

}
