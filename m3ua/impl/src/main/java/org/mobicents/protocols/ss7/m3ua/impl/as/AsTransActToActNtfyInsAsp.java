package org.mobicents.protocols.ss7.m3ua.impl.as;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

public class AsTransActToActNtfyInsAsp implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(AsTransActToActNtfyInsAsp.class);

    private AsImpl as = null;
    private FSM fsm;

    public AsTransActToActNtfyInsAsp(AsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public boolean process(State state) {

        // Iterate through all the ASP for this AS and activate if they are
        // inactive
        for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n
                .getNext()) != end;) {
            Asp aspTemp = n.getValue();
            LocalAspFactory factory = (LocalAspFactory) aspTemp.getAspFactory();
            if (aspTemp.getState() == AspState.INACTIVE && factory.getStatus()) {
                factory.sendAspActive(this.as);
                try {
                    aspTemp.getFSM().signal(TransitionState.ASP_ACTIVE_SENT);
                } catch (UnknownTransitionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
