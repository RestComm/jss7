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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;

/**
 * @author baranowb
 * 
 */
public class ReturnErrorImpl implements ReturnError {

	// mandatory
	private Long invokeId;

	// mandatory
	private ErrorCode errorCode;

	//FIXME: check this aganist ASN and traces!
	// optional
	private Parameter parameter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError#getErrorCode()
	 */
	public ErrorCode getErrorCode() {

		return this.errorCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError#getInvokeId()
	 */
	public Long getInvokeId() {

		return this.invokeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError#getParameter()
	 */
	public Parameter getParameter() {
		return this.parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError#setErrorCode(org
	 * .mobicents.protocols.ss7.tcap.asn.comp.ErrorCode)
	 */
	public void setErrorCode(ErrorCode ec) {
		this.errorCode = ec;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError#setInvokeId(java
	 * .lang.Long)
	 */
	public void setInvokeId(Long i) {
		this.invokeId = i;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError#setParameter(org
	 * .mobicents.protocols.ss7.tcap.asn.comp.Parameter)
	 */
	public void setParameter(Parameter p) {
		this.parameter = p;

	}

	public ComponentType getType() {

		return ComponentType.ReturnError;
	}

	
	public String toString() {
		return "ReturnError[invokeId=" + invokeId + ", errorCode=" + errorCode + ", parameters=" + parameter + "]";
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
			if (ais.available() < len) {
				throw new ParseException("Not enough data!");
			}
			byte[] data = new byte[len];
			if (data.length != ais.read(data)) {
				throw new ParseException("Not enought data read.");
			}
			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(data));
			int tag = localAis.readTag();
			if (tag != _TAG_IID) {
				throw new ParseException("Expected InvokeID tag, found: " + tag);
			}

			this.invokeId = localAis.readInteger();

			tag = localAis.readTag();
			this.errorCode = TcapFactory.createErrorCode();
			switch (tag) {
			case ErrorCode._TAG_GLOBAL:
				((ErrorCodeImpl) this.errorCode).setErrorCodeType(ErrorCodeType.Global);
				break;
			case ErrorCode._TAG_LOCAL:
				((ErrorCodeImpl) this.errorCode).setErrorCodeType(ErrorCodeType.Local);
				break;
			default:
				throw new ParseException("Expected Local|Globa error code, found: " + tag);
			}
			this.errorCode.decode(localAis);
			
			if(localAis.available() == 0)
			{
				return;//rest is optional
			}
			
			tag = localAis.readTag();
			this.parameter = TcapFactory.createParameter(tag, localAis);
		
		
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

		if (this.invokeId == null) {
			throw new ParseException("Invoke ID not set!");
		}
		if (this.errorCode == null) {
			throw new ParseException("Error Code not set!");
		}
		try {
			AsnOutputStream localAos = new AsnOutputStream();

			localAos.writeInteger(_TAG_IID_CLASS, _TAG_IID, this.invokeId);

			this.errorCode.encode(localAos);

			if (this.parameter != null) {
				this.parameter.encode(localAos);
			}
			
			byte[] data = localAos.toByteArray();
			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			aos.writeLength(data.length);
			aos.write(data);
		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

}



