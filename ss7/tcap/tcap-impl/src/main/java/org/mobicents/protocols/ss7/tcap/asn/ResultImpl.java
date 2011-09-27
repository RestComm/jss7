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
import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * 
 */
public class ResultImpl implements Result {

	private ResultType resultType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.Result#getResultType()
	 */
	public ResultType getResultType() {

		return resultType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Result#setResultType(org.mobicents
	 * .protocols.ss7.tcap.asn.ResultType)
	 */
	public void setResultType(ResultType t) {
		this.resultType = t;

	}

	
	public String toString() {
		return "Result[resultType=" + resultType + "]";
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
			AsnInputStream localAis = ais.readSequenceStream();

			int tag = localAis.readTag();
			if (tag != Tag.INTEGER && localAis.getTagClass() != Tag.CLASS_UNIVERSAL)
				throw new ParseException("Error while decoding AARE-apdu.result: bad tag or tag class: tag=" + tag + ", tagClass=" + localAis.getTagClass());
			
			// y, its a bit of enum, should be ok to cast :)
			long t = localAis.readInteger();
			this.resultType = ResultType.getFromInt(t);
		} catch (IOException e) {
			throw new ParseException("IOException while decoding Result: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("AsnException while decoding Result: " + e.getMessage(), e);
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
		
		if (resultType == null)
			throw new ParseException("Error encoding Result: ResultType must not be null");
		
		try {
			aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
			int pos = aos.StartContentDefiniteLength();
			aos.writeInteger(this.resultType.getType());
			aos.FinalizeContent(pos);

		} catch (IOException e) {
			throw new ParseException("IOException while encoding Result: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("AsnException while encoding Result: " + e.getMessage(), e);
		}
	}

}
