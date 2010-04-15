package org.mobicents.protocols.ss7.tcap.api.tc.component;

import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;


public interface TCRejectRequest extends ComponentRequest {

	
	public Problem getProblem();
	
	public void setProblem(Problem p);
}
