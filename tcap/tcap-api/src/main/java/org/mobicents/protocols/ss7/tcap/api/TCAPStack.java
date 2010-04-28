/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api;

/**
 * @author baranowb
 *
 */
public interface TCAPStack {

	//FIXME: add address for listener ?

	
	public TCAPProvider getProvider();
	public void stop();
}
