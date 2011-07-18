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
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class LCSClientExternalIDImpl extends MAPPrimitiveBase implements LCSClientExternalID {

	private ISDNAddressString externalAddress;
	private MAPExtensionContainer extensionContainer;

	public LCSClientExternalIDImpl() {

	}

	/**
	 * 
	 */
	public LCSClientExternalIDImpl(final ISDNAddressString externalAddress, final MAPExtensionContainer extensionContainer) {
		this.externalAddress = externalAddress;
		this.extensionContainer = extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID#
	 * getExternalAddress()
	 */
	@Override
	public ISDNAddressString getExternalAddress() {
		return this.externalAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID#
	 * getExtensionContainer()
	 */
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	public void decode(Parameter p) throws MAPParsingComponentException {

		Parameter[] parameters = p.getParameters();

		if (parameters == null || parameters.length == 0) {
			// TODO Both parameters are optional here, so its possible that we
			// received LCSClientExternalID without any parameter?
			return;
		}

		for (int count = 0; count < parameters.length; count++) {
			p = parameters[count];

			switch (p.getTag()) {

			case 0:
				// externalAddress [0] ISDN-AddressString OPTIONAL,
				this.externalAddress = new ISDNAddressStringImpl();
				this.externalAddress.decode(p);
				break;
			case 1:
				// extensionContainer [1] ExtensionContainer OPTIONAL,
				this.extensionContainer = new MAPExtensionContainerImpl();
				this.extensionContainer.decode(p);
				break;

			default:
//				throw new MAPParsingComponentException("Decoding LCSClientExternalID failed. Expected externalAddress [0] or extensionContainer [1] but found "
//						+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}
	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		
		if (this.externalAddress != null) {
			// externalAddress [0] ISDN-AddressString OPTIONAL

			Parameter p = externalAddress.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(0);
			p.setPrimitive(true);
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of LCSClientExternalID failed. Failed to parse externalAddress [0] ISDN-AddressString", e);
			}
		}

		if (this.extensionContainer != null) {
			Parameter p = this.extensionContainer.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(1);
			p.setPrimitive(false); // FIXME Is it primitive?
			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of LCSClientExternalID failed. Failed to parse extensionContainer [1] ExtensionContainer", e);
			}
		}

	}
}
