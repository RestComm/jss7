package org.mobicents.protocols.ss7.tcap.api.tc.component;

import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

public interface TCResultRequest extends ComponentRequest {

	
	public OperationCode getOperation();
	public Parameter getParameter();
	public void setOperation(OperationCode oc);
	public void  setParameter(Parameter p);
	
	//determine if its last result
	public boolean isLast();
	public void setLast(boolean f);
	
}
