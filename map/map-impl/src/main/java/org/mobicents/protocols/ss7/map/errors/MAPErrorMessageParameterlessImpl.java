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

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageParameterless;

/**
 * The MAP ReturnError message without any parameters
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPErrorMessageParameterlessImpl extends MAPErrorMessageImpl implements MAPErrorMessageParameterless {

	public MAPErrorMessageParameterlessImpl(Long errorCode) {
		super(errorCode);
	}
	
	@Override
	public boolean isEmParameterless() {
		return true;
	}

	@Override
	public MAPErrorMessageParameterless getEmParameterless() {
		return this;
	}	
	

	@Override
	public int getTag() throws MAPException {
		throw new MAPException("MAPErrorMessageParameterless does not support encoding");
	}

	@Override
	public int getTagClass() {
		return 0;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
	}
	
	@Override
	public String toString() {
		return "MAPErrorMessageParameterless [errorCode=" + this.errorCode + "]"; 
	}
}
