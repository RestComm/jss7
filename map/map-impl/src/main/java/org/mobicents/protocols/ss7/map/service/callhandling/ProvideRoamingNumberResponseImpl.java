package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;


/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class ProvideRoamingNumberResponseImpl extends CallHandlingMessageImpl implements ProvideRoamingNumberResponse{

	public ISDNAddressString roamingNumber;
	public MAPExtensionContainer extensionContainer;
	public boolean releaseResourcesSupported;
	public ISDNAddressString vmscAddress;
	private long mapProtocolVersion;
	public static final String _PrimitiveName = "ProvideRoamingNumberResponse";
	
	
	public ProvideRoamingNumberResponseImpl(ISDNAddressString roamingNumber,
			MAPExtensionContainer extensionContainer,
			boolean releaseResourcesSupported, ISDNAddressString vmscAddress,
			long mapProtocolVersion) {
		super();
		this.roamingNumber = roamingNumber;
		this.extensionContainer = extensionContainer;
		this.releaseResourcesSupported = releaseResourcesSupported;
		this.vmscAddress = vmscAddress;
		this.mapProtocolVersion = mapProtocolVersion;
	}

	public ProvideRoamingNumberResponseImpl(long mapProtocolVersion){
		this.mapProtocolVersion = mapProtocolVersion;
	}
	
	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.privideRoamingNumber_Response;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.provideRoamingNumber;
	}

	@Override
	public int getTag() throws MAPException {
		if (this.mapProtocolVersion >= 3) {
			return Tag.SEQUENCE;
		} else {
			return Tag.STRING_OCTET;
		}
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		if (this.mapProtocolVersion >= 3) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void decodeAll(AsnInputStream ansIS)
			throws MAPParsingComponentException {
		
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MAPParsingComponentException("IOException when decoding "+_PrimitiveName+": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			e.printStackTrace();
			throw new MAPParsingComponentException("AsnException when decoding "+_PrimitiveName+": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MAPParsingComponentException("AsnException when decoding "+_PrimitiveName+": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
		
	}
	


	@Override
	public void decodeData(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MAPParsingComponentException("IOException when decoding "+_PrimitiveName+": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			e.printStackTrace();
			throw new MAPParsingComponentException("AsnException when decoding "+_PrimitiveName+": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.roamingNumber =  null;
		this.extensionContainer = null;
		this.releaseResourcesSupported =  false;
		this.vmscAddress = null;
		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		
		if (this.mapProtocolVersion >= 3) {
			
			int num = 0;
			
			while (true) {
				if (ais.available() == 0)
					break;

				int tag = ais.readTag();
				
				if(num ==0){
					if(ais.getTagClass() != Tag.CLASS_UNIVERSAL && tag != Tag.STRING_OCTET && !ais.isTagPrimitive()){
						throw new MAPParsingComponentException("Error while decoding  "+_PrimitiveName+" " +
								".roamingNumber: Parameter 0 bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					}
					
					this.roamingNumber = new ISDNAddressStringImpl();
					((ISDNAddressStringImpl) this.roamingNumber).decodeAll(ais);
					num++;
				}else{
					if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
						if(tag== Tag.STRING_OCTET){
							this.vmscAddress = new ISDNAddressStringImpl();
							((ISDNAddressStringImpl) this.vmscAddress).decodeAll(ais);
						}else if(tag == Tag.SEQUENCE){
							this.extensionContainer = new MAPExtensionContainerImpl();
							((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
						}else if(tag == Tag.NULL){
							ais.readNull();
							this.releaseResourcesSupported = true;
						}else{
							ais.advanceElement();
						}
						
					}else{
						ais.advanceElement();
					}					
				}
						
			}
		}else{
			this.roamingNumber = new ISDNAddressStringImpl();
			((ISDNAddressStringImpl) this.roamingNumber).decodeData(ais,length);
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
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding "+_PrimitiveName+" : " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.roamingNumber == null)
			throw new MAPException("roamingNumber parameter must not be null");

		try {
			
			if (this.mapProtocolVersion >= 3) {

				((ISDNAddressStringImpl) this.roamingNumber).encodeAll(asnOs);

				if (this.extensionContainer != null){
					((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
				}
				if (this.releaseResourcesSupported){
					asnOs.writeNull();
				}
				if (this.vmscAddress!=null){
					((ISDNAddressStringImpl) this.vmscAddress).encodeAll(asnOs);
				}
			
			} else {
				((ISDNAddressStringImpl) this.roamingNumber).encodeData(asnOs);
			}
		} catch (IOException e) {
			throw new MAPException("IOException when encoding "+_PrimitiveName+" " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding "+_PrimitiveName+" " + e.getMessage(), e);
		}
		
	}

	@Override
	public ISDNAddressString getRoamingNumber() {
		return this.roamingNumber;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public boolean getReleaseResourcesSupported() {
		return this.releaseResourcesSupported;
	}

	@Override
	public ISDNAddressString getVmscAddress() {
		return this.vmscAddress;
	}

	@Override
	public long getMapProtocolVersion() {
		return this.mapProtocolVersion;
	}

	@Override
	public String toString() {
		return "ProvideRoamingNumberResponseImpl [roamingNumber="
				+ roamingNumber + ", extensionContainer=" + extensionContainer
				+ ", releaseResourcesSupported=" + releaseResourcesSupported
				+ ", vmscAddress=" + vmscAddress + ", mapProtocolVersion="
				+ mapProtocolVersion + "]";
	}
	
	
	public static void main(String args[]){
		 AsnInputStream asn = new AsnInputStream(getEncodedData());
		 try {
			int tag = asn.readTag();
			
			 ProvideRoamingNumberResponseImpl prn = new
					 ProvideRoamingNumberResponseImpl(3);
			 prn.decodeAll(asn);
			
			 
				ISDNAddressString roamingNumber = prn.getRoamingNumber();
				MAPExtensionContainer extensionContainer = prn.getExtensionContainer();
				boolean releaseResourcesSupported = prn.getReleaseResourcesSupported();
				ISDNAddressString vmscAddress = prn.getVmscAddress(); 
				long mapProtocolVersion = prn.getMapProtocolVersion();
				
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	private static byte[] getEncodedData() {
		return new byte[] { 48, 59, 4, 7, -111, -108, -120, 115, 0, -110, -14, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 4, 7, -111, -110, 17, 19, 50, 19, -15 };
	}

	
	

}
