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

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPErrorMessageSMDeliveryFailureImpl extends MAPErrorMessageImpl implements MAPErrorMessageSMDeliveryFailure {
	
	private SMEnumeratedDeliveryFailureCause sMEnumeratedDeliveryFailureCause;
	private byte[] signalInfo;
	private MAPExtensionContainer extensionContainer;
	
	
	public MAPErrorMessageSMDeliveryFailureImpl(SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause, byte[] signalInfo,
			MAPExtensionContainer extensionContainer) {
		super((long) MAPErrorCode.smDeliveryFailure);
		
		this.sMEnumeratedDeliveryFailureCause = smEnumeratedDeliveryFailureCause;
		this.signalInfo = signalInfo;
		this.extensionContainer = extensionContainer;
	}
	
	protected MAPErrorMessageSMDeliveryFailureImpl() {
		super((long) MAPErrorCode.smDeliveryFailure);
	}


	@Override
	public SMEnumeratedDeliveryFailureCause getSMEnumeratedDeliveryFailureCause() {
		return this.sMEnumeratedDeliveryFailureCause;
	}

	@Override
	public byte[] getSignalInfo() {
		return this.signalInfo;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public void setSMEnumeratedDeliveryFailureCause(SMEnumeratedDeliveryFailureCause sMEnumeratedDeliveryFailureCause) {
		this.sMEnumeratedDeliveryFailureCause = sMEnumeratedDeliveryFailureCause;
	}

	@Override
	public void setSignalInfo(byte[] signalInfo) {
		this.signalInfo = signalInfo;
	}
	
	@Override
	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		this.extensionContainer = extensionContainer;
	}
	
	@Override
	public boolean isEmSMDeliveryFailure() {
		return true;
	}

	@Override
	public MAPErrorMessageSMDeliveryFailure getEmSMDeliveryFailure() {
		return this;
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
			throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {
		
		this.sMEnumeratedDeliveryFailureCause = null;
		this.signalInfo = null;
		this.extensionContainer = null;
		
		if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.SEQUENCE || localAis.isTagPrimitive())
			throw new MAPParsingComponentException(
					"Error decoding MAPErrorMessageSMDeliveryFailure: bad tag class or tag or parameter is primitive or no parameter data",
					MAPParsingComponentExceptionReason.MistypedParameter);

		AsnInputStream ais = localAis.readSequenceStreamData(length);

		int tag = ais.readTag();
		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.ENUMERATED)
			throw new MAPParsingComponentException("Error decoding MAPErrorMessageSMDeliveryFailure.sMEnumeratedDeliveryFailureCause: bad tag class or tag",
					MAPParsingComponentExceptionReason.MistypedParameter);
		int code = (int) ais.readInteger();
		this.sMEnumeratedDeliveryFailureCause = SMEnumeratedDeliveryFailureCause.getInstance(code);
		if (this.sMEnumeratedDeliveryFailureCause == null)
			throw new MAPParsingComponentException("Error decoding MAPErrorMessageSMDeliveryFailure.sMEnumeratedDeliveryFailureCause: bad value",
					MAPParsingComponentExceptionReason.MistypedParameter);

		while (true) {
			if (ais.available() == 0)
				break;

			tag = ais.readTag();

			switch (ais.getTagClass()) {
			case Tag.CLASS_UNIVERSAL:
				switch (tag) {
				case Tag.STRING_OCTET:
					this.signalInfo = ais.readOctetString();
					break;

				case Tag.SEQUENCE:
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
					break;

				default:
					ais.advanceElement();
					break;
				}
				break;

			default:
				ais.advanceElement();
				break;
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
			throw new MAPException("AsnException when encoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream aos) throws MAPException {

		if (this.sMEnumeratedDeliveryFailureCause == null)
			throw new MAPException("Error encoding MAPErrorMessageSMDeliveryFailure: parameter sMEnumeratedDeliveryFailureCause must not be null");

		try {
			aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.sMEnumeratedDeliveryFailureCause.getCode());
			
			if (this.signalInfo != null)
				aos.writeOctetString(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, this.signalInfo);
			if (this.extensionContainer != null)
				((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(aos);

		} catch (IOException e) {
			throw new MAPException("IOException when encoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("MAPErrorMessageSMDeliveryFailure [");
		if (this.sMEnumeratedDeliveryFailureCause != null)
			sb.append("sMEnumeratedDeliveryFailureCause=" + this.sMEnumeratedDeliveryFailureCause.toString());
		if (this.signalInfo != null)
			sb.append(", signalInfo=" + this.printDataArr(this.signalInfo));
		if (this.extensionContainer != null)
			sb.append(", extensionContainer=" + this.extensionContainer.toString());
		sb.append("]");
		
		return sb.toString();
	}
	
	private String printDataArr(byte[] data) {
		StringBuilder sb = new StringBuilder();
		if (data != null) {
			for (int b : data) {
				sb.append(b);
				sb.append(", ");
			}
		}

		return sb.toString();
	}
}
