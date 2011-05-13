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

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCodeType;

/**
 * @author baranowb
 * 
 */
public class ErrorCodeImpl implements ErrorCode {
	//FIXME: todo, ensure in case of global, check for OID
	private ErrorCodeType errorType;
	private byte[] data;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode#getData()
	 */
	public byte[] getData() {

		return this.data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode#getErrorType()
	 */
	public ErrorCodeType getErrorType() {

		return this.errorType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode#setData(byte[])
	 */
	public void setData(byte[] d) {
		this.data = d;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode#setErrorType(org.
	 * mobicents.protocols.ss7.tcap.asn.comp.ErrorCodeType)
	 */
	public void setErrorType(ErrorCodeType t) {

		this.errorType = t;
	}

	
	public String toString() {
		return "ErrorCode[errorType=" + errorType + ", data=" + Arrays.toString(data) + "]";
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
			int len = ais.readLength();
			this.data = new byte[len];
			if (len != ais.read(data)) {
				throw new ParseException("Not enough data read.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		if (errorType == null) {
			throw new ParseException("No error type set!");
		}
		if (data == null) {
			throw new ParseException("No data set!");
		}
		try {
			if (errorType == ErrorCodeType.Local) {
				aos.writeTag(_TAG_CLASS, _TAG_PRIMITIVE, _TAG_LOCAL);
			} else {
				aos.writeTag(_TAG_CLASS, _TAG_PRIMITIVE, _TAG_GLOBAL);
			}

			aos.writeLength(data.length);

			aos.write(data);
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

}
