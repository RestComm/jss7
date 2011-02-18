package org.mobicents.protocols.ss7.m3ua.impl.as;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

public class AspTransActToInactSnt implements TransitionHandler {

    private AspImpl asp;
    private FSM fsm;
    private static final Logger logger = Logger.getLogger(AspTransActToInactSnt.class);

    public AspTransActToInactSnt(AspImpl asp, FSM fsm) {
        this.asp = asp;
        this.fsm = fsm;
    }

    public boolean process(State state) {
//        try {
//
//            ASPInactive aspInact = (ASPInactive) this.asp.getM3UAProvider().getMessageFactory().createMessage(
//                    MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
//
//            long[] rcs = new long[this.asp.appServers.size()];
//            for (int count = 0; count < this.asp.appServers.size(); count++) {
//                AsImpl as = this.asp.appServers.get(count);
//
//                // Remove this ASP from AS's list of active ASP and add to list
//                // of inactive ASP
//                as.activeAppServerProcs.remove(this.asp);
//                as.inactiveAppServerProcs.add(this.asp);
//
//                rcs[count] = as.getRoutingContext().getRoutingContexts()[0];
//            }
//
//            RoutingContext rc = this.asp.m3UAProvider.getParameterFactory().createRoutingContext(rcs);
//            aspInact.setRoutingContext(rc);
//
//            this.asp.write(aspInact);
//
//            return true;
//        } catch (Exception e) {
//            logger.error(String.format("Error while sending ASPUp message. %s", this.fsm.toString()), e);
//        }

        // If any error return false
        return false;
    }

}
