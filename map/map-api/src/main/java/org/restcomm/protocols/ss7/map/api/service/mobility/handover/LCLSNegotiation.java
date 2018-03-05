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

package org.restcomm.protocols.ss7.map.api.service.mobility.handover;

import java.io.Serializable;

/**
 *
 LCLS-Negotiation::= BIT STRING { permission-indicator (0), forward-data-sending-indicator (1), backward-sending-indicator
 * (2), forward-data-reception-indicator (3), backward-data-reception-indicator (4)} (SIZE (5..8)) -- exception handling: bits 5
 * to 7 shall be ignored if received and not understood
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface LCLSNegotiation extends Serializable {

    boolean getPermissionIndicator();

    boolean getForwardDataSendingIndicator();

    boolean getBackwardSendingIndicator();

    boolean getForwardDataReceptionIndicator();

    boolean getBackwardDataReceptionIndicator();

}
