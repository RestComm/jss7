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

package org.mobicents.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponseIndication;

public class ForwardShortMessageResponseIndicationImpl extends SmsMessageImpl implements ForwardShortMessageResponseIndication {

	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.forwardSM_Response;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.mo_forwardSM;
	}

	@Override
	public int getTag() throws MAPException {
		
		throw new MAPException("ForwardShortMessageResponseIndication has no MAP message primitive");
	}

	@Override
	public int getTagClass() {

		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		
		return false;
	}

	
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		throw new MAPParsingComponentException("ForwardShortMessageResponseIndication has no MAP message primitive", MAPParsingComponentExceptionReason.MistypedParameter);
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		throw new MAPParsingComponentException("ForwardShortMessageResponseIndication has no MAP message primitive", MAPParsingComponentExceptionReason.MistypedParameter);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		throw new MAPException("ForwardShortMessageResponseIndication has no MAP message primitive");
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

		throw new MAPException("ForwardShortMessageResponseIndication has no MAP message primitive");
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		throw new MAPException("ForwardShortMessageResponseIndication has no MAP message primitive");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ForwardShortMessageResponse [");
		sb.append("]");

		return sb.toString();
	}
}

