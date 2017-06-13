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

package org.mobicents.protocols.ss7.sccp.message;

import java.io.Serializable;

import org.mobicents.protocols.ss7.sccp.parameter.Credit;
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

    /**
     * Create a SCCP connection request message (class 2)
     *
     * @param calledParty
     * @param callingParty This parameter is optional
     * @param data       This parameter is optional
     * @param importance This parameter is optional
     * @return
     */
    SccpConnCrMessage createConnectMessageClass2(int localSsn, SccpAddress calledParty, SccpAddress callingParty, byte[] data, Importance importance);

    /**
     * Create a SCCP connection request message (class 3)
     *
     * @param calledParty
     * @param callingParty This parameter is optional
     * @param credit
     * @param data       This parameter is optional
     * @param importance This parameter is optional
     * @return
     */
    SccpConnCrMessage createConnectMessageClass3(int localSsn, SccpAddress calledParty, SccpAddress callingParty, Credit credit, byte[] data, Importance importance);
}
