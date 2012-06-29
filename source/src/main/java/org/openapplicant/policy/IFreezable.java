package org.openapplicant.policy;

/**
 * Marker interface indicating that the given implementation's state can be 
 * frozen.
 * @see AssertNotFrozen
 */
public interface IFreezable {

	/**
	 * @return true if this implementation' state is frozen and it's 
	 * freezable properties must not be changed.
	 */
	boolean isFrozen();
}
