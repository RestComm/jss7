/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.primitives;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingLevel;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;

/**
 * @author amit bhayani
 * 
 */
public class AlertingPatternImpl implements AlertingPattern, MAPAsnPrimitive {

	private AlertingLevel alertingLevel;
	private AlertingCategory alertingCategory;

	/**
	 * 
	 */
	public AlertingPatternImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param patternType
	 * @param alertingPattern
	 */
	public AlertingPatternImpl(AlertingLevel alertingLevel) {
		super();
		this.alertingLevel = alertingLevel;

	}

	/**
	 * @param alertingCategory
	 */
	public AlertingPatternImpl(AlertingCategory alertingCategory) {
		super();
		this.alertingCategory = alertingCategory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass
	 * ()
	 */
	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	@Override
	public boolean getIsPrimitive() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
	 * (org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding AlertingPattern: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding AlertingPattern: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding AlertingPattern: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding AlertingPattern: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		if (length != 1)
			throw new MAPParsingComponentException("Error decoding AlertingPattern: the AlertingPattern field must contain 1 octets. Contains: " + length,
					MAPParsingComponentExceptionReason.MistypedParameter);

		byte[] rawdata = ansIS.readOctetStringData(length);
		int data = rawdata[0];

		switch (data) {
		case 0:
			this.alertingLevel = AlertingLevel.Level0;
			break;
		case 1:
			this.alertingLevel = AlertingLevel.Level1;
			break;
		case 2:
			this.alertingLevel = AlertingLevel.Level2;
			break;
		case 4:
			this.alertingCategory = AlertingCategory.Category1;
			break;
		case 5:
			this.alertingCategory = AlertingCategory.Category2;
			break;
		case 6:
			this.alertingCategory = AlertingCategory.Category3;
			break;
		case 7:
			this.alertingCategory = AlertingCategory.Category4;
			break;
		case 8:
			this.alertingCategory = AlertingCategory.Category5;
			break;
		default:
			// Reserved
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding AlertingPattern: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.alertingLevel == null && this.alertingCategory == null)
			throw new MAPException("Error when encoding AlertingPattern: alertingLevel and alertingCategory is empty");

		byte[] rawData = null;
		if (this.alertingLevel != null) {
			rawData = new byte[] { this.alertingLevel.getLevel() };
		} else {
			rawData = new byte[] { this.alertingCategory.getCategory() };
		}
		
		asnOs.writeOctetStringData(rawData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern#
	 * getAlertingLevel()
	 */
	@Override
	public AlertingLevel getAlertingLevel() {
		return this.alertingLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern#
	 * getAlertingCategory()
	 */
	@Override
	public AlertingCategory getAlertingCategory() {
		return this.alertingCategory;
	}

}
