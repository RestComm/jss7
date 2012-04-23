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
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class InformServiceCentreRequestIndicationImpl extends SmsMessageImpl implements InformServiceCentreRequestIndication {
	
	protected static final int _TAG_AdditionalAbsentSubscriberDiagnosticSM = 0;

	private ISDNAddressString storedMSISDN;
	private MWStatus mwStatus;
	private MAPExtensionContainer extensionContainer;
	private Integer absentSubscriberDiagnosticSM;
	private Integer additionalAbsentSubscriberDiagnosticSM;
	
	public InformServiceCentreRequestIndicationImpl() {
	}
	
	public InformServiceCentreRequestIndicationImpl(ISDNAddressString storedMSISDN, MWStatus mwStatus, MAPExtensionContainer extensionContainer,
			Integer absentSubscriberDiagnosticSM, Integer additionalAbsentSubscriberDiagnosticSM) {
		this.storedMSISDN = storedMSISDN;
		this.mwStatus = mwStatus;
		this.extensionContainer = extensionContainer;
		this.absentSubscriberDiagnosticSM = absentSubscriberDiagnosticSM;
		this.additionalAbsentSubscriberDiagnosticSM = additionalAbsentSubscriberDiagnosticSM;
	}

	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.InformServiceCentre_Request;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.informServiceCentre;
	}

	@Override
	public ISDNAddressString getStoredMSISDN() {
		return this.storedMSISDN;
	}

	@Override
	public MWStatus getMwStatus() {
		return this.mwStatus;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public Integer getAbsentSubscriberDiagnosticSM() {
		return this.absentSubscriberDiagnosticSM;
	}

	@Override
	public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
		return this.additionalAbsentSubscriberDiagnosticSM;
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
			throw new MAPParsingComponentException("IOException when decoding InformServiceCentreRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding InformServiceCentreRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding InformServiceCentreRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding InformServiceCentreRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.storedMSISDN = null;
		this.mwStatus = null;
		this.extensionContainer = null;
		this.absentSubscriberDiagnosticSM = null;
		this.additionalAbsentSubscriberDiagnosticSM = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

				switch (tag) {
				case Tag.STRING_OCTET:
					// storedMSISDN
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding informServiceCentreRequest: Parameter storedMSISDN is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					storedMSISDN = new ISDNAddressStringImpl();
					((ISDNAddressStringImpl)storedMSISDN).decodeAll(ais);
					break;

				case Tag.STRING_BIT:
					// mw-Status
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding informServiceCentreRequest: Parameter mw-Status is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					mwStatus = new MWStatusImpl();
					((MWStatusImpl)mwStatus).decodeAll(ais);
					break;

				case Tag.SEQUENCE:
					// extensionContainer
					if (ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding reportSMDeliveryStatusRequest: Parameter extensionContainer is primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl)extensionContainer).decodeAll(ais);
					break;

				case Tag.INTEGER:
					// absentSubscriberDiagnosticSM
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException(
								"Error while decoding informServiceCentreRequest: Parameter absentSubscriberDiagnosticSM is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					absentSubscriberDiagnosticSM = (int) ais.readInteger();
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

				switch (tag) {
				case InformServiceCentreRequestIndicationImpl._TAG_AdditionalAbsentSubscriberDiagnosticSM:
					// additionalAbsentSubscriberDiagnosticSM
					if (!ais.isTagPrimitive())
						throw new MAPParsingComponentException(
								"Error while decoding informServiceCentreRequest: Parameter deliveryOutcomeIndicator is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					additionalAbsentSubscriberDiagnosticSM = (int) ais.readInteger();
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
			throw new MAPException("AsnException when encoding InformServiceCentreRequest: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.storedMSISDN != null)
			((ISDNAddressStringImpl)this.storedMSISDN).encodeAll(asnOs);
		if (this.mwStatus != null)
			((MWStatusImpl)this.mwStatus).encodeAll(asnOs);
		if (this.extensionContainer != null)
			((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs);
		try {
			if (this.absentSubscriberDiagnosticSM != null)
				asnOs.writeInteger(this.absentSubscriberDiagnosticSM);
			if (this.additionalAbsentSubscriberDiagnosticSM != null)
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, InformServiceCentreRequestIndicationImpl._TAG_AdditionalAbsentSubscriberDiagnosticSM,
						this.additionalAbsentSubscriberDiagnosticSM);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding InformServiceCentreRequest: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding InformServiceCentreRequest: " + e.getMessage(), e);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("InformServiceCentreRequest [");

		if (this.storedMSISDN != null) {
			sb.append("storedMSISDN=");
			sb.append(this.storedMSISDN.toString());
		}
		if (this.mwStatus != null) {
			sb.append(", mwStatus=");
			sb.append(this.mwStatus.toString());
		}
		if (this.extensionContainer != null) {
			sb.append(", extensionContainer=");
			sb.append(this.extensionContainer.toString());
		}
		if (this.absentSubscriberDiagnosticSM != null) {
			sb.append(", absentSubscriberDiagnosticSM=");
			sb.append(this.absentSubscriberDiagnosticSM.toString());
		}
		if (this.additionalAbsentSubscriberDiagnosticSM != null) {
			sb.append(", additionalAbsentSubscriberDiagnosticSM=");
			sb.append(this.additionalAbsentSubscriberDiagnosticSM.toString());
		}

		sb.append("]");

		return sb.toString();
	}
	
}
