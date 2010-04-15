/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.api.tc.component.TCRejectIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;

/**
 * @author baranowb
 * 
 */
public class TCRejectIndicationImpl extends ComponentIndicationImpl implements TCRejectIndication {

	private Reject reject;

	TCRejectIndicationImpl(Reject reject) {
		super();
		this.reject = reject;
		super.component = this.reject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.component.TCRejectIndication#
	 * getProblem()
	 */
	public Problem getProblem() {

		return this.reject.getProblem();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentIndication
	 * #getType()
	 */
	public ComponentType getType() {

		return ComponentType.Reject;
	}

}
