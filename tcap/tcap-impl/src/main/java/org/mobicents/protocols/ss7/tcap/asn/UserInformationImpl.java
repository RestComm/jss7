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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.External;
import org.mobicents.protocols.asn.Tag;

/**
 * <p>
 * According to ITU-T Rec Q.773 the UserInformation is defined as
 * </p>
 * <br/>
 * <p>
 * user-information [30] IMPLICIT SEQUENCE OF EXTERNAL
 * </p>
 * <br/>
 * <p>
 * For definition of EXTERNAL look {@link org.mobicents.protocols.asn.External}
 * from Mobicents ASN module
 * </p>
 * 
 * @author baranowb
 * @author amit bhayani
 * 
 */
public class UserInformationImpl extends External implements UserInformation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.asn.External#decode(org.mobicents.protocols.asn.AsnInputStream)
	 */
	
	public void decode(AsnInputStream ais) throws ParseException {

		try {
			int length = ais.readLength();
			int tag = ais.readTag();

			if (tag != Tag.EXTERNAL) {
				throw new AsnException(
						"Wrong value of tag, expected EXTERNAL, found: " + tag);
			}

			super.decode(ais);
		} catch (AsnException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.asn.External#encode(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	
	public void encode(AsnOutputStream aos) throws ParseException {
		// this will have EXTERNAL
		AsnOutputStream localAsn = new AsnOutputStream();
		try {
			super.encode(localAsn);
		} catch (AsnException e) {
			throw new ParseException(e);
		}

		// now lets write ourselves
		aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
		byte[] externalData = localAsn.toByteArray();
		try {
			aos.writeLength(externalData.length);
			aos.write(externalData);
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}
}
