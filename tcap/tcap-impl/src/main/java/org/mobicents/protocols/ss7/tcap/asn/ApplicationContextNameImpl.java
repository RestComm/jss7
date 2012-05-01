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
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 * 
 */
public class ApplicationContextNameImpl implements ApplicationContextName {

	// object identifier value
	private long[] oid;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 * .asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {
		try {
			AsnInputStream localAis = ais.readSequenceStream();
			int tag = localAis.readTag();
			if (tag != Tag.OBJECT_IDENTIFIER || localAis.getTagClass() != Tag.CLASS_UNIVERSAL)
				throw new ParseException("Error decoding ApplicationContextName: bad tag or tagClass, found tag=" + tag + ", tagClass="
						+ localAis.getTagClass());
			this.oid = localAis.readObjectIdentifier();
			
		} catch (IOException e) {
			throw new ParseException("IOException while decoding ApplicationContextName: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("AsnException while decoding ApplicationContextName: " + e.getMessage(), e);
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
		
		if (this.oid == null)
			throw new ParseException("Error while decoding ApplicationContextName: No OID value set");
		
		try {
			aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
			int pos = aos.StartContentDefiniteLength();
			
			aos.writeObjectIdentifier(this.oid);
			
			aos.FinalizeContent(pos);
			
		} catch (IOException e) {
			throw new ParseException("IOException while encoding ApplicationContextName: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("IOException while encoding ApplicationContextName: " + e.getMessage(), e);
		}

	}

	/**
	 * @return the oid
	 */
	public long[] getOid() {
		return oid;
	}

	/**
	 * @param oid
	 *            the oid to set
	 */
	public void setOid(long[] oid) {
		this.oid = oid;
	}

	
	public String toString() {
		return "ApplicationContextName[oid=" + Arrays.toString(oid) + "]";
	}

}
