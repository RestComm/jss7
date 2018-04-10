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

package org.restcomm.protocols.ss7.sccp.impl;

import javolution.util.FastMap;

import org.restcomm.protocols.ss7.sccp.NetworkIdState;
import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpAddressedMessageImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtSccpInterface;

/**
*
* @author sergey vetyutnev
*
*/
public interface Ss7ExtSccpDetailedInterface extends Ss7ExtSccpInterface {

    /**
     * Initiating of sccp-ext
     * @param sccpStackImpl
     */
    void init(SccpStackImpl sccpStackImpl);

    /**
     * Invoking before of sccp stack start for starting of sccp-ext
     * @param persistDir
     * @param name
     */
    void startExtBefore(String persistDir, String name);

    /**
     * Invoking after of sccp stack start for starting of sccp-ext
     * @param router
     * @param sccpManagement
     */
    void startExtAfter(Router router, SccpManagement sccpManagement);

    /**
     * Invoking before of sccp stack stop for stopping of sccp-ext
     */
    void stopExt();

    /**
     * Force of removing of configured SCCP resources at sccp-ext level
     */
    void removeAllResourses();

    /**
     * Getting of dpc o which a message will be routed depending on calledPartyAddress, callingPartyAddress andmsgNetworkId
     * @param calledPartyAddress
     * @param callingPartyAddress
     * @param msgNetworkId
     * @return
     */
    int findDpsForAddresses(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId);

    /**
     * Invoking of a GT translatio function
     * @param ctx
     * @param msg
     * @throws Exception
     */
    void translationFunction(SccpRoutingCtxInterface ctx, SccpAddressedMessageImpl msg) throws Exception;

    /**
     * Invoked when a remote signal point becomes allowed
     * @param affectedPc
     * @param remoteSccpStatus
     */
    void onAllowRsp(int affectedPc, RemoteSccpStatus remoteSccpStatus);

    /**
     * Invoked when a remote signal point becomes prohibited
     * @param affectedPc
     * @param remoteSccpStatus
     * @param remoteSpc
     */
    void onProhibitRsp(int affectedPc, RemoteSccpStatus remoteSccpStatus, RemoteSignalingPointCodeImpl remoteSpc);

    /**
     * Invoked when a congestion level for a remote signal point was changed of defined
     * @param affectedPc
     * @param restrictionLevel
     * @param levelEncreased
     */
    void onRestrictionLevelChange(int affectedPc, int restrictionLevel, boolean levelEncreased);

    /**
     * Returns a list of networkId areas with a congestion / availability states
     * @param affectedPc
     * @return
     */
    FastMap<Integer, NetworkIdState> getNetworkIdList(int affectedPc);

    /**
     * Adding of an extension part of a remote signal point when a RemoteSignalingPoint creating
     * @param remoteSignalingPointCodeImpl
     * @return
     */
    RemoteSignalingPointCodeExt createRemoteSignalingPointCodeExt(RemoteSignalingPointCodeImpl remoteSignalingPointCodeImpl);

}
