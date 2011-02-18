package org.mobicents.protocols.ss7.m3ua.impl.as;

import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

public class AsImpl extends As {

    public AsImpl(String name, RoutingContext rc, RoutingKey rk, TrafficModeType trMode, M3UAProvider provider) {
        super(name, rc, rk, trMode, provider);

        // Define states
        fsm.createState(AsState.DOWN.toString());
        fsm.createState(AsState.ACTIVE.toString());
        fsm.createState(AsState.INACTIVE.toString());
        fsm.createState(AsState.PENDING.toString()).setOnTimeOut(new AsStatePenTimeout(this, this.fsm), 2000)
                .setOnEnter(new AsStateEnterPen(this, this.fsm));

        fsm.setStart(AsState.DOWN.toString());
        fsm.setEnd(AsState.DOWN.toString());
        // Define Transitions

        // ******************************************************************/
        // STATE DOWN /
        // ******************************************************************/
        fsm.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.DOWN.toString(), AsState.INACTIVE
                .toString());

        // ******************************************************************/
        // STATE INACTIVE /
        // ******************************************************************/
        fsm.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.INACTIVE.toString(), AsState.INACTIVE
                .toString());

        fsm.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.INACTIVE.toString(), AsState.ACTIVE
                .toString());

        // ******************************************************************/
        // STATE ACTIVE /
        // ******************************************************************/
        fsm.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.ACTIVE.toString(), AsState.ACTIVE
                .toString());

        fsm.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AsState.ACTIVE.toString(),
                AsState.ACTIVE.toString()).setHandler(new AsTransActToActNtfyAltAspAct(this, this.fsm));

        fsm.createTransition(TransitionState.OTHER_INSUFFICIENT_ASP, AsState.ACTIVE.toString(),
                AsState.ACTIVE.toString()).setHandler(new AsTransActToActNtfyInsAsp(this, this.fsm));

        fsm.createTransition(TransitionState.AS_STATE_CHANGE_PENDING, AsState.ACTIVE.toString(), AsState.PENDING
                .toString());

        fsm.createTransition(TransitionState.ASP_DOWN, AsState.ACTIVE.toString(), AsState.PENDING.toString())
                .setHandler(new AsTransActToPen(this, this.fsm));

        // ******************************************************************/
        // STATE PENDING /
        // ******************************************************************/
        fsm.createTransition(TransitionState.AS_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString());
        fsm.createTransition(TransitionState.AS_INACTIVE, AsState.PENDING.toString(), AsState.INACTIVE.toString());

        // If in PENDING and one of the ASP is ACTIVE again
        fsm.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.PENDING.toString(), AsState.ACTIVE
                .toString());

        // If in PENDING and one (last) ASP is recovering from communication
        // down
        fsm.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.PENDING.toString(), AsState.INACTIVE
                .toString());

    }
}
