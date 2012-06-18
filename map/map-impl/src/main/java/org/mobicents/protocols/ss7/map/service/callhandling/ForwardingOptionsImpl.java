package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingOptions;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingReason;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class ForwardingOptionsImpl implements ForwardingOptions, MAPAsnPrimitive {
	private boolean notificationToCallingParty;
	private boolean notificationToForwardingParty;
	private boolean redirectingPresentation;
	private ForwardingReason forwardingReason;
	private int code = 0;
	
	private static final int MASK_notificationForwarding = 0x80;
	private static final int MASK_redirectingPresentation = 0x40;
	private static final int MASK_notificationCalling = 0x20;
	private static final int MASK_forwardingReason = 0x0C;
	private static final int MASK_forwardingOptions = 0xEC;
	
	private static final String _PrimitiveName = "ForwardingOptions";
	
	
	public ForwardingOptionsImpl() {}

	public ForwardingOptionsImpl(boolean notificationToForwardingParty,
			 					 boolean redirectingPresentation,
								 boolean notificationToCallingParty, 
								 ForwardingReason forwardingReason) {
		this.notificationToForwardingParty = notificationToForwardingParty;
		this.redirectingPresentation = redirectingPresentation;
		this.notificationToCallingParty = notificationToCallingParty;
		this.forwardingReason = forwardingReason;
		
		int forwardingReasonCode = 3;
		if(forwardingReason != null) {
		  forwardingReasonCode = forwardingReason.getCode();
		}
			
		code = code & MASK_forwardingOptions; // bit 5 and bits 21 are 0 (unused)
		code = notificationToForwardingParty ? (code | MASK_notificationForwarding) : 
			  								   (code & ~MASK_notificationForwarding);
		code = redirectingPresentation ? (code | MASK_redirectingPresentation) : 
									     (code & ~MASK_redirectingPresentation);
		code = notificationToCallingParty ? (code | MASK_notificationCalling) : 
										    (code & ~MASK_notificationCalling);
		code = code | (forwardingReasonCode << 2) & MASK_forwardingReason;
	}
	
	@Override
	public boolean isNotificationToCallingParty() {
		return this.notificationToCallingParty;
	}

	@Override
	public boolean isNotificationToForwardingParty() {
		return this.notificationToForwardingParty;
	}

	@Override
	public boolean isRedirectingPresentation() {
		return this.redirectingPresentation;
	}

	@Override
	public ForwardingReason getForwardingReason() {
		return this.forwardingReason;
	}

	@Override
	public byte[] getEncodedData() {
		return new byte[] { (byte) code };
	}
	
	@Override
	public String getEncodedDataString() {
		return Integer.toBinaryString(code);
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
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
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
	
	private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {
		if(length != 1)
			throw new MAPParsingComponentException("Error decoding ForwardingOptions: the " + _PrimitiveName + " field must contain 1 octets. Contains: "
					+ length, MAPParsingComponentExceptionReason.MistypedParameter);
		
		this.code = ais.readOctetStringData(length)[0];
		this.notificationToForwardingParty = ((code & MASK_notificationForwarding) >> 7 == 1);
		this.redirectingPresentation = ((code & MASK_redirectingPresentation) >> 6 == 1);
		this.notificationToCallingParty = ((code & MASK_notificationCalling) >> 5 == 1);
		this.forwardingReason = ForwardingReason.getForwardingReason(
								(code & MASK_forwardingReason) >> 2);	  
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		asnOs.writeOctetStringData(getEncodedData());
	}
}