/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.api.tc.component.TCResultRequest;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;

/**
 * @author baranowb
 * 
 */
public class TCResultRequestImpl extends ComponentRequestImpl implements TCResultRequest {

	private boolean last = false;
	private Return _return;

	TCResultRequestImpl(boolean isLast) {
		super();
		// actaully override subclass set
		this.last = isLast;
		super.component = this.getEncodableComponent();
		this._return = (Return) component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.tc.component.ComponentRequestImpl#
	 * getEncodableComponent()
	 */
	@Override
	protected Component getEncodableComponent() {
		if (last) {
			return TcapFactory.createComponentReturnResultLast();
		} else {
			return TcapFactory.createComponentReturnResult();
		}
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
	 * @seeorg.mobicents.protocols.ss7.tcap.api.tc.component.TCResultRequest#
	 * setOperation(org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode)
	 */
	public void setOperation(OperationCode[] oc) {
		this._return.setOperationCode(oc);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.tc.component.TCResultRequest#
	 * setParameter(org.mobicents.protocols.ss7.tcap.asn.comp.Parameter)
	 */
	public void setParameter(Parameter p) {
		this._return.setParameter(p);

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
