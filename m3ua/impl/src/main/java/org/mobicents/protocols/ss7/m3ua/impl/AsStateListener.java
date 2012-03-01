package org.mobicents.protocols.ss7.m3ua.impl;

/**
 * <p>
 * The listener interface for receiving events when {@link As} state changes to
 * Active or not-active
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface AsStateListener {

	/**
	 * Invoked when {@link As} becomes active
	 * 
	 * @param as
	 */
	public void onAsActive(As as);

	/**
	 * Invoked when {@link As} becomes inactive
	 * 
	 * @param as
	 */
	public void onAsInActive(As as);
}
