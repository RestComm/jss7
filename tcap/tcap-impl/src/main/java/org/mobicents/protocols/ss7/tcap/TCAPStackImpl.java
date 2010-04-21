/**
 * 
 */
package org.mobicents.protocols.ss7.tcap;

import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;

/**
 * @author baranowb
 *
 */
public class TCAPStackImpl implements TCAPStack {



	private TCAPProvider tcapProvider;
	private SccpProvider sccpProvider;

	public TCAPStackImpl(SccpProvider sccpProvider) {
		super();
		this.sccpProvider = sccpProvider;
	}




	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getProvider()
	 */
	public TCAPProvider getProvider() {
		
		return tcapProvider;
	}




}
