package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.MAPMessage;


/**
* 
* @author cristian veliscu
* 
*/
public interface CallHandlingMessage extends MAPMessage {
	public MAPDialogCallHandling getMAPDialog();
}