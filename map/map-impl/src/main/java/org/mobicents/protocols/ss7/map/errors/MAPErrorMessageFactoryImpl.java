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

package org.mobicents.protocols.ss7.map.errors;

import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.errors.AdditionalNetworkResource;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageExtensionContainer;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageParameterless;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.map.api.errors.NetworkResource;
import org.mobicents.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;

/**
 * The factory of MAP ReturnError messages
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPErrorMessageFactoryImpl implements MAPErrorMessageFactory {

	/**
	 * Generate the empty message depends of the error code (for incoming messages)
	 * 
	 * @param errorCode
	 * @return
	 */
	public MAPErrorMessage createMessageFromErrorCode(Long errorCode) {
		long ec = (long) errorCode;
		switch ((int) ec) {
		// ...............................
		// TODO: implement creating other message types
		
		default:
			return new MAPErrorMessageParameterlessImpl(errorCode);
		}
		
	}

	
	public MAPErrorMessageParameterless createMessageParameterless(Long errorCode) {
		return new MAPErrorMessageParameterlessImpl(errorCode);
	}

	public MAPErrorMessageExtensionContainer createMessageExtensionContainer(Long errorCode) {
		// TODO: implement this
		return null;
	}
	

	public MAPErrorMessageSMDeliveryFailure createMessageSMDeliveryFailure(Long mapVersion, SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause,
			MAPExtensionContainer extensionContainer) {
		// TODO: implement this
		return null;
	}

	public MAPErrorMessageFacilityNotSup createErrorMessageFacilityNotSup(Long mapVersion, Boolean shapeOfLocationEstimateNotSupported,
			Boolean neededLcsCapabilityNotSupportedInServingNode) {
		// TODO: implement this
		return null;
	}

	public MAPErrorMessageSystemFailure createErrorMessageSystemFailure(Long mapVersion, NetworkResource networkResource,
			AdditionalNetworkResource additionalNetworkResource, MAPExtensionContainer extensionContainer) {
		// TODO: implement this
		return null;
	}
}
