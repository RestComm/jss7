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

package org.mobicents.protocols.ss7.cap.errors;

import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageParameterless;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 * Base class of CAP ReturnError messages
 * 
 * @author sergey vetyutnev
 * 
 */
public abstract class CAPErrorMessageImpl implements CAPErrorMessage, CAPAsnPrimitive {
	
	protected Long errorCode;
	
	
	protected CAPErrorMessageImpl(Long errorCode) {
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

//	@Override
//	public boolean isEmFacilityNotSup() {
//		return false;
//	}

	
	@Override
	public CAPErrorMessageParameterless getEmParameterless() {
		return null;
	}

//	@Override
//	public CAPErrorMessageFacilityNotSup getEmFacilityNotSup() {
//		return null;
//	}

}
