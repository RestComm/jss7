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

package org.mobicents.protocols.ss7.map.service.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.NotReachableReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SubscriberStateImpl implements SubscriberState, MAPAsnPrimitive {

	public static final int _ID_assumedIdle = 0;
	public static final int _ID_camelBusy = 1;
	public static final int _ID_notProvidedFromVLR = 2;

	public static final String _PrimitiveName = "SubscriberState";

	private SubscriberStateChoice subscriberStateChoice;
	private NotReachableReason notReachableReason;

	
	@Override
	public SubscriberStateChoice getSubscriberStateChoice() {
		return subscriberStateChoice;
	}

	@Override
	public NotReachableReason getNotReachableReason() {
		return notReachableReason;
	}
	
	
	@Override
	public int getTag() throws MAPException {

		if (this.subscriberStateChoice == null)
			throw new MAPException("Error encoding " + _PrimitiveName + ": No subscriberStateChoice value");

		switch (this.subscriberStateChoice) {
		case assumedIdle:
			return _ID_assumedIdle;
		case camelBusy:
			return _ID_camelBusy;
		case netDetNotReachable:
			return Tag.ENUMERATED;
		case notProvidedFromVLR:
			return _ID_notProvidedFromVLR;
		}

		throw new MAPException("Error encoding " + _PrimitiveName + ": Bad subscriberStateChoice value");
	}

	@Override
	public int getTagClass() {
		if (this.subscriberStateChoice != null && this.subscriberStateChoice == SubscriberStateChoice.netDetNotReachable)
			return Tag.CLASS_UNIVERSAL;
		else
			return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return true;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {
		
		this.subscriberStateChoice = null;
		this.notReachableReason = null;

		int tag = ais.getTag();

		switch (ais.getTagClass()) {
		case Tag.CLASS_UNIVERSAL:
			if (tag == Tag.ENUMERATED) {
				this.subscriberStateChoice = SubscriberStateChoice.netDetNotReachable;
				int i1 = (int) ais.readIntegerData(length);
				this.notReachableReason = NotReachableReason.getInstance(i1);
			} else {
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag for universal tag class: " + tag,
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
			break;
		case Tag.CLASS_CONTEXT_SPECIFIC:
			switch (tag) {
			case _ID_assumedIdle:
				this.subscriberStateChoice = SubscriberStateChoice.assumedIdle;
				break;
			case _ID_camelBusy:
				this.subscriberStateChoice = SubscriberStateChoice.camelBusy;
				break;
			case _ID_notProvidedFromVLR:
				this.subscriberStateChoice = SubscriberStateChoice.notProvidedFromVLR;
				break;
			default:
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag for contextSpecific tag class: " + tag,
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
			ais.readNullData(length);
			break;
		default:
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass: " + ais.getTagClass(),
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}
}
