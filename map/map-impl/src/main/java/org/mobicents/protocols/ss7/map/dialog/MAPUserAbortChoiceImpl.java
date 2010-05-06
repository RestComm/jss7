package org.mobicents.protocols.ss7.map.dialog;

import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.dialog.ResourceUnavailableReason;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPUserAbortChoiceImpl implements MAPUserAbortChoice {

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

}
