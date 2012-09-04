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
	
	public IMSIWithLMSIImpl(){
		
	}

	public IMSIWithLMSIImpl(IMSI imsi, LMSI lmsi) {
		super();
		this.imsi = imsi;
		this.lmsi = lmsi;
	}

	public static final String _PrimitiveName = "IMSIWithLMSI";
	
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
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS)
			throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding "+_PrimitiveName +": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding "+_PrimitiveName +": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding "+_PrimitiveName +" : " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding "+_PrimitiveName +": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.imsi = null;
		this.lmsi = null;
		
		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;
			
			int tag = ais.readTag();
			
			if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
				throw new MAPParsingComponentException("Error while decoding "+_PrimitiveName +": Primitive has bad tag class or is not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
	
			switch (num ) {
				case 0:
					this.imsi = new IMSIImpl();
					((IMSIImpl) this.imsi).decodeAll(ais);
					break;
				case 1:
					this.lmsi = new LMSIImpl();
					((LMSIImpl) this.lmsi).decodeAll(ais);
					break;
				default:
					throw new MAPParsingComponentException("Error while decoding "+_PrimitiveName +": bad choice tag",
							MAPParsingComponentExceptionReason.MistypedParameter);
			}
			num++;
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
			throw new MAPException("AsnException when encoding "+_PrimitiveName +": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.imsi == null || this.lmsi == null ) {
			throw new MAPException("Error while decoding "+_PrimitiveName +" : One and only one choice must be selected");
		}
		if (this.imsi != null) {
			((IMSIImpl) this.imsi).encodeAll(asnOs);
		}
		if(this.lmsi  !=null) {
			((LMSIImpl) this.lmsi).encodeAll(asnOs);
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
