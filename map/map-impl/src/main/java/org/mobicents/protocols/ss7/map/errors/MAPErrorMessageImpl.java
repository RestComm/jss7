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
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageExtensionContainer;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageParameterless;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessagePositionMethodFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSubscriberBusyForMtSms;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnauthorizedLCSClient;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnknownSubscriber;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * Base class of MAP ReturnError messages
 * 
 * @author sergey vetyutnev
 * 
 */
public abstract class MAPErrorMessageImpl implements MAPErrorMessage, MAPAsnPrimitive {
	
	protected Long errorCode;
	
	
	protected MAPErrorMessageImpl(Long errorCode) {
		this.errorCode = errorCode;
	}
	
	@Override
	public Long getErrorCode() {
		return errorCode;
	}

	
	@Override
	public boolean isEmParameterless() {
		return false;
	}

	@Override
	public boolean isEmExtensionContainer() {
		return false;
	}

	@Override
	public boolean isEmFacilityNotSup() {
		return false;
	}

	@Override
	public boolean isEmSMDeliveryFailure() {
		return false;
	}

	@Override
	public boolean isEmSystemFailure() {
		return false;
	}

	@Override
	public boolean isEmUnknownSubscriber() {
		return false;
	}

	@Override
	public boolean isEmAbsentSubscriberSM() {
		return false;
	}

	@Override
	public boolean isEmAbsentSubscriber() {
		return false;
	}

	@Override
	public boolean isEmSubscriberBusyForMtSms() {
		return false;
	}

	@Override
	public boolean isEmCallBarred() {
		return false;
	}

	@Override
	public boolean isEmUnauthorizedLCSClient() {
		return false;
	}

	@Override
	public boolean isEmPositionMethodFailure() {
		return false;
	}

	
	@Override
	public MAPErrorMessageParameterless getEmParameterless() {
		return null;
	}

	@Override
	public MAPErrorMessageExtensionContainer getEmExtensionContainer() {
		return null;
	}

	@Override
	public MAPErrorMessageFacilityNotSup getEmFacilityNotSup() {
		return null;
	}

	@Override
	public MAPErrorMessageSMDeliveryFailure getEmSMDeliveryFailure() {
		return null;
	}

	@Override
	public MAPErrorMessageSystemFailure getEmSystemFailure() {
		return null;
	}

	@Override
	public MAPErrorMessageUnknownSubscriber getEmUnknownSubscriber() {
		return null;
	}

	@Override
	public MAPErrorMessageAbsentSubscriberSM getEmAbsentSubscriberSM() {
		return null;
	}

	@Override
	public MAPErrorMessageAbsentSubscriber getEmAbsentSubscriber() {
		return null;
	}

	@Override
	public MAPErrorMessageSubscriberBusyForMtSms getEmSubscriberBusyForMtSms() {
		return null;
	}

	@Override
	public MAPErrorMessageCallBarred getEmCallBarred() {
		return null;
	}

	@Override
	public MAPErrorMessageUnauthorizedLCSClient getEmUnauthorizedLCSClient() {
		return null;
	}

	@Override
	public MAPErrorMessagePositionMethodFailure getEmPositionMethodFailure() {
		return null;
	}

}
