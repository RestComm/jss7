/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api.tc.component;

/**
 * State of operation, see Q.771 3.1.5
 * @author baranowb
 *
 */
public enum OperationState {

	//Initial and End state
	Idle,
	//Once passed to sendComponent
	Pending,
	//On Begin,Continue or End is sent and Result-NL received
	Sent,
	//On Result-L or TC-U-ERROR
	Reject_W,
	//On TC-L-REJECT
	Reject_P;
	
}
