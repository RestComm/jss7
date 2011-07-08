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
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageExtensionContainer;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageParameterless;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;

/**
 * Base class of MAP ReturnError messages
 * 
 * @author sergey vetyutnev
 * 
 */
public abstract class MAPErrorMessageImpl implements MAPErrorMessage {
	
	protected Long errorCode;
	
	
	protected MAPErrorMessageImpl(Long errorCode) {
		this.errorCode = errorCode;
	}
	
	@Override
	public Long getErrorCode() {
		return errorCode;
	}

	
	@Override
	public Boolean isEmParameterless() {
		if (this instanceof MAPErrorMessageParameterless)
			return true;
		else
			return false;
	}

	@Override
	public Boolean isEmExtensionContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isEmFacilityNotSup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isEmSMDeliveryFailure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isEmSystemFailure() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public MAPErrorMessageParameterless getEmParameterless() {
		if (this instanceof MAPErrorMessageParameterless)
			return (MAPErrorMessageParameterless)this;
		else
			return null;
	}

	@Override
	public MAPErrorMessageExtensionContainer getEmExtensionContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MAPErrorMessageFacilityNotSup getEmFacilityNotSup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MAPErrorMessageSMDeliveryFailure getEmSMDeliveryFailure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MAPErrorMessageSystemFailure getEmSystemFailure() {
		// TODO Auto-generated method stub
		return null;
	}

}
