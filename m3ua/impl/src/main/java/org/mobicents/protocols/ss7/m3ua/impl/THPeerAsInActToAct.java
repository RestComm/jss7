package org.mobicents.protocols.ss7.m3ua.impl;

import javolution.util.FastSet;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 * 
 * @author amit bhayani
 *
 */
public class THPeerAsInActToAct implements TransitionHandler {

	private static final Logger logger = Logger.getLogger(THPeerAsInActToAct.class);

	private As as = null;
	private FSM fsm;

	THPeerAsInActToAct(As as, FSM fsm) {
		this.as = as;
		this.fsm = fsm;
	}

	@Override
	public boolean process(State state) {
		FastSet<AsStateListener> asStateListeners = this.as.getAsStateListeners();
		for (FastSet.Record r = asStateListeners.head(), end = asStateListeners.tail(); (r = r.getNext()) != end;) {
			AsStateListener asAsStateListener = asStateListeners.valueOf(r);
			try {
				asAsStateListener.onAsActive(this.as);
			} catch (Exception e) {
				logger.error(String.format("Error while calling AsStateListener=%s onAsActive method for As=%s",
						asAsStateListener, this.as));
			}
		}
		return true;
	}

}
