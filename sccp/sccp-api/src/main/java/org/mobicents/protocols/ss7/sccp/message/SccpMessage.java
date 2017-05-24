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
    int MESSAGE_TYPE_CR = 0x01;
    int MESSAGE_TYPE_CC = 0x02;
    int MESSAGE_TYPE_CREF = 0x03;
    int MESSAGE_TYPE_RLSD = 0x04;
    int MESSAGE_TYPE_RLC = 0x05;
    int MESSAGE_TYPE_DT1 = 0x06;
    int MESSAGE_TYPE_DT2 = 0x07;
    int MESSAGE_TYPE_AK = 0x08;
    int MESSAGE_TYPE_RSR = 0x0D;
    int MESSAGE_TYPE_RSC = 0x0E;
    int MESSAGE_TYPE_ERR = 0x0F;
    int MESSAGE_TYPE_IT = 0x10;
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

    int getNetworkId();

    void setNetworkId(int networkId);

}
