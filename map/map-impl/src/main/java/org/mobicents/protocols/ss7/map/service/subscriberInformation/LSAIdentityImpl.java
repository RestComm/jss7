package org.mobicents.protocols.ss7.map.service.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LSAIdentity;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 *
 */
public class LSAIdentityImpl implements LSAIdentity, MAPAsnPrimitive {
	
	private byte[] data;

	/**
	 * 
	 */
	public LSAIdentityImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param data
	 */
	public LSAIdentityImpl(byte[] data) {
		super();
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
	 */
	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
	 */
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive()
	 */
	public boolean getIsPrimitive() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll(org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LSAIdentity: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding LSAIdentity: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}	
	}
	
	private void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.data = asnIS.readOctetStringData(length);
		if (this.data.length != 3 )
			throw new MAPParsingComponentException("Error while decoding LSAIdentity value must be 3 bytes length, found: "
					+ this.data.length, MAPParsingComponentExceptionReason.MistypedParameter);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData(org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding LSAIdentity: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding LSAIdentity: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);	
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding LSAIdentity: " + e.getMessage(), e);
		}		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.data == null)
			throw new MAPException("Data must not be null");
		if (this.data.length != 3)
			throw new MAPException("Data length must be 3");
		
		asnOs.writeOctetStringData(this.data);		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.LSAIdentity#getData()
	 */
	public byte[] getData() {
		return this.data;
	}

}
