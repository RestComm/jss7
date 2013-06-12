/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancel;

/**
 *
 cancel {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT CancelArg {bound} RETURN RESULT FALSE ERRORS {cancelFailed |
 * missingParameter | taskRefused | unknownCSID} CODE opcode-cancel} -- Direction: gsmSCF -> gsmSSF, or gsmSCF -> gsmSRF, Timer:
 * T can -- This operation cancels the correlated previous operation or all previous requests. The following -- operations can
 * be canceled: PlayAnnouncement, PromptAndCollectUserInformation.
 *
 * CancelArg {PARAMETERS-BOUND : bound} ::= CHOICE { invokeID [0] InvokeID, allRequests [1] NULL, callSegmentToCancel [2]
 * CallSegmentToCancel {bound} } -- The InvokeID has the same value as that which was used for the operation to be cancelled.
 *
 * InvokeID ::= TCInvokeIdSet (from TCAP)
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CancelRequest extends CircuitSwitchedCallMessage {

    Integer getInvokeID();

    boolean getAllRequests();

    CallSegmentToCancel getCallSegmentToCancel();

}
