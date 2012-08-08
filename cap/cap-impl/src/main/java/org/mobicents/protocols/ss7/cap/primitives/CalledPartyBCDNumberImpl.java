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

package org.mobicents.protocols.ss7.cap.primitives;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;
import org.mobicents.protocols.ss7.map.primitives.TbcdString;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CalledPartyBCDNumberImpl extends OctetStringBase implements CalledPartyBCDNumber {

	private static final String NAI = "nai";
	private static final String NPI = "npi";
	private static final String NUMBER = "number";

	protected static final int NO_EXTENSION_MASK = 0x80;
	protected static final int NATURE_OF_ADD_IND_MASK = 0x70;
	protected static final int NUMBERING_PLAN_IND_MASK = 0x0F;

	public CalledPartyBCDNumberImpl() {
		super(1, 41, "CalledPartyBCDNumber");
	}

	public CalledPartyBCDNumberImpl(byte[] data) {
		super(1, 41, "CalledPartyBCDNumber", data);
	}

	public CalledPartyBCDNumberImpl(AddressNature addressNature, NumberingPlan numberingPlan, String address) throws CAPException {
		super(1, 41, "CalledPartyBCDNumber");

		this.setParameters(addressNature, numberingPlan, address);
	}

	protected void setParameters(AddressNature addressNature, NumberingPlan numberingPlan, String address) throws CAPException {

		if (addressNature == null || numberingPlan == null || address == null)
			throw new CAPException("Error when encoding " + _PrimitiveName + ": addressNature, numberingPlan or address is empty");

		this._testLengthEncode(address);

		ByteArrayOutputStream stm = new ByteArrayOutputStream();

		int nature;
		if (address.length() % 2 != 0) // odd digits
			nature = 0x80;
		else
			nature = 0;
		nature = nature | (addressNature.getIndicator() << 4);
		nature = nature | (numberingPlan.getIndicator());
		stm.write(nature);
		
		if (numberingPlan == NumberingPlan.spare_5) {
			// -- In the context of the DestinationSubscriberNumber field in ConnectSMSArg or  
			// -- InitialDPSMSArg, a CalledPartyBCDNumber may also contain an alphanumeric  
			// -- character string. In this case, type-of-number '101'B is used, in accordance  
			// -- with 3GPP TS 23.040 [6]. The address is coded in accordance with the  
			// -- GSM 7-bit default alphabet definition and the SMS packing rules  
			// -- as specified in 3GPP TS 23.038 [15] in this case.
			
			// .....................................


		} else {
			try {
				TbcdString.encodeString(stm, address);
			} catch (MAPException e) {
				throw new CAPException(e);
			}
		}
	}
	
	
	public byte[] getData() {
		return data;
	}	

	public AddressNature getAddressNature() {

		if (this.data == null || this.data.length == 0)
			return null;

		int nature = this.data[0];
		int natureOfAddInd = ((nature & NATURE_OF_ADD_IND_MASK) >> 4);
		return AddressNature.getInstance(natureOfAddInd);
	}

	public NumberingPlan getNumberingPlan() {

		if (this.data == null || this.data.length == 0)
			return null;

		int nature = this.data[0];
		int numbPlanInd = (nature & NUMBERING_PLAN_IND_MASK);
		return NumberingPlan.getInstance(numbPlanInd);
	}

	public boolean isExtension() {

		if (this.data == null || this.data.length == 0)
			return false;

		int nature = this.data[0];
		if ((nature & NO_EXTENSION_MASK) == 0x80)
			return true;
		else
			return false;
	}

	public String getAddress() {

		if (this.data == null || this.data.length == 0)
			return null;

		try {
			if (this.getNumberingPlan() == NumberingPlan.spare_5) {
				// -- In the context of the DestinationSubscriberNumber field in ConnectSMSArg or  
				// -- InitialDPSMSArg, a CalledPartyBCDNumber may also contain an alphanumeric  
				// -- character string. In this case, type-of-number '101'B is used, in accordance  
				// -- with 3GPP TS 23.040 [6]. The address is coded in accordance with the  
				// -- GSM 7-bit default alphabet definition and the SMS packing rules  
				// -- as specified in 3GPP TS 23.038 [15] in this case.
				
				// .....................................
				return null;


			} else {

				ByteArrayInputStream stm = new ByteArrayInputStream(this.data);
				stm.read();
				String address = TbcdString.decodeString(stm, this.data.length - 1);
				return address;
			}
		} catch (MAPParsingComponentException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}


	
	
	protected void _testLengthEncode(String address) throws CAPException {

		if (address.length() > 38)
			throw new CAPException("Error when encoding AddressString: address length must not exceed 38 digits");
	}
	
	
	
	
	
	
	


//	private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException {
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
//		try {
//			this.address = TbcdString.decodeString(ansIS, length - 1);
//		} catch (MAPParsingComponentException e) {
//			throw new CAPParsingComponentException(e,CAPParsingComponentExceptionReason.MistypedParameter);
//		}
//	}


//	public void encodeData(AsnOutputStream asnOs) throws CAPException {
//
//		if (this.addressNature == null || this.numberingPlan == null || this.address == null)
//			throw new CAPException(
//					"Error when encoding AddressString: addressNature, numberingPlan or address is empty");
//
//		this._testLengthEncode();
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
//		try {
//			TbcdString.encodeString(asnOs, this.address);
//		} catch (MAPException e) {
//			throw new CAPException(e);
//		}
//	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");
		if (this.getAddressNature() != null) {
			sb.append("addressNature=");
			sb.append(this.getAddressNature());
		}
		if (this.getNumberingPlan() != null) {
			sb.append(", numberingPlan=");
			sb.append(this.getNumberingPlan());
		}
		if (this.getAddress() != null) {
			sb.append(", address=");
			sb.append(this.getAddress());
		}
		sb.append("]");

		return sb.toString();
	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<CalledPartyBCDNumberImpl> ADDRESS_STRING_XML = new XMLFormat<CalledPartyBCDNumberImpl>(
			CalledPartyBCDNumberImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, CalledPartyBCDNumberImpl addressStringImpl)
				throws XMLStreamException {
			try {
				addressStringImpl.setParameters(AddressNature.getInstance(xml.getAttribute(NAI, 0)), NumberingPlan.getInstance(xml.getAttribute(NPI, 0)),
						xml.getAttribute(NUMBER, ""));
			} catch (CAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void write(CalledPartyBCDNumberImpl addressStringImpl, javolution.xml.XMLFormat.OutputElement xml)
				throws XMLStreamException {

			xml.setAttribute(NAI, addressStringImpl.getAddressNature().getIndicator());
			xml.setAttribute(NPI, addressStringImpl.getNumberingPlan().getIndicator());
			xml.setAttribute(NUMBER, addressStringImpl.getAddress());
		}
	};
}

