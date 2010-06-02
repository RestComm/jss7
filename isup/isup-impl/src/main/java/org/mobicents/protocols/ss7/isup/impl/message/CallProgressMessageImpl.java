/**
 * Start time:23:56:30 2009-09-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AccessDeliveryInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ApplicationTransportParameterImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.BackwardCallIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.BackwardGVNSImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CCNRPossibleIndicatorImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CallDiversionInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CallHistoryInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CallReferenceImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CallTransferNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CircuitIdentificationCodeImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ConferenceTreatmentIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ConnectedNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.EchoControlInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.EventInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNotificationIndicatorImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.NetworkSpecificFacilityImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.OptionalBackwardCallIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ParameterCompatibilityInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.PivotRoutingBackwardInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.RedirectStatusImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.RedirectionNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.RedirectionNumberRestrictionImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.RemoteOperationsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ServiceActivationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.TransmissionMediumUsedImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.UIDActionIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.UserToUserIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.UserToUserInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.accessTransport.AccessTransportImpl;
import org.mobicents.protocols.ss7.isup.message.CallProgressMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransportParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallHistoryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.EventInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalBackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumberRestriction;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDActionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:23:56:30 2009-09-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class CallProgressMessageImpl extends ISUPMessageImpl implements CallProgressMessage {

	public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);
	private static final int _MANDATORY_VAR_COUNT = 0;

	static final int _INDEX_F_MessageType = 0;
	static final int _INDEX_F_EventInformation = 1;

	static final int _INDEX_O_CauseIndicators = 0;
	static final int _INDEX_O_CallReference = 1;
	static final int _INDEX_O_BackwardCallIndicators = 2;
	static final int _INDEX_O_OptionalBackwardCallIndicators = 3;
	static final int _INDEX_O_AccessTransport = 4;
	static final int _INDEX_O_UserToUserIndicators = 5;
	static final int _INDEX_O_RedirectionNumber = 6;
	static final int _INDEX_O_UserToUserInformation = 7;
	static final int _INDEX_O_GenericNotificationIndicator = 8;
	static final int _INDEX_O_NetworkSpecificFacility = 9;
	static final int _INDEX_O_RemoteOperations = 10;
	static final int _INDEX_O_TransmissionMediumUsed = 11;
	static final int _INDEX_O_AccessDeliveryInformation = 12;
	static final int _INDEX_O_ParameterCompatibilityInformation = 13;
	static final int _INDEX_O_CallDiversionInformation = 14;
	static final int _INDEX_O_ServiceActivation = 15;
	static final int _INDEX_O_RedirectionNumberRestriction = 16;
	static final int _INDEX_O_CallTransferNumber = 17;
	static final int _INDEX_O_EchoControlInformation = 18;
	static final int _INDEX_O_ConnectedNumber = 19;
	static final int _INDEX_O_BackwardGVNS = 20;
	static final int _INDEX_O_GenericNumber = 21;
	static final int _INDEX_O_CallHistoryInformation = 22;
	static final int _INDEX_O_ConferenceTreatmentIndicators = 23;
	static final int _INDEX_O_UIDActionIndicators = 24;
	static final int _INDEX_O_ApplicationTransportParameter = 25;
	static final int _INDEX_O_CCNRPossibleIndicator = 26;
	static final int _INDEX_O_PivotRoutingBackwardInformation = 27;
	static final int _INDEX_O_RedirectStatus = 28;
	static final int _INDEX_O_EndOfOptionalParameters = 29;

	CallProgressMessageImpl(Object source, byte[] b, Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
			Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) throws ParameterRangeInvalidException {
		this(source, mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index, optionalCode2Index);
		decodeElement(b);

	}

	CallProgressMessageImpl(Object source, Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
			Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) {
		super(source, mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index, optionalCode2Index);

		super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
		super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryParameters(byte[],
	 * int)
	 */
	@Override
	protected int decodeMandatoryParameters(byte[] b, int index) throws ParameterRangeInvalidException {

		
		
		if (b.length - index > 3) {
			int localIndex = index;
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
				if (b[index] != this.MESSAGE_CODE) {
					throw new ParameterRangeInvalidException("Message code is not: " + this.MESSAGE_CODE);
				}
			} catch (Exception e) {
				// AIOOBE or IllegalArg
				throw new ParameterRangeInvalidException("Failed to parse MessageCode due to: ", e);
			}
			index++;
			try {
				byte[] eventInformation = new byte[1];
				eventInformation[0] = b[index++];

				EventInformation _ei = new EventInformationImpl(eventInformation);
				this.addParameter(_ei);
			} catch (Exception e) {
				// AIOOBE or IllegalArg
				throw new ParameterRangeInvalidException("Failed to parse EventInformation due to: ", e);
			}

			return index - localIndex;
		} else {
			throw new ParameterRangeInvalidException("byte[] must have atleast four octets");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryVariableBody(byte
	 * [], int)
	 */
	@Override
	protected void decodeMandatoryVariableBody(byte[] parameterBody, int parameterIndex) throws ParameterRangeInvalidException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeOptionalBody(byte[],
	 * byte)
	 */
	@Override
	protected void decodeOptionalBody(byte[] parameterBody, byte parameterCode) throws ParameterRangeInvalidException {
		switch ((int) parameterCode) {
		case CauseIndicators._PARAMETER_CODE:
			break;

		case CallReference._PARAMETER_CODE:
			CallReference CR = new CallReferenceImpl(parameterBody);
			this.addParameter(CR);
			break;
		case BackwardCallIndicators._PARAMETER_CODE:
			BackwardCallIndicators BCMI = new BackwardCallIndicatorsImpl(parameterBody);
			super.addParameter(BCMI);
			break;
		case OptionalBackwardCallIndicators._PARAMETER_CODE:
			OptionalBackwardCallIndicators OBCI = new OptionalBackwardCallIndicatorsImpl(parameterBody);
			super.addParameter(OBCI);
			break;
		case AccessTransport._PARAMETER_CODE:
			AccessTransport AT = new AccessTransportImpl(parameterBody);
			super.addParameter(AT);
			break;
		case UserToUserIndicators._PARAMETER_CODE:
			UserToUserIndicators U2UI = new UserToUserIndicatorsImpl(parameterBody);
			super.addParameter(U2UI);
			break;
		case RedirectionNumber._PARAMETER_CODE:
			RedirectionNumber RN = new RedirectionNumberImpl(parameterBody);
			super.addParameter(RN);
			break;
		case UserToUserInformation._PARAMETER_CODE:
			UserToUserInformation U2UII = new UserToUserInformationImpl(parameterBody);
			super.addParameter(U2UII);
			break;
		case GenericNotificationIndicator._PARAMETER_CODE:
			GenericNotificationIndicator GNI = new GenericNotificationIndicatorImpl(parameterBody);
			super.addParameter(GNI);
			break;
		case NetworkSpecificFacility._PARAMETER_CODE:
			NetworkSpecificFacility NSF = new NetworkSpecificFacilityImpl(parameterBody);
			super.addParameter(NSF);
			break;
		case RemoteOperations._PARAMETER_CODE:
			RemoteOperations RO = new RemoteOperationsImpl(parameterBody);
			super.addParameter(RO);
			break;
		case TransmissionMediumUsed._PARAMETER_CODE:
			TransmissionMediumUsed TMU = new TransmissionMediumUsedImpl(parameterBody);
			super.addParameter(TMU);
			break;
		case AccessDeliveryInformation._PARAMETER_CODE:
			AccessDeliveryInformation ADI = new AccessDeliveryInformationImpl(parameterBody);
			super.addParameter(ADI);
			break;
		case ParameterCompatibilityInformation._PARAMETER_CODE:
			ParameterCompatibilityInformation PCI = new ParameterCompatibilityInformationImpl(parameterBody);
			super.addParameter(PCI);
			break;
		case CallDiversionInformation._PARAMETER_CODE:
			CallDiversionInformation CDI = new CallDiversionInformationImpl(parameterBody);
			super.addParameter(CDI);
			break;
		case ServiceActivation._PARAMETER_CODE:
			ServiceActivation SA = new ServiceActivationImpl(parameterBody);
			super.addParameter(SA);
			break;
		case RedirectionNumberRestriction._PARAMETER_CODE:
			RedirectionNumberRestriction RNR = new RedirectionNumberRestrictionImpl(parameterBody);
			super.addParameter(RNR);
			break;
		case CallTransferNumber._PARAMETER_CODE:
			CallTransferNumber CTR = new CallTransferNumberImpl(parameterBody);
			super.addParameter(CTR);
			break;
		case EchoControlInformation._PARAMETER_CODE:
			EchoControlInformation ECI = new EchoControlInformationImpl(parameterBody);
			super.addParameter(ECI);
			break;
		case ConnectedNumber._PARAMETER_CODE:
			ConnectedNumber CN = new ConnectedNumberImpl(parameterBody);
			super.addParameter(CN);
			break;
		case BackwardGVNS._PARAMETER_CODE:
			BackwardGVNS BGVNS = new BackwardGVNSImpl(parameterBody);
			super.addParameter(BGVNS);
			break;
		case GenericNumber._PARAMETER_CODE:
			GenericNumber GN = new GenericNumberImpl(parameterBody);
			super.addParameter(GN);
			break;
		case CallHistoryInformation._PARAMETER_CODE:
			CallHistoryInformation CHI = new CallHistoryInformationImpl(parameterBody);
			super.addParameter(CHI);
			break;
		case ConferenceTreatmentIndicators._PARAMETER_CODE:
			ConferenceTreatmentIndicators CTI = new ConferenceTreatmentIndicatorsImpl(parameterBody);
			super.addParameter(CTI);
			break;
		case UIDActionIndicators._PARAMETER_CODE:
			UIDActionIndicators UIDA = new UIDActionIndicatorsImpl(parameterBody);
			break;
		case ApplicationTransportParameter._PARAMETER_CODE:
			ApplicationTransportParameter ATP = new ApplicationTransportParameterImpl(parameterBody);
			super.addParameter(ATP);
			break;
		case CCNRPossibleIndicator._PARAMETER_CODE:
			CCNRPossibleIndicator CCNR = new CCNRPossibleIndicatorImpl(parameterBody);
			super.addParameter(CCNR);
			break;
		case PivotRoutingBackwardInformation._PARAMETER_CODE:
			PivotRoutingBackwardInformation PRBI = new PivotRoutingBackwardInformationImpl(parameterBody);
			super.addParameter(PRBI);
			break;
		case RedirectStatus._PARAMETER_CODE:
			RedirectStatus RS = new RedirectStatusImpl(parameterBody);
			super.addParameter(RS);
			break;
		default:
			throw new IllegalArgumentException("Unrecognized parameter code for optional part: " + parameterCode);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#getMessageType()
	 */
	@Override
	public MessageType getMessageType() {
		return this._MESSAGE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.isup.ISUPMessageImpl#
	 * getNumberOfMandatoryVariableLengthParameters()
	 */
	@Override
	protected int getNumberOfMandatoryVariableLengthParameters() {
		// TODO Auto-generated method stub
		return _MANDATORY_VAR_COUNT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#hasAllMandatoryParameters()
	 */
	@Override
	public boolean hasAllMandatoryParameters() {

		return this.f_Parameters.get(this._INDEX_F_EventInformation) != null;
	}

	@Override
	protected boolean optionalPartIsPossible() {

		return true;
	}
}
