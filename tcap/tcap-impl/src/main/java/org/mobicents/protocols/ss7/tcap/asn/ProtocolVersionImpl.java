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

/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * 
 */
public class ProtocolVersionImpl implements ProtocolVersion {

	//NOTE this is of type BitString, its not a sub type of it!, so no BitStrinHeader!
	private static final BitSetStrictLength _VALUE = new BitSetStrictLength(1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.ProtocolVersion#getProtocolVersion()
	 */
	public int getProtocolVersion() {

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 * .asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {

		
		try {
				
			BitSetStrictLength readV = ais.readBitString();
			if (readV.getStrictLength() == 1) {
				// ok
			} else {
				throw new ParseException("wrong version number, set bits count: " + readV.length());
			}
		} catch (IOException e) {
			throw new ParseException("IOException while decoding ProtocolVersion: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("AsnException while decoding ProtocolVersion: " + e.getMessage(), e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols
	 * .asn.AsnOutputStream)
	 */
	public void encode(AsnOutputStream aos) throws ParseException {

		// commented code is the whole case encoding implementation
		// now only one version is supported - we use for optimization purpose simple encoding
		// aos.writeBitString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_PROTOCOL_VERSION, _VALUE);
		aos.write(128);
		aos.write(2);
		aos.write(7);
		aos.write(128);

	}
}
