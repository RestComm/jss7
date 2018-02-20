/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:21:00:56 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;

import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.restcomm.protocols.ss7.isup.message.ReleaseMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.AutomaticCongestionLevel;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.DisplayInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.HTRInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageName;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageType;
import org.restcomm.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.restcomm.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectBackwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectCounter;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.restcomm.protocols.ss7.isup.message.parameter.SignalingPointCode;
import org.restcomm.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:21:00:56 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
class ReleaseMessageImpl extends ISUPMessageImpl implements ReleaseMessage {

    public static final MessageType _MESSAGE_TYPE = new MessageTypeImpl(MessageName.Release);

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

    ReleaseMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
            Map<Integer, Integer> mandatoryCode2Index, Map<Integer, Integer> mandatoryVariableCode2Index,
            Map<Integer, Integer> optionalCode2Index) {
        super(mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index,
                optionalCode2Index);

        super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());
        super.o_Parameters.put(_INDEX_O_EndOfOptionalParameters, _END_OF_OPTIONAL_PARAMETERS);

    }

    /**
     * @param parameterBody
     * @param parameterCode
     * @throws ParameterException
     */
    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {
        switch (parameterIndex) {
            case _INDEX_V_CauseIndicators:
                CauseIndicators cpn = parameterFactory.createCauseIndicators();
                ((AbstractISUPParameter) cpn).decode(parameterBody);
                this.setCauseIndicators(cpn);
                break;
            default:
                throw new ParameterException("Unrecognized parameter index for mandatory variable part: " + parameterIndex);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.ISUPMessage#decodeOptionalBody(byte[], byte)
     */

    protected void decodeOptionalBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, byte parameterCode)
            throws ParameterException {

        switch (parameterCode & 0xFF) {
            case RedirectionNumber._PARAMETER_CODE:
                RedirectionNumber rn = parameterFactory.createRedirectionNumber();
                ((AbstractISUPParameter) rn).decode(parameterBody);
                this.setRedirectionNumber(rn);
                break;
            case RedirectionInformation._PARAMETER_CODE:
                RedirectionInformation ri = parameterFactory.createRedirectionInformation();
                ((AbstractISUPParameter) ri).decode(parameterBody);
                this.setRedirectionInformation(ri);
                break;
            case AccessTransport._PARAMETER_CODE:
                AccessTransport at = parameterFactory.createAccessTransport();
                ((AbstractISUPParameter) at).decode(parameterBody);
                this.setAccessTransport(at);
                break;
            case SignalingPointCode._PARAMETER_CODE:
                SignalingPointCode v = parameterFactory.createSignalingPointCode();
                ((AbstractISUPParameter) v).decode(parameterBody);
                this.setSignalingPointCode(v);
                break;
            case UserToUserInformation._PARAMETER_CODE:
                UserToUserInformation u2ui = parameterFactory.createUserToUserInformation();
                ((AbstractISUPParameter) u2ui).decode(parameterBody);
                this.setU2UInformation(u2ui);
                break;
            case AutomaticCongestionLevel._PARAMETER_CODE:
                AutomaticCongestionLevel acl = parameterFactory.createAutomaticCongestionLevel();
                ((AbstractISUPParameter) acl).decode(parameterBody);
                this.setAutomaticCongestionLevel(acl);
                break;
            case NetworkSpecificFacility._PARAMETER_CODE:
                NetworkSpecificFacility nsf = parameterFactory.createNetworkSpecificFacility();
                ((AbstractISUPParameter) nsf).decode(parameterBody);
                this.setNetworkSpecificFacility(nsf);
                break;
            case AccessDeliveryInformation._PARAMETER_CODE:
                AccessDeliveryInformation adi = parameterFactory.createAccessDeliveryInformation();
                ((AbstractISUPParameter) adi).decode(parameterBody);
                this.setAccessDeliveryInformation(adi);
                break;
            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation pci = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) pci).decode(parameterBody);
                this.setParameterCompatibilityInformation(pci);
                break;
            case UserToUserIndicators._PARAMETER_CODE:
                UserToUserIndicators utui = parameterFactory.createUserToUserIndicators();
                ((AbstractISUPParameter) utui).decode(parameterBody);
                this.setU2UIndicators(utui);
                break;
            case DisplayInformation._PARAMETER_CODE:
                DisplayInformation di = parameterFactory.createDisplayInformation();
                ((AbstractISUPParameter) di).decode(parameterBody);
                this.setDisplayInformation(di);
                break;
            case RemoteOperations._PARAMETER_CODE:
                RemoteOperations ro = parameterFactory.createRemoteOperations();
                ((AbstractISUPParameter) ro).decode(parameterBody);
                this.setRemoteOperations(ro);
                break;
            case HTRInformation._PARAMETER_CODE:
                HTRInformation htri = parameterFactory.createHTRInformation();
                ((AbstractISUPParameter) htri).decode(parameterBody);
                this.setHTRInformation(htri);
                break;
            case RedirectBackwardInformation._PARAMETER_CODE:
                RedirectBackwardInformation rbi = parameterFactory.createRedirectBackwardInformation();
                ((AbstractISUPParameter) rbi).decode(parameterBody);
                this.setRedirectBackwardInformation(rbi);
                break;
            case RedirectCounter._PARAMETER_CODE:
                RedirectCounter rc = parameterFactory.createRedirectCounter();
                ((AbstractISUPParameter) rc).decode(parameterBody);
                this.setRedirectCounter(rc);
                break;

            default:
                throw new ParameterException("Unrecognized parameter code for optional part: " + parameterCode);
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

    public MessageType getMessageType() {
        return this._MESSAGE_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.messages.ISUPMessage# getNumberOfMandatoryVariableLengthParameters()
     */

    protected int getNumberOfMandatoryVariableLengthParameters() {

        return _MANDATORY_VAR_COUNT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.ISUPMessage#hasAllMandatoryParameters()
     */

    public boolean hasAllMandatoryParameters() {
        if (this.f_Parameters.get(_INDEX_F_MessageType) == null
                || this.f_Parameters.get(_INDEX_F_MessageType).getCode() != this.getMessageType().getCode()) {
            return false;
        }
        if (this.v_Parameters.get(_INDEX_V_CauseIndicators) == null) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.impl.ISUPMessageImpl#optionalPartIsPossible ()
     */

    protected boolean optionalPartIsPossible() {

        return true;
    }
}
