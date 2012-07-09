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

package org.mobicents.protocols.ss7.cap.api.service.gprs;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

/**
*

applyChargingGPRS OPERATION ::= {
ARGUMENT ApplyChargingGPRSArg
RETURN RESULT FALSE
ERRORS {missingParameter |
unexpectedComponentSequence |
unexpectedParameter |
unexpectedDataValue |
parameterOutOfRange |
systemFailure |
taskRefused |
unknownPDPID}
CODE opcode-applyChargingGPRS}
-- Direction gsmSCF -> gprsSSF, Timer Tacg
-- This operation is used for interacting from the gsmSCF with the gprsSSF CSE-controlled
-- GPRS session or PDP Context charging mechanism.

ApplyChargingGPRSArg ::= SEQUENCE {
chargingCharacteristics [0] ChargingCharacteristics,
tariffSwitchInterval [1] INTEGER (1..86400) OPTIONAL,
pDPID [2] PDPID OPTIONAL,
...
}
-- tariffSwitchInterval is measured in 1 second units.


* 
* @author sergey vetyutnev
* 
*/
public interface ApplyChargingGPRSRequest extends GprsMessage {

	public ChargingCharacteristics getChargingCharacteristics();

	public Integer getTariffSwitchInterval();

	public PDPID getPDPID();

}
