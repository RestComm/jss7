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

package org.mobicents.protocols.ss7.sccp;

import java.io.Serializable;

import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;

/**
 *
 * @author Oleg Kulikov
 * @author baranowb
 * @author serey vetyutnev
 */
public interface SccpListener extends Serializable {

    /**
     * Called when proper data is received, it is partially decoded. This method is called with message payload.
     *
     * @param message Message received
     */
    void onMessage(SccpDataMessage message);

    /**
     * Called when N-NOTICE indication (on receiving UDTS, XUDTS or LUDTS) N-NOTICE indication is the means by which the SCCP
     * returns to the originating user a SCCP-SDU which could not reach the final destination.
     *
     * @param message Message received
     */
    void onNotice(SccpNoticeMessage message);

    /**
     * Indication of N-COORD response when the originator (SCCP user) is informed that it may go out-of-service
     *
     * @param ssn
     * @param multiplicityIndicator
     */
    void onCoordResponse(int ssn, int multiplicityIndicator);

    /**
     * Indication of N-STATE (another local subsystem state change - UIS / UOS)
     *
     * @param dpc
     * @param ssn
     * @param inService
     * @param multiplicityIndicator
     */
    void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator);

    /**
     * Indication of N-PCSTATE (pointcode state)
     *
     * @param dpc
     * @param status
     * @param restrictedImportanceLevel
     * @param remoteSccpStatus
     */
    void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel, RemoteSccpStatus remoteSccpStatus);

    /**
     * Reporting of changing of availability / congestion state for a networkId
     *
     * @param networkId
     * @param networkIdState
     */
    void onNetworkIdState(int networkId, NetworkIdState networkIdState);

}
