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

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

/**
 *
 continueGPRS OPERATION ::= { ARGUMENT ContinueGPRSArg RETURN RESULT FALSE ERRORS {missingParameter | unknownPDPID | -
 * Direction: gsmSCF -> gprsSSF, Timer: Tcueg -- This operation is used to request the gprsSSF to proceed with processing at the
 * DP at -- which it previously suspended processing to await gsmSCF instructions (i.e., proceed to -- the next point in
 * processing in the Attach/Detach state model or PDP Context -- state model) substituting new data from the gsmSCF.
 *
 * ContinueGPRSArg ::= SEQUENCE { pDPID [0] PDPID OPTIONAL, ... }
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ContinueGPRSRequest extends GprsMessage {

    PDPID getPDPID();

}
