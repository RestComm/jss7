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
import java.util.BitSet;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * @author baranowb
 * 
 */
public class ProtocolVersionImpl implements ProtocolVersion {

	//NOTE this is of type BitString, its not a sub type of it!, so no BitStrinHeader!
	private static final BitSet _VALUE = new BitSet(0);
	private static byte[] _ENCODED_VALUE;
	static {
		_VALUE.set(0);
		AsnOutputStream aos = new AsnOutputStream();
		try {
			aos.writeStringBinary(_TAG_PROTOCOL_VERSION_CLASS, _TAG_PROTOCOL_VERSION,_VALUE);
			_ENCODED_VALUE = aos.toByteArray();
		} catch (AsnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

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
				
			BitSet readV = new BitSet();
			ais.readBitString(readV);
			if (readV.length() == 1) {
				// ok
			} else {
				throw new ParseException("wrong version number, set bits count: " + readV.length());
			}
		} catch (IOException e) {

			throw new ParseException(e);
		} catch (AsnException e) {
			throw new ParseException(e);
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
		
		try {
			aos.write(_ENCODED_VALUE);
		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

}
