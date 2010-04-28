/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api.tc.dialog;

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
