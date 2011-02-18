package org.mobicents.protocols.ss7.m3ua.impl.as;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPUp;

public class AspTransDwnToAspUpSnt implements TransitionHandler {

    private AspImpl asp;
    private FSM fsm;
    private static final Logger logger = Logger.getLogger(AspTransDwnToAspUpSnt.class);

    public AspTransDwnToAspUpSnt(AspImpl asp, FSM fsm) {
        this.asp = asp;
        this.fsm = fsm;
    }

    public boolean process(State state) {

        try {

            ASPUp aspUp = (ASPUp) this.asp.getM3UAProvider().getMessageFactory().createMessage(
                    MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
            aspUp.setASPIdentifier(this.asp.getASPIdentifier());

            this.asp.getAspFactory().write(aspUp);

            return true;
        } catch (Exception e) {
            logger.error(String.format("Error while sending ASPUp message. %s", this.fsm.toString()), e);
        }

        // If any error return false
        return false;
    }

}
