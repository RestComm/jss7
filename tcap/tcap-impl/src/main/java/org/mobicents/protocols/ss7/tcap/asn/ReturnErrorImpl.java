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
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
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
	private Parameter[] parameters;

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
	public Parameter[] getParameters() {
		return this.parameters;
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
	public void setParameters(Parameter[] p) {
		this.parameters = p;

	}

	public ComponentType getType() {

		return ComponentType.ReturnError;
	}

	
	public String toString() {
		return "ReturnError[invokeId=" + invokeId + ", errorCode=" + errorCode + ", parameters=" + Arrays.toString(parameters) + "]";
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
			if (tag == ErrorCode._TAG_GLOBAL || tag == ErrorCode._TAG_LOCAL) {
				this.errorCode = TcapFactory.createErrorCode(tag == ErrorCode._TAG_GLOBAL ? ErrorCodeType.Global : ErrorCodeType.Local);
				this.errorCode.decode(localAis);
				
			} else {
				throw new ParseException("Expected Local|Globa error code, found: " + tag);
			}
			if(localAis.available() == 0)
			{
				return;//rest is optional
			}
			tag = localAis.readTag();
			
			if (tag == Tag.SEQUENCE) {
				
				int length = localAis.readLength();
				
				List<Parameter> paramsList = new ArrayList<Parameter>();

				while (localAis.available() > 0) {
					// This is Parameter Tag
					tag = localAis.readTag();
					Parameter p = TcapFactory.createParameter(tag, localAis);
					paramsList.add(p);
				}
				
				this.parameters = new Parameter[paramsList.size()];
				this.parameters = paramsList.toArray(this.parameters);
				
				paramsList.clear();				
				
			} else {
				this.parameters = new Parameter[] { TcapFactory
						.createParameter(tag, localAis) };
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

		if (this.invokeId == null) {
			throw new ParseException("Invoke ID not set!");
		}
		if (this.errorCode == null) {
			throw new ParseException("Operation Code not set!");
		}
		try {
			AsnOutputStream localAos = new AsnOutputStream();

			localAos.writeInteger(_TAG_IID_CLASS, _TAG_IID, this.invokeId);

			this.errorCode.encode(localAos);

			if (this.parameters != null) {
				if(this.parameters.length > 1 ){
					
					AsnOutputStream aosTemp = new AsnOutputStream();
					for(Parameter p : this.parameters){
						p.encode(aosTemp);
					}
					
					byte[] paramData = aosTemp.toByteArray();
					
					//Sequence TAG
					localAos.write(0x30);
					
					//Sequence Length
					localAos.write(paramData.length);
					
					//Now write the Parameter's 
					localAos.write(paramData);
					
				} else{
					this.parameters[0].encode(localAos);
				}
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
