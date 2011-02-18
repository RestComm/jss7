package org.mobicents.protocols.ss7.m3ua.impl.as;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActive;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

public class AspTransInactToActSnt implements TransitionHandler {

    private AspImpl asp;
    private FSM fsm;
    private static final Logger logger = Logger.getLogger(AspTransInactToActSnt.class);

    public AspTransInactToActSnt(AspImpl asp, FSM fsm) {
        this.asp = asp;
        this.fsm = fsm;
    }

    public boolean process(State state) {

//        try {
//            long[] rcs = (long[]) this.fsm.getAttribute(AspImpl.ATTRIBUTE_ROUTING_CONTEXTS);
//            RoutingContext rc = this.asp.m3UAProvider.getParameterFactory().createRoutingContext(rcs);
//            
//            ASPActive aspUp = (ASPActive) this.asp.m3UAProvider.getMessageFactory().createMessage(
//                    MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
//            aspUp.setRoutingContext(rc);
//
//            this.asp.write(aspUp);
//
//            return true;
//        } catch (Exception e) {
//            logger.error(String.format("Error while sending ASPUp message. %s", this.fsm.toString()), e);
//        }

        // If any error return false
        return false;
    }

}
