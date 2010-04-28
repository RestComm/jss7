package org.mobicents.protocols.ss7.map.api.dialog;

/**
 * Reason ::= ENUMERATED {
 *    noReasonGiven (0),
 *   invalidDestinationReference (1),
 *   invalidOriginatingReference (2)}
 *
 * 
 * @author amit bhayani
 *
 */
public enum Reason {

	noReasonGiven(0), 
	invalidDestinationReference(1), 
	invalidOriginatingReference(2);

	private int code;

	private Reason(int code) {
		this.code = code;
	}

}
