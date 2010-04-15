/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.api.tc.component.TCResultIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * @author baranowb
 * 
 */
public class TCResultIndicationImpl extends ComponentIndicationImpl implements TCResultIndication {

	private boolean last = false;
	private Return _return;

	public TCResultIndicationImpl(ReturnResult res) {
		super();
		// actaully override subclass set
		this.last = false;
		this._return = res;
		super.component = res;
	}

	public TCResultIndicationImpl(ReturnResultLast res) {
		super();
		// actaully override subclass set
		this.last = true;
		this._return = res;
		super.component = res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.tc.component.TCResultRequest#
	 * getOperation()
	 */
	public OperationCode[] getOperation() {
		return this._return.getOperationCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.tc.component.TCResultRequest#
	 * getParameter()
	 */
	public Parameter getParameter() {
		return this._return.getParameter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.component.TCResultRequest#isLast
	 * ()
	 */
	public boolean isLast() {

		return this.last;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest#getType
	 * ()
	 */
	public ComponentType getType() {
		return this._return.getType();
	}

}
