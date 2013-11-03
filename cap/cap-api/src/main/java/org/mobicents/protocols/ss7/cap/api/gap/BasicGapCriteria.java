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

package org.mobicents.protocols.ss7.cap.api.gap;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.isup.Digits;

/**
 *
 BasicGapCriteria {PARAMETERS-BOUND : bound} ::= CHOICE { calledAddressValue [0] Digits {bound}, gapOnService [2]
 * GapOnService, calledAddressAndService [29] SEQUENCE { calledAddressValue [0] Digits {bound}, serviceKey [1] ServiceKey, ...
 * }, callingAddressAndService [30] SEQUENCE { callingAddressValue [0] Digits {bound}, serviceKey [1] ServiceKey, ... } } --
 * Both calledAddressValue and callingAddressValue can be -- incomplete numbers, in the sense that a limited amount of digits
 * can be given. -- For the handling of numbers starting with the same digit string refer to the detailed -- procedure of the
 * CallGap operation
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface BasicGapCriteria extends Serializable {

    Digits getCalledAddressValue();

    GapOnService getGapOnService();

    CalledAddressAndService getCalledAddressAndService();

    CallingAddressAndService getCallingAddressAndService();

}