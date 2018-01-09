/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpManagementEventListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpNoticeMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;

/**
 *
 * @author Oleg Kulikov
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class SccpProviderImpl implements SccpProvider, Serializable {
    private static final Logger logger = Logger.getLogger(SccpProviderImpl.class);

    private transient SccpStackImpl stack;
    protected FastMap<Integer, SccpListener> ssnToListener = new FastMap<Integer, SccpListener>();
    protected FastList<SccpManagementEventListener> managementEventListeners = new FastList<SccpManagementEventListener>();

    private MessageFactoryImpl messageFactory;
    private ParameterFactoryImpl parameterFactory;

    //<ssn - congestion level>
    private ConcurrentHashMap<Integer, Integer> congestionSsn = new ConcurrentHashMap<Integer, Integer>();

    SccpProviderImpl(SccpStackImpl stack) {
        this.stack = stack;
        this.messageFactory = stack.messageFactory;
        this.parameterFactory = new ParameterFactoryImpl();
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public ParameterFactory getParameterFactory() {
        return parameterFactory;
    }

    public void registerSccpListener(int ssn, SccpListener listener) {
        synchronized (this) {
            SccpListener existingListener = ssnToListener.get(ssn);
            if (existingListener != null) {
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String.format("Registering SccpListener=%s for already existing SccpListnere=%s for SSN=%d",
                            listener, existingListener, ssn));
                }
            }
            FastMap<Integer, SccpListener> newListener = new FastMap<Integer, SccpListener>();
            newListener.putAll(ssnToListener);
            newListener.put(ssn, listener);
            ssnToListener = newListener;

            this.stack.broadcastChangedSsnState(ssn, true);
        }
    }

    public void deregisterSccpListener(int ssn) {
        synchronized (this) {
            FastMap<Integer, SccpListener> newListener = new FastMap<Integer, SccpListener>();
            newListener.putAll(ssnToListener);
            SccpListener existingListener = newListener.remove(ssn);
            if (existingListener == null) {
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String.format("No existing SccpListnere=%s for SSN=%d", existingListener, ssn));
                }
            }
            ssnToListener = newListener;

            this.stack.broadcastChangedSsnState(ssn, false);
        }
    }

    public void registerManagementEventListener(SccpManagementEventListener listener) {
        synchronized (this) {
            if (this.managementEventListeners.contains(listener))
                return;

            FastList<SccpManagementEventListener> newManagementEventListeners = new FastList<SccpManagementEventListener>();
            newManagementEventListeners.addAll(this.managementEventListeners);
            newManagementEventListeners.add(listener);
            this.managementEventListeners = newManagementEventListeners;
        }
    }

    public void deregisterManagementEventListener(SccpManagementEventListener listener) {
        synchronized (this) {
            if (!this.managementEventListeners.contains(listener))
                return;

            FastList<SccpManagementEventListener> newManagementEventListeners = new FastList<SccpManagementEventListener>();
            newManagementEventListeners.addAll(this.managementEventListeners);
            newManagementEventListeners.remove(listener);
            this.managementEventListeners = newManagementEventListeners;
        }
    }

    protected SccpListener getSccpListener(int ssn) {
        return ssnToListener.get(ssn);
    }

    protected FastMap<Integer, SccpListener> getAllSccpListeners() {
        return ssnToListener;
    }

    @Override
    public void send(SccpDataMessage message) throws IOException {
        try{
            SccpDataMessageImpl msg = ((SccpDataMessageImpl) message);
            stack.send(msg);
        }catch(Exception e){
            throw new IOException(e);
        }
    }

    @Override
    public void send(SccpNoticeMessage message) throws IOException {
        try{
            SccpNoticeMessageImpl msg = ((SccpNoticeMessageImpl) message);
            stack.send(msg);
        }catch(Exception e){
            throw new IOException(e);
        }
    }

    public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId) {
        return this.stack.getMaxUserDataLength(calledPartyAddress, callingPartyAddress, msgNetworkId);
    }

    @Override
    public FastMap<Integer, NetworkIdState> getNetworkIdStateList() {
        return this.stack.router.getNetworkIdList(-1);
    }

    @Override
    public void coordRequest(int ssn) {
        // TODO Auto-generated method stub

    }

    @Override
    public ExecutorCongestionMonitor[] getExecutorCongestionMonitorList() {
        ArrayList<ExecutorCongestionMonitor> res = new ArrayList<ExecutorCongestionMonitor>();
        for (FastMap.Entry<Integer, Mtp3UserPart> e = this.stack.mtp3UserParts.head(), end = this.stack.mtp3UserParts.tail(); (e = e
                .getNext()) != end;) {
            Mtp3UserPart mup = e.getValue();
            ExecutorCongestionMonitor ecm = mup.getExecutorCongestionMonitor();
            if (ecm != null)
                res.add(ecm);
        }

        ExecutorCongestionMonitor[] ress = new ExecutorCongestionMonitor[res.size()];
        res.toArray(ress);
        return ress;
    }

    @Override
    public SccpStack getSccpStack() {
        return this.stack;
    }

    public ConcurrentHashMap<Integer, Integer> getCongestionSsn() {
        return this.congestionSsn;
    }

    @Override
    public void updateSPCongestion(Integer ssn, Integer congestionLevel) {
        congestionSsn.put(ssn, congestionLevel);
    }

}
