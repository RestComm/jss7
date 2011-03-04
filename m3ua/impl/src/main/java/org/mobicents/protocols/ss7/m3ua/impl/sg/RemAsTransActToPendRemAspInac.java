package org.mobicents.protocols.ss7.m3ua.impl.sg;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

public class RemAsTransActToPendRemAspInac implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(RemAsTransActToPendRemAspInac.class);

    private RemAsImpl as = null;
    private FSM fsm;

    private int lbCount = 0;

    public RemAsTransActToPendRemAspInac(RemAsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public boolean process(State state) {
        try {
            RemAspImpl remAsp = (RemAspImpl) this.fsm.getAttribute(RemAsImpl.ATTRIBUTE_ASP);

            if (this.as.getTrafficModeType().getMode() == TrafficModeType.Broadcast) {
                // We don't support this
                return false;

            }

            if (this.as.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
                this.lbCount = 0;

                for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList()
                        .tail(); (n = n.getNext()) != end;) {
                    RemAspImpl remAspImpl = (RemAspImpl) n.getValue();
                    if (remAspImpl.getState() == AspState.ACTIVE) {
                        this.lbCount++;
                    }
                }// for

                if (this.lbCount >= this.as.getMinAspActiveForLb()) {
                    // we still have more ASP's ACTIVE for lb. Don't change
                    // state
                    return false;
                }

                // We are below minAspActiveForLb required for LB
                if (this.lbCount > 0) {
                    // But In any case if we have at least one ASP that can take
                    // care of traffic, don't change state but send the "Ins.
                    // ASPs" to INACTIVE ASP's

                    for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as
                            .getAspList().tail(); (n = n.getNext()) != end;) {
                        remAsp = (RemAspImpl) n.getValue();
                        if (remAsp.getState() == AspState.INACTIVE) {
                            Notify notify = this.createNotify(remAsp, Status.STATUS_Other,
                                    Status.INFO_Insufficient_ASP_Resources_Active);
                            remAsp.getAspFactory().write(notify);
                        }
                    }

                    return false;
                }
            }

            // We have reached here means AS is transitioning to be PENDING.
            // Send new AS STATUS to all INACTIVE APS's

            for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList()
                    .tail(); (n = n.getNext()) != end;) {
                remAsp = (RemAspImpl) n.getValue();
                if (remAsp.getState() == AspState.INACTIVE) {
                    Notify notify = this.createNotify(remAsp, Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING);
                    remAsp.getAspFactory().write(notify);
                }
            }
        } catch (Exception e) {
            logger.error(String.format("Error while translating Rem AS to PENDING. %s", this.fsm.toString()), e);
        }
        return true;
    }

    private Notify createNotify(RemAspImpl remAsp, int type, int info) {
        Notify msg = (Notify) this.as.getM3UAProvider().getMessageFactory().createMessage(MessageClass.MANAGEMENT,
                MessageType.NOTIFY);

        Status status = this.as.getM3UAProvider().getParameterFactory().createStatus(type, info);
        msg.setStatus(status);

        if (remAsp.getASPIdentifier() != null) {
            msg.setASPIdentifier(remAsp.getASPIdentifier());
        }

        msg.setRoutingContext(this.as.getRoutingContext());

        return msg;
    }

}
