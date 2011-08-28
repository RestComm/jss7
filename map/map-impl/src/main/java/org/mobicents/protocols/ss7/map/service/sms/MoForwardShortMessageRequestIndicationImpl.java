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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;


/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MoForwardShortMessageRequestIndicationImpl extends SmsServiceImpl implements MoForwardShortMessageRequestIndication {
	
	private SM_RP_DA sm_RP_DA;
	private SM_RP_OA sm_RP_OA;
	private byte[] sm_RP_UI;
	private MAPExtensionContainer extensionContainer;
	private IMSI imsi;

	
	public MoForwardShortMessageRequestIndicationImpl() {
	}
	
	public MoForwardShortMessageRequestIndicationImpl(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer, IMSI imsi) {
		this.sm_RP_DA = sm_RP_DA;
		this.sm_RP_OA = sm_RP_OA;
		this.sm_RP_UI = sm_RP_UI;
		this.extensionContainer = extensionContainer;
		this.imsi = imsi;
	}
	
	@Override
	public SM_RP_DA getSM_RP_DA() {
		return this.sm_RP_DA;
	}

	@Override
	public SM_RP_OA getSM_RP_OA() {
		return this.sm_RP_OA;
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
	public IMSI getIMSI() {
		return this.imsi;
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
			throw new MAPParsingComponentException("IOException when decoding moForwardShortMessageRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding moForwardShortMessageRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding moForwardShortMessageRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding moForwardShortMessageRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		
		this.sm_RP_DA = null;
		this.sm_RP_OA = null;
		this.sm_RP_UI = null;
		this.extensionContainer = null;
		this.imsi = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			switch (num) {
			case 0:
				// SM_RP_DA
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter 0 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.sm_RP_DA = new SM_RP_DAImpl();
				((SM_RP_DAImpl)this.sm_RP_DA).decodeAll(ais);
				break;

			case 1:
				// SM_RP_OA
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter 1 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.sm_RP_OA = new SM_RP_OAImpl();
				((SM_RP_OAImpl)this.sm_RP_OA).decodeAll(ais);
				break;

			case 2:
				// sm-RP-UI
				if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter 2 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				if (tag != Tag.STRING_OCTET)
					throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter 2 tag must be STRING_OCTET, found: "
							+ tag, MAPParsingComponentExceptionReason.MistypedParameter);
				this.sm_RP_UI = ais.readOctetString();
				break;

			default:
				if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter extensionContainer is primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
				} else if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Parameter imsi is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.imsi = new IMSIImpl();
					((IMSIImpl)this.imsi).decodeAll(ais);
				} else {
					ais.advanceElement();
				}
				break;
			}

			num++;
		}

		if (num < 3)
			throw new MAPParsingComponentException("Error while decoding moForwardShortMessageRequest: Needs at least 3 mandatory parameters, found " + num,
					MAPParsingComponentExceptionReason.MistypedParameter);

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
			throw new MAPException("AsnException when encoding moForwardShortMessageRequest: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.sm_RP_DA == null || this.sm_RP_OA == null || this.sm_RP_UI == null)
			throw new MAPException("sm_RP_DA,sm_RP_OA and sm_RP_UI must not be null");

		try {
			((SM_RP_DAImpl)this.sm_RP_DA).encodeAll(asnOs);
			((SM_RP_OAImpl)this.sm_RP_OA).encodeAll(asnOs);
			asnOs.writeOctetString(this.sm_RP_UI);

			if (this.extensionContainer != null)
				((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs);
			if (this.imsi != null)
				((IMSIImpl)this.imsi).encodeAll(asnOs);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding moForwardShortMessageRequest: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding moForwardShortMessageRequest: " + e.getMessage(), e);
		}
	}	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MoForwardShortMessageRequest [");

		if (this.sm_RP_DA != null) {
			sb.append("sm_RP_DA=");
			sb.append(this.sm_RP_DA.toString());
		}
		if (this.sm_RP_OA != null) {
			sb.append(", sm_RP_OA=");
			sb.append(this.sm_RP_OA.toString());
		}
		if (this.sm_RP_UI != null) {
			sb.append(", sm_RP_UI=[");
			sb.append(this.printDataArr(this.sm_RP_UI));
			sb.append("]");
		}
		if (this.extensionContainer != null) {
			sb.append(", extensionContainer=");
			sb.append(this.extensionContainer.toString());
		}
		if (this.imsi != null) {
			sb.append(", imsi=");
			sb.append(this.imsi.toString());
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

