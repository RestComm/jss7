package org.mobicents.protocols.ss7.m3ua.impl.as;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

public class AsTransActToActNtfyAltAspAct implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(AsTransActToActNtfyAltAspAct.class);

    private AsImpl as = null;
    private FSM fsm;

    public AsTransActToActNtfyAltAspAct(AsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public boolean process(State state) {
        AspImpl causeAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);

        try {
            causeAsp.getFSM().signal(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE);
        } catch (UnknownTransitionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

}
