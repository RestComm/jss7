package org.mobicents.protocols.ss7.m3ua.impl.sg;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

public class RemAsTransInactToDwn implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(RemAsTransInactToDwn.class);

    private RemAsImpl as = null;
    private FSM fsm;

    public RemAsTransInactToDwn(RemAsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    int inactCount = 0;

    public boolean process(State state) {
        inactCount = 0;

        try {
            for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList()
                    .tail(); (n = n.getNext()) != end;) {
                RemAspImpl remAspImpl = (RemAspImpl) n.getValue();

                if (remAspImpl.getState() == AspState.INACTIVE) {
                    inactCount++;
                }
            }

            if (inactCount > 0) {
                // We have atleast one more ASP in INACTIVE state, the AS should
                // remain INACTIVE
                return false;
            }

            return true;
        } catch (Exception e) {
            logger.error(String.format("Error while translating Rem AS to DOWN. %s", this.fsm.toString()), e);
        }
        // something wrong
        return false;

    }
}
