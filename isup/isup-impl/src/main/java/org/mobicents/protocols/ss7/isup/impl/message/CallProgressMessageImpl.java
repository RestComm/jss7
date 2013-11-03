/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * Start time:23:56:30 2009-09-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.EndOfOptionalParametersImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.message.CallProgressMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransport;
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

    CallProgressMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryParameters (byte[], int)
     */

    protected int decodeMandatoryParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {

        int localIndex = index;
        index += super.decodeMandatoryParameters(parameterFactory, b, index);
        if (b.length - index > 1) {
            try {
                byte[] eventInformation = new byte[1];
                eventInformation[0] = b[index++];

                EventInformation _ei = parameterFactory.createEventInformation();
                ((AbstractISUPParameter) _ei).decode(eventInformation);
                this.setEventInformation(_ei);
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse EventInformation due to: ", e);
            }

            return index - localIndex;
        } else {
            throw new ParameterException("byte[] must have atleast four octets");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryVariableBody (byte [], int)
     */

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeOptionalBody(byte [], byte)
     */

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException {
        switch (parameterCode & 0xFF) {
            case CauseIndicators._PARAMETER_CODE:
                CauseIndicators CI = parameterFactory.createCauseIndicators();
                ((AbstractISUPParameter) CI).decode(parameterBody);
                this.setCauseIndicators(CI);
                break;

            case CallReference._PARAMETER_CODE:
                CallReference CR = parameterFactory.createCallReference();
                ((AbstractISUPParameter) CR).decode(parameterBody);
                this.setCallReference(CR);
                break;
            case BackwardCallIndicators._PARAMETER_CODE:
                BackwardCallIndicators BCMI = parameterFactory.createBackwardCallIndicators();
                ((AbstractISUPParameter) BCMI).decode(parameterBody);
                this.setBackwardCallIndicators(BCMI);
                break;
            case OptionalBackwardCallIndicators._PARAMETER_CODE:
                OptionalBackwardCallIndicators OBCI = parameterFactory.createOptionalBackwardCallIndicators();
                ((AbstractISUPParameter) OBCI).decode(parameterBody);
                this.setOptionalBackwardCallIndicators(OBCI);
                break;
            case AccessTransport._PARAMETER_CODE:
                AccessTransport AT = parameterFactory.createAccessTransport();
                ((AbstractISUPParameter) AT).decode(parameterBody);
                this.setAccessTransport(AT);
                break;
            case UserToUserIndicators._PARAMETER_CODE:
                UserToUserIndicators U2UI = parameterFactory.createUserToUserIndicators();
                ((AbstractISUPParameter) U2UI).decode(parameterBody);
                this.setUserToUserIndicators(U2UI);
                break;
            case RedirectionNumber._PARAMETER_CODE:
                RedirectionNumber RN = parameterFactory.createRedirectionNumber();
                ((AbstractISUPParameter) RN).decode(parameterBody);
                this.setRedirectionNumber(RN);
                break;
            case UserToUserInformation._PARAMETER_CODE:
                UserToUserInformation U2UII = parameterFactory.createUserToUserInformation();
                ((AbstractISUPParameter) U2UII).decode(parameterBody);
                this.setUserToUserInformation(U2UII);
                break;
            case GenericNotificationIndicator._PARAMETER_CODE:
                GenericNotificationIndicator GNI = parameterFactory.createGenericNotificationIndicator();
                ((AbstractISUPParameter) GNI).decode(parameterBody);
                this.setGenericNotificationIndicator(GNI);
                break;
            case NetworkSpecificFacility._PARAMETER_CODE:
                NetworkSpecificFacility NSF = parameterFactory.createNetworkSpecificFacility();
                ((AbstractISUPParameter) NSF).decode(parameterBody);
                this.setNetworkSpecificFacility(NSF);
                break;
            case RemoteOperations._PARAMETER_CODE:
                RemoteOperations RO = parameterFactory.createRemoteOperations();
                ((AbstractISUPParameter) RO).decode(parameterBody);
                this.setRemoteOperations(RO);
                break;
            case TransmissionMediumUsed._PARAMETER_CODE:
                TransmissionMediumUsed TMU = parameterFactory.createTransmissionMediumUsed();
                ((AbstractISUPParameter) TMU).decode(parameterBody);
                this.setTransmissionMediumUsed(TMU);
                break;
            case AccessDeliveryInformation._PARAMETER_CODE:
                AccessDeliveryInformation ADI = parameterFactory.createAccessDeliveryInformation();
                ((AbstractISUPParameter) ADI).decode(parameterBody);
                this.setAccessDeliveryInformation(ADI);
                break;
            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation PCI = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) PCI).decode(parameterBody);
                this.setParameterCompatibilityInformation(PCI);
                break;
            case CallDiversionInformation._PARAMETER_CODE:
                CallDiversionInformation CDI = parameterFactory.createCallDiversionInformation();
                ((AbstractISUPParameter) CDI).decode(parameterBody);
                this.setCallDiversionInformation(CDI);
                break;
            case ServiceActivation._PARAMETER_CODE:
                ServiceActivation SA = parameterFactory.createServiceActivation();
                ((AbstractISUPParameter) SA).decode(parameterBody);
                this.setServiceActivation(SA);
                break;
            case RedirectionNumberRestriction._PARAMETER_CODE:
                RedirectionNumberRestriction RNR = parameterFactory.createRedirectionNumberRestriction();
                ((AbstractISUPParameter) RNR).decode(parameterBody);
                this.setRedirectionNumberRestriction(RNR);
                break;
            case CallTransferNumber._PARAMETER_CODE:
                CallTransferNumber CTR = parameterFactory.createCallTransferNumber();
                ((AbstractISUPParameter) CTR).decode(parameterBody);
                this.setCallTransferNumber(CTR);
                break;
            case EchoControlInformation._PARAMETER_CODE:
                EchoControlInformation ECI = parameterFactory.createEchoControlInformation();
                ((AbstractISUPParameter) ECI).decode(parameterBody);
                this.setEchoControlInformation(ECI);
                break;
            case ConnectedNumber._PARAMETER_CODE:
                ConnectedNumber CN = parameterFactory.createConnectedNumber();
                ((AbstractISUPParameter) CN).decode(parameterBody);
                this.setConnectedNumber(CN);
                break;
            case BackwardGVNS._PARAMETER_CODE:
                BackwardGVNS BGVNS = parameterFactory.createBackwardGVNS();
                ((AbstractISUPParameter) BGVNS).decode(parameterBody);
                this.setBackwardGVNS(BGVNS);
                break;
            case GenericNumber._PARAMETER_CODE:
                GenericNumber GN = parameterFactory.createGenericNumber();
                ((AbstractISUPParameter) GN).decode(parameterBody);
                this.setGenericNumber(GN);
                break;
            case CallHistoryInformation._PARAMETER_CODE:
                CallHistoryInformation CHI = parameterFactory.createCallHistoryInformation();
                ((AbstractISUPParameter) CHI).decode(parameterBody);
                this.setCallHistoryInformation(CHI);
                break;
            case ConferenceTreatmentIndicators._PARAMETER_CODE:
                ConferenceTreatmentIndicators CTI = parameterFactory.createConferenceTreatmentIndicators();
                ((AbstractISUPParameter) CTI).decode(parameterBody);
                this.setConferenceTreatmentIndicators(CTI);
                break;
            case UIDActionIndicators._PARAMETER_CODE:
                UIDActionIndicators UIDA = parameterFactory.createUIDActionIndicators();
                ((AbstractISUPParameter) UIDA).decode(parameterBody);
                this.setUIDActionIndicators(UIDA);
                break;
            case ApplicationTransport._PARAMETER_CODE:
                ApplicationTransport ATP = parameterFactory.createApplicationTransport();
                ((AbstractISUPParameter) ATP).decode(parameterBody);
                this.setApplicationTransportParameter(ATP);
                break;
            case CCNRPossibleIndicator._PARAMETER_CODE:
                CCNRPossibleIndicator CCNR = parameterFactory.createCCNRPossibleIndicator();
                ((AbstractISUPParameter) CCNR).decode(parameterBody);
                this.setCCNRPossibleIndicator(CCNR);
                break;
            case PivotRoutingBackwardInformation._PARAMETER_CODE:
                PivotRoutingBackwardInformation PRBI = parameterFactory.createPivotRoutingBackwardInformation();
                ((AbstractISUPParameter) PRBI).decode(parameterBody);
                this.setPivotRoutingBackwardInformation(PRBI);
                break;
            case RedirectStatus._PARAMETER_CODE:
                RedirectStatus RS = parameterFactory.createRedirectStatus();
                ((AbstractISUPParameter) RS).decode(parameterBody);
                this.setRedirectStatus(RS);
                break;
            case EndOfOptionalParametersImpl._PARAMETER_CODE:
                // we add this by default
                break;
            default:
                throw new ParameterException("Unrecognized parameter code for optional part: " + parameterCode);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#getMessageType()
     */

    public MessageType getMessageType() {
        return this._MESSAGE_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.isup.ISUPMessageImpl# getNumberOfMandatoryVariableLengthParameters()
     */

    protected int getNumberOfMandatoryVariableLengthParameters() {
        // TODO Auto-generated method stub
        return _MANDATORY_VAR_COUNT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#hasAllMandatoryParameters ()
     */

    public boolean hasAllMandatoryParameters() {

        return this.f_Parameters.get(this._INDEX_F_EventInformation) != null;
    }

    protected boolean optionalPartIsPossible() {

        return true;
    }

    /**
     * @param _ei
     */
    public void setEventInformation(EventInformation ei) {
        super.f_Parameters.put(_INDEX_F_EventInformation, ei);
    }

    /**
     * @param _ei
     */
    public EventInformation getEventInformation() {
        return (EventInformation) super.f_Parameters.get(_INDEX_F_EventInformation);

    }

    /**
     * @param rS
     */
    public void setRedirectStatus(RedirectStatus rS) {
        super.o_Parameters.put(_INDEX_O_RedirectStatus, rS);

    }

    /**
     * @param pRBI
     */
    public void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation pRBI) {
        super.o_Parameters.put(_INDEX_O_PivotRoutingBackwardInformation, pRBI);

    }

    /**
     * @param cCNR
     */
    public void setCCNRPossibleIndicator(CCNRPossibleIndicator cCNR) {
        super.o_Parameters.put(_INDEX_O_CCNRPossibleIndicator, cCNR);

    }

    /**
     * @param aTP
     */
    public void setApplicationTransportParameter(ApplicationTransport aTP) {
        super.o_Parameters.put(_INDEX_O_ApplicationTransportParameter, aTP);

    }

    /**
     * @param uIDA
     */
    public void setUIDActionIndicators(UIDActionIndicators uIDA) {
        super.o_Parameters.put(_INDEX_O_UIDActionIndicators, uIDA);

    }

    /**
     * @param cTI
     */
    public void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators cTI) {
        super.o_Parameters.put(_INDEX_O_ConferenceTreatmentIndicators, cTI);

    }

    /**
     * @param cHI
     */
    public void setCallHistoryInformation(CallHistoryInformation cHI) {
        super.o_Parameters.put(_INDEX_O_CallHistoryInformation, cHI);

    }

    /**
     * @param gN
     */
    public void setGenericNumber(GenericNumber gN) {
        super.o_Parameters.put(_INDEX_O_GenericNumber, gN);

    }

    /**
     * @param bGVNS
     */
    public void setBackwardGVNS(BackwardGVNS bGVNS) {
        super.o_Parameters.put(_INDEX_O_BackwardGVNS, bGVNS);

    }

    /**
     * @param cN
     */
    public void setConnectedNumber(ConnectedNumber cN) {
        super.o_Parameters.put(_INDEX_O_ConnectedNumber, cN);

    }

    /**
     * @param eCI
     */
    public void setEchoControlInformation(EchoControlInformation eCI) {
        super.o_Parameters.put(_INDEX_O_EchoControlInformation, eCI);

    }

    /**
     * @param cTR
     */
    public void setCallTransferNumber(CallTransferNumber cTR) {
        super.o_Parameters.put(_INDEX_O_CallTransferNumber, cTR);

    }

    /**
     * @param rNR
     */
    public void setRedirectionNumberRestriction(RedirectionNumberRestriction rNR) {
        super.o_Parameters.put(_INDEX_O_RedirectionNumberRestriction, rNR);

    }

    /**
     * @param sA
     */
    public void setServiceActivation(ServiceActivation sA) {
        super.o_Parameters.put(_INDEX_O_ServiceActivation, sA);

    }

    /**
     * @param cDI
     */
    public void setCallDiversionInformation(CallDiversionInformation cDI) {
        super.o_Parameters.put(_INDEX_O_CallDiversionInformation, cDI);

    }

    /**
     * @param pCI
     */
    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation pCI) {
        super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, pCI);

    }

    /**
     * @param aDI
     */
    public void setAccessDeliveryInformation(AccessDeliveryInformation aDI) {
        super.o_Parameters.put(_INDEX_O_AccessDeliveryInformation, aDI);

    }

    /**
     * @param tMU
     */
    public void setTransmissionMediumUsed(TransmissionMediumUsed tMU) {
        super.o_Parameters.put(_INDEX_O_TransmissionMediumUsed, tMU);

    }

    /**
     * @param rO
     */
    public void setRemoteOperations(RemoteOperations rO) {
        super.o_Parameters.put(_INDEX_O_RemoteOperations, rO);

    }

    /**
     * @param nSF
     */
    public void setNetworkSpecificFacility(NetworkSpecificFacility nSF) {
        super.o_Parameters.put(_INDEX_O_NetworkSpecificFacility, nSF);

    }

    /**
     * @param gNI
     */
    public void setGenericNotificationIndicator(GenericNotificationIndicator gNI) {
        super.o_Parameters.put(_INDEX_O_GenericNotificationIndicator, gNI);

    }

    /**
     * @param u2uii
     */
    public void setUserToUserInformation(UserToUserInformation u2uii) {
        super.o_Parameters.put(_INDEX_O_UserToUserInformation, u2uii);

    }

    /**
     * @param rN
     */
    public void setRedirectionNumber(RedirectionNumber rN) {
        super.o_Parameters.put(_INDEX_O_RedirectionNumber, rN);

    }

    /**
     * @param u2ui
     */
    public void setUserToUserIndicators(UserToUserIndicators u2ui) {
        super.o_Parameters.put(_INDEX_O_UserToUserIndicators, u2ui);

    }

    /**
     * @param aT
     */
    public void setAccessTransport(AccessTransport aT) {
        super.o_Parameters.put(_INDEX_O_AccessTransport, aT);

    }

    /**
     * @param oBCI
     */
    public void setOptionalBackwardCallIndicators(OptionalBackwardCallIndicators oBCI) {
        super.o_Parameters.put(_INDEX_O_OptionalBackwardCallIndicators, oBCI);

    }

    /**
     * @param bCMI
     */
    public void setBackwardCallIndicators(BackwardCallIndicators bCMI) {
        super.o_Parameters.put(_INDEX_O_BackwardCallIndicators, bCMI);

    }

    /**
     * @param cR
     */
    public void setCallReference(CallReference cR) {
        super.o_Parameters.put(_INDEX_O_CallReference, cR);

    }

    /**
     * @param cI
     */
    public void setCauseIndicators(CauseIndicators cI) {
        super.o_Parameters.put(_INDEX_O_CauseIndicators, cI);

    }

    /**
     * @param rS
     */
    public RedirectStatus getRedirectStatus() {
        return (RedirectStatus) super.o_Parameters.get(_INDEX_O_RedirectStatus);

    }

    /**
     * @param pRBI
     */
    public PivotRoutingBackwardInformation getPivotRoutingBackwardInformation() {
        return (PivotRoutingBackwardInformation) super.o_Parameters.get(_INDEX_O_PivotRoutingBackwardInformation);

    }

    /**
     * @param cCNR
     */
    public CCNRPossibleIndicator getCCNRPossibleIndicator() {
        return (CCNRPossibleIndicator) super.o_Parameters.get(_INDEX_O_CCNRPossibleIndicator);

    }

    /**
     * @param aTP
     */
    public ApplicationTransport getApplicationTransportParameter() {
        return (ApplicationTransport) super.o_Parameters.get(_INDEX_O_ApplicationTransportParameter);

    }

    /**
     * @param uIDA
     */
    public UIDActionIndicators getUIDActionIndicators() {
        return (UIDActionIndicators) super.o_Parameters.get(_INDEX_O_UIDActionIndicators);

    }

    /**
     * @param cTI
     */
    public ConferenceTreatmentIndicators getConferenceTreatmentIndicators() {
        return (ConferenceTreatmentIndicators) super.o_Parameters.get(_INDEX_O_ConferenceTreatmentIndicators);

    }

    /**
     * @param cHI
     */
    public CallHistoryInformation getCallHistoryInformation() {
        return (CallHistoryInformation) super.o_Parameters.get(_INDEX_O_CallHistoryInformation);

    }

    /**
     * @param gN
     */
    public GenericNumber getGenericNumber() {
        return (GenericNumber) super.o_Parameters.get(_INDEX_O_GenericNumber);

    }

    /**
     * @param bGVNS
     */
    public BackwardGVNS getBackwardGVNS() {
        return (BackwardGVNS) super.o_Parameters.get(_INDEX_O_BackwardGVNS);

    }

    /**
     * @param cN
     */
    public ConnectedNumber getConnectedNumber() {
        return (ConnectedNumber) super.o_Parameters.get(_INDEX_O_ConnectedNumber);

    }

    /**
     * @param eCI
     */
    public EchoControlInformation getEchoControlInformation() {
        return (EchoControlInformation) super.o_Parameters.get(_INDEX_O_EchoControlInformation);

    }

    /**
     * @param cTR
     */
    public CallTransferNumber getCallTransferNumber() {
        return (CallTransferNumber) super.o_Parameters.get(_INDEX_O_CallTransferNumber);

    }

    /**
     * @param rNR
     */
    public RedirectionNumberRestriction getRedirectionNumberRestriction() {
        return (RedirectionNumberRestriction) super.o_Parameters.get(_INDEX_O_RedirectionNumberRestriction);

    }

    /**
     * @param sA
     */
    public ServiceActivation getServiceActivation() {
        return (ServiceActivation) super.o_Parameters.get(_INDEX_O_ServiceActivation);

    }

    /**
     * @param cDI
     */
    public CallDiversionInformation getCallDiversionInformation() {
        return (CallDiversionInformation) super.o_Parameters.get(_INDEX_O_CallDiversionInformation);

    }

    /**
     * @param pCI
     */
    public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
        return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);

    }

    /**
     * @param aDI
     */
    public AccessDeliveryInformation getAccessDeliveryInformation() {
        return (AccessDeliveryInformation) super.o_Parameters.get(_INDEX_O_AccessDeliveryInformation);

    }

    /**
     * @param tMU
     */
    public TransmissionMediumUsed getTransmissionMediumUsed() {
        return (TransmissionMediumUsed) super.o_Parameters.get(_INDEX_O_TransmissionMediumUsed);

    }

    /**
     * @param rO
     */
    public RemoteOperations getRemoteOperations() {
        return (RemoteOperations) super.o_Parameters.get(_INDEX_O_RemoteOperations);

    }

    /**
     * @param nSF
     */
    public NetworkSpecificFacility getNetworkSpecificFacility() {
        return (NetworkSpecificFacility) super.o_Parameters.get(_INDEX_O_NetworkSpecificFacility);

    }

    /**
     * @param gNI
     */
    public GenericNotificationIndicator getGenericNotificationIndicator() {
        return (GenericNotificationIndicator) super.o_Parameters.get(_INDEX_O_GenericNotificationIndicator);

    }

    /**
     * @param u2uii
     */
    public UserToUserInformation getUserToUserInformation() {
        return (UserToUserInformation) super.o_Parameters.get(_INDEX_O_UserToUserInformation);

    }

    /**
     * @param rN
     */
    public RedirectionNumber getRedirectionNumber() {
        return (RedirectionNumber) super.o_Parameters.get(_INDEX_O_RedirectionNumber);

    }

    /**
     * @param u2ui
     */
    public UserToUserIndicators getUserToUserIndicators() {
        return (UserToUserIndicators) super.o_Parameters.get(_INDEX_O_UserToUserIndicators);

    }

    /**
     * @param aT
     */
    public AccessTransport getAccessTransport() {
        return (AccessTransport) super.o_Parameters.get(_INDEX_O_AccessTransport);

    }

    /**
     * @param oBCI
     */
    public OptionalBackwardCallIndicators getOptionalBackwardCallIndicators() {
        return (OptionalBackwardCallIndicators) super.o_Parameters.get(_INDEX_O_OptionalBackwardCallIndicators);

    }

    /**
     * @param bCMI
     */
    public BackwardCallIndicators getBackwardCallIndicators() {
        return (BackwardCallIndicators) super.o_Parameters.get(_INDEX_O_BackwardCallIndicators);

    }

    /**
     * @param cR
     */
    public CallReference getCallReference() {
        return (CallReference) super.o_Parameters.get(_INDEX_O_CallReference);

    }

    /**
     * @param cI
     */
    public CauseIndicators getCauseIndicators() {
        return (CauseIndicators) super.o_Parameters.get(_INDEX_O_CauseIndicators);

    }

}
