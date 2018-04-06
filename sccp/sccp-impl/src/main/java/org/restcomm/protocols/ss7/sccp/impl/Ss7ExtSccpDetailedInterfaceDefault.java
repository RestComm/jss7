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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.NetworkIdState;
import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpAddressedMessageImpl;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
*
* @author sergey vetyutnev
*
*/
public class Ss7ExtSccpDetailedInterfaceDefault implements Ss7ExtSccpDetailedInterface {
    private Logger logger;

    @Override
    public void init(SccpStackImpl sccpStackImpl) {
        this.logger = Logger.getLogger(Ss7ExtSccpDetailedInterfaceDefault.class.getCanonicalName() + "-" + sccpStackImpl.getName());
    }

    @Override
    public void startExtBefore(String persistDir, String name) {
    }

    @Override
    public void startExtAfter(Router router, SccpManagement sccpManagement) {
    }

    @Override
    public void stopExt() {
    }

    @Override
    public void removeAllResourses() {
    }

    @Override
    public int findDpsForAddresses(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId) {
        return -1;
    }

    @Override
    public void translationFunction(SccpRoutingCtxInterface ctx, SccpAddressedMessageImpl msg) throws Exception {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(String.format(
                    "Ss7ExtSccpDetailedInterfaceDefault does not support routing based on GT\nSccpMessage=%s", msg));
        }
        ctx.sendSccpError(msg, ReturnCauseValue.ERR_IN_LOCAL_PROCESSING, RefusalCauseValue.DESTINATION_ADDRESS_UNKNOWN);
    }

    @Override
    public void onAllowRsp(int affectedPc, RemoteSccpStatus remoteSccpStatus) {
    }

    @Override
    public void onProhibitRsp(int affectedPc, RemoteSccpStatus remoteSccpStatus, RemoteSignalingPointCodeImpl remoteSpc) {
    }

    @Override
    public void onRestrictionLevelChange(int affectedPc, int restrictionLevel, boolean levelEncreased) {
    }

    @Override
    public FastMap<Integer, NetworkIdState> getNetworkIdList(int affectedPc) {
        return new FastMap<Integer, NetworkIdState>();
    }

    @Override
    public RemoteSignalingPointCodeExt createRemoteSignalingPointCodeExt(
            RemoteSignalingPointCodeImpl remoteSignalingPointCodeImpl) {
        return new RemoteSignalingPointCodeExtDefault();
    }

}
