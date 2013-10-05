/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.isup.message.parameter;

import java.io.Serializable;

/**
 * @author baranowb
 *
 */
public interface PivotReason extends Serializable{

    byte REASON_UKNOWN = 0;
    byte REASON_SERVICE_PROVIDER_PORTABILITY = 0x01;
    byte REASON_RESERVER_FOR_LOCAL_PORTABILITY = 0x02;
    byte REASON_RESERVER_FOR_SERVICE_PORTABILITY = 0x03;

    byte PERFORMING_EXCHANGE_NO_INDICATION = 0;
    byte PERFORMING_EXCHANGE_POSSIBLE_BEFORE_ACM = 0x01;
    byte PERFORMING_EXCHANGE_POSSIBLE_BEFORE_ANM = 0x02;
    byte PERFORMING_EXCHANGE_ANY_TIME = 0x03;

    byte getPivotReason();

    void setPivotReason(byte b);

    byte getPivotPossibleAtPerformingExchange();

    void setPivotPossibleAtPerformingExchange(byte b);
}
