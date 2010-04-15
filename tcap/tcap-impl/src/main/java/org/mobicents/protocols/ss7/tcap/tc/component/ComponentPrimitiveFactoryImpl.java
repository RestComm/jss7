/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCRejectIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCRejectRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCResultIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.component.TCResultRequest;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * @author baranowb
 * 
 */
public class ComponentPrimitiveFactoryImpl implements ComponentPrimitiveFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCInvokeIndication
	 * (org.mobicents.protocols.ss7.tcap.asn.comp.Invoke)
	 */
	public TCInvokeIndication createTCInvokeIndication(Invoke i) {
		if (i == null) {
			throw new NullPointerException("Argument must not be null");
		}
		TCInvokeIndicationImpl t = new TCInvokeIndicationImpl(i);

		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCInvokeRequest()
	 */
	public TCInvokeRequest createTCInvokeRequest() {

		TCInvokeRequestImpl t = new TCInvokeRequestImpl();
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCRejectIndication
	 * (org.mobicents.protocols.ss7.tcap.asn.comp.Reject)
	 */
	public TCRejectIndication createTCRejectIndication(Reject rej) {
		if (rej == null) {
			throw new NullPointerException("Argument must not be null");
		}
		TCRejectIndicationImpl t = new TCRejectIndicationImpl(rej);

		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCRejectRequest()
	 */
	public TCRejectRequest createTCRejectRequest() {

		TCRejectRequestImpl t = new TCRejectRequestImpl();

		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCResultRequest(boolean)
	 */
	public TCResultRequest createTCResultRequest(boolean last) {

		TCResultRequestImpl t = new TCResultRequestImpl(last);

		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCResultRequest(org.mobicents.protocols.ss7.tcap.asn.comp.Return)
	 */
	public TCResultIndication createTCResultRequest(Return ret) {
		if (ret == null) {
			throw new NullPointerException("Argument must not be null");
		}

		if (ret.getType() == ComponentType.ReturnResult) {
			TCResultIndicationImpl t = new TCResultIndicationImpl((ReturnResult) ret);
			return t;
		} else {
			TCResultIndicationImpl t = new TCResultIndicationImpl((ReturnResultLast) ret);
			return t;
		}
	}

}
