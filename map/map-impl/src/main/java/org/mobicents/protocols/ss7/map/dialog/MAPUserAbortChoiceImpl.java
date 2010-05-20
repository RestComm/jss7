package org.mobicents.protocols.ss7.map.dialog;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.dialog.ResourceUnavailableReason;

/**
 * MAP-UserAbortChoice ::= CHOICE {
 *   userSpecificReason               [0] NULL,
 *   userResourceLimitation           [1] NULL,
 *   resourceUnavailable              [2] ResourceUnavailableReason,
 *   applicationProcedureCancellation [3] ProcedureCancellationReason}
 * @author amit bhayani
 * 
 */
public class MAPUserAbortChoiceImpl implements MAPUserAbortChoice {
	
	protected static final int USER_SPECIFIC_REASON_TAG = 0;
	protected static final int USER_RESOURCE_LIMITATION_TAG = 1;
	protected static final int RESOURCE_UNAVAILABLE = 2;
	protected static final int APPLICATION_PROCEDURE_CANCELLATION = 3;

	private ProcedureCancellationReason procedureCancellationReason = null;
	private boolean isProcedureCancellationReason = false;

	private ResourceUnavailableReason resourceUnavailableReason = null;
	private boolean isResourceUnavailableReason = false;

	private boolean isUserResourceLimitation = false;

	private boolean isUserSpecificReason = false;

	public ProcedureCancellationReason getProcedureCancellationReason() {
		return this.procedureCancellationReason;
	}

	public ResourceUnavailableReason getResourceUnavailableReason() {
		return this.resourceUnavailableReason;
	}

	public boolean isProcedureCancellationReason() {
		return this.isProcedureCancellationReason;
	}

	public boolean isResourceUnavailableReason() {
		return this.isResourceUnavailableReason;
	}

	public boolean isUserResourceLimitation() {
		return this.isUserResourceLimitation;
	}

	public boolean isUserSpecificReason() {
		return this.isUserSpecificReason;
	}

	public void setProcedureCancellationReason(
			ProcedureCancellationReason procCanReasn) {
		this.procedureCancellationReason = procCanReasn;
		this.isProcedureCancellationReason = true;
	}

	public void setResourceUnavailableReason(
			ResourceUnavailableReason resUnaReas) {
		this.resourceUnavailableReason = resUnaReas;
		this.isResourceUnavailableReason = true;
	}

	public void setUserResourceLimitation() {
		this.isUserResourceLimitation = true;
	}

	public void setUserSpecificReason() {
		this.isUserSpecificReason = true;
	}

	public void decode(AsnInputStream ais) throws AsnException, IOException,
			MAPException {

	}

	public void encode(AsnOutputStream asnOS) throws IOException, MAPException {
		
	}

}
