package org.mobicents.protocols.ss7.tcap.api;


import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

public interface ComponentPrimitiveFactory {

	public Invoke createTCInvokeRequest();

	public Reject createTCRejectRequest();

	public ReturnResultLast createTCResultLastRequest();

	public ReturnResult createTCResultRequest();

}
