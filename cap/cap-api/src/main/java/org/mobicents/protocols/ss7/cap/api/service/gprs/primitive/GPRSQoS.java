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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;

/**
 *
 GPRS-QoS ::= CHOICE { short-QoS-format [0] QoS-Subscribed, long-QoS-format [1] Ext-QoS-Subscribed } -- Short-QoS-format shall
 * be sent for QoS in pre GSM release 99 format. -- Long-QoS-format shall be sent for QoS in GSM release 99 (and beyond) format.
 * -- Which of the two QoS formats shall be sent is determined by which QoS -- format is available in the SGSN at the time of
 * sending. -- Refer to 3GPP TS 29.002 [11] for encoding details of QoS-Subscribed and -- Ext-QoS-Subscribed.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GPRSQoS extends Serializable {

    QoSSubscribed getShortQoSFormat();

    ExtQoSSubscribed getLongQoSFormat();

}