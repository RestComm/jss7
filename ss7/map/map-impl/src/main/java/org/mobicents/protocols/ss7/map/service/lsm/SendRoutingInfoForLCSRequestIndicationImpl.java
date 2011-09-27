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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * @author amit bhayani
 * 
 */
public class SendRoutingInfoForLCSRequestIndicationImpl extends LsmMessageImpl implements SendRoutingInfoForLCSRequestIndication {

	private static final int _TAG_MLC_NUMBER = 0;
	private static final int _TAG_TARGET_MS = 1;
	private static final int _TAG_EXTENSION_CONTAINER = 2;

	private MAPExtensionContainer extensionContainer = null;
	private SubscriberIdentity targetMS = null;
	private ISDNAddressString mlcNumber = null;

	/**
	 * 
	 */
	public SendRoutingInfoForLCSRequestIndicationImpl() {
		super();
	}

	/**
	 * @param targetMS
	 * @param mlcNumber
	 */
	public SendRoutingInfoForLCSRequestIndicationImpl(ISDNAddressString mlcNumber, SubscriberIdentity targetMS) {
		super();
		this.targetMS = targetMS;
		this.mlcNumber = mlcNumber;
	}

	/**
	 * @param extensionContainer
	 * @param targetMS
	 * @param mlcNumber
	 */
	public SendRoutingInfoForLCSRequestIndicationImpl(ISDNAddressString mlcNumber, SubscriberIdentity targetMS, MAPExtensionContainer extensionContainer) {
		this(mlcNumber, targetMS);
		this.extensionContainer = extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSRequestIndication#getMLCNumber()
	 */
	@Override
	public ISDNAddressString getMLCNumber() {
		return this.mlcNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSRequestIndication#getTargetMS()
	 */
	@Override
	public SubscriberIdentity getTargetMS() {
		return this.targetMS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSRequestIndication#getExtensionContainer()
	 */
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass
	 * ()
	 */
	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive
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
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
	 * (org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding ProvideSubscriberLocationRequestIndication: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding ProvideSubscriberLocationRequestIndication: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding ProvideSubscriberLocationRequestIndication: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding ProvideSubscriberLocationRequestIndication: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		// mlcNumber [0] ISDN-AddressString,
		int tag = ais.readTag();

		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_MLC_NUMBER) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSRequestIndication: Parameter [mlcNumber [0] ISDN-AddressString] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.mlcNumber = new ISDNAddressStringImpl();
		((ISDNAddressStringImpl)this.mlcNumber).decodeAll(ais);

		// targetMS [1] SubscriberIdentity
		tag = ais.readTag();
		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive() || tag != _TAG_TARGET_MS) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSRequestIndication: Parameter [targetMS [1] SubscriberIdentity] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

		int length1 = ais.readLength();
		tag = ais.readTag();

		this.targetMS = new SubscriberIdentityImpl();
		((SubscriberIdentityImpl)this.targetMS).decodeAll(ais);

		while (true) {
			if (ais.available() == 0)
				break;

			tag = ais.readTag();

			switch (tag) {
			case _TAG_EXTENSION_CONTAINER:
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSRequestIndication: Parameter [extensionContainer [2] ExtensionContainer] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
				break;
			default:
				// DO we care?
				ais.advanceElement();
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MWStatus: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.mlcNumber == null) {
			throw new MAPException(
					"Encoding of SendRoutingInforForLCSRequestIndication failed. Manadatory parameter mlcNumber [0] ISDN-AddressString is not set");
		}

		if (this.targetMS == null) {
			throw new MAPException(
					"Encoding of SendRoutingInforForLCSRequestIndication failed. Manadatory parameter targetMS [1] SubscriberIdentity is not set");
		}

		((ISDNAddressStringImpl)this.mlcNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_MLC_NUMBER);

		try {
			asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_TARGET_MS);
		} catch (AsnException e) {
			throw new MAPException("AsnException while encoding parameter targetMS [1] SubscriberIdentity");
		}

		int pos = asnOs.StartContentDefiniteLength();
		((SubscriberIdentityImpl)this.targetMS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, ((SubscriberIdentityImpl)this.targetMS).getTag());
		asnOs.FinalizeContent(pos);

		if (this.extensionContainer != null) {
			((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
		}
	}

}
