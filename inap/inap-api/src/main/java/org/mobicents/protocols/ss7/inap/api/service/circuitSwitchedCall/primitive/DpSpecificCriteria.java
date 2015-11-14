/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
*
<code>
DpSpecificCriteria {PARAMETERS-BOUND : bound} ::= CHOICE {
  numberOfDigits     [0] NumberOfDigits,
  applicationTimer   [1] ApplicationTimer,
  midCallControlInfo [2] MidCallControlInfo {bound}
}
-- The SCF may specify the number of digits to be collected by the SSF
-- for the EventReportBCSMEvent event.
-- When all digits are collected, the SSF reports the event to the SCF.
-- The SCF may set a timer in the SSF for the No Answer event. If the user does not answer the call
-- within the allotted time, the SSF reports the event to the SCF

NumberOfDigits ::= INTEGER (1..255)
-- Indicates the number of digits to be collected

ApplicationTimer ::=INTEGER (0..2047)
-- Used by the SCF to set a timer in the SSF. The timer is in seconds.
</code>
*
*
* @author sergey vetyutnev
*
*/
public interface DpSpecificCriteria extends Serializable {

    Integer getNumberOfDigits();

    Integer getApplicationTimer();

    MidCallControlInfo getMidCallControlInfo();

}
