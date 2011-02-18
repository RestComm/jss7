package org.mobicents.protocols.ss7.m3ua.impl.as;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.StateEventHandler;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

public class AsStateEnterPen implements StateEventHandler {

    private static final Logger logger = Logger.getLogger(AsStateEnterPen.class);

    private AsImpl as = null;
    private FSM fsm;

    public AsStateEnterPen(AsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public void onEvent(State state) {
        // If there is even one ASP in INACTIVE state for this AS, ACTIVATE it
        for (FastList.Node<Asp> n = as.getAppServerProcess().head(), end = as.getAppServerProcess().tail(); (n = n
                .getNext()) != end;) {
            AspImpl asp = (AspImpl) n.getValue();
            
            if (asp.getState() == AspState.INACTIVE) {
                LocalAspFactory localAspFactory = (LocalAspFactory) asp.getAspFactory();
                localAspFactory.sendAspActive(this.as);

                // Transition the state of ASP to ACTIVE_SENT
                try {
                    asp.getFSM().signal(TransitionState.ASP_ACTIVE_SENT);
                } catch (UnknownTransitionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
