/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author baranowb
 * 
 */
public class TCInvokeIndicationImpl extends ComponentIndicationImpl implements TCInvokeIndication {

	private Invoke invoke;

	public TCInvokeIndicationImpl(Invoke invoke) {
		super();
		this.invoke = invoke;
		super.component = this.invoke;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeIndication#
	 * getLinkedId()
	 */
	public Long getLinkedId() {

		return this.invoke.getLinkedId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeIndication#
	 * getOperation()
	 */
	public OperationCode getOperation() {
		return this.invoke.getOperationCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.component.TCInvokeIndication#
	 * getParameter()
	 */
	public Parameter getParameter() {
		//return this.invoke.getParameter();
		//TODO This class will not be used, clean it == Amit
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentIndication
	 * #getType()
	 */
	public ComponentType getType() {
		return ComponentType.Invoke;
	}

}
