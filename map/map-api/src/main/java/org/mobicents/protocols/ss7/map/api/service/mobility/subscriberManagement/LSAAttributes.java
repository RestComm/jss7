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
 LSAAttributes ::= OCTET STRING (SIZE (1)) -- Octets are coded according to TS 3GPP TS 48.008 [49]
 *
 * --Bits 1 to 4 of octet (x+1) define the priority of the LSA identification. --Bit 4321 -- 0000 priority 1 = lowest priority
 * -- 0001 priority 2 = second lowest priority -- : : : : -- 1111 priority 16= highest priority
 *
 * --If the preferential access indicator (bit 5 of octet (x+1)) is set to 1 the subscriber has preferential access in the LSA.
 * If --the active mode support indicator (bit 6 of octet (x+1)) is set to 1 the subscriber has active mode support in the LSA.
 *
 * --Coding of the i-th LSA identification with attributes: --8 7 6 5 4 3 2 1 --spare act pref priority
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface LSAAttributes extends Serializable {

    int getData();

    LSAIdentificationPriorityValue getLSAIdentificationPriority();

    boolean isPreferentialAccessAvailable();

    boolean isActiveModeSupportAvailable();
}
