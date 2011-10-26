package org.mobicents.protocols.ss7.m3ua.impl.as;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AsTransInActToDwn implements TransitionHandler {

	private static final Logger logger = Logger.getLogger(AsTransActToPen.class);

	private As as = null;
	private FSM fsm;

	public AsTransInActToDwn(As as, FSM fsm) {
		this.as = as;
		this.fsm = fsm;
	}

	public boolean process(State state) {
		Asp causeAsp = (Asp) this.fsm.getAttribute(As.ATTRIBUTE_ASP);

		// check if there is atleast one other ASP in INACTIVE state. If
		// yes this AS remains in INACTIVE state else goes in PENDING state.
		for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			if (asp != causeAsp && asp.getState() == AspState.INACTIVE) {
				return false;
			}
		}
		return true;
	}

}
