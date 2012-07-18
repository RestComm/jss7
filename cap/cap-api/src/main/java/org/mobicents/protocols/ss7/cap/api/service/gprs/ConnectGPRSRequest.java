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

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

/**
*

connectGPRS {PARAMETERS-BOUND: bound} OPERATION::= {
ARGUMENT ConnectGPRSArg {bound}
RETURN RESULT FALSE
ERRORS {missingParameter |
parameterOutOfRange |
unknownPDPID |
systemFailure |
taskRefused |
unexpectedComponentSequence |
unexpectedDataValue |
unexpectedParameter}
CODE opcode-connectGPRS}
-- Direction: gsmSCF -> gprsSSF, Timer: Tcong
-- This operation is used to modify the Access Point Name used when establishing a PDP Context.

ConnectGPRSArg {PARAMETERS-BOUND: bound}::= SEQUENCE {
accessPointName [0] AccessPointName {bound},
pdpID [1] PDPID OPTIONAL,
...
}


* 
* @author sergey vetyutnev
* 
*/
public interface ConnectGPRSRequest extends GprsMessage {

	public AccessPointName getAccessPointName();

	public PDPID getPDPID();

}
