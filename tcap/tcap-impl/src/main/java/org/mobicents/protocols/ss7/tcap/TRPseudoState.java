/**
 * 
 */
package org.mobicents.protocols.ss7.tcap;

/**
 * @author baranowb
 *
 */
public enum TRPseudoState {

	Idle,
	
	InitialReceived,InitialSent,
	
	Active,
	//additional state to mark removal
	Expunged
	;
}
