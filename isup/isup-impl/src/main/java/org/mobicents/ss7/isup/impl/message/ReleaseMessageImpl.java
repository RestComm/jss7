/**
 * Start time:21:00:56 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.impl.message.parameter.AccessDeliveryInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.AutomaticCongestionLevelImpl;
import org.mobicents.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.mobicents.ss7.isup.impl.message.parameter.CircuitIdentificationCodeImpl;
import org.mobicents.ss7.isup.impl.message.parameter.DisplayInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.HTRInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.ss7.isup.impl.message.parameter.NetworkSpecificFacilityImpl;
import org.mobicents.ss7.isup.impl.message.parameter.ParameterCompatibilityInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.RedirectBackwardInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.RedirectCounterImpl;
import org.mobicents.ss7.isup.impl.message.parameter.RedirectionInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.RedirectionNumberImpl;
import org.mobicents.ss7.isup.impl.message.parameter.RemoteOperationsImpl;
import org.mobicents.ss7.isup.impl.message.parameter.SignalingPointCodeImpl;
import org.mobicents.ss7.isup.impl.message.parameter.UserToUserIndicatorsImpl;
import org.mobicents.ss7.isup.impl.message.parameter.UserToUserInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.accessTransport.AccessTransportImpl;
import org.mobicents.ss7.isup.message.ReleaseMessage;
import org.mobicents.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.ss7.isup.message.parameter.AutomaticCongestionLevel;
import org.mobicents.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.ss7.isup.message.parameter.DisplayInformation;
import org.mobicents.ss7.isup.message.parameter.HTRInformation;
import org.mobicents.ss7.isup.message.parameter.ISUPParameter;
import org.mobicents.ss7.isup.message.parameter.MessageType;
import org.mobicents.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.ss7.isup.message.parameter.RedirectBackwardInformation;
import org.mobicents.ss7.isup.message.parameter.RedirectCounter;
import org.mobicents.ss7.isup.message.parameter.RedirectionInformation;
import org.mobicents.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.ss7.isup.message.parameter.SignalingPointCode;
import org.mobicents.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:21:00:56 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
class ReleaseMessageImpl extends ISUPMessageImpl implements ReleaseMessage {

	public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(_MESSAGE_CODE_REL);

	private static final int _MANDATORY_VAR_COUNT = 1;
	// mandatory fixed L
	static final int _INDEX_F_MessageType = 0;
	// mandatory variable L
	static final int _INDEX_V_CauseIndicators = 0;
	// optional
	static final int _INDEX_O_RedirectionInformation = 0;
	static final int _INDEX_O_RedirectionNumber = 1;
	static final int _INDEX_O_AccessTransport = 2;
	static final int _INDEX_O_SignalingPointCode = 3;
	static final int _INDEX_O_U2UInformation = 4;
	static final int _INDEX_O_AutomaticCongestionLevel = 5;
	static final int _INDEX_O_NetworkSpecificFacility = 6;
	static final int _INDEX_O_AccessDeliveryInformation = 7;
	static final int _INDEX_O_ParameterCompatibilityInformation = 8;
	static final int _INDEX_O_U2UIndicators = 9;
	static final int _INDEX_O_DisplayInformation = 10;
	static final int _INDEX_O_RemoteOperations = 11;
	static final int _INDEX_O_HTRInformation = 12;
	static final int _INDEX_O_RedirectCounter = 13;
	static final int _INDEX_O_RedirectBackwardInformation = 14;
	static final int _INDEX_O_EndOfOptionalParameters = 15;

	ReleaseMessageImpl(Object source, byte[] b, Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
			Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) throws ParameterRangeInvalidException {
		this(source, mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index, optionalCode2Index);
		decodeElement(b);

	}

	ReleaseMessageImpl(Object source, Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
			Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) {
		super(source, mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index, optionalCode2Index);

		super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
		super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);

	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.messages.ISUPMessage#decodeMandatoryParameters(byte[],
	 * int)
	 */
	@Override
	protected int decodeMandatoryParameters(byte[] b, int index) throws ParameterRangeInvalidException {
		int localIndex = index;

		if (b.length - index > 2) {

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
				if (b[index] != this._MESSAGE_CODE_REL) {
					throw new ParameterRangeInvalidException("Message code is not: " + this._MESSAGE_CODE_REL);
				}
			} catch (Exception e) {
				// AIOOBE or IllegalArg
				throw new ParameterRangeInvalidException("Failed to parse MessageCode due to: ", e);
			}
			index++;

			return index - localIndex;
		} else {
			throw new ParameterRangeInvalidException("byte[] must have atleast three octets");
		}
	}

	/**
	 * @param parameterBody
	 * @param parameterCode
	 * @throws ParameterRangeInvalidException
	 */
	protected void decodeMandatoryVariableBody(byte[] parameterBody, int parameterIndex) throws ParameterRangeInvalidException {
		switch (parameterIndex) {
		case _INDEX_V_CauseIndicators:
			CauseIndicatorsImpl cpn = new CauseIndicatorsImpl(parameterBody);
			this.setCauseIndicators(cpn);
			break;
		default:
			throw new ParameterRangeInvalidException("Unrecognized parameter index for mandatory variable part: " + parameterIndex);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.messages.ISUPMessage#decodeOptionalBody(byte[],
	 * byte)
	 */
	@Override
	protected void decodeOptionalBody(byte[] parameterBody, byte parameterCode) throws ParameterRangeInvalidException {

		switch ((int) parameterCode) {
		case RedirectionNumberImpl._PARAMETER_CODE:
			RedirectionNumberImpl rn = new RedirectionNumberImpl(parameterBody);
			this.setRedirectionNumber(rn);
			break;
		case RedirectionInformationImpl._PARAMETER_CODE:
			RedirectionInformationImpl ri = new RedirectionInformationImpl(parameterBody);
			this.setRedirectionInformation(ri);
			break;
		case AccessTransportImpl._PARAMETER_CODE:
			AccessTransportImpl at = new AccessTransportImpl(parameterBody);
			this.setAccessTransport(at);
			break;
		case SignalingPointCodeImpl._PARAMETER_CODE:
			SignalingPointCodeImpl v = new SignalingPointCodeImpl(parameterBody);
			this.setSignalingPointCode(v);
			break;
		case UserToUserInformationImpl._PARAMETER_CODE:
			UserToUserInformationImpl u2ui = new UserToUserInformationImpl(parameterBody);
			this.setU2UInformation(u2ui);
			break;
		case AutomaticCongestionLevelImpl._PARAMETER_CODE:
			AutomaticCongestionLevelImpl acl = new AutomaticCongestionLevelImpl(parameterBody);
			this.setAutomaticCongestionLevel(acl);
			break;
		case NetworkSpecificFacilityImpl._PARAMETER_CODE:
			NetworkSpecificFacilityImpl nsf = new NetworkSpecificFacilityImpl(parameterBody);
			this.setNetworkSpecificFacility(nsf);
			break;
		case AccessDeliveryInformationImpl._PARAMETER_CODE:
			AccessDeliveryInformationImpl adi = new AccessDeliveryInformationImpl(parameterBody);
			this.setAccessDeliveryInformation(adi);
			break;
		case ParameterCompatibilityInformationImpl._PARAMETER_CODE:
			ParameterCompatibilityInformationImpl pci = new ParameterCompatibilityInformationImpl(parameterBody);
			this.setParameterCompatibilityInformation(pci);
			break;
		case UserToUserIndicatorsImpl._PARAMETER_CODE:
			UserToUserIndicatorsImpl utui = new UserToUserIndicatorsImpl(parameterBody);
			this.setU2UIndicators(utui);
			break;
		case DisplayInformationImpl._PARAMETER_CODE:
			DisplayInformationImpl di = new DisplayInformationImpl(parameterBody);
			this.setDisplayInformation(di);
			break;
		case RemoteOperationsImpl._PARAMETER_CODE:
			RemoteOperationsImpl ro = new RemoteOperationsImpl(parameterBody);
			this.setRemoteOperations(ro);
			break;
		case HTRInformationImpl._PARAMETER_CODE:
			HTRInformationImpl htri = new HTRInformationImpl(parameterBody);
			this.setHTRInformation(htri);
			break;
		case RedirectBackwardInformationImpl._PARAMETER_CODE:
			RedirectBackwardInformationImpl rbi = new RedirectBackwardInformationImpl(parameterBody);
			this.setRedirectBackwardInformation(rbi);
			break;
		case RedirectCounterImpl._PARAMETER_CODE:
			RedirectCounterImpl rc = new RedirectCounterImpl(parameterBody);
			this.setRedirectCounter(rc);
			break;

		default:
			throw new IllegalArgumentException("Unrecognized parameter code for optional part: " + parameterCode);
		}

	}

	public CauseIndicators getCauseIndicators() {
		return (CauseIndicators) super.v_Parameters.get(_INDEX_V_CauseIndicators);
	}

	public void setCauseIndicators(CauseIndicators v) {
		super.v_Parameters.put(_INDEX_V_CauseIndicators, v);
	}

	public RedirectionInformation getRedirectionInformation() {
		return (RedirectionInformation) super.o_Parameters.get(_INDEX_O_RedirectionInformation);
	}

	public void setRedirectionInformation(RedirectionInformation v) {
		super.o_Parameters.put(_INDEX_O_RedirectionInformation, v);
	}

	public RedirectionNumber getRedirectionNumber() {
		return (RedirectionNumber) super.o_Parameters.get(_INDEX_O_RedirectionNumber);
	}

	public void setRedirectionNumber(RedirectionNumber v) {
		super.o_Parameters.put(_INDEX_O_RedirectionNumber, v);
	}

	public AccessTransport getAccessTransport() {
		return (AccessTransport) super.o_Parameters.get(_INDEX_O_AccessTransport);
	}

	public void setAccessTransport(AccessTransport v) {
		super.o_Parameters.put(_INDEX_O_AccessTransport, v);
	}

	public SignalingPointCode getSignalingPointCode() {
		return (SignalingPointCode) super.o_Parameters.get(_INDEX_O_SignalingPointCode);
	}

	public void setSignalingPointCode(SignalingPointCode v) {
		super.o_Parameters.put(_INDEX_O_SignalingPointCode, v);
	}

	public UserToUserInformation getU2UInformation() {
		return (UserToUserInformation) super.o_Parameters.get(_INDEX_O_U2UInformation);
	}

	public void setU2UInformation(UserToUserInformation v) {
		super.o_Parameters.put(_INDEX_O_U2UInformation, v);
	}

	public AutomaticCongestionLevel getAutomaticCongestionLevel() {
		return (AutomaticCongestionLevel) super.o_Parameters.get(_INDEX_O_AutomaticCongestionLevel);
	}

	public void setAutomaticCongestionLevel(AutomaticCongestionLevel v) {
		super.o_Parameters.put(_INDEX_O_AutomaticCongestionLevel, v);
	}

	public NetworkSpecificFacility getNetworkSpecificFacility() {
		return (NetworkSpecificFacility) super.o_Parameters.get(_INDEX_O_NetworkSpecificFacility);
	}

	public void setNetworkSpecificFacility(NetworkSpecificFacility v) {
		super.o_Parameters.put(_INDEX_O_NetworkSpecificFacility, v);
	}

	public AccessDeliveryInformation getAccessDeliveryInformation() {
		return (AccessDeliveryInformation) super.o_Parameters.get(_INDEX_O_AccessDeliveryInformation);
	}

	public void setAccessDeliveryInformation(AccessDeliveryInformation v) {
		super.o_Parameters.put(_INDEX_O_AccessDeliveryInformation, v);
	}

	public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
		return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);
	}

	public void setParameterCompatibilityInformation(ParameterCompatibilityInformation v) {
		super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, v);
	}

	public UserToUserIndicators getU2UIndicators() {
		return (UserToUserIndicators) super.o_Parameters.get(_INDEX_O_U2UIndicators);
	}

	public void setU2UIndicators(UserToUserIndicators v) {
		super.o_Parameters.put(_INDEX_O_U2UIndicators, v);
	}

	public DisplayInformation getDisplayInformation() {
		return (DisplayInformation) super.o_Parameters.get(_INDEX_O_DisplayInformation);
	}

	public void setDisplayInformation(DisplayInformation v) {
		super.o_Parameters.put(_INDEX_O_DisplayInformation, v);
	}

	public RemoteOperations getRemoteOperations() {
		return (RemoteOperations) super.o_Parameters.get(_INDEX_O_RemoteOperations);
	}

	public void setRemoteOperations(RemoteOperations v) {
		super.o_Parameters.put(_INDEX_O_RemoteOperations, v);
	}

	public HTRInformation getHTRInformation() {
		return (HTRInformation) super.o_Parameters.get(_INDEX_O_HTRInformation);
	}

	public void setHTRInformation(HTRInformation v) {
		super.o_Parameters.put(_INDEX_O_HTRInformation, v);
	}

	public RedirectCounter getRedirectCounter() {
		return (RedirectCounter) super.o_Parameters.get(_INDEX_O_RedirectCounter);
	}

	public void setRedirectCounter(RedirectCounter v) {
		super.o_Parameters.put(_INDEX_O_RedirectCounter, v);
	}

	public RedirectBackwardInformation getRedirectBackwardInformation() {
		return (RedirectBackwardInformation) super.o_Parameters.get(_INDEX_O_RedirectBackwardInformation);
	}

	public void setRedirectBackwardInformation(RedirectBackwardInformation v) {
		super.o_Parameters.put(_INDEX_O_RedirectBackwardInformation, v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.messages.ISUPMessage#getMessageType()
	 */
	@Override
	public MessageType getMessageType() {
		return this._MESSAGE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.isup.messages.ISUPMessage#
	 * getNumberOfMandatoryVariableLengthParameters()
	 */
	@Override
	protected int getNumberOfMandatoryVariableLengthParameters() {

		return _MANDATORY_VAR_COUNT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.messages.ISUPMessage#hasAllMandatoryParameters()
	 */
	@Override
	public boolean hasAllMandatoryParameters() {
		if (this.f_Parameters.get(_INDEX_F_MessageType) == null || this.f_Parameters.get(_INDEX_F_MessageType).getCode() != this.getMessageType().getCode()) {
			return false;
		}
		if (this.v_Parameters.get(_INDEX_V_CauseIndicators) == null) {
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.impl.ISUPMessageImpl#optionalPartIsPossible()
	 */
	@Override
	protected boolean optionalPartIsPossible() {
		
		return true;
	}
}
