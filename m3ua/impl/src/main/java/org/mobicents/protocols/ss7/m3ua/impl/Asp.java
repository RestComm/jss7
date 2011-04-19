/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
