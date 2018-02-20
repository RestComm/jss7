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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

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
