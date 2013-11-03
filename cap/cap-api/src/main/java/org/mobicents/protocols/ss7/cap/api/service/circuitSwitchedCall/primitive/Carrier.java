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

/**
 *
ISUP Carrier wrapper

Carrier {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minCarrierLength .. bound.&maxCarrierLength))
-- This parameter is used for North America (na) only.
-- It contains the carrier selection field (first octet) followed by Carrier ID
-- information (North America (na)).

-- The Carrier selection is one octet and is encoded as:
-- 00000000 No indication
-- 00000001 Selected carrier identification code (CIC) pre subscribed and not
-- input by calling party
-- 00000010 Selected carrier identification code (CIC) pre subscribed and input by
-- calling party
-- 00000011 Selected carrier identification code (CIC) pre subscribed, no
-- indication of whether input by calling party (undetermined)
-- 00000100 Selected carrier identification code (CIC) not pre-subscribed and input by calling party
-- 00000101 to 11111110 - Spare
-- 11111111 Reserved

-- Refer to ANSI T1.113-1995 [92] for encoding of na carrier ID information (3 octets).
 *
 * minCarrierLength ::= 4 maxCarrierLength ::= 4
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface Carrier extends Serializable {

    byte[] getData();

    // TODO: internal structure is not covered

}