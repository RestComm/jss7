package org.mobicents.protocols.ss7.tcap.api.tc.component;

import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

public interface TCRejectIndication extends ComponentIndication {

	public Problem getProblem();

}
