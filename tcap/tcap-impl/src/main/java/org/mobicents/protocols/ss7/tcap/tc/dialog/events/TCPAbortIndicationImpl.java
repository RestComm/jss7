/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

/**
 * @author baranowb
 *
 */
public class TCPAbortIndicationImpl extends DialogIndicationImpl implements TCPAbortIndication {
	//This indication is used to inform user of abnormal cases.
	private PAbortCauseType cause;
	
	TCPAbortIndicationImpl() {
		super(EventType.PAbort);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication#getPAbortCause()
	 */
	public PAbortCauseType getPAbortCause() {
		return this.cause;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication#setPAbortCause(org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType)
	 */
	public void setPAbortCause(PAbortCauseType t) {
		this.cause = t;

	}

}
