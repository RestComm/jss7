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
package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class SendRoutingInfoForLCSResponseIndicationImpl extends LsmMessageImpl implements SendRoutingInfoForLCSResponseIndication {

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
	public SendRoutingInfoForLCSResponseIndicationImpl() {
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
	public SendRoutingInfoForLCSResponseIndicationImpl(SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo, MAPExtensionContainer extensionContainer,
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

	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 2) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSResponseIndication: At least 2 mandatory parameters should be present but have"
							+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// targetMS [0] SubscriberIdentity,
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [targetMS [0] SubscriberIdentity] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.targetMS = new SubscriberIdentityImpl();
		this.targetMS.decode(p);

		// lcsLocationInfo [1] LCSLocationInfo,
		p = parameters[1];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [lcsLocationInfo [1] LCSLocationInfo] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.lcsLocationInfo = new LCSLocationInfoImpl();
		this.lcsLocationInfo.decode(p);

		for (int count = 2; count < parameters.length; count++) {
			p = parameters[count];

			switch (p.getTag()) {
			case 2:
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [extensionContainer [2] ExtensionContainer] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.extensionContainer.decode(p);
				break;
			case 3:
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [v-gmlc-Address [3] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.vgmlcAddress = p.getData();
				break;
			case 4:
				// h-gmlc-Address [4] GSN-Address OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [h-gmlc-Address [4] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.hGmlcAddress = p.getData();
				break;
			case 5:
				// ppr-Address [5] GSN-Address OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [ppr-Address [5] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.pprAddress = p.getData();
				break;
			case 6:
				// additional-v-gmlc-Address [6] GSN-Address OPTIONAL
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding SendRoutingInforForLCSResponseIndication: Parameter [additional-v-gmlc-Address [6] GSN-Address] bad tag class or not primitive or not Sequence",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.additionalVGmlcAddress = p.getData();
				break;
			default:
				throw new MAPParsingComponentException("Error while decoding SendRoutingInforForLCSResponseIndication: Expected parameter tag 0 - 6 but found"
						+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}

	}

	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.targetMS == null) {
			throw new MAPException(
					"Encoding of SendRoutingInforForLCSResponseIndication failed. Manadatory parameter targetMS [0] SubscriberIdentity is not set");
		}

		if (this.lcsLocationInfo == null) {
			throw new MAPException(
					"Encoding of SendRoutingInforForLCSResponseIndication failed. Manadatory parameter lcsLocationInfo [1] LCSLocationInfo is not set");
		}

		// targetMS [0] SubscriberIdentity,
		Parameter p = this.targetMS.encode();
		p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
		p.setTag(0);
		p.setPrimitive(false);
		try {
			p.encode(asnOs);
		} catch (ParseException e1) {
			throw new MAPException("Encoding of SendRoutingInforForLCSResponseIndication failed. Failed to parse targetMS [0] SubscriberIdentity", e1);
		}

		// lcsLocationInfo [1] LCSLocationInfo
		p = this.lcsLocationInfo.encode();
		p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
		p.setTag(1);
		p.setPrimitive(false);
		try {
			p.encode(asnOs);
		} catch (ParseException e1) {
			throw new MAPException("Encoding of SendRoutingInforForLCSResponseIndication failed. Failed to parse lcsLocationInfo [1] LCSLocationInfo", e1);
		}

		if (this.extensionContainer != null) {
			// extensionContainer [2] ExtensionContainer OPTIONAL,
			p = this.extensionContainer.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(2);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportResponseIndication failed. Failed to parse extensionContainer ExtensionContainer OPTIONAL", e);
			}
		}

		if (this.vgmlcAddress != null) {
			// v-gmlc-Address [3] GSN-Address OPTIONAL,
			asnOs.write(0x83);
			asnOs.write(this.vgmlcAddress.length);
			try {
				asnOs.write(this.vgmlcAddress);
			} catch (IOException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportResponseIndication failed. Failed to parse v-gmlc-Address [3] GSN-Address OPTIONAL", e);
			}
		}

		if (this.hGmlcAddress != null) {
			// h-gmlc-Address [4] GSN-Address OPTIONAL,
			asnOs.write(0x83);
			asnOs.write(this.hGmlcAddress.length);
			try {
				asnOs.write(this.hGmlcAddress);
			} catch (IOException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportResponseIndication failed. Failed to parse h-gmlc-Address [4] GSN-Address OPTIONAL", e);
			}
		}

		if (this.pprAddress != null) {
			// ppr-Address [5] GSN-Address OPTIONAL,
			asnOs.write(0x83);
			asnOs.write(this.pprAddress.length);
			try {
				asnOs.write(this.pprAddress);
			} catch (IOException e) {
				throw new MAPException("Encoding of SubscriberLocationReportResponseIndication failed. Failed to ppr-Address [5] GSN-Address OPTIONAL", e);
			}
		}

		if (this.additionalVGmlcAddress != null) {
			// additional-v-gmlc-Address [6] GSN-Address OPTIONAL,
			asnOs.write(0x83);
			asnOs.write(this.additionalVGmlcAddress.length);
			try {
				asnOs.write(this.additionalVGmlcAddress);
			} catch (IOException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportResponseIndication failed. Failed to parse additional-v-gmlc-Address [6] GSN-Address OPTIONAL", e);
			}
		}
	}

}
