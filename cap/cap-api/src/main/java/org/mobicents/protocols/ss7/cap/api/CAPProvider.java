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

package org.mobicents.protocols.ss7.cap.api;

import java.io.Serializable;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPServiceGprs;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPServiceSms;
import org.mobicents.protocols.ss7.inap.api.INAPParameterFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPProvider extends Serializable {

    /**
     * Add CAP Dialog listener to the Stack
     *
     * @param capDialogListener
     */
    void addCAPDialogListener(CAPDialogListener capDialogListener);

    /**
     * Remove CAP DIalog Listener from the stack
     *
     * @param capDialogListener
     */
    void removeCAPDialogListener(CAPDialogListener capDialogListener);

    /**
     * Get the {@link CAPParameterFactory}
     *
     * @return
     */
    CAPParameterFactory getCAPParameterFactory();

    /**
     * Get the {@link MAPParameterFactory}
     *
     * @return
     */
    MAPParameterFactory getMAPParameterFactory();

    /**
     * Get the {@link ISUPParameterFactory}
     *
     * @return
     */
    ISUPParameterFactory getISUPParameterFactory();

    /**
     * Get the {@link INAPParameterFactory}
     *
     * @return
     */
    INAPParameterFactory getINAPParameterFactory();

    /**
     * Get the {@link CAPErrorMessageFactory}
     *
     * @return
     */
    CAPErrorMessageFactory getCAPErrorMessageFactory();

    /**
     * Get {@link CAPDialog} corresponding to passed dialogId
     *
     * @param dialogId
     * @return
     */
    CAPDialog getCAPDialog(Long dialogId);

    CAPServiceCircuitSwitchedCall getCAPServiceCircuitSwitchedCall();

    CAPServiceGprs getCAPServiceGprs();

    CAPServiceSms getCAPServiceSms();

    /**
     * The collection of netwokIds that are marked as prohibited or congested.
     *
     * @return The collection of pairs: netwokId value - NetworkIdState (prohibited / congested state)
     */
    FastMap<Integer, NetworkIdState> getNetworkIdStateList();

    /**
     * Returns the state of availability / congestion for a networkId subnetwork. Returns null if there is no info (we need to
     * treat it as available)
     *
     * @param networkId
     * @return
     */
    NetworkIdState getNetworkIdState(int networkId);

    /**
     * Setting of a congestion level for a TCAP user "congObject"
     *
     * @param congObject a String with the name of an object
     * @param level a congestion level for this object
     */
    void setUserPartCongestionLevel(String congObject, int level);

    /**
     * Returns a congestion level of a Memory congestion monitor
     *
     * @return
     */
    int getMemoryCongestionLevel();

    /**
     * Returns a congestion level of thread Executors for processing of incoming messages
     *
     * @return
     */
    int getExecutorCongestionLevel();

    /**
     * Returns a max congestion level for UserPartCongestion, MemoryCongestion and ExecutorCongestionLevel
     *
     * @return
     */
    int getCumulativeCongestionLevel();

    /**
     * @return current count of active TCAP dialogs
     */
    int getCurrentDialogsCount();

}
