package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;

import javax.print.CancelablePrintJob;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ExtExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;



/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class CancelLocationRequestImpl extends MobilityMessageImpl implements CancelLocationRequest{
	
	private static final int TAG_typeOfUpdate = 0;
	private static final int TAG_mtrfSupportedAndAuthorized = 1;
	private static final int TAG_mtrfSupportedAndNotAuthorized = 2;
	private static final int TAG_newMSCNumber = 3;
	private static final int TAG_newVLRNumber = 4;
	private static final int TAG_newLmsi = 5;
	
	public static final int TAG_cancelLocationRequest = 3;
		
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
	
	public CancelLocationRequestImpl(IMSI imsi, IMSIWithLMSI imsiWithLmsi,
			CancellationType cancellationType,
			MAPExtensionContainer extensionContainer,
			TypeOfUpdate typeOfUpdate, boolean mtrfSupportedAndAuthorized,
			boolean mtrfSupportedAndNotAuthorized,
			ISDNAddressString newMSCNumber, ISDNAddressString newVLRNumber,
			LMSI newLmsi, long mapProtocolVersion) {
		super();
		this.imsi = imsi;
		this.imsiWithLmsi = imsiWithLmsi;
		this.cancellationType = cancellationType;
		this.extensionContainer = extensionContainer;
		this.typeOfUpdate = typeOfUpdate;
		this.mtrfSupportedAndAuthorized = mtrfSupportedAndAuthorized;
		this.mtrfSupportedAndNotAuthorized = mtrfSupportedAndNotAuthorized;
		this.newMSCNumber = newMSCNumber;
		this.newVLRNumber = newVLRNumber;
		this.newLmsi = newLmsi;
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
		if(this.mapProtocolVersion ==3){
			return TAG_cancelLocationRequest;
		}else{
			if(imsi != null){
				return Tag.STRING_OCTET;
			}else{
				return Tag.SEQUENCE;
			}
		}
	}

	@Override
	public int getTagClass() {
		if(this.mapProtocolVersion ==3){
			return Tag.CLASS_CONTEXT_SPECIFIC;
		}else{
			return Tag.CLASS_UNIVERSAL;
		}
		
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
			throw new MAPParsingComponentException(
					"IOException when decoding CancelLocationRequest: ",
					e, MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException(
					"AsnException when decoding CancelLocationRequest: ",
					e, MAPParsingComponentExceptionReason.MistypedParameter);
		}
		
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException(
					"IOException when decoding CancelLocationRequest: ",
					e, MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException(
					"AsnException when decoding CancelLocationRequest: ",
					e, MAPParsingComponentExceptionReason.MistypedParameter);
		}
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		try {
			this.encodeAll(asnOs, this.getTagClass(), this.getTag());
		} catch (Exception e) {
			e.printStackTrace();
			throw new MAPException(e);
		}
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag)
			throws MAPException {
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
	
		} catch (AsnException e) {
			e.printStackTrace();
			throw new MAPException(
					"AsnException when encoding CancelLocationRequest: "
							+ e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MAPException(
					"AsnException when encoding CancelLocationRequest: "
							+ e.getMessage(), e);
		}
		
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.imsi == null && this.imsiWithLmsi ==null) {
			throw new MAPException(
					"Error while encoding CancelLocationRequest the mandatory parameter imsi and imsiWithLmsi is not defined");
		}

		if(this.imsi!=null){
			((IMSIImpl) this.imsi).encodeAll(asnOs);
		}else{
			((IMSIWithLMSIImpl) this.imsiWithLmsi).encodeAll(asnOs);
		}
		
		
		if(this.mapProtocolVersion >= 3){
			
			if (this.cancellationType != null) {
				try {
					asnOs.writeInteger( this.cancellationType.getCode());
				} catch (IOException e) {
					throw new MAPException(
							"IOException while encoding CancelLocationRequest parameter cancellationType",
							e);
				} catch (AsnException e) {
					throw new MAPException(
							"IOException while encoding CancelLocationRequest parameter cancellationType",
							e);
				}
			}
			
			if(this.extensionContainer != null){
				((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
			}
			
			if (this.typeOfUpdate != null) {			
				try {
					asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC,CancelLocationRequestImpl.TAG_typeOfUpdate,this.typeOfUpdate.getCode());
				} catch (IOException e) {
					throw new MAPException(
							"IOException while encoding CancelLocationRequest parameter typeOfUpdate",
							e);
				} catch (AsnException e) {
					throw new MAPException(
							"IOException while encoding CancelLocationRequest parameter typeOfUpdate",
							e);
				}
			}
			
			if (this.mtrfSupportedAndAuthorized) {
				try {
					asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC,
							TAG_mtrfSupportedAndAuthorized);
				} catch (IOException e) {
					throw new MAPException(
							"IOException while encoding CancelLocationRequest parameter mtrfSupportedAndAuthorized",
							e);
				} catch (AsnException e) {
					throw new MAPException(
							"AsnException while encoding CancelLocationRequest parameter mtrfSupportedAndAuthorized",
							e);
				}
			}
			
			if (this.mtrfSupportedAndNotAuthorized) {
				try {
					asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC,
							TAG_mtrfSupportedAndNotAuthorized);
				} catch (IOException e) {
					throw new MAPException(
							"IOException while encoding CancelLocationRequest parameter mtrfSupportedAndNotAuthorized",
							e);
				} catch (AsnException e) {
					throw new MAPException(
							"AsnException while encoding CancelLocationRequest parameter mtrfSupportedAndNotAuthorized",
							e);
				}
			}
			
			if (this.newMSCNumber != null) {
				((ISDNAddressStringImpl) this.newMSCNumber).encodeAll(asnOs,
						Tag.CLASS_CONTEXT_SPECIFIC, TAG_newMSCNumber);
			}
			
			if (this.newVLRNumber != null) {
				((ISDNAddressStringImpl) this.newVLRNumber).encodeAll(asnOs,
						Tag.CLASS_CONTEXT_SPECIFIC, TAG_newVLRNumber);
			}
	
			if (this.newLmsi != null) {
				((LMSIImpl) this.newLmsi).encodeAll(asnOs,
						Tag.CLASS_CONTEXT_SPECIFIC, TAG_newLmsi);
			}

		}
		
	}

	@Override
	public long getMapProtocolVersion() {
		return this.mapProtocolVersion;
	}
	
	private void _decode(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException, IOException, AsnException {
	
		this.imsi = null;
		this.imsiWithLmsi = null;
		this.cancellationType =  null;
		this.extensionContainer = null;
		this.typeOfUpdate = null;
		this.mtrfSupportedAndAuthorized = false;
		this.mtrfSupportedAndNotAuthorized = false;
		this.newMSCNumber = null;
		this.newVLRNumber = null;
		this.newLmsi = null;
		
		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		int num = 0;
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();
			
			if(this.mapProtocolVersion == 3 ){

				if(tag == Tag.STRING_OCTET &&  ais.getTagClass() == Tag.CLASS_UNIVERSAL){
					this.imsi = new IMSIImpl();
					((IMSIImpl) this.imsi).decodeAll(ais);
				}else if(tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive()){
					this.imsiWithLmsi = new IMSIWithLMSIImpl();
					((IMSIWithLMSIImpl) this.imsiWithLmsi).decodeAll(ais);
				}else if(tag == Tag.INTEGER &&  ais.getTagClass() == Tag.CLASS_UNIVERSAL ){
					this.cancellationType = CancellationType.getInstance((int) ais.readInteger());
				}else if (ais.getTagClass() != Tag.CLASS_UNIVERSAL && tag != Tag.SEQUENCE && (!ais.isTagPrimitive())){
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
				}else{
					switch (num) {
						case CancelLocationRequestImpl.TAG_typeOfUpdate: // lmsi
							this.typeOfUpdate = TypeOfUpdate.getInstance((int) ais.readInteger());
							break;
						case CancelLocationRequestImpl.TAG_mtrfSupportedAndAuthorized: 
							ais.readNull();
							this.mtrfSupportedAndAuthorized = true;
							break;
						case CancelLocationRequestImpl.TAG_mtrfSupportedAndNotAuthorized: 
							ais.readNull();
							this.mtrfSupportedAndNotAuthorized = true;
							break;
						case CancelLocationRequestImpl.TAG_newMSCNumber: 
							this.newMSCNumber = new ISDNAddressStringImpl();
							((ISDNAddressStringImpl) this.newMSCNumber).decodeAll(ais);
							break;
						case CancelLocationRequestImpl.TAG_newVLRNumber: 
							this.newVLRNumber = new ISDNAddressStringImpl();
							((ISDNAddressStringImpl) this.newVLRNumber).decodeAll(ais);
							break;
						case CancelLocationRequestImpl.TAG_newLmsi: 
							this.newLmsi = new LMSIImpl();
							((LMSIImpl) this.newLmsi).decodeAll(ais);
							break;
						default:
							ais.advanceElement();
							break;
					}
				}
						
			}else{
				if(tag == Tag.STRING_OCTET &&  ais.getTagClass() == Tag.CLASS_UNIVERSAL){
					this.imsi = new IMSIImpl();
					((IMSIImpl) this.imsi).decodeAll(ais);
				}
				if(tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive()){
					this.imsiWithLmsi = new IMSIWithLMSIImpl();
					((IMSIWithLMSIImpl) this.imsiWithLmsi).decodeAll(ais);
				}
				
			}

			num++;
		}

		if (num < 1)
			throw new MAPParsingComponentException("Error while decoding CancelLocationRequest: Needs at least 1 mandatory parameters, found " + num,
					MAPParsingComponentExceptionReason.MistypedParameter);
		
		
	}

}
