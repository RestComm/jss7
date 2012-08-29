package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;


/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class IMSIWithLMSIImpl implements IMSIWithLMSI, MAPAsnPrimitive  {
	
	private IMSI imsi;
	private LMSI lmsi;
	
	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
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
	public void decodeAll(AsnInputStream ansIS)
			throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding IMSIWithLMSIImpl: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding IMSIWithLMSIImpl: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding IMSIWithLMSIImpl : " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding IMSIWithLMSIImpl: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	

	private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.imsi = null;
		this.lmsi = null;

		int tag = ais.getTag();

		if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
			throw new MAPParsingComponentException("Error while decoding IMSIWithLMSIImpl: Primitive has bad tag class or is not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);

		switch (tag) {
			case 0:
				this.imsi = new IMSIImpl();
				((IMSIImpl) this.imsi).decodeData(ais, length);
				break;
			case 1:
				this.lmsi = new LMSIImpl();
				((LMSIImpl) this.lmsi).decodeData(ais, length);
				break;
			default:
				throw new MAPParsingComponentException("Error while decoding IMSIWithLMSIImpl: bad choice tag",
						MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag)
			throws MAPException {
		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding IMSIWithLMSIImpl: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		this.imsi = null;
		this.lmsi = null;
		
		if (this.imsi == null || this.lmsi == null ) {
			throw new MAPException("Error while decoding IMSIWithLMSIImpl : One and only one choice must be selected");
		}
		if (this.imsi != null) {
			((IMSIImpl) this.imsi).encodeData(asnOs);
		}
		if(this.lmsi  !=null) {
			((LMSIImpl) this.lmsi).encodeData(asnOs);
		}
	}

	@Override
	public IMSI getImsi() {
		return this.imsi;
	}

	@Override
	public LMSI getLmsi() {
		return this.lmsi;
	}
}
