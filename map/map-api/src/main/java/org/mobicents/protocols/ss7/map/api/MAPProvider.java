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

package org.mobicents.protocols.ss7.map.api;

import java.io.Serializable;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandling;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.mobicents.protocols.ss7.map.api.service.oam.MAPServiceOam;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivation;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.protocols.ss7.sccp.NetworkIdState;

/**
 *
 * @author amit bhayani
 *
 */
public interface MAPProvider extends Serializable {

    // int NETWORK_UNSTRUCTURED_SS_CONTEXT_V2 = 1;

    /**
     * Add MAP Dialog listener to the Stack
     *
     * @param mapDialogListener
     */
    void addMAPDialogListener(MAPDialogListener mapDialogListener);

    /**
     * Remove MAP DIalog Listener from the stack
     *
     * @param mapDialogListener
     */
    void removeMAPDialogListener(MAPDialogListener mapDialogListener);

    /**
     * Get the {@link MAPParameterFactory}
     *
     * @return
     */
    MAPParameterFactory getMAPParameterFactory();

    /**
     * Get the {@link MAPErrorMessageFactory}
     *
     * @return
     */
    MAPErrorMessageFactory getMAPErrorMessageFactory();

    /**
     * Get {@link MAPDialog} corresponding to passed dialogId
     *
     * @param dialogId
     * @return
     */
    MAPDialog getMAPDialog(Long dialogId);

    /**
     *
     * @return
     */
    MAPSmsTpduParameterFactory getMAPSmsTpduParameterFactory();

    MAPServiceMobility getMAPServiceMobility();

    MAPServiceCallHandling getMAPServiceCallHandling();

    MAPServiceOam getMAPServiceOam();

    MAPServicePdpContextActivation getMAPServicePdpContextActivation();

    MAPServiceSupplementary getMAPServiceSupplementary();

    MAPServiceSms getMAPServiceSms();

    MAPServiceLsm getMAPServiceLsm();

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

}
