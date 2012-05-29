package org.mobicents.protocols.ss7.map.service.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MSRadioAccessCapability;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 * 
 */
public class MSRadioAccessCapabilityImpl implements MSRadioAccessCapability, MAPAsnPrimitive {

	public static final String _PrimitiveName = "MSNetworkCapability";

	private byte[] data;

	/**
	 * 
	 */
	public MSRadioAccessCapabilityImpl() {
		// TODO Auto-generated constructor stub
	}

	public MSRadioAccessCapabilityImpl(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
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
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.data = ansIS.readOctetStringData(length);

		if (this.data.length < 1 || this.data.length > 50)
			throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName + ": data length must be from 1 to 50, found: " + this.data.length,
					MAPParsingComponentExceptionReason.MistypedParameter);
	}

	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, this.getTag());
	}

	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.data == null)
			throw new MAPException("Error when encoding " + _PrimitiveName + ": data is empty");
		if (this.data.length < 1 || this.data.length > 50)
			throw new MAPException("Error when encoding " + _PrimitiveName + ": data length must be from 1 to 50, found: " + this.data.length);

		asnOs.writeOctetStringData(data);
	}

}
