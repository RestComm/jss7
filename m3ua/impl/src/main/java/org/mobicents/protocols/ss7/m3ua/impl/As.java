package org.mobicents.protocols.ss7.m3ua.impl;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

public abstract class As {

    private static final Logger logger = Logger.getLogger(As.class);

    public static final String ATTRIBUTE_ASP = "asp";

    protected int minAspActiveForLb = 1;

    // List of all the ASP's for this AS
    protected FastList<Asp> appServerProcs = new FastList<Asp>();

    protected String name;
    protected RoutingContext rc;
    private RoutingKey rk;
    protected TrafficModeType trMode;
    protected M3UAProvider m3UAProvider = null;

    protected final TrafficModeType defaultTrafModType;

    protected ConcurrentLinkedQueue<PayloadData> penQueue = new ConcurrentLinkedQueue<PayloadData>();

    // Queue for incoming Payload messages. Message received from peer
    protected ConcurrentLinkedQueue<PayloadData> rxQueue = new ConcurrentLinkedQueue<PayloadData>();

    protected FSM fsm;

    public As(String name, RoutingContext rc, RoutingKey rk, TrafficModeType trMode, M3UAProvider provider) {
        this.name = name;
        this.rc = rc;
        this.rk = rk;
        this.trMode = trMode;
        this.m3UAProvider = provider;

        this.defaultTrafModType = this.m3UAProvider.getParameterFactory().createTrafficModeType(
                TrafficModeType.Loadshare);

        fsm = new FSM(this.name);
    }

    public String getName() {
        return this.name;
    }

    public FastList<Asp> getAppServerProcess() {
        return this.appServerProcs;
    }

    public AsState getState() {
        return AsState.getState(this.fsm.getState().getName());
    }

    public RoutingContext getRoutingContext() {
        return this.rc;
    }

    public RoutingKey getRoutingKey() {
        return this.rk;
    }

    public void setTrafficModeType(TrafficModeType trMode) {
        // TODO : Check if TrafficModeType is not null throw error?
        this.trMode = trMode;
    }

    public TrafficModeType getTrafficModeType() {
        return this.trMode;
    }

    public void setDefaultTrafficModeType() {
        // TODO : Check if TrafficModeType is not null throw error?
        this.trMode = this.defaultTrafModType;
    }

    public M3UAProvider getM3UAProvider() {
        return m3UAProvider;
    }

    public void setMinAspActiveForLb(int lb) {
        this.minAspActiveForLb = lb;
    }

    public int getMinAspActiveForLb() {
        return this.minAspActiveForLb;
    }

    public FSM getFSM() {
        return this.fsm;
    }

    public void addAppServerProcess(Asp asp) throws Exception {
        // Check if already added?
        for (FastList.Node<Asp> n = this.appServerProcs.head(), end = this.appServerProcs.tail(); (n = n.getNext()) != end;) {
            Asp aspTemp = n.getValue();
            if (aspTemp.getName().compareTo(asp.getName()) == 0) {
                throw new Exception(String.format("Asp name=%s already added", asp.getName()));
            }
        }

        asp.setAs(this);
        appServerProcs.add(asp);
    }

    public void aspStateChange(Asp asp, String asTransition) throws UnknownTransitionException {
        this.fsm.setAttribute(ATTRIBUTE_ASP, asp);
        this.fsm.signal(asTransition);
    }

    public void write(PayloadData message) throws IOException {

        switch (this.getState()) {
        case ACTIVE:
            // TODO : Algo to select correct ASP
            for (FastList.Node<Asp> n = this.appServerProcs.head(), end = this.appServerProcs.tail(); (n = n.getNext()) != end;) {
                Asp aspTemp = n.getValue();
                if(aspTemp.getState() == AspState.ACTIVE){
                    aspTemp.getAspFactory().write(message);
                    break;
                }
            }
            
            break;
        case PENDING:
            this.penQueue.add(message);
            break;
        default:
            throw new IOException(String.format("As name=%s is not ACTIVE", this.name));
        }
    }

    public void received(PayloadData payload) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Received PayloadData=%s for As=%s", payload.toString(), this.name));
        }
        this.rxQueue.add(payload);
    }

    public PayloadData poll() {
        return this.rxQueue.poll();
    }
}
