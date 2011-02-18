package org.mobicents.protocols.ss7.m3ua.impl.as;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

public class AsTransActToPen implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(AsTransActToPen.class);

    private AsImpl as = null;
    private FSM fsm;

    public AsTransActToPen(AsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public boolean process(State state) {
        AspImpl causeAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);

        // check if there is atleast one other ASP in ACTIVE state. If
        // yes this AS remains in ACTIVE state else goes in PENDING state.
        for (FastList.Node<Asp> n = this.as.getAppServerProcess().head(), end = this.as.getAppServerProcess().tail(); (n = n
                .getNext()) != end;) {
            AspImpl asp = (AspImpl) n.getValue();
            if (asp != causeAsp && asp.getState() == AspState.ACTIVE) {
                return false;
            }
        }
        return true;
    }

}
