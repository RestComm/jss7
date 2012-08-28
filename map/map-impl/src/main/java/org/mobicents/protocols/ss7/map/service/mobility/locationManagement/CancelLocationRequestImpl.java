package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import javax.print.CancelablePrintJob;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

public class CancelLocationRequestImpl extends MobilityMessageImpl implements CancelLocationRequest{

	private IMSI imsi;
	private IMSIWithLMSI imsiWithLmsi;
	private CancellationType cancellationType;
	private MAPExtensionContainer extensionContainer;
	private TypeOfUpdate typeOfUpdate;
	private boolean mtrfSupportedAndAuthorized;
	private boolean mtrfSupportedAndNotAuthorized;
	private ISDNAddressString newMSCNumber;
	private ISDNAddressString newVLRNumber;
	private LMSI newLmsi;
	private long mapProtocolVersion;
	
	public CancelLocationRequestImpl(long mapProtocolVersion){
		this.mapProtocolVersion = mapProtocolVersion;
	}

	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.cancelLocation_Request;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.cancelLocation;
	}

	@Override
	public IMSI getImsi() {
		return this.imsi;
	}

	@Override
	public IMSIWithLMSI getImsiWithLmsi() {
		return this.imsiWithLmsi;
	}

	@Override
	public CancellationType getCancellationType() {
		return this.cancellationType;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public TypeOfUpdate getTypeOfUpdate() {
		return this.typeOfUpdate;
	}

	@Override
	public boolean getMtrfSupportedAndAuthorized() {
		return this.mtrfSupportedAndAuthorized;
	}

	@Override
	public boolean getMtrfSupportedAndNotAuthorized() {
		return this.mtrfSupportedAndNotAuthorized;
	}

	@Override
	public ISDNAddressString getNewMSCNumber() {
		return this.newMSCNumber;
	}

	@Override
	public ISDNAddressString getNewVLRNumber() {
		return this.newVLRNumber;
	}

	@Override
	public LMSI getNewLmsi() {
		return this.newLmsi;
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
	public long getMapProtocolVersion() {
		return this.mapProtocolVersion;
	}

}
