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
	public void addTCListener(TCListener lst);

	public void removeTCListener(TCListener lst);
	
	//TR part for direct access

	
	public void addTRListener(TRListener lst);

	public void removeTRListener(TRListener lst);
	
	public TCAPProvider getProvider();
	
}
