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
 * Status holder for {@link PivotStatus}
 *
 * @author baranowb
 *
 */
public interface Status extends Serializable{

    int STATUS_NOT_USED = 0x00;
    int STATUS_ACK_OF_PIVOT_ROUTING = 0x01;
    int STATUS_WILL_NOT_BE_INVOKED = 0x02;
    int STATUS_SPARE = 0x03;

    byte getStatus();

    void setStatus(byte b);
}
