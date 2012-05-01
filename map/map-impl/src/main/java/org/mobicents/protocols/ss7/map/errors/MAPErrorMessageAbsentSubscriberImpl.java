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
import org.mobicents.protocols.ss7.map.api.errors.AbsentSubscriberReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriber;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPErrorMessageAbsentSubscriberImpl extends MAPErrorMessageImpl implements MAPErrorMessageAbsentSubscriber {

	public static final int AbsentSubscriberReason_TAG = 0;

	private MAPExtensionContainer extensionContainer;
	private AbsentSubscriberReason absentSubscriberReason;	
	
	
	public MAPErrorMessageAbsentSubscriberImpl(MAPExtensionContainer extensionContainer, AbsentSubscriberReason absentSubscriberReason) {
		super((long) MAPErrorCode.absentSubscriber);

		this.extensionContainer = extensionContainer;
		this.absentSubscriberReason = absentSubscriberReason;
	}	
	
	protected MAPErrorMessageAbsentSubscriberImpl() {
		super((long) MAPErrorCode.absentSubscriber);
	}	

	
	@Override
	public boolean isEmAbsentSubscriber() {
		return true;
	}

	@Override
	public MAPErrorMessageAbsentSubscriber getEmAbsentSubscriber() {
		return this;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public AbsentSubscriberReason getAbsentSubscriberReason() {
		return this.absentSubscriberReason;
	}

	@Override
	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		this.extensionContainer = extensionContainer;
	}

	@Override
	public void setAbsentSubscriberReason(AbsentSubscriberReason absentSubscriberReason) {
		this.absentSubscriberReason = absentSubscriberReason;
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
			throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.extensionContainer = null;
		this.absentSubscriberReason = null;
		
		if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.SEQUENCE || localAis.isTagPrimitive())
			throw new MAPParsingComponentException(
					"Error decoding MAPErrorMessageAbsentSubscriber: bad tag class or tag or parameter is primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);

		AsnInputStream ais = localAis.readSequenceStreamData(length);

		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			switch (ais.getTagClass()) {
			case Tag.CLASS_UNIVERSAL:
				switch (tag) {
				case Tag.SEQUENCE:
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
					break;

				default:
					ais.advanceElement();
					break;
				}
				break;

			case Tag.CLASS_CONTEXT_SPECIFIC:
				switch (tag) {
				case AbsentSubscriberReason_TAG:
					int code = (int) ais.readInteger();
					this.absentSubscriberReason = AbsentSubscriberReason.getInstance(code);
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
			throw new MAPException("AsnException when encoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream aos) throws MAPException {

		if (this.absentSubscriberReason == null && this.extensionContainer == null)
			return;

		try {
			if (this.extensionContainer != null)
				((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(aos);
			if (this.absentSubscriberReason != null)
				aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, AbsentSubscriberReason_TAG, this.absentSubscriberReason.getCode());

		} catch (IOException e) {
			throw new MAPException("IOException when encoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MAPErrorMessageAbsentSubscriber: " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("MAPErrorMessageAbsentSubscriber [");
		if (this.extensionContainer != null)
			sb.append("extensionContainer=" + this.extensionContainer.toString());
		if (this.absentSubscriberReason != null)
			sb.append(", absentSubscriberReason=" + this.absentSubscriberReason.toString());
		sb.append("]");
		
		return sb.toString();
	}

}
