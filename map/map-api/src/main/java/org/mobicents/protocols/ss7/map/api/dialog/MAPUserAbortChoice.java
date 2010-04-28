package org.mobicents.protocols.ss7.map.api.dialog;

import org.mobicents.protocols.ss7.map.api.MAPDialog;

/**
 * MAP-UserAbortChoice ::= CHOICE {
 *   userSpecificReason               [0] NULL,
 *   userResourceLimitation           [1] NULL,
 *   resourceUnavailable              [2] ResourceUnavailableReason,
 *   applicationProcedureCancellation [3] ProcedureCancellationReason}
 *
 * @author amit bhayani
 *
 */
public interface MAPUserAbortChoice {
	
	public void setUserSpecificReason();
	
	public void setUserResourceLimitation();
	
	public void setResourceUnavailableReason(ResourceUnavailableReason resUnaReas);
	
	public void setProcedureCancellationReason(ProcedureCancellationReason procCanReasn);
	
	public ProcedureCancellationReason getProcedureCancellationReason();
	
	public ResourceUnavailableReason getResourceUnavailableReason();
	
	public boolean isUserSpecificReason();
	
	public boolean isUserResourceLimitation();
	
	public boolean isResourceUnavailableReason();
	
	public boolean isProcedureCancellationReason();
	
	public MAPDialog getMAPDialog();
	
	public void setMAPDialog(MAPDialog mapDialog);

}
