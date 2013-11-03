/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;
import java.io.Serializable;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpManagementEventListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

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

    public void send(SccpDataMessage message) throws IOException {

        SccpDataMessageImpl msg = ((SccpDataMessageImpl) message);
        stack.send(msg);
    }

    public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress) {
        return this.stack.getMaxUserDataLength(calledPartyAddress, callingPartyAddress);
    }
}
