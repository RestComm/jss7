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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;

/**
 *
 AlertingPattern ::= OCTET STRING (SIZE(3))
 -- Indicates a specific pattern that is used to alert a subscriber
 -- (e.g. distinctive ringing, tones, etc.).
 -- The encoding of the last octet of this parameter is as defined in 3GPP TS 29.002 [11].
 -- Only the trailing OCTET is used, the remaining OCTETS shall be sent as NULL (zero)
 -- The receiving side shall ignore the leading two OCTETS.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AlertingPatternCap extends Serializable {

    byte[] getData();

    AlertingPattern getAlertingPattern();

}