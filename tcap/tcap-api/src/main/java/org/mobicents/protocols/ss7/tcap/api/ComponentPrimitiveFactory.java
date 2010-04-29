package org.mobicents.protocols.ss7.tcap.api;


import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

public interface ComponentPrimitiveFactory {

	public Invoke createTCInvokeRequest();

	public Reject createTCRejectRequest();

	public ReturnResultLast createTCResultLastRequest();

	public ReturnResult createTCResultRequest();

	public OperationCode createOperationCode(boolean isGlobal, Long code);
	
	public Parameter createParameter();
	public Parameter createParameter(int tag, int tagClass, boolean isPrimitive);
}
