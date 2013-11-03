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
 * Start time:14:30:39 2009-04-20<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.EndOfOptionalParametersImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransport;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.HTRInformation;
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
 * Start time:14:30:39 2009-04-20<br>
 * Project: mobicents-isup-stack<br>
 * See Table 21/Q.763
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
class AddressCompleteMessageImpl extends ISUPMessageImpl implements AddressCompleteMessage {

    public static final MessageTypeImpl _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);

    private static final int _MANDATORY_VAR_COUNT = 0;

    static final int _INDEX_F_MessageType = 0;
    static final int _INDEX_F_BackwardCallIndicators = 1;
    // FIXME: those can be sent in any order, but we prefer this way, its faster
    // to access by index than by hash ?
    static final int _INDEX_O_OptionalBackwardCallIndicators = 0;
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

    AddressCompleteMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);
    }

    public boolean hasAllMandatoryParameters() {

        if (super.f_Parameters.get(_INDEX_F_MessageType) == null) {
            return false;
        }

        if (super.f_Parameters.get(_INDEX_F_BackwardCallIndicators) == null
                || super.f_Parameters.get(_INDEX_F_BackwardCallIndicators).getCode() != BackwardCallIndicators._PARAMETER_CODE) {
            return false;
        }

        return true;
    }

    public MessageType getMessageType() {
        return _MESSAGE_TYPE;
    }

    public void setBackwardCallIndicators(BackwardCallIndicators indicators) {
        super.f_Parameters.put(_INDEX_F_BackwardCallIndicators, indicators);
    }

    public BackwardCallIndicators getBackwardCallIndicators() {
        return (BackwardCallIndicators) super.f_Parameters.get(_INDEX_F_BackwardCallIndicators);
    }

    public void setOptionalBackwardCallIndicators(OptionalBackwardCallIndicators value) {
        super.o_Parameters.put(_INDEX_O_OptionalBackwardCallIndicators, value);

    }

    public OptionalBackwardCallIndicators getOptionalBackwardCallIndicators() {
        return (OptionalBackwardCallIndicators) super.o_Parameters.get(_INDEX_O_OptionalBackwardCallIndicators);
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

    public void setApplicationTransportParameter(ApplicationTransport value) {
        super.o_Parameters.put(_INDEX_O_ApplicationTransportParameter, value);
    }

    public ApplicationTransport getApplicationTransportParameter() {
        return (ApplicationTransport) super.o_Parameters.get(_INDEX_O_ApplicationTransportParameter);
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

    protected int decodeMandatoryParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {
        int localIndex = index;
        index += super.decodeMandatoryParameters(parameterFactory, b, index);
        if (b.length - index > 1) {

            // this.circuitIdentificationCode = b[index++];
            try {
                byte[] backwardCallIndicator = new byte[2];
                backwardCallIndicator[0] = b[index++];
                backwardCallIndicator[1] = b[index++];
                BackwardCallIndicators bci = parameterFactory.createBackwardCallIndicators();
                ((AbstractISUPParameter) bci).decode(backwardCallIndicator);
                this.setBackwardCallIndicators(bci);
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse BackwardCallIndicators due to: ", e);
            }

            // return 3;
            return index - localIndex;
        } else {
            throw new IllegalArgumentException("byte[] must have atleast five octets");
        }

    }

    protected int getNumberOfMandatoryVariableLengthParameters() {

        return _MANDATORY_VAR_COUNT;
    }

    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {
        throw new UnsupportedOperationException("This message does not support mandatory variable parameters.");
    }

    protected int decodeMandatoryVariableParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {
        throw new UnsupportedOperationException("This message does not support mandatory variable parameters.");
    }

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException {

        switch (parameterCode & 0xFF) {
            case OptionalBackwardCallIndicators._PARAMETER_CODE:
                OptionalBackwardCallIndicators obi = parameterFactory.createOptionalBackwardCallIndicators();
                ((AbstractISUPParameter) obi).decode(parameterBody);
                this.setOptionalBackwardCallIndicators(obi);
                break;
            case CallReference._PARAMETER_CODE:
                CallReference cr = parameterFactory.createCallReference();
                ((AbstractISUPParameter) cr).decode(parameterBody);
                this.setCallReference(cr);
                break;
            case CauseIndicators._PARAMETER_CODE:
                CauseIndicators ci = parameterFactory.createCauseIndicators();
                ((AbstractISUPParameter) ci).decode(parameterBody);
                this.setCauseIndicators(ci);
                break;
            case UserToUserIndicators._PARAMETER_CODE:
                UserToUserIndicators utsi = parameterFactory.createUserToUserIndicators();
                ((AbstractISUPParameter) utsi).decode(parameterBody);
                this.setUserToUserIndicators(utsi);
                break;
            case UserToUserInformation._PARAMETER_CODE:
                UserToUserInformation utsi2 = parameterFactory.createUserToUserInformation();
                ((AbstractISUPParameter) utsi2).decode(parameterBody);
                this.setUserToUserInformation(utsi2);
                break;
            case AccessTransport._PARAMETER_CODE:
                AccessTransport at = parameterFactory.createAccessTransport();
                ((AbstractISUPParameter) at).decode(parameterBody);
                this.setAccessTransport(at);
                break;
            // FIXME: There can be more of those.
            case GenericNotificationIndicator._PARAMETER_CODE:
                GenericNotificationIndicator gni = parameterFactory.createGenericNotificationIndicator();
                ((AbstractISUPParameter) gni).decode(parameterBody);
                this.setGenericNotificationIndicator(gni);
                break;
            case TransmissionMediumUsed._PARAMETER_CODE:
                TransmissionMediumUsed tmu = parameterFactory.createTransmissionMediumUsed();
                ((AbstractISUPParameter) tmu).decode(parameterBody);
                this.setTransmissionMediumUsed(tmu);
                break;
            case EchoControlInformation._PARAMETER_CODE:
                EchoControlInformation eci = parameterFactory.createEchoControlInformation();
                ((AbstractISUPParameter) eci).decode(parameterBody);
                this.setEchoControlInformation(eci);
                break;
            case AccessDeliveryInformation._PARAMETER_CODE:
                AccessDeliveryInformation adi = parameterFactory.createAccessDeliveryInformation();
                ((AbstractISUPParameter) adi).decode(parameterBody);
                this.setAccessDeliveryInformation(adi);
                break;
            case RedirectionNumber._PARAMETER_CODE:
                RedirectionNumber rn = parameterFactory.createRedirectionNumber();
                ((AbstractISUPParameter) rn).decode(parameterBody);
                this.setRedirectionNumber(rn);
                break;
            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation pci = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) pci).decode(parameterBody);
                this.setParameterCompatibilityInformation(pci);
                break;
            case CallDiversionInformation._PARAMETER_CODE:
                CallDiversionInformation cdi = parameterFactory.createCallDiversionInformation();
                ((AbstractISUPParameter) cdi).decode(parameterBody);
                this.setCallDiversionInformation(cdi);
                break;
            case NetworkSpecificFacility._PARAMETER_CODE:
                NetworkSpecificFacility nsf = parameterFactory.createNetworkSpecificFacility();
                ((AbstractISUPParameter) nsf).decode(parameterBody);
                this.setNetworkSpecificFacility(nsf);
                break;
            case RemoteOperations._PARAMETER_CODE:
                RemoteOperations ro = parameterFactory.createRemoteOperations();
                ((AbstractISUPParameter) ro).decode(parameterBody);
                this.setRemoteOperations(ro);
                break;
            case ServiceActivation._PARAMETER_CODE:
                ServiceActivation sa = parameterFactory.createServiceActivation();
                ((AbstractISUPParameter) sa).decode(parameterBody);
                this.setServiceActivation(sa);
                break;
            case RedirectionNumberRestriction._PARAMETER_CODE:
                RedirectionNumberRestriction rnr = parameterFactory.createRedirectionNumberRestriction();
                ((AbstractISUPParameter) rnr).decode(parameterBody);
                this.setRedirectionNumberRestriction(rnr);
                break;
            case ConferenceTreatmentIndicators._PARAMETER_CODE:
                ConferenceTreatmentIndicators cti = parameterFactory.createConferenceTreatmentIndicators();
                ((AbstractISUPParameter) cti).decode(parameterBody);
                this.setConferenceTreatmentIndicators(cti);
                break;
            case UIDActionIndicators._PARAMETER_CODE:
                UIDActionIndicators uidAI = parameterFactory.createUIDActionIndicators();
                ((AbstractISUPParameter) uidAI).decode(parameterBody);
                this.setUIDActionIndicators(uidAI);
                break;
            case ApplicationTransport._PARAMETER_CODE:
                ApplicationTransport atp = parameterFactory.createApplicationTransport();
                ((AbstractISUPParameter) atp).decode(parameterBody);
                this.setApplicationTransportParameter(atp);
                break;
            case CCNRPossibleIndicator._PARAMETER_CODE:
                CCNRPossibleIndicator ccnrPI = parameterFactory.createCCNRPossibleIndicator();
                ((AbstractISUPParameter) ccnrPI).decode(parameterBody);
                this.setCCNRPossibleIndicator(ccnrPI);
                break;
            case HTRInformation._PARAMETER_CODE:
                HTRInformation htr = parameterFactory.createHTRInformation();
                ((AbstractISUPParameter) htr).decode(parameterBody);
                this.setHTRInformation(htr);
                break;
            case PivotRoutingBackwardInformation._PARAMETER_CODE:
                PivotRoutingBackwardInformation pivot = parameterFactory.createPivotRoutingBackwardInformation();
                ((AbstractISUPParameter) pivot).decode(parameterBody);
                this.setPivotRoutingBackwardInformation(pivot);
                break;
            case RedirectStatus._PARAMETER_CODE:
                RedirectStatus rs = parameterFactory.createRedirectStatus();
                ((AbstractISUPParameter) rs).decode(parameterBody);
                this.setRedirectStatus(rs);
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
     * @see org.mobicents.protocols.ss7.isup.impl.ISUPMessageImpl#mandatoryVariablePartPossible ()
     */
    //
    // protected boolean mandatoryVariablePartPossible() {
    //
    // return false;
    // }
    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.ISUPMessageImpl#optionalPartIsPossible()
     */

    protected boolean optionalPartIsPossible() {

        return true;
    }

}
