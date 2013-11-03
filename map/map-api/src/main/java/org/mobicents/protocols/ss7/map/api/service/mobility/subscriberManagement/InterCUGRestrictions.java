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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

/**
 *
 InterCUG-Restrictions ::= OCTET STRING (SIZE (1))
 *
 * -- bits 876543: 000000 (unused) -- Exception handling: -- bits 876543 shall be ignored if received and not understood
 *
 * -- bits 21 -- 00 CUG only facilities -- 01 CUG with outgoing access -- 10 CUG with incoming access -- 11 CUG with both
 * outgoing and incoming access
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InterCUGRestrictions extends Serializable {

    int getData();

    InterCUGRestrictionsValue getInterCUGRestrictionsValue();

}
