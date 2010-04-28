package org.mobicents.protocols.ss7.map.api.dialog;

/**
 * ProcedureCancellationReason ::= ENUMERATED {
 *    handoverCancellation (0),
 *   radioChannelRelease (1),
 *   networkPathRelease (2),
 *   callRelease (3),
 *   associatedProcedureFailure (4),
 *   tandemDialogueRelease (5),
 *   remoteOperationsFailure (6)}
 *
 * 
 * @author amit bhayani
 *
 */
public enum ProcedureCancellationReason {
	handoverCancellation(0), radioChannelRelease(1), 
	networkPathRelease(2), callRelease(3),
	associatedProcedureFailure(4), tandemDialogueRelease(5),
	remoteOperationsFailure(6);

	private int code;

	private ProcedureCancellationReason(int code) {
		this.code = code;
	}

	public ProcedureCancellationReason getInstance(int code) {
		switch (code) {
		case 0:
			return handoverCancellation;
		case 1:
			return radioChannelRelease;
		case 2:
			return networkPathRelease;
		case 3:
			return callRelease;
		case 4:
			return associatedProcedureFailure;
		case 5:
			return tandemDialogueRelease;
		case 6:
			return remoteOperationsFailure;
		default:
			return null;
		}
	}

	public int getCode() {
		return code;
	}

}
