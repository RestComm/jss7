/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.TCAProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * @author baranowb
 * 
 */
public class ComponentPrimitiveFactoryImpl implements ComponentPrimitiveFactory {

	private TCAProviderImpl provider;
	
	public ComponentPrimitiveFactoryImpl(TCAProviderImpl tcaProviderImpl) {
		this.provider = tcaProviderImpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCInvokeRequest()
	 */
	public Invoke createTCInvokeRequest() {

		InvokeImpl t = (InvokeImpl) TcapFactory.createComponentInvoke();
		t.setProvider(provider);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCRejectRequest()
	 */
	public Reject createTCRejectRequest() {

		return TcapFactory.createComponentReject();
	}
	


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCResultRequest(boolean)
	 */
	public ReturnResultLast createTCResultLastRequest() {
		
		return TcapFactory.createComponentReturnResultLast();
		
	}
	public ReturnResult createTCResultRequest() {
		
		return TcapFactory.createComponentReturnResult();
	}
	
}
