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

package org.mobicents.protocols.ss7.map.api.errors;

import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkResource;

/**
 * The factory of MAP ReturnError messages
 * 
 * @author sergey vetyutnev
 * 
 */
public interface MAPErrorMessageFactory {

	/**
	 * Generate the empty message depends of the error code (for incoming
	 * messages)
	 * 
	 * @param errorCode
	 * @return
	 */
	public MAPErrorMessage createMessageFromErrorCode(Long errorCode);

	public MAPErrorMessageParameterless createMAPErrorMessageParameterless(Long errorCode);

	public MAPErrorMessageExtensionContainer createMAPErrorMessageExtensionContainer(Long errorCode, MAPExtensionContainer extensionContainer);

	public MAPErrorMessageSMDeliveryFailure createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause,
			byte[] signalInfo, MAPExtensionContainer extensionContainer);

	public MAPErrorMessageFacilityNotSup createMAPErrorMessageFacilityNotSup(MAPExtensionContainer extensionContainer, Boolean shapeOfLocationEstimateNotSupported,
			Boolean neededLcsCapabilityNotSupportedInServingNode);

	public MAPErrorMessageSystemFailure createMAPErrorMessageSystemFailure(long mapVersion, NetworkResource networkResource,
			AdditionalNetworkResource additionalNetworkResource, MAPExtensionContainer extensionContainer);

	public MAPErrorMessageUnknownSubscriber createMAPErrorMessageUnknownSubscriber(MAPExtensionContainer extensionContainer,
			UnknownSubscriberDiagnostic unknownSubscriberDiagnostic);

	public MAPErrorMessageAbsentSubscriberSM createMAPErrorMessageAbsentSubscriberSM(Integer absentSubscriberDiagnosticSM,
			MAPExtensionContainer extensionContainer, Integer additionalAbsentSubscriberDiagnosticSM);

	public MAPErrorMessageSubscriberBusyForMtSms createMAPErrorMessageSubscriberBusyForMtSms(MAPExtensionContainer extensionContainer,
			Boolean gprsConnectionSuspended);

	public MAPErrorMessageCallBarred createMAPErrorMessageCallBarred(Long mapVersion, CallBarringCause callBarringCause,
			MAPExtensionContainer extensionContainer, Boolean unauthorisedMessageOriginator);

	public MAPErrorMessageAbsentSubscriber createMAPErrorMessageAbsentSubscriber(MAPExtensionContainer extensionContainer,
			AbsentSubscriberReason absentSubscriberReason);

	public MAPErrorMessageUnauthorizedLCSClient createMAPErrorMessageUnauthorizedLCSClient(UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic,
			MAPExtensionContainer extensionContainer);

	public MAPErrorMessagePositionMethodFailure createMAPErrorMessagePositionMethodFailure(PositionMethodFailureDiagnostic positionMethodFailureDiagnostic,
			MAPExtensionContainer extensionContainer);
}


