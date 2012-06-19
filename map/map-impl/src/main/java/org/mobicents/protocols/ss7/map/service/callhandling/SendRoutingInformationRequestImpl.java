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
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IstSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class SendRoutingInformationRequestImpl extends CallHandlingMessageImpl
	   implements SendRoutingInformationRequest {
	private ISDNAddressString msisdn;
	private ISDNAddressString gmscAddress;
	private InterrogationType interrogationType;
	private MAPExtensionContainer extensionContainer;
	private CallReferenceNumber callReferenceNumber;
	private ForwardingReason forwardingReason;
	private ExtBasicServiceCode basicServiceGroup;
	private ExtBasicServiceCode basicServiceGroup2;
	private ExternalSignalInfo networkSignalInfo;
	private ExternalSignalInfo networkSignalInfo2;
	private ExtExternalSignalInfo additionalSignalInfo;
	private AlertingPattern alertingPattern;
	private IstSupportIndicator istSupportIndicator;
	
	private static final int TAG_msisdn = 0;
	private static final int TAG_cugCheckInfo = 1;
	private static final int TAG_numberOfForwarding = 2;
	private static final int TAG_interrogationType = 3;
	private static final int TAG_orInterrogation = 4;
	private static final int TAG_orCapability = 5;
	private static final int TAG_gmscOrGsmSCFAddress = 6;
	private static final int TAG_callReferenceNumber = 7;
	private static final int TAG_forwardingReason = 8; 
	private static final int TAG_basicServiceGroup = 9;
	private static final int TAG_networkSignalInfo = 10; 
	private static final int TAG_camelInfo = 11;
	private static final int TAG_suppressionOfAnnouncement = 12;
	private static final int TAG_extensionContainer = 13;
	private static final int TAG_alertingPattern = 14;
	private static final int TAG_ccbsCall = 15;
	private static final int TAG_supportedCCBSPhase = 16;
	private static final int TAG_additionalSignalInfo = 17;
	private static final int TAG_istSupportIndicator = 18;
	private static final int TAG_prePagingSupported = 19;
	private static final int TAG_callDiversionTreatmentIndicator = 20;
	private static final int TAG_longFTNSupported = 21;
	private static final int TAG_suppress_VT_CSI = 22; 
	private static final int TAG_suppressIncomingCallBarring = 23;
	private static final int TAG_gsmSCFInitiatedCall = 24;
	private static final int TAG_basicServiceGroup2 = 25;
	private static final int TAG_networkSignalInfo2 = 26;
	private static final int TAG_suppressMTSS = 27;
	private static final int TAG_mtRoamingRetrySupported = 28; 
	private static final int TAG_callPriority = 29;
	
	private static final String _PrimitiveName = "SendRoutingInformationRequest";
	
	
	public SendRoutingInformationRequestImpl() { }
	
	public SendRoutingInformationRequestImpl(ISDNAddressString msisdn, ISDNAddressString gmscAddress, 
				InterrogationType interrogationType, MAPExtensionContainer extensionContainer) { 
		this.msisdn = msisdn;
		this.gmscAddress = gmscAddress;
		this.interrogationType = interrogationType;
		this.extensionContainer = extensionContainer;
	}
	
	public SendRoutingInformationRequestImpl(ISDNAddressString msisdn, ISDNAddressString gmscAddress, 
				InterrogationType interrogationType, MAPExtensionContainer extensionContainer, 
				CallReferenceNumber callReferenceNumber, ForwardingReason forwardingReason, 
				ExtBasicServiceCode basicServiceGroup, ExtBasicServiceCode basicServiceGroup2, 
				ExternalSignalInfo networkSignalInfo, ExternalSignalInfo networkSignalInfo2, 
				ExtExternalSignalInfo additionalSignalInfo, AlertingPattern alertingPattern, 
				IstSupportIndicator istSupportIndicator) { 
		this.msisdn = msisdn;
		this.gmscAddress = gmscAddress;
		this.interrogationType = interrogationType;
		this.extensionContainer = extensionContainer;
		this.callReferenceNumber = callReferenceNumber;
		this.forwardingReason = forwardingReason;
		this.basicServiceGroup = basicServiceGroup;
		this.basicServiceGroup2 = basicServiceGroup2;
		this.networkSignalInfo = networkSignalInfo;
		this.networkSignalInfo2 = networkSignalInfo2;
		this.additionalSignalInfo = additionalSignalInfo;
		this.alertingPattern = alertingPattern;
		this.istSupportIndicator = istSupportIndicator;
	}
	
	@Override
	public ISDNAddressString getMsisdn() {
		return this.msisdn;
	}

	@Override
	public ISDNAddressString getGatewayMSCAddress() {
		return this.gmscAddress;
	}

	@Override
	public InterrogationType getInterogationType() {
		return this.interrogationType;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public CallReferenceNumber getCallReferenceNumber() {
		return this.callReferenceNumber;
	}

	@Override
	public ForwardingReason getForwardingReason() {
		return this.forwardingReason;
	}

	@Override
	public ExtBasicServiceCode getBasicServiceGroup() {
		return this.basicServiceGroup;
	}

	@Override
	public ExtBasicServiceCode getBasicServiceGroup2() {
		return this.basicServiceGroup2;
	}

	@Override
	public ExternalSignalInfo getNetworkSignalInfo() {
		return this.networkSignalInfo;
	}

	@Override
	public ExternalSignalInfo getNetworkSignalInfo2() {
		return this.networkSignalInfo2;
	}

	@Override
	public ExtExternalSignalInfo getAdditionalSignalInfo() {
		return this.additionalSignalInfo;
	}

	@Override
	public AlertingPattern getAlertingPattern() {
		return this.alertingPattern;
	}

	@Override
	public IstSupportIndicator getIstSupportIndicator() {
		return this.istSupportIndicator;
	}
	
	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.sendRoutingInfo_Request;
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
			throw new MAPParsingComponentException("IOException when decoding SendRoutingInformationRequest: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SendRoutingInformationRequest: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SendRoutingInformationRequest: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SendRoutingInformationRequest: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	
	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.msisdn = null;
		this.gmscAddress = null;
		this.interrogationType = null;
		this.extensionContainer = null;
		this.callReferenceNumber = null;
		this.forwardingReason = null;
		this.basicServiceGroup = null;
		this.basicServiceGroup2 = null;
		this.networkSignalInfo = null;
		this.networkSignalInfo2 = null;
		this.additionalSignalInfo = null;
		this.alertingPattern = null;
		this.istSupportIndicator = null;
		
		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		int num = TAG_msisdn;
		while (true) {
			if (ais.available() == 0)
				break;
			
			int tag = ais.readTag();
			switch (num) {
			case TAG_msisdn: 
				if(ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
					+ ".msisdn: Parameter with bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
			
				this.msisdn = new ISDNAddressStringImpl();
				((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
				break;
			case TAG_interrogationType: 
				if(!ais.isTagPrimitive())
				    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
					+ ".interrogationType: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
				
				int code = (int) ais.readInteger();
				this.interrogationType = InterrogationType.getInterrogationType(code);
				break;
			case TAG_gmscOrGsmSCFAddress: 
				if(ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
					throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
					+ ".gmscAddress: Parameter with bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
				
				this.gmscAddress = new ISDNAddressStringImpl();
				((ISDNAddressStringImpl) this.gmscAddress).decodeAll(ais);
				break;
			default:
				if(ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					switch (tag) {
					case TAG_callReferenceNumber: 
						this.callReferenceNumber = new CallReferenceNumberImpl();
						((CallReferenceNumberImpl) this.callReferenceNumber).decodeAll(ais);
						break; 
					case TAG_forwardingReason: 
						int i = (int) ais.readInteger();
						this.forwardingReason = ForwardingReason.getForwardingReason(i);
						break; 
					case TAG_basicServiceGroup: //explicit tag encoding
						AsnInputStream ais1 = ais.readSequenceStream();
						ais1.readTag();
						this.basicServiceGroup = new ExtBasicServiceCodeImpl();
						((ExtBasicServiceCodeImpl)this.basicServiceGroup).decodeAll(ais1);
						break;
					case TAG_basicServiceGroup2: //explicit tag encoding
						AsnInputStream ais2 = ais.readSequenceStream();
						ais2.readTag();
						this.basicServiceGroup2 = new ExtBasicServiceCodeImpl();
						((ExtBasicServiceCodeImpl)this.basicServiceGroup2).decodeAll(ais2);
						break;
					case TAG_networkSignalInfo: 
						this.networkSignalInfo = new ExternalSignalInfoImpl();
						((ExternalSignalInfoImpl) this.networkSignalInfo).decodeAll(ais);
						break; 
					case TAG_networkSignalInfo2: 
						this.networkSignalInfo2 = new ExternalSignalInfoImpl();
						((ExternalSignalInfoImpl) this.networkSignalInfo2).decodeAll(ais);
						break; 
					case TAG_additionalSignalInfo: 
						this.additionalSignalInfo = new ExtExternalSignalInfoImpl();
						((ExtExternalSignalInfoImpl) this.additionalSignalInfo).decodeAll(ais);
						break; 
					case TAG_extensionContainer: 
						this.extensionContainer = new MAPExtensionContainerImpl();
						((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
						break;
					case TAG_alertingPattern: 
						this.alertingPattern = new AlertingPatternImpl();
						((AlertingPatternImpl) this.alertingPattern).decodeAll(ais);
						break; 
					case TAG_istSupportIndicator: 
						int j = (int) ais.readInteger();
						this.istSupportIndicator = IstSupportIndicator.getInstance(j);
						break; 
					/*// TODO:
					case TAG_cugCheckInfo: break; 
					case TAG_numberOfForwarding: break; 
					case TAG_orInterrogation: break; 
					case TAG_orCapability: break; 
					case TAG_camelInfo: break; 
					case TAG_suppressionOfAnnouncement: break; 
					case TAG_ccbsCall: break; 
					case TAG_supportedCCBSPhase: break; 
					case TAG_prePagingSupported: break; 
					case TAG_callDiversionTreatmentIndicator: break;
					case TAG_longFTNSupported: break; 
					case TAG_suppress_VT_CSI: break; 
					case TAG_suppressIncomingCallBarring: break; 
					case TAG_gsmSCFInitiatedCall: break; 
					case TAG_suppressMTSS: break; 
					case TAG_mtRoamingRetrySupported: break; 
					case TAG_callPriority: break;
					*/
					default:
						ais.advanceElement();
						break;
					}	
				} else {
					ais.advanceElement();
					//break;
				}
				
				// default:
				break;
			}
			
			num += 3; //0,3,6 tag numbers are mandatory
		}
		
		if (num < 9)
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Needs at least 3 mandatory parameters, found " + num,
					MAPParsingComponentExceptionReason.MistypedParameter);
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
			throw new MAPException("AsnException when encoding SendRoutingInformationRequest: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		try {
			if(this.msisdn == null || this.gmscAddress == null || this.interrogationType == null)
				throw new MAPException("MSISDN, Gateway MSC address and Interrogation Type parameters must not be null");
			
			if(this.msisdn != null)
			  ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_msisdn);
			
			if(this.interrogationType != null)
			  asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_interrogationType, this.interrogationType.getCode());
			
			if(this.gmscAddress != null)
			  ((ISDNAddressStringImpl) this.gmscAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_gmscOrGsmSCFAddress);
			
			if(this.callReferenceNumber != null)
			  ((CallReferenceNumberImpl) this.callReferenceNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_callReferenceNumber);
			
			if(this.forwardingReason != null)
			  asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_forwardingReason, this.forwardingReason.getCode());
			
			if(this.basicServiceGroup != null) { // explicit tag encoding
			  asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_basicServiceGroup);
			  int pos = asnOs.StartContentDefiniteLength();
			  ((ExtBasicServiceCodeImpl) this.basicServiceGroup).encodeAll(asnOs);
			  asnOs.FinalizeContent(pos);
			}
			
			if(this.networkSignalInfo != null)
			  ((ExternalSignalInfoImpl) this.networkSignalInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_networkSignalInfo);
				
			if(this.extensionContainer != null)
			  ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_extensionContainer);
			
			if(this.alertingPattern != null)
			  ((AlertingPatternImpl) this.alertingPattern).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_alertingPattern);
			
			if(this.additionalSignalInfo != null)
			   ((ExtExternalSignalInfoImpl) this.additionalSignalInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_additionalSignalInfo);
				
			if(this.istSupportIndicator != null)
			  asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_istSupportIndicator, this.istSupportIndicator.getCode());
			
			if(this.basicServiceGroup2 != null) { // explicit tag encoding 
			  asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_basicServiceGroup2);
			  int pos = asnOs.StartContentDefiniteLength();
			  ((ExtBasicServiceCodeImpl) this.basicServiceGroup2).encodeAll(asnOs);
			  asnOs.FinalizeContent(pos);
			}
			
			if(this.networkSignalInfo2 != null)
			  ((ExternalSignalInfoImpl) this.networkSignalInfo2).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_networkSignalInfo2);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}	
	}
}