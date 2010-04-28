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
		this.tcapProvider = new TCAPProviderImpl(this.sccpProvider, this);

	}




	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getProvider()
	 */
	public TCAPProvider getProvider() {
		
		return tcapProvider;
	}




	public void stop() {
		if(this.tcapProvider!=null)
		{
			this.sccpProvider.removeListener();
		}
		
	}




}
