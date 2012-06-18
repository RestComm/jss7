package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.apache.log4j.Logger;
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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class SendRoutingInformationResponseImpl extends CallHandlingMessageImpl 
	   implements SendRoutingInformationResponse {
	private IMSI imsi;
	private ISDNAddressString msisdn; 
	private ExtendedRoutingInfo extRoutingInfo; 
	private RoutingInfo routingInfo2; 
	private SubscriberInfo subscriberInfo; 
	private ExtBasicServiceCode basicService; 
	private ExtBasicServiceCode basicService2; 
	private ISDNAddressString vmscAddress; 
	private NumberPortabilityStatus nrPortabilityStatus; 
	private SupportedCamelPhases supportedCamelPhases; 
	private OfferedCamel4CSIs offeredCamel4CSIs; 
	private ExternalSignalInfo gsmBearerCapability; 
	private UnavailabilityCause unavailabilityCause; 
	private MAPExtensionContainer extensionContainer; 
	
	private static final int TAG_imsi = 9;
	private static final int TAG_cugCheckInfo = 3;
	private static final int TAG_cugSubscriptionFlag = 6;
	private static final int TAG_subscriberInfo = 7;
	private static final int TAG_ssList = 1;
	private static final int TAG_basicService = 5;
	private static final int TAG_forwardingInterrogationRequired = 4;
	private static final int TAG_vmscAddress = 2;
	private static final int TAG_extensionContainer = 0;
	private static final int TAG_naeaPreferredCI = 10;
	private static final int TAG_ccbsIndicators = 11;
	private static final int TAG_msisdn = 12;
	private static final int TAG_numberPortabilityStatus = 13;
	private static final int TAG_istAlertTimer = 14;
	private static final int TAG_supportedCamelPhasesInVMSC = 15;
	private static final int TAG_offeredCamel4CSIsInVMSC = 16;
	private static final int TAG_routingInfo2 = 17;
	private static final int TAG_ssList2 = 18;
	private static final int TAG_basicService2 = 19;
	private static final int TAG_allowedServices = 20;
	private static final int TAG_unavailabilityCause = 21;
	private static final int TAG_releaseResourcesSupported = 22;
	private static final int TAG_gsmBearerCapability = 23;
	
	private static final String _PrimitiveName = "SendRoutingInformationResponse";
	
	
	public SendRoutingInformationResponseImpl() { }
	
	public SendRoutingInformationResponseImpl(IMSI imsi, ExtendedRoutingInfo extRoutingInfo, 
			SubscriberInfo subscriberInfo,  MAPExtensionContainer extensionContainer) {
		this.imsi = imsi;
		this.extRoutingInfo = extRoutingInfo;
		this.subscriberInfo = subscriberInfo;
		this.extensionContainer = extensionContainer;
	}

	public SendRoutingInformationResponseImpl(IMSI imsi, ExtendedRoutingInfo extRoutingInfo, 
			SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer, 
			ExtBasicServiceCode basicService, ExtBasicServiceCode basicService2, 
			RoutingInfo routingInfo2, ISDNAddressString vmscAddress, ISDNAddressString msisdn, 
			NumberPortabilityStatus nrPortabilityStatus, SupportedCamelPhases supportedCamelPhases, 
			OfferedCamel4CSIs offeredCamel4CSIs, UnavailabilityCause unavailabilityCause, 
			ExternalSignalInfo gsmBearerCapability) { 
		this.imsi = imsi;
		this.msisdn = msisdn;
		this.extRoutingInfo = extRoutingInfo;
		this.routingInfo2 = routingInfo2;
		this.subscriberInfo = subscriberInfo;
		this.basicService = basicService;
		this.basicService2 = basicService2;
		this.vmscAddress = vmscAddress;
		this.nrPortabilityStatus = nrPortabilityStatus;
		this.supportedCamelPhases = supportedCamelPhases;
		this.offeredCamel4CSIs = offeredCamel4CSIs;
		this.unavailabilityCause = unavailabilityCause;
		this.gsmBearerCapability = gsmBearerCapability;
		this.extensionContainer = extensionContainer;
	}
	
	@Override
	public IMSI getIMSI() {
		return this.imsi;
	}
	
	@Override
	public ISDNAddressString getMsisdn() {
		return this.msisdn;
	}

	@Override
	public ExtendedRoutingInfo getExtendedRoutingInfo() {
		return this.extRoutingInfo;
	}

	@Override
	public RoutingInfo getRoutingInfo2() {
		return this.routingInfo2;
	}

	@Override
	public SubscriberInfo getSubscriberInfo() {
		return this.subscriberInfo;
	}

	@Override
	public ExtBasicServiceCode getBasicService() {
		return this.basicService;
	}

	@Override
	public ExtBasicServiceCode getBasicService2() {
		return this.basicService2;
	}

	@Override
	public ISDNAddressString getVmscAddress() {
		return this.vmscAddress;
	}

	@Override
	public NumberPortabilityStatus getNumberPortabilityStatus() {
		return this.nrPortabilityStatus;
	}

	@Override
	public SupportedCamelPhases getSupportedCamelPhases() {
		return this.supportedCamelPhases;
	}

	@Override
	public OfferedCamel4CSIs getOfferedCamel4CSIs() {
		return this.offeredCamel4CSIs;
	}
	
	@Override
	public UnavailabilityCause getUnavailabilityCause() {
		return this.unavailabilityCause;
	}

	@Override
	public ExternalSignalInfo getGsmBearerCapability() {
		return this.gsmBearerCapability;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}
	
	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.sendRoutingInfo_Response;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.sendRoutingInfo;
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
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SendRoutingInformationResponse: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SendRoutingInformationResponse: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SendRoutingInformationResponse: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SendRoutingInformationResponse: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.imsi = null;
		this.msisdn = null;
		this.extRoutingInfo = null;
		this.routingInfo2 = null;
		this.subscriberInfo = null;
		this.basicService = null;
		this.basicService2 = null;
		this.vmscAddress = null;
		this.nrPortabilityStatus = null;
		this.supportedCamelPhases = null;
		this.offeredCamel4CSIs = null;
		this.unavailabilityCause = null;
		this.gsmBearerCapability = null;
		this.extensionContainer = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();
			if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
				switch (tag) {
				case Tag.STRING_OCTET: 
				case Tag.SEQUENCE:
					this.extRoutingInfo = new ExtendedRoutingInfoImpl();
					((ExtendedRoutingInfoImpl) this.extRoutingInfo).decodeAll(ais);
					break;
				default:
					ais.advanceElement();
					break;
				}
			} else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case TAG_imsi: 
					this.imsi = new IMSIImpl();
					((IMSIImpl) this.imsi).decodeAll(ais);
					break; 
				case TAG_msisdn: 
					this.msisdn = new ISDNAddressStringImpl();
					((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
					break; 
				case TAG_subscriberInfo: 
					this.subscriberInfo = new SubscriberInfoImpl();
					((SubscriberInfoImpl) this.subscriberInfo).decodeAll(ais);
					break; 
				case TAG_basicService: // explicit tag encoding
					AsnInputStream ais1 = ais.readSequenceStream();
					ais1.readTag();
					this.basicService = new ExtBasicServiceCodeImpl();
					((ExtBasicServiceCodeImpl) this.basicService).decodeAll(ais1);
					break; 
				case TAG_basicService2: // explicit tag encoding
					AsnInputStream ais2 = ais.readSequenceStream();
					ais2.readTag();
					this.basicService2 = new ExtBasicServiceCodeImpl();
					((ExtBasicServiceCodeImpl) this.basicService2).decodeAll(ais2);
					break;  
				case TAG_routingInfo2: 
					AsnInputStream ais0 = ais.readSequenceStream();
					ais0.readTag();
					this.routingInfo2 = new RoutingInfoImpl();
					((RoutingInfoImpl) this.routingInfo2).decodeAll(ais0);
					break;
				case TAG_vmscAddress:
					this.vmscAddress = new ISDNAddressStringImpl();
					((ISDNAddressStringImpl) this.vmscAddress).decodeAll(ais);
					break; 
				case TAG_extensionContainer: 
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
					break;
				case TAG_numberPortabilityStatus: 
					int type = (int) ais.readInteger();
					this.nrPortabilityStatus = NumberPortabilityStatus.getInstance(type);
					break; 
				case TAG_gsmBearerCapability: 
					this.gsmBearerCapability = new ExternalSignalInfoImpl();
					((ExternalSignalInfoImpl) this.gsmBearerCapability).decodeAll(ais);
					break; 
				case TAG_supportedCamelPhasesInVMSC: 
					this.supportedCamelPhases = new SupportedCamelPhasesImpl();
					((SupportedCamelPhasesImpl) this.supportedCamelPhases).decodeAll(ais);
					break; 
				case TAG_offeredCamel4CSIsInVMSC: 
					this.offeredCamel4CSIs = new OfferedCamel4CSIsImpl();
					((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).decodeAll(ais);
					break; 
				case TAG_unavailabilityCause: 
					int code = (int) ais.readInteger();
					this.unavailabilityCause = UnavailabilityCause.getUnavailabilityCause(code);
					break; 
				/*// TODO:
				case TAG_cugCheckInfo: break; 
				case TAG_cugSubscriptionFlag: break; 
				case TAG_ssList: break; 
				case TAG_forwardingInterrogationRequired: break; 
				case TAG_naeaPreferredCI: break; 
				case TAG_ccbsIndicators: break; 
				case TAG_istAlertTimer: break;
				case TAG_ssList2: break; 
				case TAG_allowedServices: break; 
				case TAG_releaseResourcesSupported: break; 
				*/
				default:
					ais.advanceElement();
					break;
				}
			}
			else {
				 ais.advanceElement();
				 //break;
			}
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding SendRoutingInformationResponse: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		try {
			if(this.imsi != null) 
			  ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_imsi);
			
			if(this.extRoutingInfo != null) { // Universal TAG class here
			  ((ExtendedRoutingInfoImpl) this.extRoutingInfo).encodeAll(asnOs);
			}
			
			if(this.subscriberInfo != null)
			  ((SubscriberInfoImpl) this.subscriberInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_subscriberInfo);
			
			if(this.basicService != null) { // explicit tag encoding
			  asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_basicService);
			  int pos = asnOs.StartContentDefiniteLength();
			  ((ExtBasicServiceCodeImpl) this.basicService).encodeAll(asnOs);
			  asnOs.FinalizeContent(pos);
			}
			
			if(this.vmscAddress != null)
			  ((ISDNAddressStringImpl) this.vmscAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_vmscAddress);
			
			if(this.extensionContainer != null)
			  ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_extensionContainer);
		
			if(this.msisdn != null)
			  ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_msisdn);
			
			if(this.nrPortabilityStatus != null)
			  asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_numberPortabilityStatus, this.nrPortabilityStatus.getType());
		
			if(this.supportedCamelPhases != null)
			  ((SupportedCamelPhasesImpl) this.supportedCamelPhases).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_supportedCamelPhasesInVMSC);
			
			if(this.offeredCamel4CSIs != null)
			  ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_offeredCamel4CSIsInVMSC);
			
			if(this.routingInfo2 != null) { // explicit tag encoding
			  asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_routingInfo2);
			  int pos = asnOs.StartContentDefiniteLength();
			  ((RoutingInfoImpl) this.routingInfo2).encodeAll(asnOs);
			  asnOs.FinalizeContent(pos);
			}
			
			if(this.basicService2 != null) {
			  asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_basicService2);
			  int pos = asnOs.StartContentDefiniteLength();
			  ((ExtBasicServiceCodeImpl) this.basicService2).encodeAll(asnOs);
			  asnOs.FinalizeContent(pos);
			}
			
			if(this.unavailabilityCause != null)
			  asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_unavailabilityCause, this.unavailabilityCause.getCode());
			
			if(this.gsmBearerCapability != null)
			  ((ExternalSignalInfoImpl) this.gsmBearerCapability).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_gsmBearerCapability);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}
}