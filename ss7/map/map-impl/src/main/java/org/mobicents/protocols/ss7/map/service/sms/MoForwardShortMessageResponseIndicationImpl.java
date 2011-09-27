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

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MoForwardShortMessageResponseIndicationImpl extends SmsMessageImpl implements MoForwardShortMessageResponseIndication {

	private byte[] sm_RP_UI;
	private MAPExtensionContainer extensionContainer;


	public MoForwardShortMessageResponseIndicationImpl() {
	}

	public MoForwardShortMessageResponseIndicationImpl(byte[] sm_RP_UI, MAPExtensionContainer extensionContainer) {
		this.sm_RP_UI = sm_RP_UI;
		this.extensionContainer = extensionContainer;
	}

	
	@Override
	public byte[] getSM_RP_UI() {
		return this.sm_RP_UI;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
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
			throw new MAPParsingComponentException("IOException when decoding moForwardShortMessageResponse: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding moForwardShortMessageResponse: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding moForwardShortMessageResponse: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding moForwardShortMessageResponse: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		
		this.sm_RP_UI = null;
		this.extensionContainer = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
				switch (tag) {
				case Tag.STRING_OCTET:
					// sm-RP-UI
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding moForwardShortMessageResponse: Parameter sm-RP-UI is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.sm_RP_UI = ais.readOctetString();
					break;

				case Tag.SEQUENCE:
					// ExtensionContainer
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding moForwardShortMessageResponse: Parameter extensionContainer is primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
					break;

				default:
					ais.advanceElement();
					break;
				}

			} else {
				ais.advanceElement();
			}
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding moForwardShortMessageResponse: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		try {
			if (this.sm_RP_UI != null)
				asnOs.writeOctetString(this.sm_RP_UI);
			if (this.extensionContainer != null)
				((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding moForwardShortMessageResponse: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding moForwardShortMessageResponse: " + e.getMessage(), e);
		}
	}	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MoForwardShortMessageResponse [");

		if (this.sm_RP_UI != null) {
			sb.append(", sm_RP_UI=[");
			sb.append(this.printDataArr(this.sm_RP_UI));
			sb.append("]");
		}
		if (this.extensionContainer != null) {
			sb.append(", extensionContainer=");
			sb.append(this.extensionContainer.toString());
		}

		sb.append("]");

		return sb.toString();
	}

	private String printDataArr(byte[] arr) {
		StringBuilder sb = new StringBuilder();
		for (int b : arr) {
			sb.append(b);
			sb.append(", ");
		}

		return sb.toString();
	}
}
