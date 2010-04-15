/**
 * 
 */
package org.mobicents.protocols.ss7.tcap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.TRListener;

/**
 * @author baranowb
 *
 */
public class TCAPStackImpl implements TCAPStack {

	private Set<TCListener> tcListeners = new HashSet<TCListener>();
	private Set<TRListener> trListeners = new HashSet<TRListener>();
	private TCAPProvider tcapProvider;
	private SccpProvider sccpProvider;

	public TCAPStackImpl(SccpProvider sccpProvider) {
		super();
		this.sccpProvider = sccpProvider;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#addTCListener(org.mobicents.protocols.ss7.tcap.api.TCListener)
	 */
	public void addTCListener(TCListener lst) {
		if(this.tcListeners.contains(lst))
		{
			
		}else
		{
			this.tcListeners.add(lst);
		}

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#addTRListener(org.mobicents.protocols.ss7.tcap.api.TRListener)
	 */
	public void addTRListener(TRListener lst) {
		if(this.trListeners.contains(lst))
		{
			
		}else
		{
			this.trListeners.add(lst);
		}


	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getProvider()
	 */
	public TCAPProvider getProvider() {
		
		return tcapProvider;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#removeTCListener(org.mobicents.protocols.ss7.tcap.api.TCListener)
	 */
	public void removeTCListener(TCListener lst) {
		this.tcListeners.remove(lst);

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#removeTRListener(org.mobicents.protocols.ss7.tcap.api.TRListener)
	 */
	public void removeTRListener(TRListener lst) {
		this.trListeners.remove(lst);

	}

}
