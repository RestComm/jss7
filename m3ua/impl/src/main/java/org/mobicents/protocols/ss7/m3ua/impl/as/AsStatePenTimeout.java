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

public class AsStatePenTimeout implements StateEventHandler {

    private AsImpl as;
    private FSM fsm;
    private static final Logger logger = Logger.getLogger(AsStatePenTimeout.class);

    boolean inactive = false;

    public AsStatePenTimeout(AsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public void onEvent(State state) {
        this.inactive = false;
        // check if there are any ASP's who are INACTIVE, transition to
        // INACTIVE else DOWN
        for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n
                .getNext()) != end;) {
            Asp asp = n.getValue();
            if (asp.getState() == AspState.INACTIVE) {
                try {
                    this.fsm.signal(TransitionState.AS_INACTIVE);
                    inactive = true;
                    break;
                } catch (UnknownTransitionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }// if
        }// for

        if (!this.inactive) {
            // else transition to DOWN
            try {
                this.fsm.signal(TransitionState.AS_DOWN);
                inactive = true;
            } catch (UnknownTransitionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
