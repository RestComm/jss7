package org.mobicents.protocols.ss7.map.service.mobility.imei;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;

public class RequestedEquipmentInfoImpl implements RequestedEquipmentInfo {

	private BitSetStrictLength bitString = new BitSetStrictLength(2);

	@Override
	public boolean getEquipmentStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBmuef() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getTag() throws MAPException {
		return Tag.STRING_BIT;
	}
	
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}
	
	public boolean getIsPrimitive() {
		return true;
	}
	
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		//TODO: I should understand what this mean
		if (length == 0 || length > 4)
			throw new MAPParsingComponentException("Error decoding RequestedEquipmentInfoImpl: the RequestedEquipmentInfoImpl field must contain from 1 or 4 octets. Contains: " + length,
					MAPParsingComponentExceptionReason.MistypedParameter);
		
		this.bitString = ansIS.readBitStringData(length);
	}
	
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);
	}

	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) { //TODO: What is the MWStatus?
			throw new MAPException("AsnException when encoding MWStatus: " + e.getMessage(), e);
		}
	}
	
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		try {
			asnOs.writeBitStringData(this.bitString);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding MWStatus: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MWStatus: " + e.getMessage(), e);
		}
	}
	

}
