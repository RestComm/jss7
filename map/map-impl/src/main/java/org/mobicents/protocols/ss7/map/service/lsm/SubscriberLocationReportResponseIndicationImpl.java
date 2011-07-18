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
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponseIndication;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class SubscriberLocationReportResponseIndicationImpl extends LsmMessageImpl implements SubscriberLocationReportResponseIndication {

	private ISDNAddressString naEsrd = null;
	private ISDNAddressString naEsrk = null;
	private MAPExtensionContainer extensionContainer = null;

	/**
	 * 
	 */
	public SubscriberLocationReportResponseIndicationImpl() {
		super();
	}

	/**
	 * @param naEsrd
	 * @param naEsrk
	 * @param extensionContainer
	 */
	public SubscriberLocationReportResponseIndicationImpl(ISDNAddressString naEsrd, ISDNAddressString naEsrk, MAPExtensionContainer extensionContainer) {
		super();
		this.naEsrd = naEsrd;
		this.naEsrk = naEsrk;
		this.extensionContainer = extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportResponseIndication#getExtensionContainer()
	 */
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportResponseIndication#getNaESRK()
	 */
	@Override
	public AddressString getNaESRK() {
		return this.naEsrk;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.
	 * SubscriberLocationReportResponseIndication#getNaESRD()
	 */
	@Override
	public AddressString getNaESRD() {
		return this.naEsrd;
	}

	public void decode(Parameter param) throws MAPParsingComponentException {

		Parameter[] parameters = param.getParameters();

		if (parameters != null) {

			for (int count = 0; count < parameters.length; count++) {
				Parameter p = parameters[count];
				switch (p.getTag()) {
				case 0:
					// na-ESRK [0] ISDN-AddressString OPTIONAL
					if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
						throw new MAPParsingComponentException(
								"Error while decoding SubscriberLocationReportResponseIndication: Parameter [na-ESRK [0] ISDN-AddressString] bad tag class or not primitive or not Sequence",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
					this.naEsrk = new ISDNAddressStringImpl();
					this.naEsrk.decode(p);
					break;
				case 1:
					// na-ESRD [1] ISDN-AddressString OPTIONAL,
					if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
						throw new MAPParsingComponentException(
								"Error while decoding SubscriberLocationReportResponseIndication: Parameter [na-ESRD [1] ISDN-AddressString] bad tag class or not primitive or not Sequence",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
					this.naEsrd = new ISDNAddressStringImpl();
					this.naEsrd.decode(p);
					break;
				default:
					if (p.getTagClass() == Tag.CLASS_UNIVERSAL && p.getTag() == Tag.EXTERNAL) {
						this.extensionContainer = new MAPExtensionContainerImpl();
						this.extensionContainer.decode(p);
					} else {
//						throw new MAPParsingComponentException(
//								"Error while decoding SubscriberLocationReportResponseIndication: Expected tags 0 or 1 but found" + p.getTag(),
//								MAPParsingComponentExceptionReason.MistypedParameter);
					}
				}

			}// For loop
		}// if (parameters != null)
	}

	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.extensionContainer != null) {
			// extensionContainer ExtensionContainer OPTIONAL
			Parameter p = this.extensionContainer.encode();
			p.setTagClass(Tag.CLASS_UNIVERSAL);
			p.setPrimitive(true);
			p.setTag(Tag.EXTERNAL);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException(
						"Encoding of SubscriberLocationReportResponseIndication failed. Failed to parse extensionContainer ExtensionContainer OPTIONAL", e);
			}
		}

		if (this.naEsrk != null) {
			// na-ESRK [0] ISDN-AddressString OPTIONAL
			Parameter p = this.naEsrd.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x00);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of SubscriberLocationReportResponseIndication failed. Failed to parse na-ESRK [0] ISDN-AddressString", e);
			}
		}

		if (this.naEsrd != null) {
			// na-ESRD [1] ISDN-AddressString OPTIONAL ,
			Parameter p = this.naEsrd.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(0x01);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of SubscriberLocationReportResponseIndication failed. Failed to parse na-ESRD [1] ISDN-AddressString", e);
			}
		}

	}

}
