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
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;

/**
 * @author baranowb
 * 
 */
public class OperationCodeImpl implements OperationCode {

	private Long operationCode;
	private OperationCodeType type;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.OperationCode#getCode()
	 */
	public Long getCode() {

		return operationCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.OperationCode#getOperationType()
	 */
	public OperationCodeType getOperationType() {

		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.OperationCode#setCode(java.lang.
	 * Integer)
	 */
	public void setCode(Long i) {
		operationCode = i;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.OperationCode#setOperationType(org
	 * .mobicents.protocols.ss7.tcap.asn.OperationCodeType)
	 */
	public void setOperationType(OperationCodeType t) {
		this.type = t;

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
			this.operationCode = ais.readInteger();
		} catch (AsnException e) {
			throw new ParseException(e);
		} catch (IOException e) {
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
		if (this.type == null) {
			throw new ParseException("No indication Global|Local.");
		}
		if (this.operationCode == null) {
			throw new ParseException("Operation code not set.");
		}
		try {
			if (type == OperationCodeType.Global) {

				aos.writeInteger(_TAG_CLASS, _TAG_GLOBAL, this.operationCode);

			} else {
				aos.writeInteger(_TAG_CLASS, _TAG_LOCAL, this.operationCode);
			}
		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

}
