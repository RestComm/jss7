package org.mobicents.protocols.ss7.m3ua.impl.sg;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.StateEventHandler;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

public class RemAsStatePenTimeout implements StateEventHandler {

    private RemAsImpl as;
    private FSM fsm;
    private static final Logger logger = Logger.getLogger(RemAsStatePenTimeout.class);

    boolean inactive = false;

    public RemAsStatePenTimeout(RemAsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public void onEvent(State state) {
        this.inactive = false;
        // check if there are any ASP's who are INACTIVE, transition to
        // INACTIVE else DOWN
        for (FastList.Node<Asp> n = this.as.getAppServerProcess().head(), end = this.as.getAppServerProcess().tail(); (n = n
                .getNext()) != end;) {
            RemAspImpl remAspImpl = (RemAspImpl) n.getValue();
            if (remAspImpl.getState() == AspState.INACTIVE) {
                try {

                    if (!this.inactive) {
                        this.fsm.signal(TransitionState.AS_INACTIVE);
                        inactive = true;
                    }
                    Notify msg = createNotify(remAspImpl);
                    remAspImpl.getAspFactory().write(msg);
                } catch (UnknownTransitionException e) {
                    logger.error(String.format("Error while translating Rem AS to INACTIVE. %s", this.fsm.toString()),
                            e);
                }

            }// if (remAspImpl.getState() == AspState.INACTIVE)
        }// for

        if (!this.inactive) {
            // else transition to DOWN
            try {
                this.fsm.signal(TransitionState.AS_DOWN);
                inactive = true;
            } catch (UnknownTransitionException e) {
                logger.error(String.format("Error while translating Rem AS to DOWN. %s", this.fsm.toString()), e);
            }
        }
    }

    private Notify createNotify(RemAspImpl remAsp) {
        Notify msg = (Notify) this.as.getM3UAProvider().getMessageFactory().createMessage(MessageClass.MANAGEMENT,
                MessageType.NOTIFY);

        Status status = this.as.getM3UAProvider().getParameterFactory().createStatus(Status.STATUS_AS_State_Change,
                Status.INFO_AS_INACTIVE);
        msg.setStatus(status);

        if (remAsp.getASPIdentifier() != null) {
            msg.setASPIdentifier(remAsp.getASPIdentifier());
        }

        msg.setRoutingContext(this.as.getRoutingContext());

        return msg;
    }

}
