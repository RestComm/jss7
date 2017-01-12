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

package org.mobicents.protocols.ss7.cap.api.gap;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.isup.Digits;

/**
 *
<code>
BasicGapCriteria {PARAMETERS-BOUND : bound} ::= CHOICE {
  calledAddressValue         [0] Digits {bound},
  gapOnService               [2] GapOnService,
  calledAddressAndService    [29] SEQUENCE {
    calledAddressValue [0] Digits {bound},
    serviceKey         [1] ServiceKey,
    ...
  },
  callingAddressAndService   [30] SEQUENCE {
    callingAddressValue [0] Digits {bound},
    serviceKey          [1] ServiceKey,
    ...
  }
}
-- Both calledAddressValue and callingAddressValue can be
-- incomplete numbers, in the sense that a limited amount of digits can be given.
-- For the handling of numbers starting with the same digit string refer to the detailed
-- procedure of the CallGap operation
</code>
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
