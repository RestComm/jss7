package org.mobicents.protocols.ss7.tcap.api.tc.component;

import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

public interface TCInvokeIndication extends ComponentIndication {

	
	public Long getLinkedId();
	public OperationCode getOperation();
	public Parameter getParameter();
	
	
}
