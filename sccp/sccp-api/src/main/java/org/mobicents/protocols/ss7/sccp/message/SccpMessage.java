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

/**
 * Specifies the SCCP messages formats and codes for the support of connection-oriented services, connectionless services and
 * the management of SCCP.
 *
 * A SCCP message consists of the following parts:
 * <ul>
 * <li>the message type code;</li>
 * <li>the mandatory fixed part;</li>
 * <li>the mandatory variable part;</li>
 * <li>the optional part, which may contain fixed length and variable length fields.</li>
 * </ul>
 *
 * @author baranowb
 * @author kulikov
 * @author sergey vetyutnev
 */
public interface SccpMessage {

    int MESSAGE_TYPE_UNDEFINED = -1;
    int MESSAGE_TYPE_UDT = 0x09;
    int MESSAGE_TYPE_UDTS = 0x0A;
    int MESSAGE_TYPE_XUDT = 0x11;
    int MESSAGE_TYPE_XUDTS = 0x12;
    int MESSAGE_TYPE_LUDT = 0x13;
    int MESSAGE_TYPE_LUDTS = 0x14;

    /**
     * The message type code consists of a one octet field and is mandatory for all messages. The message type code uniquely
     * defines the function and format of each SCCP message.
     *
     * @return message type code
     */
    int getType();

    boolean getIsMtpOriginated();

    int getOriginLocalSsn();

    int getSls();

    int getIncomingOpc();

    void setIncomingOpc(int opc);

    int getIncomingDpc();

}
