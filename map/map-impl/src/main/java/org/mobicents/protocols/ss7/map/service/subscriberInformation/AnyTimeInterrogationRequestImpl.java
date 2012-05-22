/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.service.subscriberInformation;

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
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;

/**
 * @author amit bhayani
 * 
 */
public class AnyTimeInterrogationRequestImpl extends SubscriberInformationMessageImpl implements AnyTimeInterrogationRequest,
		MAPAsnPrimitive {

	private static final int _TAG_SUBSCRIBER_IDENTITY = 0;
	private static final int _TAG_REQUESTED_INFO = 1;
	private static final int _TAG_EXTENSION_CONTAINER = 2;
	private static final int _TAG_GSM_SCF_ADDRESS = 3;

	private SubscriberIdentity subscriberIdentity = null;
	private RequestedInfo requestedInfo = null;
	private ISDNAddressString gsmSCFAddress = null;
	private MAPExtensionContainer extensionContainer = null;

	public AnyTimeInterrogationRequestImpl() {

	}

	public AnyTimeInterrogationRequestImpl(SubscriberIdentity subscriberIdentity, RequestedInfo requestedInfo, ISDNAddressString gsmSCFAddress,
			MAPExtensionContainer extensionContainer) {
		this.subscriberIdentity = subscriberIdentity;
		this.requestedInfo = requestedInfo;
		this.gsmSCFAddress = gsmSCFAddress;
		this.extensionContainer = extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
	 */
	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll(
	 * org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding anyTimeInterrogationRequestIndication: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding anyTimeInterrogationRequestIndication: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding anyTimeInterrogationRequestIndication: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding anyTimeInterrogationRequestIndication: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			switch (tag) {
			case _TAG_SUBSCRIBER_IDENTITY:
				// decode SubscriberIdentity
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
					throw new MAPParsingComponentException(
							"Error while decoding anyTimeInterrogationRequestIndication: Parameter 0 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);

				int length1 = ais.readLength();
				tag = ais.readTag();

				this.subscriberIdentity = new SubscriberIdentityImpl();
				((SubscriberIdentityImpl) this.subscriberIdentity).decodeAll(ais);
				break;
			case _TAG_REQUESTED_INFO:
				// decode RequestedInfo
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
					throw new MAPParsingComponentException(
							"Error while decoding anyTimeInterrogationRequestIndication: Parameter 1 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.requestedInfo = new RequestedInfoImpl();
				((RequestedInfoImpl) this.requestedInfo).decodeAll(ais);
				break;
			case _TAG_EXTENSION_CONTAINER:
				// decode extensionContainer
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding RequestedInfo: Parameter 2 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl) extensionContainer).decodeAll(ais);
				break;
			case _TAG_GSM_SCF_ADDRESS:
				// decode gsmSCF-Address
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding RequestedInfo: Parameter 3 bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.gsmSCFAddress = new ISDNAddressStringImpl();
				((ISDNAddressStringImpl) this.gsmSCFAddress).decodeAll(ais);
				break;
			default:
				ais.advanceElement();
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding anyTimeInterrogationRequestIndication: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.subscriberIdentity == null) {
			throw new MAPException("Error while encoding anyTimeInterrogationRequestIndication the mandatory parameter subscriberIdentity is not defined");
		}

		if (this.requestedInfo == null) {
			throw new MAPException("Error while encoding anyTimeInterrogationRequestIndication the mandatory parameter requestedInfo is not defined");
		}

		if (this.gsmSCFAddress == null) {
			throw new MAPException("Error while encoding anyTimeInterrogationRequestIndication the mandatory parameter gsmSCF-Address is not defined");
		}
		try {
			asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_SUBSCRIBER_IDENTITY);
		} catch (AsnException e) {
			throw new MAPException("AsnException while encoding parameter subscriberIdentity	[0] SubscriberIdentity");
		}

		int pos = asnOs.StartContentDefiniteLength();
		((SubscriberIdentityImpl) this.subscriberIdentity).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, ((SubscriberIdentityImpl)this.subscriberIdentity).getTag());
		asnOs.FinalizeContent(pos);
		
		((RequestedInfoImpl) this.requestedInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_REQUESTED_INFO);

		((ISDNAddressStringImpl) this.gsmSCFAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GSM_SCF_ADDRESS);

		if (this.extensionContainer != null) {
			((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * AnyTimeInterrogationRequestIndication#getSubscriberIdentity()
	 */
	@Override
	public SubscriberIdentity getSubscriberIdentity() {
		return this.subscriberIdentity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * AnyTimeInterrogationRequestIndication#getRequestedInfo()
	 */
	@Override
	public RequestedInfo getRequestedInfo() {
		return this.requestedInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * AnyTimeInterrogationRequestIndication#getGsmSCFAddress()
	 */
	@Override
	public ISDNAddressString getGsmSCFAddress() {
		return this.gsmSCFAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
	 * AnyTimeInterrogationRequestIndication#getExtensionContainer()
	 */
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.MAPMessage#getMessageType()
	 */
	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.anyTimeInterrogation_Request;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.MAPMessage#getOperationCode()
	 */
	@Override
	public int getOperationCode() {
		return MAPOperationCode.anyTimeInterrogation;
	}

}
