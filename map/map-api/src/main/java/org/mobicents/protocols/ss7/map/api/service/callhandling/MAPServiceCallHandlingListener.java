package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.MAPServiceListener;


/*
 * 
 * @author cristian veliscu
 * 
 */
public interface MAPServiceCallHandlingListener extends MAPServiceListener {
	public void onSendRoutingInformationRequest(SendRoutingInformationRequest request); 
    public void onSendRoutingInformationResponse(SendRoutingInformationResponse response); 
}