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

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.TripletList;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class AuthenticationSetListImpl implements AuthenticationSetList, MAPAsnPrimitive {

	public static final int _TAG_tripletList = 0;
	public static final int _TAG_quintupletList = 1;

	public static final String _PrimitiveName = "AuthenticationSetList";

	private TripletList tripletList;
	private QuintupletList quintupletList;

	
	public AuthenticationSetListImpl() {
	}

	public AuthenticationSetListImpl(TripletList tripletList) {
		this.tripletList = tripletList;
	}

	public AuthenticationSetListImpl(QuintupletList quintupletList) {
		this.quintupletList = quintupletList;
	}
	

	@Override
	public TripletList getTripletList() {
		return tripletList;
	}

	@Override
	public QuintupletList getQuintupletList() {
		return quintupletList;
	}


	@Override
	public int getTag() throws MAPException {
		if (tripletList != null)
			return _TAG_tripletList;
		else
			return _TAG_quintupletList;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
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

		this.tripletList = null;
		this.quintupletList = null;

		int tag = ais.getTag();

		if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
			switch (tag) {
			case _TAG_tripletList:
				this.tripletList = new TripletListImpl();
				((TripletListImpl) this.tripletList).decodeData(ais, length);
				break;
			case _TAG_quintupletList:
				int i1 = 0;
				i1++;
//				this.bearerCap = new BearerCapImpl();
//				((BearerCapImpl) this.bearerCap).decodeData(ais, length);
				break;

			default:
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
		} else {
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
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
