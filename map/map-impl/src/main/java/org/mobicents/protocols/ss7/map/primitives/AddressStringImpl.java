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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tcap.asn.ParameterImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class AddressStringImpl extends TbcdString implements AddressString {

	protected int NO_EXTENSION_MASK = 0x80;
	protected int NATURE_OF_ADD_IND_MASK = 0x70;
	protected int NUMBERING_PLAN_IND_MASK = 0x0F;

	protected AddressNature addressNature;
	protected NumberingPlan numberingPlan;
	protected String address;

	private boolean isExtension;

	public AddressStringImpl() {
	}

	public AddressStringImpl(AddressNature addressNature,
			NumberingPlan numberingPlan, String address) {
		super();
		this.addressNature = addressNature;
		this.numberingPlan = numberingPlan;
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public AddressNature getAddressNature() {
		return this.addressNature;
	}

	public NumberingPlan getNumberingPlan() {
		return this.numberingPlan;
	}

	public boolean isExtension() {
		return isExtension;
	}
	
	
	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
	}

	public void decode(AsnInputStream ansIS, int tagClass, boolean isPrimitive, int tag, int length) throws MAPParsingComponentException {
		
		if (length > 20)
			throw new MAPParsingComponentException("Error when decoding AddressString: mesage length must not exceed 20",
					MAPParsingComponentExceptionReason.MistypedParameter);

		try {
			// The first byte has extension, nature of address indicator and
			// numbering plan indicator
			int nature = ansIS.read();

			if ((nature & NO_EXTENSION_MASK) == 0x80) {
				this.isExtension = false;
			} else {
				this.isExtension = true;
			}

			int natureOfAddInd = ((nature & NATURE_OF_ADD_IND_MASK) >> 4);

			this.addressNature = AddressNature.getInstance(natureOfAddInd);

			int numbPlanInd = (nature & NUMBERING_PLAN_IND_MASK);

			this.numberingPlan = NumberingPlan.getInstance(numbPlanInd);

			this.address = this.decodeString(ansIS, length - 1);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding AddressString: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public void encode(AsnOutputStream asnOs) throws MAPException {
		
		if (this.addressNature == null || this.numberingPlan == null || this.address == null)
			throw new MAPException("Error when encoding AddressString: addressNature, numberingPlan or address is empty");
		if (this.address.length() > 38)
			throw new MAPException("Error when encoding AddressString: address length must not exceed 38 digits");

		int nature = 1;

		if (this.isExtension) {
			nature = 0;
		}

		nature = nature << 7;

		nature = nature | (this.addressNature.getIndicator() << 4);

		nature = nature | (this.numberingPlan.getIndicator());

		asnOs.write(nature);

		this.encodeString(asnOs, this.address);
	}


	
//	public void decode(AsnInputStream ansIS) throws MAPException, IOException {
//		
//		if (ansIS.available() > 20)
//			throw new MAPException("Error when decoding AddressString: mesage length must not exceed 20");
//
//		// The first byte has extension, nature of address indicator and
//		// numbering plan indicator
//		int nature = ansIS.read();
//
//		if ((nature & NO_EXTENSION_MASK) == 0x80) {
//			this.isExtension = false;
//		} else {
//			this.isExtension = true;
//		}
//
//		int natureOfAddInd = ((nature & NATURE_OF_ADD_IND_MASK) >> 4);
//
//		this.addressNature = AddressNature.getInstance(natureOfAddInd);
//
//		int numbPlanInd = (nature & NUMBERING_PLAN_IND_MASK);
//
//		this.numberingPlan = NumberingPlan.getInstance(numbPlanInd);
//
//		this.address = this.decodeString(ansIS);
//
//	}
//
//	public void encode(AsnOutputStream asnOs) throws MAPException {
//		
//		if (this.addressNature == null || this.numberingPlan == null || this.address == null)
//			throw new MAPException("Error when encoding AddressString: addressNature, numberingPlan or address is empty");
//		if (this.address.length() > 38)
//			throw new MAPException("Error when encoding AddressString: address length must not exceed 38 digits");
//
//		int nature = 1;
//
//		if (this.isExtension) {
//			nature = 0;
//		}
//
//		nature = nature << 7;
//
//		nature = nature | (this.addressNature.getIndicator() << 4);
//
//		nature = nature | (this.numberingPlan.getIndicator());
//
//		asnOs.write(nature);
//
//		this.encodeString(asnOs, this.address);
//
//	}

	@Override
	public String toString() {
		return "AddressString[AddressNature=" + this.addressNature.toString() + ", NumberingPlan=" + this.numberingPlan.toString() + ", Address="
				+ this.address + "]";
	}

}
