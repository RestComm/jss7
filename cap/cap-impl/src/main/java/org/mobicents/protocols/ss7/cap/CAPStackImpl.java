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

package org.mobicents.protocols.ss7.cap;

import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.CAPStack;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPStackImpl implements CAPStack {

    protected TCAPStack tcapStack = null;

    protected CAPProviderImpl capProvider = null;

    private State state = State.IDLE;

    private final String name;

    public CAPStackImpl(String name, SccpProvider sccpPprovider, int ssn) {
        this.name = name;
        this.tcapStack = new TCAPStackImpl(name, sccpPprovider, ssn);
        TCAPProvider tcapProvider = tcapStack.getProvider();
        capProvider = new CAPProviderImpl(name, tcapProvider);

        this.state = State.CONFIGURED;
    }

    public CAPStackImpl(String name, TCAPProvider tcapProvider) {
        this.name = name;
        capProvider = new CAPProviderImpl(name, tcapProvider);
        this.tcapStack = tcapProvider.getStack();
        this.state = State.CONFIGURED;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CAPProvider getCAPProvider() {
        return this.capProvider;
    }

    @Override
    public void start() throws Exception {
        if (state != State.CONFIGURED) {
            throw new IllegalStateException("Stack has not been configured or is already running!");
        }
        if (tcapStack != null) {
            // this is null in junits!
            this.tcapStack.start();
        }
        this.capProvider.start();

        this.state = State.RUNNING;

    }

    @Override
    public void stop() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Stack is not running!");
        }
        this.capProvider.stop();
        if (tcapStack != null) {
            this.tcapStack.stop();
        }

        this.state = State.CONFIGURED;
    }

    @Override
    public TCAPStack getTCAPStack() {
        return this.tcapStack;
    }

    private enum State {
        IDLE, CONFIGURED, RUNNING;
    }

}
