package org.mobicents.protocols.ss7.m3ua.impl;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 * 
 * @author amit bhayani
 * 
 */
public class THPeerAsInActToDwn implements TransitionHandler {

	private static final Logger logger = Logger.getLogger(THPeerAsActToPen.class);

	private As as = null;
	private FSM fsm;

	public THPeerAsInActToDwn(As as, FSM fsm) {
		this.as = as;
		this.fsm = fsm;
	}

	public boolean process(State state) {
		Asp causeAsp = (Asp) this.fsm.getAttribute(As.ATTRIBUTE_ASP);

		// check if there is atleast one other ASP in INACTIVE state. If
		// yes this AS remains in INACTIVE state else goes in DOWN state.
		for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();

			FSM aspLocalFSM = asp.getLocalFSM();
			AspState aspState = AspState.getState(aspLocalFSM.getState().getName());

			if (asp != causeAsp && aspState == AspState.INACTIVE) {
				return false;
			}
		}
		return true;
	}

}
