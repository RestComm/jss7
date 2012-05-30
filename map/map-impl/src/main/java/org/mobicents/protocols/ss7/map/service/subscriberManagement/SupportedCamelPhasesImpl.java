package org.mobicents.protocols.ss7.map.service.subscriberManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 * 
 */
public class SupportedCamelPhasesImpl implements SupportedCamelPhases, MAPAsnPrimitive {

	private static final int _INDEX_Phase1 = 0;
	private static final int _INDEX_Phase2 = 1;
	private static final int _INDEX_Phase3 = 2;
	private static final int _INDEX_Phase4 = 3;

	private BitSetStrictLength bitString = new BitSetStrictLength(4);

	/**
	 * 
	 */
	public SupportedCamelPhasesImpl() {
	}

	public SupportedCamelPhasesImpl(boolean phase1, boolean phase2, boolean phase3, boolean phase4) {
		if (phase1)
			this.bitString.set(_INDEX_Phase1);
		if (phase2)
			this.bitString.set(_INDEX_Phase2);
		if (phase3)
			this.bitString.set(_INDEX_Phase3);
		if (phase4)
			this.bitString.set(_INDEX_Phase4);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.
	 * SupportedCamelPhases#getPhase1Supported()
	 */
	public boolean getPhase1Supported() {
		return this.bitString.get(_INDEX_Phase1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.
	 * SupportedCamelPhases#getPhase2Supported()
	 */
	public boolean getPhase2Supported() {
		return this.bitString.get(_INDEX_Phase2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.
	 * SupportedCamelPhases#getPhase3Supported()
	 */
	public boolean getPhase3Supported() {
		return this.bitString.get(_INDEX_Phase3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.
	 * SupportedCamelPhases#getPhase4Supported()
	 */
	public boolean getPhase4Supported() {
		return this.bitString.get(_INDEX_Phase4);
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
	 */
	public int getTag() throws MAPException {
		return Tag.STRING_BIT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
	 */
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	public boolean getIsPrimitive() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll(
	 * org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SupportedCamelPhases: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SupportedCamelPhases: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SupportedCamelPhases: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SupportedCamelPhases: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	
	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		if (length < 2 || length > 3)
			throw new MAPParsingComponentException("Error decoding SupportedCamelPhases: the SupportedCamelPhases field must contain from 2 or 3 octets. Contains: " + length,
					MAPParsingComponentExceptionReason.MistypedParameter);
		
		this.bitString = ansIS.readBitStringData(length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding SupportedCamelPhases: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		try {
			asnOs.writeBitStringData(this.bitString);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding SupportedCamelPhases: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding SupportedCamelPhases: " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SupportedCamelPhases [");

		if (getPhase1Supported())
			sb.append("Phase1Supported, ");
		if (getPhase2Supported())
			sb.append("Phase2Supported, ");
		if (getPhase3Supported())
			sb.append("Phase3Supported, ");
		if (getPhase4Supported())
			sb.append("Phase4Supported, ");

		sb.append("]");

		return sb.toString();
	}
}

