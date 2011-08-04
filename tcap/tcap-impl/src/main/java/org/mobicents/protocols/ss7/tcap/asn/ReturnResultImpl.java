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
import java.util.List;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;

/**
 * @author baranowb
 * @author amit bhayani
 * 
 */
public class ReturnResultImpl implements ReturnResult {

	// mandatory
	private Long invokeId;

	
	//This is sequence, both must be present
	// optional: this is sequence
	private OperationCode operationCode;
	// optional
	private Parameter parameter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#getInvokeId()
	 */
	public Long getInvokeId() {

		return this.invokeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#getOperationCode()
	 */
	public OperationCode getOperationCode() {

		return this.operationCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#getParameter()
	 */
	public Parameter getParameter() {
		return this.parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Return#setInvokeId(java.lang
	 * .Long)
	 */
	public void setInvokeId(Long i) {
		if ((i == null) || (i < -128 || i > 127)) {
			throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
		}
		this.invokeId = i;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Return#setOperationCode(org
	 * .mobicents.protocols.ss7.tcap.asn.comp.OperationCode[])
	 */
	public void setOperationCode(OperationCode oc) {
		this.operationCode = oc;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Return#setParameter(org.mobicents
	 * .protocols.ss7.tcap.asn.comp.Parameter)
	 */
	public void setParameter(Parameter p) {
		this.parameter = p;
	}

	public ComponentType getType() {

		return ComponentType.ReturnResult;
	}

	
	public String toString() {
		return "ReturnResult[invokeId=" + invokeId + ", operationCode=" + operationCode + ", parameter=" + parameter + "]";
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
			if (len == Tag.Indefinite_Length) {
				throw new ParseException("Unspiecified length is not supported.");
			}

			byte[] data = new byte[len];
			if (len != ais.read(data)) {
				throw new ParseException("Not enough data read.");
			}

			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(data));

			int tag = localAis.readTag();
			if (tag != _TAG_IID) {
				throw new ParseException("Expected InvokeID tag, found: " + tag);
			}

			this.invokeId = localAis.readInteger();

			if (localAis.available() <= 0) {
				return;
			}

			tag = localAis.readTag();

			if (tag == Tag.SEQUENCE) {
				// sequence of OperationCode

				len = localAis.readLength();
				if (len == Tag.Indefinite_Length) {
					throw new ParseException("Unspiecified length is not supported.");
				}

				data = new byte[len];
				int tlen = localAis.read(data);
				if (len != tlen) {
					throw new ParseException("Not enough data read. Expected: " + len + ", actaul: " + tlen);
				}
				AsnInputStream sequenceStream = new AsnInputStream(new ByteArrayInputStream(data));

				tag = sequenceStream.readTag();
				if (tag == OperationCode._TAG_GLOBAL || tag == OperationCode._TAG_LOCAL) {
					this.operationCode = TcapFactory.createOperationCode(tag, sequenceStream);
				} else {
					throw new ParseException("Expected Global|Local operation code.");
				}

				if (sequenceStream.available() > 0) {
					tag = sequenceStream.readTag();
				
					this.parameter = TcapFactory.createParameter(tag, sequenceStream);
					
				} else {
					throw new ParseException("Not enought data to decode Parameter part of result!");
				}
			} else {
				throw new ParseException("Expected SEQUENCE tag for OperationCode and Parameter part, found: " + tag);
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

		if (invokeId == null) {
			throw new ParseException("No Invoke ID set.");
		}
		try {
			AsnOutputStream localAos = new AsnOutputStream();
			byte[] data = null;
			if (this.operationCode != null && this.parameter != null) {
				this.operationCode.encode(localAos);
				this.parameter.encode(localAos);
				data = localAos.toByteArray();
				localAos.reset();
			} else {
				// FIXME: add checks for both present?
			}

			// form msg from top.
			localAos.writeInteger(_TAG_IID_CLASS, _TAG_IID, this.invokeId);
			if (data != null) {
				localAos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
				localAos.writeLength(data.length);
				localAos.write(data);
			}
			data = localAos.toByteArray();

			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			aos.writeLength(data.length);
			aos.write(data);
		} catch (IOException e) {
			throw new ParseException(e);
		} catch (AsnException e) {
			throw new ParseException(e);
		}
	}

}
