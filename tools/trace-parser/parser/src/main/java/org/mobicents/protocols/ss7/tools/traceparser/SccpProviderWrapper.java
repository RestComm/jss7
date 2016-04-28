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

package org.mobicents.protocols.ss7.tools.traceparser;

import java.io.IOException;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpManagementEventListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SccpProviderWrapper implements SccpProvider {

    private SccpStackImpl stack;

    public SccpProviderWrapper(SccpStackImpl stack) {
        this.stack = stack;
    }

    public MessageFactory getMessageFactory() {
        return new MessageFactoryImpl(this.stack);
    }

    public ParameterFactory getParameterFactory() {
        return new ParameterFactoryImpl();
    }

    public void registerSccpListener(int ssn, SccpListener listener) {
        // TODO Auto-generated method stub

    }

    public void deregisterSccpListener(int ssn) {
        // TODO Auto-generated method stub

    }

    public void send(SccpMessage message, int seqControl) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getMaxUserDataLength(SccpAddress arg0, SccpAddress arg1, int networkId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void send(SccpDataMessage arg0) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerManagementEventListener(SccpManagementEventListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deregisterManagementEventListener(SccpManagementEventListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void coordRequest(int ssn) {
        // TODO Auto-generated method stub

    }

    @Override
    public FastMap<Integer, NetworkIdState> getNetworkIdStateList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExecutorCongestionMonitor[] getExecutorCongestionMonitorList() {
        // TODO Auto-generated method stub
        return null;
    }

}
