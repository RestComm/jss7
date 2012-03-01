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
	
	
	@Override
	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return true;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding AddressString: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding AddressString: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	protected void _testLengthDecode(int length) throws MAPParsingComponentException {
		if (length > 20)
			throw new MAPParsingComponentException("Error when decoding AddressString: mesage length must not exceed 20",
					MAPParsingComponentExceptionReason.MistypedParameter);
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException {

		this._testLengthDecode(length);
		
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
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding AddressString: " + e.getMessage(), e);
		}
	}

	protected void _testLengthEncode() throws MAPException {

		if (this.address.length() > 38)
			throw new MAPException("Error when encoding AddressString: address length must not exceed 38 digits");
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		
		if (this.addressNature == null || this.numberingPlan == null || this.address == null)
			throw new MAPException("Error when encoding AddressString: addressNature, numberingPlan or address is empty");
		
		this._testLengthEncode();

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

	@Override
	public String toString() {
		return "AddressString[AddressNature=" + this.addressNature.toString() + ", NumberingPlan=" + this.numberingPlan.toString() + ", Address="
				+ this.address + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((addressNature == null) ? 0 : addressNature.hashCode());
		result = prime * result + ((numberingPlan == null) ? 0 : numberingPlan.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddressStringImpl other = (AddressStringImpl) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (addressNature != other.addressNature)
			return false;
		if (numberingPlan != other.numberingPlan)
			return false;
		return true;
	}
}

