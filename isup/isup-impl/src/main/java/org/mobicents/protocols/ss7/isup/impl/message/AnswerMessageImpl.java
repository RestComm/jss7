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
 * Start time:23:55:28 2009-09-06<br>
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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.message.AnswerMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransport;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.CallHistoryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.DisplayInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;
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
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:23:55:28 2009-09-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
class AnswerMessageImpl extends ISUPMessageImpl implements AnswerMessage {

    public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);
    private static final int _MANDATORY_VAR_COUNT = 0;

    static final int _INDEX_F_MessageType = 0;

    static final int _INDEX_O_BackwardCallIndicators = 0;
    static final int _INDEX_O_OptionalBackwardCallIndicators = 1;
    static final int _INDEX_O_CallReference = 2;
    static final int _INDEX_O_UserToUserIndicators = 3;
    static final int _INDEX_O_UserToUserInformation = 4;
    static final int _INDEX_O_ConnectedNumber = 5;
    static final int _INDEX_O_AccessTransport = 6;
    static final int _INDEX_O_AccessDeliveryInformation = 7;
    static final int _INDEX_O_GenericNotificationIndicator = 8;
    static final int _INDEX_O_ParameterCompatibilityInformation = 9;
    static final int _INDEX_O_BackwardGVNS = 10;
    static final int _INDEX_O_CallHistoryInformation = 11;
    static final int _INDEX_O_GenericNumber = 12;
    static final int _INDEX_O_TransmissionMediumUsed = 13;
    static final int _INDEX_O_NetworkSpecificFacility = 14;
    static final int _INDEX_O_RemoteOperations = 15;
    static final int _INDEX_O_RedirectionNumber = 16;
    static final int _INDEX_O_ServiceActivation = 17;
    static final int _INDEX_O_EchoControlInformation = 18;
    static final int _INDEX_O_RedirectionNumberRestriction = 19;
    static final int _INDEX_O_DisplayInformation = 20;
    static final int _INDEX_O_ConferenceTreatmentIndicators = 21;
    static final int _INDEX_O_ApplicationTransportParameter = 22;
    static final int _INDEX_O_PivotRoutingBackwardInformation = 23;
    static final int _INDEX_O_RedirectStatus = 24;
    static final int _INDEX_O_EndOfOptionalParameters = 25;

    AnswerMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
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
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getAccessDeliveryInformation()
     */
    public AccessDeliveryInformation getAccessDeliveryInformation() {
        return (AccessDeliveryInformation) super.o_Parameters.get(this._INDEX_O_AccessDeliveryInformation);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getAccessTransport ()
     */
    public AccessTransport getAccessTransport() {
        return (AccessTransport) super.o_Parameters.get(this._INDEX_O_AccessTransport);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getApplicationTransportParameter()
     */
    public ApplicationTransport getApplicationTransportParameter() {
        return (ApplicationTransport) super.o_Parameters.get(this._INDEX_O_ApplicationTransportParameter);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getBackwardCallIndicators()
     */
    public BackwardCallIndicators getBackwardCallIndicators() {
        return (BackwardCallIndicators) super.o_Parameters.get(this._INDEX_O_BackwardCallIndicators);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getBackwardGVNS()
     */
    public BackwardGVNS getBackwardGVNS() {
        return (BackwardGVNS) super.o_Parameters.get(this._INDEX_O_BackwardGVNS);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getCallHistoryInformation()
     */
    public CallHistoryInformation getCallHistoryInformation() {
        return (CallHistoryInformation) super.o_Parameters.get(this._INDEX_O_CallHistoryInformation);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getCallReference()
     */
    public CallReference getCallReference() {
        return (CallReference) super.o_Parameters.get(this._INDEX_O_CallReference);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getConferenceTreatmentIndicators()
     */
    public ConferenceTreatmentIndicators getConferenceTreatmentIndicators() {
        return (ConferenceTreatmentIndicators) super.o_Parameters.get(this._INDEX_O_ConferenceTreatmentIndicators);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getConnectedNumber ()
     */
    public ConnectedNumber getConnectedNumber() {
        return (ConnectedNumber) super.o_Parameters.get(this._INDEX_O_ConnectedNumber);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getDisplayInformation ()
     */
    public DisplayInformation getDisplayInformation() {
        return (DisplayInformation) super.o_Parameters.get(this._INDEX_O_DisplayInformation);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getEchoControlInformation()
     */
    public EchoControlInformation getEchoControlInformation() {
        return (EchoControlInformation) super.o_Parameters.get(this._INDEX_O_EchoControlInformation);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getGenericNotificationIndicator()
     */
    public GenericNotificationIndicator getGenericNotificationIndicator() {
        return (GenericNotificationIndicator) super.o_Parameters.get(this._INDEX_O_GenericNotificationIndicator);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getGenericNumber()
     */
    public GenericNumber getGenericNumber() {
        return (GenericNumber) super.o_Parameters.get(this._INDEX_O_GenericNumber);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getNetworkSpecificFacility()
     */
    public NetworkSpecificFacility getNetworkSpecificFacility() {
        return (NetworkSpecificFacility) super.o_Parameters.get(this._INDEX_O_NetworkSpecificFacility);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getOptionalBackwardCallIndicators()
     */
    public OptionalBackwardCallIndicators getOptionalBackwardCallIndicators() {
        return (OptionalBackwardCallIndicators) super.o_Parameters.get(this._INDEX_O_OptionalBackwardCallIndicators);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getParameterCompatibilityInformation()
     */
    public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
        return (ParameterCompatibilityInformation) super.o_Parameters.get(this._INDEX_O_ParameterCompatibilityInformation);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getPivotRoutingBackwardInformation()
     */
    public PivotRoutingBackwardInformation getPivotRoutingBackwardInformation() {
        return (PivotRoutingBackwardInformation) super.o_Parameters.get(this._INDEX_O_PivotRoutingBackwardInformation);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getRedirectionNumber ()
     */
    public RedirectionNumber getRedirectionNumber() {
        return (RedirectionNumber) super.o_Parameters.get(this._INDEX_O_RedirectionNumber);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getRedirectionNumberRestriction()
     */
    public RedirectionNumberRestriction getRedirectionNumberRestriction() {
        return (RedirectionNumberRestriction) super.o_Parameters.get(this._INDEX_O_RedirectionNumberRestriction);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getRedirectStatus ()
     */
    public RedirectStatus getRedirectStatus() {
        return (RedirectStatus) super.o_Parameters.get(this._INDEX_O_RedirectStatus);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getRemoteOperations ()
     */
    public RemoteOperations getRemoteOperations() {
        return (RemoteOperations) super.o_Parameters.get(this._INDEX_O_RemoteOperations);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#getServiceActivation ()
     */
    public ServiceActivation getServiceActivation() {
        return (ServiceActivation) super.o_Parameters.get(this._INDEX_O_ServiceActivation);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getTransmissionMediumUsed()
     */
    public TransmissionMediumUsed getTransmissionMediumUsed() {
        return (TransmissionMediumUsed) super.o_Parameters.get(this._INDEX_O_TransmissionMediumUsed);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getUserToUserIndicators()
     */
    public UserToUserIndicators getUserToUserIndicators() {
        return (UserToUserIndicators) super.o_Parameters.get(this._INDEX_O_UserToUserIndicators);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# getUserToUserInformation()
     */
    public UserToUserInformation getUserToUserInformation() {
        return (UserToUserInformation) super.o_Parameters.get(this._INDEX_O_UserToUserInformation);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setAccessDeliveryInformation
     * (org.mobicents.protocols.ss7.isup.message.parameter .AccessDeliveryInformation)
     */
    public void setAccessDeliveryInformation(AccessDeliveryInformation value) {
        super.o_Parameters.put(this._INDEX_O_AccessDeliveryInformation, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setAccessTransport
     * (org.mobicents.protocols.ss7.isup.message.parameter.accessTransport. AccessTransport)
     */
    public void setAccessTransport(AccessTransport value) {
        super.o_Parameters.put(this._INDEX_O_AccessTransport, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setApplicationTransportParameter
     * (org.mobicents.protocols.ss7.isup.message. parameter.ApplicationTransportParameter)
     */
    public void setApplicationTransportParameter(ApplicationTransport value) {
        super.o_Parameters.put(this._INDEX_O_ApplicationTransportParameter, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setBackwardCallIndicators
     * (org.mobicents.protocols.ss7.isup.message.parameter .BackwardCallIndicators)
     */
    public void setBackwardCallIndicators(BackwardCallIndicators indicators) {
        super.o_Parameters.put(this._INDEX_O_BackwardCallIndicators, indicators);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setBackwardGVNS
     * (org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS)
     */
    public void setBackwardGVNS(BackwardGVNS value) {
        super.o_Parameters.put(this._INDEX_O_BackwardGVNS, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setCallHistoryInformation
     * (org.mobicents.protocols.ss7.isup.message.parameter .CallHistoryInformation)
     */
    public void setCallHistoryInformation(CallHistoryInformation value) {
        super.o_Parameters.put(this._INDEX_O_CallHistoryInformation, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setCallReference
     * (org.mobicents.protocols.ss7.isup.message.parameter.CallReference)
     */
    public void setCallReference(CallReference value) {
        super.o_Parameters.put(this._INDEX_O_CallReference, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setConferenceTreatmentIndicators
     * (org.mobicents.protocols.ss7.isup.message. parameter.ConferenceTreatmentIndicators)
     */
    public void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators value) {
        super.o_Parameters.put(this._INDEX_O_ConferenceTreatmentIndicators, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setConnectedNumber
     * (org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber)
     */
    public void setConnectedNumber(ConnectedNumber value) {
        super.o_Parameters.put(this._INDEX_O_ConnectedNumber, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setDisplayInformation
     * (org.mobicents.protocols.ss7.isup.message.parameter.DisplayInformation)
     */
    public void setDisplayInformation(DisplayInformation value) {
        super.o_Parameters.put(this._INDEX_O_DisplayInformation, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setEchoControlInformation
     * (org.mobicents.protocols.ss7.isup.message.parameter .EchoControlInformation)
     */
    public void setEchoControlInformation(EchoControlInformation value) {
        super.o_Parameters.put(this._INDEX_O_EchoControlInformation, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setGenericNotificationIndicator
     * (org.mobicents.protocols.ss7.isup.message.parameter .GenericNotificationIndicator)
     */
    public void setGenericNotificationIndicator(GenericNotificationIndicator value) {
        super.o_Parameters.put(this._INDEX_O_GenericNotificationIndicator, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setGenericNumber
     * (org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber)
     */
    public void setGenericNumber(GenericNumber value) {
        super.o_Parameters.put(this._INDEX_O_GenericNumber, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setNetworkSpecificFacility
     * (org.mobicents.protocols.ss7.isup.message.parameter .NetworkSpecificFacility)
     */
    public void setNetworkSpecificFacility(NetworkSpecificFacility value) {
        super.o_Parameters.put(this._INDEX_O_NetworkSpecificFacility, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setOptionalBackwardCallIndicators
     * (org.mobicents.protocols.ss7.isup.message .parameter.OptionalBackwardCallIndicators)
     */
    public void setOptionalBackwardCallIndicators(OptionalBackwardCallIndicators value) {
        super.o_Parameters.put(this._INDEX_O_OptionalBackwardCallIndicators, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setParameterCompatibilityInformation
     * (org.mobicents.protocols.ss7.isup.message .parameter.ParameterCompatibilityInformation)
     */
    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation value) {
        super.o_Parameters.put(this._INDEX_O_ParameterCompatibilityInformation, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setPivotRoutingBackwardInformation
     * (org.mobicents.protocols.ss7.isup.message .parameter.PivotRoutingBackwardInformation)
     */
    public void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation value) {
        super.o_Parameters.put(this._INDEX_O_PivotRoutingBackwardInformation, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setRedirectionNumber
     * (org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber)
     */
    public void setRedirectionNumber(RedirectionNumber value) {
        super.o_Parameters.put(this._INDEX_O_RedirectionNumber, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setRedirectionNumberRestriction
     * (org.mobicents.protocols.ss7.isup.message.parameter .RedirectionNumberRestriction)
     */
    public void setRedirectionNumberRestriction(RedirectionNumberRestriction value) {
        super.o_Parameters.put(this._INDEX_O_RedirectionNumberRestriction, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setRedirectStatus
     * (org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus)
     */
    public void setRedirectStatus(RedirectStatus value) {
        super.o_Parameters.put(this._INDEX_O_RedirectStatus, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setRemoteOperations
     * (org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations)
     */
    public void setRemoteOperations(RemoteOperations value) {
        super.o_Parameters.put(this._INDEX_O_RemoteOperations, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage#setServiceActivation
     * (org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation)
     */
    public void setServiceActivation(ServiceActivation value) {
        super.o_Parameters.put(this._INDEX_O_ServiceActivation, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setTransmissionMediumUsed
     * (org.mobicents.protocols.ss7.isup.message.parameter .TransmissionMediumUsed)
     */
    public void setTransmissionMediumUsed(TransmissionMediumUsed value) {
        super.o_Parameters.put(this._INDEX_O_TransmissionMediumUsed, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setUserToUserIndicators
     * (org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators)
     */
    public void setUserToUserIndicators(UserToUserIndicators value) {
        super.o_Parameters.put(this._INDEX_O_UserToUserIndicators, value);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.message.AnswerMessage# setUserToUserInformation
     * (org.mobicents.protocols.ss7.isup.message.parameter .UserToUserInformation)
     */
    public void setUserToUserInformation(UserToUserInformation value) {
        super.o_Parameters.put(this._INDEX_O_UserToUserInformation, value);

    }

    protected int decodeMandatoryVariableParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {

        throw new UnsupportedOperationException("This message does not support mandatory variable parameters.");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryVariableBody (byte [], int)
     */

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {
        throw new UnsupportedOperationException("This message does not support mandatory variable parameters.");

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeOptionalBody(byte [], byte)
     */

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException {

        switch (parameterCode & 0xFF) {

            case BackwardCallIndicators._PARAMETER_CODE:
                BackwardCallIndicators bci = parameterFactory.createBackwardCallIndicators();
                ((AbstractISUPParameter) bci).decode(parameterBody);
                this.setBackwardCallIndicators(bci);
                break;
            case OptionalBackwardCallIndicators._PARAMETER_CODE:
                OptionalBackwardCallIndicators OBCI = parameterFactory.createOptionalBackwardCallIndicators();
                ((AbstractISUPParameter) OBCI).decode(parameterBody);
                this.setOptionalBackwardCallIndicators(OBCI);
                break;
            case CallReference._PARAMETER_CODE:
                CallReference CR = parameterFactory.createCallReference();
                ((AbstractISUPParameter) CR).decode(parameterBody);
                this.setCallReference(CR);
                break;
            case UserToUserIndicators._PARAMETER_CODE:
                UserToUserIndicators U2UI = parameterFactory.createUserToUserIndicators();
                ((AbstractISUPParameter) U2UI).decode(parameterBody);
                this.setUserToUserIndicators(U2UI);
                break;
            case UserToUserInformation._PARAMETER_CODE:
                UserToUserInformation U2UII = parameterFactory.createUserToUserInformation();
                ((AbstractISUPParameter) U2UII).decode(parameterBody);
                this.setUserToUserInformation(U2UII);
                break;
            case ConnectedNumber._PARAMETER_CODE:
                ConnectedNumber CN = parameterFactory.createConnectedNumber();
                ((AbstractISUPParameter) CN).decode(parameterBody);
                this.setConnectedNumber(CN);
                break;
            case AccessTransport._PARAMETER_CODE:
                AccessTransport AT = parameterFactory.createAccessTransport();
                ((AbstractISUPParameter) AT).decode(parameterBody);
                this.setAccessTransport(AT);
                break;
            case AccessDeliveryInformation._PARAMETER_CODE:
                AccessDeliveryInformation ADI = parameterFactory.createAccessDeliveryInformation();
                ((AbstractISUPParameter) ADI).decode(parameterBody);
                this.setAccessDeliveryInformation(ADI);
                break;
            case GenericNotificationIndicator._PARAMETER_CODE:
                GenericNotificationIndicator GNI = parameterFactory.createGenericNotificationIndicator();
                ((AbstractISUPParameter) GNI).decode(parameterBody);
                this.setGenericNotificationIndicator(GNI);
                break;
            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation PCI = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) PCI).decode(parameterBody);
                this.setParameterCompatibilityInformation(PCI);
                break;
            case BackwardGVNS._PARAMETER_CODE:
                BackwardGVNS BGVNS = parameterFactory.createBackwardGVNS();
                ((AbstractISUPParameter) BGVNS).decode(parameterBody);
                this.setBackwardGVNS(BGVNS);
                break;
            case CallHistoryInformation._PARAMETER_CODE:
                CallHistoryInformation CHI = parameterFactory.createCallHistoryInformation();
                ((AbstractISUPParameter) CHI).decode(parameterBody);
                this.setCallHistoryInformation(CHI);
                break;
            case GenericNumber._PARAMETER_CODE:
                GenericNumber GN = parameterFactory.createGenericNumber();
                ((AbstractISUPParameter) GN).decode(parameterBody);
                this.setGenericNumber(GN);
                break;
            case TransmissionMediumUsed._PARAMETER_CODE:
                TransmissionMediumUsed TMU = parameterFactory.createTransmissionMediumUsed();
                ((AbstractISUPParameter) TMU).decode(parameterBody);
                this.setTransmissionMediumUsed(TMU);
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
            case RedirectionNumber._PARAMETER_CODE:
                RedirectionNumber RN = parameterFactory.createRedirectionNumber();
                ((AbstractISUPParameter) RN).decode(parameterBody);
                this.setRedirectionNumber(RN);
                break;
            case ServiceActivation._PARAMETER_CODE:
                ServiceActivation SA = parameterFactory.createServiceActivation();
                ((AbstractISUPParameter) SA).decode(parameterBody);
                this.setServiceActivation(SA);
                break;
            case EchoControlInformation._PARAMETER_CODE:
                EchoControlInformation ECI = parameterFactory.createEchoControlInformation();
                ((AbstractISUPParameter) ECI).decode(parameterBody);
                this.setEchoControlInformation(ECI);
                break;
            case RedirectionNumberRestriction._PARAMETER_CODE:
                RedirectionNumberRestriction RNR = parameterFactory.createRedirectionNumberRestriction();
                ((AbstractISUPParameter) RNR).decode(parameterBody);
                this.setRedirectionNumberRestriction(RNR);
                break;
            case DisplayInformation._PARAMETER_CODE:
                DisplayInformation DI = parameterFactory.createDisplayInformation();
                ((AbstractISUPParameter) DI).decode(parameterBody);
                this.setDisplayInformation(DI);
                break;
            case ConferenceTreatmentIndicators._PARAMETER_CODE:
                ConferenceTreatmentIndicators CTI = parameterFactory.createConferenceTreatmentIndicators();
                ((AbstractISUPParameter) CTI).decode(parameterBody);
                this.setConferenceTreatmentIndicators(CTI);
                break;
            case ApplicationTransport._PARAMETER_CODE:
                ApplicationTransport ATP = parameterFactory.createApplicationTransport();
                ((AbstractISUPParameter) ATP).decode(parameterBody);
                this.setApplicationTransportParameter(ATP);
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

        return _MANDATORY_VAR_COUNT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#hasAllMandatoryParameters ()
     */

    public boolean hasAllMandatoryParameters() {

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.ISUPMessageImpl#optionalPartIsPossible ()
     */

    protected boolean optionalPartIsPossible() {

        return true;
    }
}
