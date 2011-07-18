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
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class SendRoutingInfoForLCSRequestIndicationImpl extends LsmMessageImpl implements SendRoutingInfoForLCSRequestIndication {

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
	public SendRoutingInfoForLCSRequestIndicationImpl(MAPExtensionContainer extensionContainer, SubscriberIdentity targetMS, ISDNAddressString mlcNumber) {
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

	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length < 2) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSRequestIndication: At least 2 mandatory parameters should be present but have"
							+ (parameters == null ? null : parameters.length), MAPParsingComponentExceptionReason.MistypedParameter);
		}

		// mlcNumber [0] ISDN-AddressString,
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSRequestIndication: Parameter [mlcNumber [0] ISDN-AddressString] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.mlcNumber = new ISDNAddressStringImpl();
		this.mlcNumber.decode(p);

		// targetMS [1] SubscriberIdentity
		p = parameters[1];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
			throw new MAPParsingComponentException(
					"Error while decoding SendRoutingInforForLCSRequestIndication: Parameter [targetMS [1] SubscriberIdentity] bad tag class or not primitive or not Sequence",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		this.targetMS = new SubscriberIdentityImpl();
		this.targetMS.decode(p);

		if (parameters.length > 2) {
			p = parameters[2];
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
				throw new MAPParsingComponentException(
						"Error while decoding SendRoutingInforForLCSRequestIndication: Parameter [extensionContainer [2] ExtensionContainer] bad tag class or not primitive or not Sequence",
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
			this.extensionContainer.decode(p);
		}

	}

	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.mlcNumber == null) {
			throw new MAPException(
					"Encoding of SendRoutingInforForLCSRequestIndication failed. Manadatory parameter mlcNumber [0] ISDN-AddressString is not set");
		}

		if (this.targetMS == null) {
			throw new MAPException(
					"Encoding of SendRoutingInforForLCSRequestIndication failed. Manadatory parameter targetMS [1] SubscriberIdentity is not set");
		}

		// mlcNumber [0] ISDN-AddressString
		Parameter p = this.mlcNumber.encode();
		p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
		p.setTag(0);
		p.setPrimitive(true);
		try {
			p.encode(asnOs);
		} catch (ParseException e1) {
			throw new MAPException("Encoding of SubscriberLocationReportResponseIndication failed. Failed to parse mlcNumber [0] ISDN-AddressString", e1);
		}

		// targetMS [1] SubscriberIdentity,
		p = this.targetMS.encode();
		p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
		p.setTag(1);
		p.setPrimitive(false);
		try {
			p.encode(asnOs);
		} catch (ParseException e1) {
			throw new MAPException("Encoding of SubscriberLocationReportResponseIndication failed. Failed to parse targetMS [1] SubscriberIdentity", e1);
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
	}
}
