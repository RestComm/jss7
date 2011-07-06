package org.mobicents.protocols.ss7.map.functional;

import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.sccp.SccpProvider;

public class MAPStackImplWrapper extends MAPStackImpl {

	public MAPStackImplWrapper(SccpProvider sccpPprovider, int ssn) {
		super(sccpPprovider, ssn);
		this.mapProvider = new MAPProviderImplWrapper(this.tcapStack.getProvider());
	}

}
