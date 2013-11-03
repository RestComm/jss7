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

package org.mobicents.protocols.ss7.sccp.message;

import java.io.Serializable;

import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Factory for creating messages.
 *
 * @author baranowb
 * @author kulikov
 * @author sergey vetyutnev
 *
 */
public interface MessageFactory extends Serializable {

    /**
     * Create a SCCP data transfer message (class 0)
     *
     * @param calledParty
     * @param callingParty
     * @param data
     * @param localSsn
     * @param returnMessageOnError
     * @param hopCounter This parameter is optional
     * @param importance This parameter is optional
     * @return
     */
    SccpDataMessage createDataMessageClass0(SccpAddress calledParty, SccpAddress callingParty, byte[] data,
            int localSsn, boolean returnMessageOnError, HopCounter hopCounter, Importance importance);

    /**
     * Create a SCCP data transfer message (class 1)
     *
     * @param calledParty
     * @param callingParty
     * @param data
     * @param sls
     * @param localSsn
     * @param returnMessageOnError
     * @param hopCounter This parameter is optional
     * @param importance This parameter is optional
     * @return
     */
    SccpDataMessage createDataMessageClass1(SccpAddress calledParty, SccpAddress callingParty, byte[] data, int sls,
            int localSsn, boolean returnMessageOnError, HopCounter hopCounter, Importance importance);

    // SccpNoticeMessage createNoticeMessage(ReturnCause returnCause, int outgoingSls, SccpAddress calledParty,
    // SccpAddress callingParty, byte[] data,
    // HopCounter hopCounter, Importance importance);

}
