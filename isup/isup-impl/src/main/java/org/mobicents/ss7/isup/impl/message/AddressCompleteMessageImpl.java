/**
 * Start time:14:30:39 2009-04-20<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.impl.message.parameter.*;
import org.mobicents.ss7.isup.impl.message.parameter.accessTransport.*;
import org.mobicents.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.ss7.isup.message.parameter.ApplicationTransportParameter;
import org.mobicents.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.mobicents.ss7.isup.message.parameter.CallDiversionInformation;
import org.mobicents.ss7.isup.message.parameter.CallReference;
import org.mobicents.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.ss7.isup.message.parameter.HTRInformation;
import org.mobicents.ss7.isup.message.parameter.ISUPParameter;
import org.mobicents.ss7.isup.message.parameter.MessageType;
import org.mobicents.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.ss7.isup.message.parameter.OptionalBackwardCallIndicators;
import org.mobicents.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.mobicents.ss7.isup.message.parameter.RedirectStatus;
import org.mobicents.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.ss7.isup.message.parameter.RedirectionNumberRestriction;
import org.mobicents.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.ss7.isup.message.parameter.TransmissionMediumUsed;
import org.mobicents.ss7.isup.message.parameter.UIDActionIndicators;
import org.mobicents.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.ss7.isup.message.parameter.accessTransport.AccessTransport;
import org.mobicents.ss7.isup.message.AddressCompleteMessage;

/**
 * Start time:14:30:39 2009-04-20<br>
 * Project: mobicents-isup-stack<br>
 * See Table 21/Q.763
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
class AddressCompleteMessageImpl extends ISUPMessageImpl implements AddressCompleteMessage {

	public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(_MESSAGE_CODE_ACM);

	private static final int _MANDATORY_VAR_COUNT = 0;

	static final int _INDEX_F_MessageType = 0;
	static final int _INDEX_F_BackwardCallIndicators = 1;
	// FIXME: those can be sent in any order, but we prefer this way, its faster
	// to access by index than by hash ?
	static final int _INDEX_O_OptionalBakwardCallIndicators = 0;
	static final int _INDEX_O_CallReference = 1;
	static final int _INDEX_O_CauseIndicators = 2;
	static final int _INDEX_O_UserToUserIndicators = 3;
	static final int _INDEX_O_UserToUserInformation = 4;
	static final int _INDEX_O_AccessTransport = 5;
	// FIXME: There can be more of those.
	static final int _INDEX_O_GenericNotificationIndicator = 6;
	static final int _INDEX_O_TransmissionMediumUsed = 7;
	static final int _INDEX_O_EchoControlInformation = 8;
	static final int _INDEX_O_AccessDeliveryInformation = 9;
	static final int _INDEX_O_RedirectionNumber = 10;
	static final int _INDEX_O_ParameterCompatibilityInformation = 11;
	static final int _INDEX_O_CallDiversionInformation = 12;
	static final int _INDEX_O_NetworkSpecificFacility = 13;
	static final int _INDEX_O_RemoteOperations = 14;
	static final int _INDEX_O_ServiceActivation = 15;
	static final int _INDEX_O_RedirectionNumberRestriction = 16;
	static final int _INDEX_O_ConferenceTreatmentIndicators = 17;
	static final int _INDEX_O_UIDActionIndicators = 18;
	static final int _INDEX_O_ApplicationTransportParameter = 19;
	static final int _INDEX_O_CCNRPossibleIndicator = 20;
	static final int _INDEX_O_HTRInformation = 21;
	static final int _INDEX_O_PivotRoutingBackwardInformation = 22;
	static final int _INDEX_O_RedirectStatus = 23;
	static final int _INDEX_O_EndOfOptionalParameters = 24;

	AddressCompleteMessageImpl(Object source, byte[] b, Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
			Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) throws ParameterRangeInvalidException {
		this(source, mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index, optionalCode2Index);
		decodeElement(b);

	}

	AddressCompleteMessageImpl(Object source, Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
			Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) {
		super(source, mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index, optionalCode2Index);

		super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
		super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
	}

	@Override
	public boolean hasAllMandatoryParameters() {
		if (super.f_Parameters.get(_INDEX_F_MessageType) == null || super.f_Parameters.get(_INDEX_F_MessageType).getCode() != MessageTypeImpl._PARAMETER_CODE) {
			return false;
		}

		if (super.f_Parameters.get(_INDEX_F_BackwardCallIndicators) == null || super.f_Parameters.get(_INDEX_F_BackwardCallIndicators).getCode() != BackwardCallIndicatorsImpl._PARAMETER_CODE) {
			return false;
		}

		return true;
	}

	@Override
	public MessageType getMessageType() {
		return _MESSAGE_TYPE;
	}

	public void setBackwardCallIndicators(BackwardCallIndicators indicators) {
		super.f_Parameters.put(_INDEX_F_BackwardCallIndicators, indicators);
	}

	public BackwardCallIndicators getBackwardCallIndicators() {
		return (BackwardCallIndicators) super.f_Parameters.get(_INDEX_F_BackwardCallIndicators);
	}

	public void setOptionalBakwardCallIndicators(OptionalBackwardCallIndicators value) {
		super.o_Parameters.put(_INDEX_O_OptionalBakwardCallIndicators, value);

	}

	public OptionalBackwardCallIndicators getOptionalBakwardCallIndicators() {
		return (OptionalBackwardCallIndicators) super.o_Parameters.get(_INDEX_O_OptionalBakwardCallIndicators);
	}

	public void setCallReference(CallReference value) {
		super.o_Parameters.put(_INDEX_O_CallReference, value);
	}

	public CallReference getCallReference() {
		return (CallReference) super.o_Parameters.get(_INDEX_O_CallReference);
	}

	public void setCauseIndicators(CauseIndicators value) {
		super.o_Parameters.put(_INDEX_O_CauseIndicators, value);

	}

	public CauseIndicators getCauseIndicators() {
		return (CauseIndicators) super.o_Parameters.get(_INDEX_O_CauseIndicators);
	}

	public void setUserToUserIndicators(UserToUserIndicators value) {
		super.o_Parameters.put(_INDEX_O_UserToUserIndicators, value);
	}

	public UserToUserIndicators getUserToUserIndicators() {
		return (UserToUserIndicators) super.o_Parameters.get(_INDEX_O_UserToUserIndicators);
	}

	public void setUserToUserInformation(UserToUserInformation value) {
		super.o_Parameters.put(_INDEX_O_UserToUserInformation, value);
	}

	public UserToUserInformation getUserToUserInformation() {
		return (UserToUserInformation) super.o_Parameters.get(_INDEX_O_UserToUserInformation);
	}

	public void setAccessTransport(AccessTransport value) {
		super.o_Parameters.put(_INDEX_O_AccessTransport, value);
	}

	public AccessTransport getAccessTransport() {
		return (AccessTransport) super.o_Parameters.get(_INDEX_O_AccessTransport);
	}

	public void setGenericNotificationIndicator(GenericNotificationIndicator value) {
		super.o_Parameters.put(_INDEX_O_GenericNotificationIndicator, value);
	}

	public GenericNotificationIndicator getGenericNotificationIndicator() {
		return (GenericNotificationIndicator) super.o_Parameters.get(_INDEX_O_GenericNotificationIndicator);
	}

	public void setTransmissionMediumUsed(TransmissionMediumUsed value) {
		super.o_Parameters.put(_INDEX_O_TransmissionMediumUsed, value);
	}

	public TransmissionMediumUsed getTransmissionMediumUsed() {
		return (TransmissionMediumUsed) super.o_Parameters.get(_INDEX_O_TransmissionMediumUsed);
	}

	public void setEchoControlInformation(EchoControlInformation value) {
		super.o_Parameters.put(_INDEX_O_EchoControlInformation, value);
	}

	public EchoControlInformation getEchoControlInformation() {
		return (EchoControlInformation) super.o_Parameters.get(_INDEX_O_EchoControlInformation);
	}

	public void setAccessDeliveryInformation(AccessDeliveryInformation value) {
		super.o_Parameters.put(_INDEX_O_AccessDeliveryInformation, value);
	}

	public AccessDeliveryInformation getAccessDeliveryInformation() {
		return (AccessDeliveryInformation) super.o_Parameters.get(_INDEX_O_AccessDeliveryInformation);
	}

	public void setRedirectionNumber(RedirectionNumber value) {
		super.o_Parameters.put(_INDEX_O_RedirectionNumber, value);
	}

	public RedirectionNumber getRedirectionNumber() {
		return (RedirectionNumber) super.o_Parameters.get(_INDEX_O_RedirectionNumber);
	}

	public void setParameterCompatibilityInformation(ParameterCompatibilityInformation value) {
		super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, value);
	}

	public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
		return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);
	}

	public void setCallDiversionInformation(CallDiversionInformation value) {
		super.o_Parameters.put(_INDEX_O_CallDiversionInformation, value);
	}

	public CallDiversionInformation getCallDiversionInformation() {
		return (CallDiversionInformation) super.o_Parameters.get(_INDEX_O_CallDiversionInformation);
	}

	public void setNetworkSpecificFacility(NetworkSpecificFacility value) {
		super.o_Parameters.put(_INDEX_O_NetworkSpecificFacility, value);
	}

	public NetworkSpecificFacility getNetworkSpecificFacility() {
		return (NetworkSpecificFacility) super.o_Parameters.get(_INDEX_O_NetworkSpecificFacility);
	}

	public void setRemoteOperations(RemoteOperations value) {
		super.o_Parameters.put(_INDEX_O_RemoteOperations, value);
	}

	public RemoteOperations getRemoteOperations() {
		return (RemoteOperations) super.o_Parameters.get(_INDEX_O_RemoteOperations);
	}

	public void setServiceActivation(ServiceActivation value) {
		super.o_Parameters.put(_INDEX_O_ServiceActivation, value);
	}

	public RedirectionNumberRestriction getRedirectionNumberRestriction() {
		return (RedirectionNumberRestriction) super.o_Parameters.get(_INDEX_O_ServiceActivation);
	}

	public void setRedirectionNumberRestriction(RedirectionNumberRestriction value) {
		super.o_Parameters.put(_INDEX_O_RedirectionNumberRestriction, value);
	}

	public ServiceActivation getServiceActivation() {
		return (ServiceActivation) super.o_Parameters.get(_INDEX_O_RedirectionNumberRestriction);
	}

	public void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators value) {
		super.o_Parameters.put(_INDEX_O_ConferenceTreatmentIndicators, value);
	}

	public ConferenceTreatmentIndicators getConferenceTreatmentIndicators() {
		return (ConferenceTreatmentIndicators) super.o_Parameters.get(_INDEX_O_ConferenceTreatmentIndicators);
	}

	public void setUIDActionIndicators(UIDActionIndicators value) {
		super.o_Parameters.put(_INDEX_O_UIDActionIndicators, value);
	}

	public UIDActionIndicators getUIDActionIndicators() {
		return (UIDActionIndicators) super.o_Parameters.get(_INDEX_O_UIDActionIndicators);
	}

	public void setApplicationTransportParameter(ApplicationTransportParameter value) {
		super.o_Parameters.put(_INDEX_O_ApplicationTransportParameter, value);
	}

	public ApplicationTransportParameter getApplicationTransportParameter() {
		return (ApplicationTransportParameter) super.o_Parameters.get(_INDEX_O_ApplicationTransportParameter);
	}

	public void setCCNRPossibleIndicator(CCNRPossibleIndicator value) {
		super.o_Parameters.put(_INDEX_O_CCNRPossibleIndicator, value);
	}

	public CCNRPossibleIndicator getCCNRPossibleIndicator() {
		return (CCNRPossibleIndicator) super.o_Parameters.get(_INDEX_O_CCNRPossibleIndicator);
	}

	public void setHTRInformation(HTRInformation value) {
		super.o_Parameters.put(_INDEX_O_HTRInformation, value);
	}

	public HTRInformation getHTRInformation() {
		return (HTRInformation) super.o_Parameters.get(_INDEX_O_HTRInformation);
	}

	public void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation value) {
		super.o_Parameters.put(_INDEX_O_PivotRoutingBackwardInformation, value);
	}

	public PivotRoutingBackwardInformation getPivotRoutingBackwardInformation() {
		return (PivotRoutingBackwardInformation) super.o_Parameters.get(_INDEX_O_PivotRoutingBackwardInformation);
	}

	public void setRedirectStatus(RedirectStatus value) {
		super.o_Parameters.put(_INDEX_O_RedirectStatus, value);
	}

	public RedirectStatus getRedirectStatus() {
		return (RedirectStatus) super.o_Parameters.get(_INDEX_O_RedirectStatus);
	}

	@Override
	protected int decodeMandatoryParameters(byte[] b, int index) throws ParameterRangeInvalidException {
		int localIndex = index;
		if (b.length - index > 1) {
			try {
				byte[] cic = new byte[2];
				cic[0] = b[index++];
				cic[1] = b[index++];
				super.cic = new CircuitIdentificationCodeImpl();
				super.cic.decodeElement(cic);

			} catch (Exception e) {
				// AIOOBE or IllegalArg
				throw new ParameterRangeInvalidException("Failed to parse CircuitIdentificationCode due to: ", e);
			}
			try {
				// Message Type
				if (b[index] != this._MESSAGE_CODE_ACM) {
					throw new ParameterRangeInvalidException("Message code is not: " + this._MESSAGE_CODE_ACM);
				}
			} catch (Exception e) {
				// AIOOBE or IllegalArg
				throw new ParameterRangeInvalidException("Failed to parse MessageCode due to: ", e);
			}
			index++;
			// this.circuitIdentificationCode = b[index++];
			try {
				byte[] backwardCallIndicator = new byte[2];
				backwardCallIndicator[0] = b[index++];
				backwardCallIndicator[1] = b[index++];
				BackwardCallIndicatorsImpl bci = new BackwardCallIndicatorsImpl(backwardCallIndicator);
				this.setBackwardCallIndicators(bci);
			} catch (Exception e) {
				// AIOOBE or IllegalArg
				throw new ParameterRangeInvalidException("Failed to parse BackwardCallIndicators due to: ", e);
			}

			// return 3;
			return index - localIndex;
		} else {
			throw new IllegalArgumentException("byte[] must have atleast five octets");
		}

	}

	@Override
	protected int getNumberOfMandatoryVariableLengthParameters() {

		return _MANDATORY_VAR_COUNT;
	}

	protected void decodeMandatoryVariableBody(byte[] parameterBody, int parameterIndex) throws ParameterRangeInvalidException {
		throw new UnsupportedOperationException("This message does not support mandatory variable parameters.");
	}

	@Override
	protected int decodeMandatoryVariableParameters(byte[] b, int index) throws ParameterRangeInvalidException {
		throw new UnsupportedOperationException("This message does not support mandatory variable parameters.");
	}

	protected void decodeOptionalBody(byte[] parameterBody, byte parameterCode) throws ParameterRangeInvalidException {

		switch ((int) parameterCode) {
		case OptionalBackwardCallIndicatorsImpl._PARAMETER_CODE:
			OptionalBackwardCallIndicatorsImpl obi = new OptionalBackwardCallIndicatorsImpl(parameterBody);
			this.setOptionalBakwardCallIndicators(obi);
			break;
		case CallReferenceImpl._PARAMETER_CODE:
			CallReferenceImpl cr = new CallReferenceImpl(parameterBody);
			this.setCallReference(cr);
			break;
		case CauseIndicatorsImpl._PARAMETER_CODE:
			CauseIndicatorsImpl ci = new CauseIndicatorsImpl(parameterBody);
			this.setCauseIndicators(ci);
			break;
		case UserToUserIndicatorsImpl._PARAMETER_CODE:
			UserToUserIndicatorsImpl utsi = new UserToUserIndicatorsImpl(parameterBody);
			this.setUserToUserIndicators(utsi);
			break;
		case UserToUserInformationImpl._PARAMETER_CODE:
			UserToUserInformationImpl utsi2 = new UserToUserInformationImpl(parameterBody);
			this.setUserToUserInformation(utsi2);
			break;
		case AccessTransportImpl._PARAMETER_CODE:
			AccessTransportImpl at = new AccessTransportImpl(parameterBody);
			this.setAccessTransport(at);
			break;
		// FIXME: There can be more of those.
		case GenericNotificationIndicatorImpl._PARAMETER_CODE:
			GenericNotificationIndicatorImpl gni = new GenericNotificationIndicatorImpl(parameterBody);
			this.setGenericNotificationIndicator(gni);
			break;
		case TransmissionMediumUsedImpl._PARAMETER_CODE:
			TransmissionMediumUsedImpl tmu = new TransmissionMediumUsedImpl(parameterBody);
			this.setTransmissionMediumUsed(tmu);
			break;
		case EchoControlInformationImpl._PARAMETER_CODE:
			EchoControlInformationImpl eci = new EchoControlInformationImpl(parameterBody);
			this.setEchoControlInformation(eci);
			break;
		case AccessDeliveryInformationImpl._PARAMETER_CODE:
			AccessDeliveryInformationImpl adi = new AccessDeliveryInformationImpl(parameterBody);
			this.setAccessDeliveryInformation(adi);
			break;
		case RedirectionNumberImpl._PARAMETER_CODE:
			RedirectionNumberImpl rn = new RedirectionNumberImpl(parameterBody);
			this.setRedirectionNumber(rn);
			break;
		case ParameterCompatibilityInformationImpl._PARAMETER_CODE:
			ParameterCompatibilityInformationImpl pci = new ParameterCompatibilityInformationImpl(parameterBody);
			this.setParameterCompatibilityInformation(pci);
			break;
		case CallDiversionInformationImpl._PARAMETER_CODE:
			CallDiversionInformationImpl cdi = new CallDiversionInformationImpl(parameterBody);
			this.setCallDiversionInformation(cdi);
			break;
		case NetworkSpecificFacilityImpl._PARAMETER_CODE:
			NetworkSpecificFacilityImpl nsf = new NetworkSpecificFacilityImpl(parameterBody);
			this.setNetworkSpecificFacility(nsf);
			break;
		case RemoteOperationsImpl._PARAMETER_CODE:
			RemoteOperationsImpl ro = new RemoteOperationsImpl(parameterBody);
			this.setRemoteOperations(ro);
			break;
		case ServiceActivationImpl._PARAMETER_CODE:
			ServiceActivationImpl sa = new ServiceActivationImpl(parameterBody);
			this.setServiceActivation(sa);
			break;
		case RedirectionNumberRestrictionImpl._PARAMETER_CODE:
			RedirectionNumberRestrictionImpl rnr = new RedirectionNumberRestrictionImpl(parameterBody);
			this.setRedirectionNumberRestriction(rnr);
			break;
		case ConferenceTreatmentIndicatorsImpl._PARAMETER_CODE:
			ConferenceTreatmentIndicatorsImpl cti = new ConferenceTreatmentIndicatorsImpl(parameterBody);
			this.setConferenceTreatmentIndicators(cti);
			break;
		case UIDActionIndicatorsImpl._PARAMETER_CODE:
			UIDActionIndicatorsImpl uidAI = new UIDActionIndicatorsImpl(parameterBody);
			this.setUIDActionIndicators(uidAI);
			break;
		case ApplicationTransportParameterImpl._PARAMETER_CODE:
			ApplicationTransportParameterImpl atp = new ApplicationTransportParameterImpl(parameterBody);
			this.setApplicationTransportParameter(atp);
			break;
		case CCNRPossibleIndicator._PARAMETER_CODE:
			CCNRPossibleIndicatorImpl ccnrPI = new CCNRPossibleIndicatorImpl(parameterBody);
			this.setCCNRPossibleIndicator(ccnrPI);
			break;
		case HTRInformationImpl._PARAMETER_CODE:
			HTRInformationImpl htr = new HTRInformationImpl(parameterBody);
			this.setHTRInformation(htr);
			break;
		case PivotRoutingBackwardInformationImpl._PARAMETER_CODE:
			PivotRoutingBackwardInformationImpl pivot = new PivotRoutingBackwardInformationImpl(parameterBody);
			this.setPivotRoutingBackwardInformation(pivot);
			break;
		case RedirectStatusImpl._PARAMETER_CODE:
			RedirectStatusImpl rs = new RedirectStatusImpl(parameterBody);
			this.setRedirectStatus(rs);
			break;
		case EndOfOptionalParametersImpl._PARAMETER_CODE:
			// we add this by default
			break;

		default:
			throw new IllegalArgumentException("Unrecognized parameter code for optional part: " + parameterCode);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.ss7.isup.impl.ISUPMessageImpl#mandatoryVariablePartPossible
	 * ()
	 */
	// @Override
	// protected boolean mandatoryVariablePartPossible() {
	//		
	// return false;
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.impl.ISUPMessageImpl#optionalPartIsPossible()
	 */
	@Override
	protected boolean optionalPartIsPossible() {

		return true;
	}

}
