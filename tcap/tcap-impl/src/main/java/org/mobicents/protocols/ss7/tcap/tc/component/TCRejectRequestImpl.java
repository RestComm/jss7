/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.api.tc.component.TCRejectRequest;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;

/**
 * @author baranowb
 *
 */
public class TCRejectRequestImpl extends ComponentRequestImpl implements TCRejectRequest {

	private Reject reject;

	TCRejectRequestImpl() {
		super();
		this.reject = (Reject) super.component;
	}

	
	public Problem getProblem() {
		return this.reject.getProblem();
	}

	public void setProblem(Problem p) {
		this.reject.setProblem(p);
		
	}

	public ComponentType getType() {
		
		return ComponentType.Reject;
	}
	
	
	@Override
	protected Component getEncodableComponent() {
		
		return TcapFactory.createComponentReject();
	}

	
	
}
