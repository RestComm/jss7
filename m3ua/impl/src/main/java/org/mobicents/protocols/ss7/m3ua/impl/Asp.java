package org.mobicents.protocols.ss7.m3ua.impl;

import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;

public abstract class Asp {

    protected String name;
    protected M3UAProvider m3UAProvider;

    protected FSM fsm;

    protected AspFactory aspFactory;

    protected As as;
    
    protected ASPIdentifier aSPIdentifier;

    public Asp(String name, M3UAProvider m3UAProvider, AspFactory aspFactroy) {
        this.name = name;
        this.m3UAProvider = m3UAProvider;
        this.aspFactory = aspFactroy;

        fsm = new FSM(this.name);
    }

    public String getName() {
        return this.name;
    }

    public AspState getState() {
        return AspState.getState(this.fsm.getState().getName());
    }

    public FSM getFSM() {
        return this.fsm;
    }

    public As getAs() {
        return as;
    }

    public void setAs(As as) {
        this.as = as;
    }
    
    public AspFactory getAspFactory(){
        return this.aspFactory;
    }

    public M3UAProvider getM3UAProvider() {
        return this.m3UAProvider;
    }
    
    public ASPIdentifier getASPIdentifier() {
        return aSPIdentifier;
    }

    public void setASPIdentifier(ASPIdentifier identifier) {
        aSPIdentifier = identifier;
    }

}
