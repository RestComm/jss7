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
     * Indication of N-COORD request
     *
     * @param dpc
     * @param ssn
     * @param multiplicityIndicator
     */
    void onCoordRequest(int dpc, int ssn, int multiplicityIndicator);

    /**
     * Indication of N-COORD response
     *
     * @param dpc
     * @param ssn
     * @param multiplicityIndicator
     */
    void onCoordResponse(int dpc, int ssn, int multiplicityIndicator);

    /**
     * Indication of N-STATE (subsystem state)
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
    void onPcState(int dpc, SignallingPointStatus status, int restrictedImportanceLevel,
            RemoteSccpStatus remoteSccpStatus);

}
