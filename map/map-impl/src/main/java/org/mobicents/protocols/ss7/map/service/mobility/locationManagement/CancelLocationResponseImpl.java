package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

public class CancelLocationResponseImpl extends MobilityMessageImpl implements CancelLocationResponse{

	private MAPExtensionContainer extensionContainer;
	private long mapProtocolVersion;
	
	public CancelLocationResponseImpl(long mapProtocolVersion) {
		this.mapProtocolVersion = mapProtocolVersion;
	}
	
	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.cancelLocation_Response;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.cancelLocation;
	}

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag)
			throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public long getMapProtocolVersion() {
		return this.mapProtocolVersion;
	}

}
