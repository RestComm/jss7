package org.mobicents.protocols.ss7.m3ua.impl.sg;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

public class RemAsTransInactToInact implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(RemAsTransInactToInact.class);

    private RemAsImpl as = null;
    private FSM fsm;

    public RemAsTransInactToInact(RemAsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public boolean process(State state) {
        try {

            RemAspImpl remAsp = (RemAspImpl) this.fsm.getAttribute(RemAsImpl.ATTRIBUTE_ASP);

            if (remAsp == null) {
                logger.error(String.format("No ASP found. %s", this.fsm.toString()));
                return false;
            }

            Notify msg = createNotify(remAsp);
            remAsp.getAspFactory().write(msg);
            return true;
        } catch (Exception e) {
            logger.error(String.format("Error while translating Rem AS to INACTIVE message. %s", this.fsm.toString()),
                    e);
        }
        return false;
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
