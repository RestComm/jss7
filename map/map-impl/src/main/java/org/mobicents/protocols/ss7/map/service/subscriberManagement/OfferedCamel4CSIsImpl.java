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
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 *
 */
public class OfferedCamel4CSIsImpl implements OfferedCamel4CSIs, MAPAsnPrimitive {
	
	private static final int _ID_o_csi = 0;
	private static final int _ID_d_csi = 1;
	private static final int _ID_vt_csi = 2;
	private static final int _ID_t_csi = 3;
	private static final int _ID_mt_sms_csi = 4;
	private static final int _ID_mg_csi = 5;
	private static final int _ID_psi_enhancements = 6;
	
	private BitSetStrictLength bitString = new BitSetStrictLength(16);
	
	/**
	 * 
	 */
	public OfferedCamel4CSIsImpl() {
	}


	/**
	 * @param oCsi
	 * @param dCsi
	 * @param vtCsi
	 * @param tCsi
	 * @param mtSMSCsi
	 * @param mgCsi
	 * @param psiEnhancements
	 */
	public OfferedCamel4CSIsImpl(boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi, boolean mgCsi, boolean psiEnhancements) {
		super();
		if (oCsi)
			this.bitString.set(_ID_o_csi);
		if (dCsi)
			this.bitString.set(_ID_d_csi);
		if (vtCsi)
			this.bitString.set(_ID_vt_csi);
		if (tCsi)
			this.bitString.set(_ID_t_csi);
		if (mtSMSCsi)
			this.bitString.set(_ID_mt_sms_csi);
		if (mgCsi)
			this.bitString.set(_ID_mg_csi);
		if (psiEnhancements)
			this.bitString.set(_ID_psi_enhancements);
	}



	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getOCsi()
	 */
	@Override
	public boolean getOCsi() {
		return this.bitString.get(_ID_o_csi);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getDCsi()
	 */
	@Override
	public boolean getDCsi() {
		return this.bitString.get(_ID_d_csi);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getVtCsi()
	 */
	@Override
	public boolean getVtCsi() {
		return this.bitString.get(_ID_vt_csi);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getTCsi()
	 */
	@Override
	public boolean getTCsi() {
		return this.bitString.get(_ID_t_csi);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getMtSmsCsi()
	 */
	@Override
	public boolean getMtSmsCsi() {
		return this.bitString.get(_ID_mt_sms_csi);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getMgCsi()
	 */
	@Override
	public boolean getMgCsi() {
		return this.bitString.get(_ID_mg_csi);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getPsiEnhancements()
	 */
	@Override
	public boolean getPsiEnhancements() {
		return this.bitString.get(_ID_psi_enhancements);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		return Tag.STRING_BIT;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
	 */
	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive()
	 */
	@Override
	public boolean getIsPrimitive() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll(org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding OfferedCamel4CSIs: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding OfferedCamel4CSIs: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData(org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding OfferedCamel4CSIs: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding OfferedCamel4CSIs: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		if (length == 0 || length > 2)
			throw new MAPParsingComponentException("Error decoding SupportedCamelPhases: the OfferedCamel4CSIs field must contain from 1 or 2 octets. Contains: " + length,
					MAPParsingComponentExceptionReason.MistypedParameter);
		
		this.bitString = ansIS.readBitStringData(length);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding OfferedCamel4CSIs: " + e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		try {
			asnOs.writeBitStringData(this.bitString);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding OfferedCamel4CSIs: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding OfferedCamel4CSIs: " + e.getMessage(), e);
		}
	}
}
