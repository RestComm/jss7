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
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;

/**
 *
 ISUP CauseIndicators wrapper
 *
 * Cause {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minCauseLength .. bound.&maxCauseLength)) -- Indicates the
 * cause for interface related information. -- Refer to ETSI EN 300 356-1 [23] Cause parameter for encoding. -- For the use of
 * cause and location values refer to ITU-T Recommendation Q.850 [47] -- Shall always include the cause value and shall also
 * include the diagnostics field, -- if available.
 *
 * minCauseLength ::= 2 maxCauseLength ::= 32
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CauseCap extends Serializable {

    byte[] getData();

    CauseIndicators getCauseIndicators() throws CAPException;

}