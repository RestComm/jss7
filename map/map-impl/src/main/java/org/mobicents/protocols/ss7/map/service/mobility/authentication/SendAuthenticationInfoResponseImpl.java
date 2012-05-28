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
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SendAuthenticationInfoResponseImpl extends MobilityMessageImpl implements SendAuthenticationInfoResponse {

	public static final int _TAG_General = 3;
	protected static final int _TAG_eps_AuthenticationSetList = 2;

	public static final String _PrimitiveName = "SendAuthenticationInfoResponse";

	private AuthenticationSetList authenticationSetList;
	private MAPExtensionContainer extensionContainer;
	private EpsAuthenticationSetList epsAuthenticationSetList;
	private long mapProtocolVersion;

	
	public SendAuthenticationInfoResponseImpl(long mapProtocolVersion) {
		this.mapProtocolVersion = mapProtocolVersion;
	}

	public SendAuthenticationInfoResponseImpl(long mapProtocolVersion, AuthenticationSetList authenticationSetList, MAPExtensionContainer extensionContainer,
			EpsAuthenticationSetList epsAuthenticationSetList) {
		this.mapProtocolVersion = mapProtocolVersion;
		this.authenticationSetList = authenticationSetList;
		this.extensionContainer = extensionContainer;
		this.epsAuthenticationSetList = epsAuthenticationSetList;
	}
	
	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.sendAuthenticationInfo_Response;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.sendAuthenticationInfo;
	}

	@Override
	public AuthenticationSetList getAuthenticationSetList() {
		return authenticationSetList;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	@Override
	public EpsAuthenticationSetList getEpsAuthenticationSetList() {
		return epsAuthenticationSetList;
	}


	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
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

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.authenticationSetList = null;
		this.extensionContainer = null;
		this.epsAuthenticationSetList = null;

		if (mapProtocolVersion >= 3) {

			AsnInputStream ais = ansIS.readSequenceStreamData(length);
			while (true) {
				if (ais.available() == 0)
					break;

				int tag = ais.readTag();
				if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					switch (tag) {
					case AuthenticationSetListImpl._TAG_tripletList:
					case AuthenticationSetListImpl._TAG_quintupletList:
						// authenticationSetList
						this.authenticationSetList = new AuthenticationSetListImpl();
						((AuthenticationSetListImpl)this.authenticationSetList).decodeAll(ais);
						break;
					case _TAG_eps_AuthenticationSetList:
						// epsAuthenticationSetList
						if (ais.isTagPrimitive())
							throw new MAPParsingComponentException(
									"Error while decoding " + _PrimitiveName + ".epsAuthenticationSetList: Parameter epsAuthenticationSetList is primitive",
									MAPParsingComponentExceptionReason.MistypedParameter);
						// TODO: implement it
						ais.advanceElement();
						break;

					default:
						ais.advanceElement();
						break;
					}
				} else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

					switch (tag) {
					case Tag.SEQUENCE:
						// extensionContainer
						if (ais.isTagPrimitive())
							throw new MAPParsingComponentException(
									"Error while decoding " + _PrimitiveName + ".extensionContainer: Parameter extensionContainer is primitive",
									MAPParsingComponentExceptionReason.MistypedParameter);
						this.extensionContainer = new MAPExtensionContainerImpl();
						((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
						break;
					}
				} else {

					ais.advanceElement();
				}
			}
		} else {

			// ..........................................
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
