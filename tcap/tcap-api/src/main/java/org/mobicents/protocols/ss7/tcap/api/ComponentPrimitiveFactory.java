package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCRejectIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCRejectRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCResultIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCResultRequest;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;

public interface ComponentPrimitiveFactory {

	public TCInvokeRequest createTCInvokeRequest();

	public TCRejectRequest createTCRejectRequest();

	public TCResultRequest createTCResultRequest(boolean last);
	
	//hmm is this reasonable to have ?
	public TCInvokeIndication createTCInvokeIndication(Invoke i);

	public TCRejectIndication createTCRejectIndication(Reject rej);

	public TCResultIndication createTCResultRequest(Return ret);

}
