package org.mobicents.protocols.ss7.tcap.api.tc.component;

import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

public interface TCInvokeRequest extends ComponentRequest {

	//this is stack prop, identifies operation class, 
	public void setInvokeClass(InvokeClass c);
	public InvokeClass getInvokeClass();
	public void setInvokeTimeout(long t);
	public long getInvokeTimeout();

	
	//asn encoded
	//public void setInvokeId(Long l);
	//public Long getInvokeId();
	
	public void setLinkedId(Long l);
	public Long getLinkedId();
	
	public void setOperation(OperationCode oc);
	public OperationCode getOperation();
	
	public void setParameter(Parameter p);
	public Parameter getParameter();
	


}
