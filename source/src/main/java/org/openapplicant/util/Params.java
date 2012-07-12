package org.openapplicant.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Wraps a Map with a fluid interface. 
 */
public class Params {
	
	private final Map<String, Object> map = new HashMap<String, Object>();
	
	public Params() {}
	
	/**
	 * Constructs a new Params with an intitial parameter
	 * @param name the parameter name
	 * @param value the parameter value
	 */
	public Params(String name, Object value) {
		add(name, value);
	}
	
	/**
	 * Adds a new parameter value
	 * @param name the parameter name
	 * @param value the parameter value
	 * @return this params object
	 */
	public Params add(String name, Object value) {
		map.put(name, value);
		return this;
	}
	
	/**
	 * @return the backing map.
	 */
	public Map<String, Object> getAll() {
		return map;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
						.append("map", map)
						.toString();
	}

}
