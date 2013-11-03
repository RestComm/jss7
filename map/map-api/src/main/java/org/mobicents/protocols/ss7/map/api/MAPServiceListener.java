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

package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface MAPServiceListener {
    /**
     * Invoked when TC-U-ERROR primitive is received from the other peer
     *
     * @param mapDialog
     * @param invokeId
     * @param mapErrorMessage
     */
    void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage);

    /**
     * Invoked when the provider error when parsing the response
     *
     * @param mapDialog
     * @param invokeId
     * @param providerError
     */
    // void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError);

    /**
     * Invoked when TC-U-REJECT primitive is received from the other peer
     *
     * @param mapDialog
     * @param invokeId This parameter is optional and may be the null
     * @param problem
     * @param isLocalOriginated true: local originated Reject (rejecting a bad incoming primitive by a local side) false: remote
     *        originated Reject (rejecting a bad outgoing primitive by a peer)
     */
    void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated);

    /**
     * Invoked when no answer from the other peer for a long time - for sending the a reject for the Invoke
     *
     * @param mapDialog
     * @param invokeId
     */
    void onInvokeTimeout(MAPDialog mapDialog, Long invokeId);

    /**
     * Called when any MAPMessage received (Invoke, ReturnResult, ReturnResultLast components) This component will be invoked
     * before the special service component
     *
     * @param mapDialog
     */
    void onMAPMessage(MAPMessage mapMessage);

}
