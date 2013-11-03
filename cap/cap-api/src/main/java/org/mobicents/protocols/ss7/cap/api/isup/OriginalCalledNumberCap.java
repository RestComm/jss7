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

package org.mobicents.protocols.ss7.cap.api.isup;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;

/**
 *
 ISUP OriginalCalledNumber wrapper
 *
 * OriginalCalledPartyID {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minOriginalCalledPartyIDLength ..
 * bound.&maxOriginalCalledPartyIDLength)) -- Indicates the original called number. Refer to ETSI EN 300 356-1 [23] Original
 * Called Number -- for encoding.
 *
 * minOriginalCalledPartyIDLength ::= 2 maxOriginalCalledPartyIDLength ::= 10
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface OriginalCalledNumberCap extends Serializable {

    byte[] getData();

    OriginalCalledNumber getOriginalCalledNumber() throws CAPException;

}