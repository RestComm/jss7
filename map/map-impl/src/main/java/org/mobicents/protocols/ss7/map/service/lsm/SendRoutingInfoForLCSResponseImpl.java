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
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;

/**
 * @author amit bhayani
 * 
 */
public class SendRoutingInfoForLCSResponseImpl extends LsmMessageImpl implements SendRoutingInfoForLCSResponse {

	private static final int _TAG_TARGET_MS = 0;
	private static final int _TAG_LCS_LOCATION_INFO = 1;
	private static final int _TAG_EXTENSION_CONTAINER = 2;
	private static final int _TAG_V_GMLC_ADDRESS = 3;
	private static final int _TAG_H_GMLC_ADDRESS = 4;
	private static final int _TAG_PPR_ADDRESS = 5;
	private static final int _TAG_ADDITIONAL_V_GMLC_ADDRESS = 6;

	private SubscriberIdentity targetMS = null;
	private LCSLocationInfo lcsLocationInfo = null;
	private MAPExtensionContainer extensionContainer = null;
	private byte[] vgmlcAddress = null;
	private byte[] hGmlcAddress = null;
	private byte[] pprAddress = null;
	private byte[] additionalVGmlcAddress = null;

	/**
	 * 
	 */
	public SendRoutingInfoForLCSResponseImpl() {
		super();
	}

	/**
	 * @param targetMS
	 * @param lcsLocationInfo
	 * @param extensionContainer
	 * @param vgmlcAddress
	 * @param hGmlcAddress
	 * @param pprAddress
	 * @param additionalVGmlcAddress
	 */
	public SendRoutingInfoForLCSResponseImpl(SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo, MAPExtensionContainer extensionContainer,
			byte[] vgmlcAddress, byte[] hGmlcAddress, byte[] pprAddress, byte[] additionalVGmlcAddress) {
		super();
		this.targetMS = targetMS;
		this.lcsLocationInfo = lcsLocationInfo;
		this.extensionContainer = extensionContainer;
		this.vgmlcAddress = vgmlcAddress;
		this.hGmlcAddress = hGmlcAddress;
		this.pprAddress = pprAddress;
		this.additionalVGmlcAddress = additionalVGmlcAddress;
	}

	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.sendRoutingInfoForLCS_Response;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.sendRoutingInfoForLCS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSResponseIndication#getTargetMS()
	 */
	@Override
	public SubscriberIdentity getTargetMS() {
		return this.targetMS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSResponseIndication#getLCSLocationInfo()
	 */
	@Override
	public LCSLocationInfo getLCSLocationInfo() {
		return this.lcsLocationInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSResponseIndication#getExtensionContainer()
	 */
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSResponseIndication#getVgmlcAddress()
	 */
	@Override
	public byte[] getVgmlcAddress() {
		return this.vgmlcAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSResponseIndication#getHGmlcAddress()
	 */
	@Override
	public byte[] getHGmlcAddress() {
		return this.hGmlcAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSResponseIndication#getPprAddress()
	 */
	@Override
	public byte[] getPprAddress() {
		return this.pprAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SendRoutingInforForLCSResponseIndication#getAdditionalVGmlcAddress()
	 */
	@Override
	public byte[] getAdditionalVGmlcAddress() {
		return this.additionalVGmlcAddress;
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

		// targetMS [0] SubscriberIdentity
		int tag = ais.readTag();

		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive() || tag != _TAG_TARGET_MS) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [targetMS [0] SubscriberIdentity] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		int length1 = ais.readLength();
		tag = ais.readTag();

		this.targetMS = new SubscriberIdentityImpl();
		((SubscriberIdentityImpl)this.targetMS).decodeAll(ais);

		// lcsLocationInfo [1] LCSLocationInfo,
		tag = ais.readTag();
		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive() || tag != _TAG_LCS_LOCATION_INFO) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [lcsLocationInfo [1] LCSLocationInfo] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.lcsLocationInfo = new LCSLocationInfoImpl();
		((LCSLocationInfoImpl)this.lcsLocationInfo).decodeAll(ais);

		while (true) {
			if (ais.available() == 0)
				break;

			tag = ais.readTag();

			switch (tag) {
			case _TAG_EXTENSION_CONTAINER:
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [extensionContainer [2] ExtensionContainer] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
				break;
			case _TAG_V_GMLC_ADDRESS:
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [v-gmlc-Address [3] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.vgmlcAddress = ais.readOctetStringData(length1);
				break;
			case _TAG_H_GMLC_ADDRESS:
				// h-gmlc-Address [4] GSN-Address OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [h-gmlc-Address [4] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.hGmlcAddress = ais.readOctetStringData(length1);
				break;
			case _TAG_PPR_ADDRESS:
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [ppr-Address [5] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.pprAddress = ais.readOctetStringData(length1);
				break;
			case _TAG_ADDITIONAL_V_GMLC_ADDRESS:
				// additional-v-gmlc-Address [6] GSN-Address OPTIONAL
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [additional-v-gmlc-Address [6] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.additionalVGmlcAddress = ais.readOctetStringData(length1);
				break;
			default:
				ais.advanceElement();
				break;
			}
		}// while

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
		if (this.targetMS == null) {
			throw new MAPException(
					"Encoding of SendRoutingInforForLCSResponseIndication failed. Manadatory parameter targetMS [0] SubscriberIdentity is not set");
		}

		if (this.lcsLocationInfo == null) {
			throw new MAPException(
					"Encoding of SendRoutingInforForLCSResponseIndication failed. Manadatory parameter lcsLocationInfo [1] LCSLocationInfo is not set");
		}

		try {
			asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_TARGET_MS);
		} catch (AsnException e) {
			throw new MAPException("AsnException while encoding parameter targetMS [0] SubscriberIdentity");
		}

		int pos = asnOs.StartContentDefiniteLength();
		((SubscriberIdentityImpl)this.targetMS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, ((SubscriberIdentityImpl)this.targetMS).getTag());
		asnOs.FinalizeContent(pos);

		((LCSLocationInfoImpl)this.lcsLocationInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_LOCATION_INFO);

		if (this.extensionContainer != null) {
			// extensionContainer [2] ExtensionContainer OPTIONAL,
			((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
		}

		if (this.vgmlcAddress != null) {
			// v-gmlc-Address [3] GSN-Address OPTIONAL,
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_V_GMLC_ADDRESS, this.vgmlcAddress);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter v-gmlc-Address");
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter v-gmlc-Address");
			}
		}

		if (this.hGmlcAddress != null) {
			// h-gmlc-Address [4] GSN-Address OPTIONAL,
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_H_GMLC_ADDRESS, this.hGmlcAddress);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter hGmlcAddress");
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter hGmlcAddress");
			}
		}

		if (this.pprAddress != null) {
			// ppr-Address [5] GSN-Address OPTIONAL,
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_PPR_ADDRESS, this.pprAddress);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter pprAddress");
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter pprAddress");
			}
		}

		if (this.additionalVGmlcAddress != null) {
			// additional-v-gmlc-Address [6] GSN-Address OPTIONAL,
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ADDITIONAL_V_GMLC_ADDRESS, this.additionalVGmlcAddress);
			} catch (IOException e) {
				throw new MAPException("IOException while encoding parameter additionalVGmlcAddress");
			} catch (AsnException e) {
				throw new MAPException("AsnException while encoding parameter additionalVGmlcAddress");
			}
		}
	}

}
