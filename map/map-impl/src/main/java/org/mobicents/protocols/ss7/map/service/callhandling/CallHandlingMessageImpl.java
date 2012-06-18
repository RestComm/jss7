package org.mobicents.protocols.ss7.map.service.callhandling;

import org.mobicents.protocols.ss7.map.MessageImpl;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallHandlingMessage;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;


/*
 * 
 * @author cristian veliscu
 * 
 */
public abstract class CallHandlingMessageImpl extends MessageImpl 
			    implements CallHandlingMessage, MAPAsnPrimitive {
	
	@Override
	public MAPDialogCallHandling getMAPDialog() {
		return (MAPDialogCallHandling) super.getMAPDialog();
	}
}