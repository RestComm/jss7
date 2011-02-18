package org.mobicents.protocols.ss7.m3ua.impl.sg;

import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 * Transition handler that doesn't cause State change
 * 
 * @author amit bhayani
 * 
 */
public class RemAsNoTrans implements TransitionHandler {

    public RemAsNoTrans() {
        // TODO Auto-generated constructor stub
    }

    public boolean process(State state) {
        return false;
    }

}
