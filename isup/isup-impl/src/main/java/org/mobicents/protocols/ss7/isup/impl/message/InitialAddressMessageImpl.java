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
 * Start time:08:17:13 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransport;
import org.mobicents.protocols.ss7.isup.message.parameter.CCSS;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CallOfferingTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledDirectoryNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledINNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitAssigmentMap;
import org.mobicents.protocols.ss7.isup.message.parameter.ClosedUserGroupInterlockCode;
import org.mobicents.protocols.ss7.isup.message.parameter.CollectCallRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.CorrelationID;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericReference;
import org.mobicents.protocols.ss7.isup.message.parameter.HopCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.MLPPPrecedence;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageName;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.NatureOfConnectionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkManagementControls;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkRoutingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledINNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginatingISCPointCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotCapability;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingForwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PropagationDelayCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.QueryOnReleaseCapability;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectCapability;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectForwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.SCFID;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.protocols.ss7.isup.message.parameter.TransimissionMediumRequierementPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.TransitNetworkSelection;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumRequirement;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDCapabilityIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformationPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:08:17:13 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class InitialAddressMessageImpl extends ISUPMessageImpl implements InitialAddressMessage {

    public static final MessageType _MESSAGE_TYPE = new MessageTypeImpl(MessageName.InitialAddress);
    private static final int _MANDATORY_VAR_COUNT = 1;
    // mandatory fixed L
    static final int _INDEX_F_MessageType = 0;
    static final int _INDEX_F_NatureOfConnectionIndicators = 1;
    static final int _INDEX_F_ForwardCallIndicators = 2;
    static final int _INDEX_F_CallingPartyCategory = 3;
    static final int _INDEX_F_TransmissionMediumRequirement = 4;
    // mandatory variable L
    static final int _INDEX_V_CalledPartyNumber = 0;
    // optional
    static final int _INDEX_O_TransitNetworkSelection = 0;
    static final int _INDEX_O_CallReference = 1;
    static final int _INDEX_O_CallingPartyNumber = 2;
    static final int _INDEX_O_OptionalForwardCallIndicators = 3;
    static final int _INDEX_O_RedirectingNumber = 4;
    static final int _INDEX_O_RedirectionInformation = 5;
    static final int _INDEX_O_ClosedUserGroupInterlockCode = 6;
    static final int _INDEX_O_ConnectionRequest = 7;
    static final int _INDEX_O_OriginalCalledNumber = 8;
    static final int _INDEX_O_UserToUserInformation = 9;
    static final int _INDEX_O_AccessTransport = 10;
    static final int _INDEX_O_UserServiceInformation = 11;
    static final int _INDEX_O_User2UIndicators = 12;
    static final int _INDEX_O_GenericNumber = 13;
    static final int _INDEX_O_PropagationDelayCounter = 14;
    static final int _INDEX_O_UserServiceInformationPrime = 15;
    static final int _INDEX_O_NetworkSPecificFacility = 16;
    static final int _INDEX_O_GenericDigits = 17;
    static final int _INDEX_O_OriginatingISCPointCode = 18;
    static final int _INDEX_O_UserTeleserviceInformation = 19;
    static final int _INDEX_O_RemoteOperations = 20;
    static final int _INDEX_O_ParameterCompatibilityInformation = 21;
    static final int _INDEX_O_GenericNotificationIndicator = 22;
    static final int _INDEX_O_ServiceActivation = 23;
    static final int _INDEX_O_GenericReference = 24;
    static final int _INDEX_O_MLPPPrecedence = 25;
    static final int _INDEX_O_TransimissionMediumRequierementPrime = 26;
    static final int _INDEX_O_LocationNumber = 27;
    static final int _INDEX_O_ForwardGVNS = 28;
    static final int _INDEX_O_CCSS = 29;
    static final int _INDEX_O_NetworkManagementControls = 30;
    static final int _INDEX_O_CircuitAssigmentMap = 31;
    static final int _INDEX_O_CorrelationID = 32;
    static final int _INDEX_O_CallDiversionTreatmentIndicators = 33;
    static final int _INDEX_O_CalledINNumber = 34;
    static final int _INDEX_O_CallOfferingTreatmentIndicators = 35;
    static final int _INDEX_O_ConferenceTreatmentIndicators = 36;
    static final int _INDEX_O_SCFID = 37;
    static final int _INDEX_O_UIDCapabilityIndicators = 38;
    static final int _INDEX_O_EchoControlInformation = 39;
    static final int _INDEX_O_HopCounter = 40;
    static final int _INDEX_O_CollectCallRequest = 41;
    static final int _INDEX_O_ApplicationTransport = 42;
    static final int _INDEX_O_PivotCapability = 43;
    static final int _INDEX_O_CalledDirectoryNumber = 44;
    static final int _INDEX_O_OriginalCalledINNumber = 45;
    static final int _INDEX_O_NetworkRoutingNumber = 46;
    static final int _INDEX_O_QueryOnReleaseCapability = 47;
    static final int _INDEX_O_PivotCounter = 48;
    static final int _INDEX_O_PivotRoutingForwardInformation = 49;
    static final int _INDEX_O_RedirectCapability = 50;
    static final int _INDEX_O_RedirectCounter = 51;
    static final int _INDEX_O_RedirectStatus = 52;
    static final int _INDEX_O_RedirectForwardInformation = 53;
    static final int _INDEX_O_EndOfOptionalParameters = 54;

    protected static final List<Integer> mandatoryParam;
    static {
        List<Integer> tmp = new ArrayList<Integer>();
        tmp.add(_INDEX_F_MessageType);
        tmp.add(_INDEX_F_NatureOfConnectionIndicators);
        tmp.add(_INDEX_F_ForwardCallIndicators);
        tmp.add(_INDEX_F_CallingPartyCategory);
        tmp.add(_INDEX_F_TransmissionMediumRequirement);

        mandatoryParam = Collections.unmodifiableList(tmp);

    }

    InitialAddressMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes,
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
     * @see org.mobicents.isup.messages.ISUPMessage#decodeMandatoryParameters(byte[], int)
     */

    protected int decodeMandatoryParameters(ISUPParameterFactory parameterFactory, byte[] b, int index)
            throws ParameterException {
        int localIndex = index;
        index += super.decodeMandatoryParameters(parameterFactory, b, index);
        if (b.length - index > 5) {

            try {
                byte[] natureOfConnectionIndicators = new byte[1];
                natureOfConnectionIndicators[0] = b[index++];

                NatureOfConnectionIndicators _nai = parameterFactory.createNatureOfConnectionIndicators();
                ((AbstractISUPParameter) _nai).decode(natureOfConnectionIndicators);
                this.setNatureOfConnectionIndicators(_nai);
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse NatureOfConnectionIndicators due to: ", e);
            }

            try {
                byte[] body = new byte[2];
                body[0] = b[index++];
                body[1] = b[index++];

                ForwardCallIndicators v = parameterFactory.createForwardCallIndicators();
                ((AbstractISUPParameter) v).decode(body);
                this.setForwardCallIndicators(v);
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse ForwardCallIndicators due to: ", e);
            }

            try {
                byte[] body = new byte[1];
                body[0] = b[index++];

                CallingPartyCategory v = parameterFactory.createCallingPartyCategory();
                ((AbstractISUPParameter) v).decode(body);
                this.setCallingPartCategory(v);
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse CallingPartyCategory due to: ", e);
            }
            try {
                byte[] body = new byte[1];
                body[0] = b[index++];

                TransmissionMediumRequirement v = parameterFactory.createTransmissionMediumRequirement();
                ((AbstractISUPParameter) v).decode(body);
                this.setTransmissionMediumRequirement(v);
            } catch (Exception e) {
                // AIOOBE or IllegalArg
                throw new ParameterException("Failed to parse TransmissionMediumRequirement due to: ", e);
            }

            return index - localIndex;
        } else {
            throw new ParameterException("byte[] must have atleast eight octets");
        }
    }

    /**
     * @param parameterBody
     * @param parameterCode
     * @throws ParameterException
     */
    protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory, byte[] parameterBody, int parameterIndex)
            throws ParameterException {
        switch (parameterIndex) {
            case _INDEX_V_CalledPartyNumber:
                CalledPartyNumber cpn = parameterFactory.createCalledPartyNumber();
                ((AbstractISUPParameter) cpn).decode(parameterBody);
                this.setCalledPartyNumber(cpn);
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

        // TODO Auto-generated method stub
        switch (parameterCode & 0xFF) {
            case TransitNetworkSelection._PARAMETER_CODE:
                TransitNetworkSelection v = parameterFactory.createTransitNetworkSelection();
                ((AbstractISUPParameter) v).decode(parameterBody);
                setTransitNetworkSelection(v);
                break;
            case CallReference._PARAMETER_CODE:
                CallReference cr = parameterFactory.createCallReference();
                ((AbstractISUPParameter) cr).decode(parameterBody);
                this.setCallReference(cr);
                break;
            case CallingPartyNumber._PARAMETER_CODE:
                CallingPartyNumber cpn = parameterFactory.createCallingPartyNumber();
                ((AbstractISUPParameter) cpn).decode(parameterBody);
                this.setCallingPartyNumber(cpn);
                break;
            case OptionalForwardCallIndicators._PARAMETER_CODE:
                OptionalForwardCallIndicators ofci = parameterFactory.createOptionalForwardCallIndicators();
                ((AbstractISUPParameter) ofci).decode(parameterBody);
                this.setOptForwardCallIndicators(ofci);
                break;
            case RedirectingNumber._PARAMETER_CODE:
                RedirectingNumber rn = parameterFactory.createRedirectingNumber();
                ((AbstractISUPParameter) rn).decode(parameterBody);
                this.setRedirectingNumber(rn);
                break;
            case RedirectionInformation._PARAMETER_CODE:
                RedirectionInformation ri = parameterFactory.createRedirectionInformation();
                ((AbstractISUPParameter) ri).decode(parameterBody);
                this.setRedirectionInformation(ri);
                break;
            case ClosedUserGroupInterlockCode._PARAMETER_CODE:
                ClosedUserGroupInterlockCode cugic = parameterFactory.createClosedUserGroupInterlockCode();
                ((AbstractISUPParameter) cugic).decode(parameterBody);
                this.setCUserGroupInterlockCode(cugic);
                break;
            case ConnectionRequest._PARAMETER_CODE:
                ConnectionRequest cr2 = parameterFactory.createConnectionRequest();
                ((AbstractISUPParameter) cr2).decode(parameterBody);
                this.setConnectionRequest(cr2);
                break;
            case OriginalCalledNumber._PARAMETER_CODE:
                OriginalCalledNumber orn = parameterFactory.createOriginalCalledNumber();
                ((AbstractISUPParameter) orn).decode(parameterBody);
                this.setOriginalCalledNumber(orn);
                break;
            case UserToUserInformation._PARAMETER_CODE:
                UserToUserInformation u2ui = parameterFactory.createUserToUserInformation();
                ((AbstractISUPParameter) u2ui).decode(parameterBody);
                this.setU2UInformation(u2ui);
                break;
            case AccessTransport._PARAMETER_CODE:
                AccessTransport at = parameterFactory.createAccessTransport();
                ((AbstractISUPParameter) at).decode(parameterBody);
                this.setAccessTransport(at);
                break;
            case UserServiceInformation._PARAMETER_CODE:
                UserServiceInformation usi = parameterFactory.createUserServiceInformation();
                ((AbstractISUPParameter) usi).decode(parameterBody);
                this.setUserServiceInformation(usi);
                break;
            case UserToUserIndicators._PARAMETER_CODE:
                UserToUserIndicators utui = parameterFactory.createUserToUserIndicators();
                ((AbstractISUPParameter) utui).decode(parameterBody);
                this.setU2UIndicators(utui);
                break;
            case GenericNumber._PARAMETER_CODE:
                GenericNumber gn = parameterFactory.createGenericNumber();
                ((AbstractISUPParameter) gn).decode(parameterBody);
                this.setGenericNumber(gn);
                break;
            case PropagationDelayCounter._PARAMETER_CODE:
                PropagationDelayCounter pdc = parameterFactory.createPropagationDelayCounter();
                ((AbstractISUPParameter) pdc).decode(parameterBody);
                this.setPropagationDelayCounter(pdc);
                break;
            case UserServiceInformationPrime._PARAMETER_CODE:
                UserServiceInformationPrime usip = parameterFactory.createUserServiceInformationPrime();
                ((AbstractISUPParameter) usip).decode(parameterBody);
                this.setUserServiceInformationPrime(usip);
                break;
            case NetworkSpecificFacility._PARAMETER_CODE:
                NetworkSpecificFacility nsf = parameterFactory.createNetworkSpecificFacility();
                ((AbstractISUPParameter) nsf).decode(parameterBody);
                this.setNetworkSpecificFacility(nsf);
                break;
            case GenericDigits._PARAMETER_CODE:
                GenericDigits gd = parameterFactory.createGenericDigits();
                ((AbstractISUPParameter) gd).decode(parameterBody);
                this.setGenericDigits(gd);
                break;
            case OriginatingISCPointCode._PARAMETER_CODE:
                OriginatingISCPointCode vv = parameterFactory.createOriginatingISCPointCode();
                ((AbstractISUPParameter) vv).decode(parameterBody);
                this.setOriginatingISCPointCode(vv);
                break;
            case UserTeleserviceInformation._PARAMETER_CODE:
                UserTeleserviceInformation uti = parameterFactory.createUserTeleserviceInformation();
                ((AbstractISUPParameter) uti).decode(parameterBody);
                this.setUserTeleserviceInformation(uti);
                break;
            case RemoteOperations._PARAMETER_CODE:
                RemoteOperations ro = parameterFactory.createRemoteOperations();
                ((AbstractISUPParameter) ro).decode(parameterBody);
                this.setRemoteOperations(ro);
                break;
            case ParameterCompatibilityInformation._PARAMETER_CODE:
                ParameterCompatibilityInformation pci = parameterFactory.createParameterCompatibilityInformation();
                ((AbstractISUPParameter) pci).decode(parameterBody);
                this.setParameterCompatibilityInformation(pci);
                break;
            case GenericNotificationIndicator._PARAMETER_CODE:
                GenericNotificationIndicator gni = parameterFactory.createGenericNotificationIndicator();
                ((AbstractISUPParameter) gni).decode(parameterBody);
                this.setGenericNotificationIndicator(gni);
                break;
            case ServiceActivation._PARAMETER_CODE:
                ServiceActivation sa = parameterFactory.createServiceActivation();
                ((AbstractISUPParameter) sa).decode(parameterBody);
                this.setServiceActivation(sa);
                break;
            case GenericReference._PARAMETER_CODE:
                GenericReference gr = parameterFactory.createGenericReference();
                ((AbstractISUPParameter) gr).decode(parameterBody);
                this.setGenericReference(gr);
                break;
            case MLPPPrecedence._PARAMETER_CODE:
                MLPPPrecedence mlpp = parameterFactory.createMLPPPrecedence();
                ((AbstractISUPParameter) mlpp).decode(parameterBody);
                this.setMLPPPrecedence(mlpp);
                break;
            case TransmissionMediumRequirement._PARAMETER_CODE:
                TransmissionMediumRequirement tmr = parameterFactory.createTransmissionMediumRequirement();
                ((AbstractISUPParameter) tmr).decode(parameterBody);
                this.setTransmissionMediumRequirement(tmr);
                break;
            case LocationNumber._PARAMETER_CODE:
                LocationNumber ln = parameterFactory.createLocationNumber();
                ((AbstractISUPParameter) ln).decode(parameterBody);
                this.setLocationNumber(ln);
                break;
            case ForwardGVNS._PARAMETER_CODE:
                ForwardGVNS fgvns = parameterFactory.createForwardGVNS();
                ((AbstractISUPParameter) fgvns).decode(parameterBody);
                this.setForwardGVNS(fgvns);
                break;
            case CCSS._PARAMETER_CODE:
                CCSS ccss = parameterFactory.createCCSS();
                ((AbstractISUPParameter) ccss).decode(parameterBody);
                this.setCCSS(ccss);
                break;
            case NetworkManagementControls._PARAMETER_CODE:
                NetworkManagementControls nmc = parameterFactory.createNetworkManagementControls();
                ((AbstractISUPParameter) nmc).decode(parameterBody);
                this.setNetworkManagementControls(nmc);
                break;
            case CircuitAssigmentMap._PARAMETER_CODE:
                CircuitAssigmentMap cam = parameterFactory.createCircuitAssigmentMap();
                ((AbstractISUPParameter) cam).decode(parameterBody);
                this.setCircuitAssigmentMap(cam);
                break;
            case CorrelationID._PARAMETER_CODE:
                CorrelationID cid = parameterFactory.createCorrelationID();
                ((AbstractISUPParameter) cid).decode(parameterBody);
                this.setCorrelationID(cid);
                break;
            case CallDiversionTreatmentIndicators._PARAMETER_CODE:
                CallDiversionTreatmentIndicators cdti = parameterFactory.createCallDiversionTreatmentIndicators();
                ((AbstractISUPParameter) cdti).decode(parameterBody);
                this.setCallDiversionTreatmentIndicators(cdti);
                break;
            case CalledINNumber._PARAMETER_CODE:
                CalledINNumber cin = parameterFactory.createCalledINNumber();
                ((AbstractISUPParameter) cin).decode(parameterBody);
                this.setCalledINNumber(cin);
                break;
            case CallOfferingTreatmentIndicators._PARAMETER_CODE:
                CallOfferingTreatmentIndicators coti = parameterFactory.createCallOfferingTreatmentIndicators();
                ((AbstractISUPParameter) coti).decode(parameterBody);
                this.setCallOfferingTreatmentIndicators(coti);
                break;
            case ConferenceTreatmentIndicators._PARAMETER_CODE:
                ConferenceTreatmentIndicators cti = parameterFactory.createConferenceTreatmentIndicators();
                ((AbstractISUPParameter) cti).decode(parameterBody);
                this.setConferenceTreatmentIndicators(cti);
                break;
            case SCFID._PARAMETER_CODE:
                SCFID scfid = parameterFactory.createSCFID();
                ((AbstractISUPParameter) scfid).decode(parameterBody);
                this.setSCFID(scfid);
                break;
            case UIDCapabilityIndicators._PARAMETER_CODE:
                UIDCapabilityIndicators uci = parameterFactory.createUIDCapabilityIndicators();
                ((AbstractISUPParameter) uci).decode(parameterBody);
                this.setUIDCapabilityIndicators(uci);
                break;
            case EchoControlInformation._PARAMETER_CODE:
                EchoControlInformation eci = parameterFactory.createEchoControlInformation();
                ((AbstractISUPParameter) eci).decode(parameterBody);
                this.setEchoControlInformation(eci);
                break;
            case HopCounter._PARAMETER_CODE:
                HopCounter hc = parameterFactory.createHopCounter();
                ((AbstractISUPParameter) hc).decode(parameterBody);
                this.setHopCounter(hc);
                break;
            case CollectCallRequest._PARAMETER_CODE:
                CollectCallRequest ccr = parameterFactory.createCollectCallRequest();
                ((AbstractISUPParameter) ccr).decode(parameterBody);
                this.setCollectCallRequest(ccr);
                break;
            case ApplicationTransport._PARAMETER_CODE:
                ApplicationTransport atr = parameterFactory.createApplicationTransport();
                ((AbstractISUPParameter) atr).decode(parameterBody);
                this.setApplicationTransport(atr);
                break;
            case PivotCapability._PARAMETER_CODE:
                PivotCapability pc = parameterFactory.createPivotCapability();
                ((AbstractISUPParameter) pc).decode(parameterBody);
                this.setPivotCapability(pc);
                break;
            case CalledDirectoryNumber._PARAMETER_CODE:
                CalledDirectoryNumber cdn = parameterFactory.createCalledDirectoryNumber();
                ((AbstractISUPParameter) cdn).decode(parameterBody);
                this.setCalledDirectoryNumber(cdn);
                break;
            case OriginalCalledINNumber._PARAMETER_CODE:
                OriginalCalledINNumber ocin = parameterFactory.createOriginalCalledINNumber();
                ((AbstractISUPParameter) ocin).decode(parameterBody);
                this.setOriginalCalledINNumber(ocin);
                break;
            case NetworkRoutingNumber._PARAMETER_CODE:
                NetworkRoutingNumber nrn = parameterFactory.createNetworkRoutingNumber();
                ((AbstractISUPParameter) nrn).decode(parameterBody);
                this.setNetworkRoutingNumber(nrn);
                break;
            case QueryOnReleaseCapability._PARAMETER_CODE:
                QueryOnReleaseCapability qorc = parameterFactory.createQueryOnReleaseCapability();
                ((AbstractISUPParameter) qorc).decode(parameterBody);
                this.setQueryOnReleaseCapability(qorc);
                break;
            case PivotCounter._PARAMETER_CODE:
                PivotCounter pcntr = parameterFactory.createPivotCounter();
                ((AbstractISUPParameter) pcntr).decode(parameterBody);
                this.setPivotCounter(pcntr);
                break;
            case PivotRoutingForwardInformation._PARAMETER_CODE:
                PivotRoutingForwardInformation prfi = parameterFactory.createPivotRoutingForwardInformation();
                ((AbstractISUPParameter) prfi).decode(parameterBody);
                this.setPivotRoutingForwardInformation(prfi);
                break;
            case RedirectCapability._PARAMETER_CODE:
                RedirectCapability rc = parameterFactory.createRedirectCapability();
                ((AbstractISUPParameter) rc).decode(parameterBody);
                this.setRedirectCapability(rc);
                break;
            case RedirectCounter._PARAMETER_CODE:
                RedirectCounter rcntr = parameterFactory.createRedirectCounter();
                ((AbstractISUPParameter) rcntr).decode(parameterBody);
                this.setRedirectCounter(rcntr);
                break;
            case RedirectStatus._PARAMETER_CODE:
                RedirectStatus rs = parameterFactory.createRedirectStatus();
                ((AbstractISUPParameter) rs).decode(parameterBody);
                this.setRedirectStatus(rs);
                break;
            case RedirectForwardInformation._PARAMETER_CODE:
                RedirectForwardInformation rfi = parameterFactory.createRedirectForwardInformation();
                ((AbstractISUPParameter) rfi).decode(parameterBody);
                this.setRedirectForwardInformation(rfi);
                break;
            default:
                throw new ParameterException("Unrecognized parameter code for optional part: " + parameterCode);
        }
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
     * @see org.mobicents.isup.messages.ISUPMessage#getMessageType()
     */

    public MessageType getMessageType() {
        return this._MESSAGE_TYPE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.ISUPMessage#hasAllMandatoryParameters()
     */

    public boolean hasAllMandatoryParameters() {
        if (!super.f_Parameters.keySet().containsAll(mandatoryParam) || super.f_Parameters.values().contains(null)) {
            return false;
        }
        if (!super.v_Parameters.containsKey(_INDEX_V_CalledPartyNumber)
                || super.v_Parameters.get(_INDEX_V_CalledPartyNumber) == null) {
            return false;
        }
        return true;
    }

    public NatureOfConnectionIndicators getNatureOfConnectionIndicators() {
        return (NatureOfConnectionIndicators) super.f_Parameters.get(_INDEX_F_NatureOfConnectionIndicators);
    }

    public void setNatureOfConnectionIndicators(NatureOfConnectionIndicators v) {
        super.f_Parameters.put(_INDEX_F_NatureOfConnectionIndicators, v);
    }

    public ForwardCallIndicators getForwardCallIndicators() {
        return (ForwardCallIndicators) super.f_Parameters.get(_INDEX_F_ForwardCallIndicators);
    }

    public void setForwardCallIndicators(ForwardCallIndicators v) {
        super.f_Parameters.put(_INDEX_F_ForwardCallIndicators, v);
    }

    public CallingPartyCategory getCallingPartCategory() {
        return (CallingPartyCategory) super.f_Parameters.get(_INDEX_F_CallingPartyCategory);
    }

    public void setCallingPartCategory(CallingPartyCategory v) {
        super.f_Parameters.put(_INDEX_F_CallingPartyCategory, v);
    }

    public TransmissionMediumRequirement getTransmissionMediumRequirement() {
        return (TransmissionMediumRequirement) super.f_Parameters.get(_INDEX_F_TransmissionMediumRequirement);
    }

    public void setTransmissionMediumRequirement(TransmissionMediumRequirement v) {
        super.f_Parameters.put(_INDEX_F_TransmissionMediumRequirement, v);
    }

    public CalledPartyNumber getCalledPartyNumber() {
        return (CalledPartyNumber) super.v_Parameters.get(_INDEX_V_CalledPartyNumber);
    }

    public void setCalledPartyNumber(CalledPartyNumber v) {
        super.v_Parameters.put(_INDEX_V_CalledPartyNumber, v);
    }

    public TransitNetworkSelection getTransitNetworkSelection() {
        return (TransitNetworkSelection) super.o_Parameters.get(_INDEX_O_TransitNetworkSelection);
    }

    public void setTransitNetworkSelection(TransitNetworkSelection v) {
        super.o_Parameters.put(_INDEX_O_TransitNetworkSelection, v);
    }

    public CallReference getCallReference() {
        return (CallReference) super.o_Parameters.get(_INDEX_O_CallReference);
    }

    public void setCallReference(CallReference v) {
        super.o_Parameters.put(_INDEX_O_CallReference, v);
    }

    public CallingPartyNumber getCallingPartyNumber() {
        return (CallingPartyNumber) super.o_Parameters.get(_INDEX_O_CallingPartyNumber);
    }

    public void setCallingPartyNumber(CallingPartyNumber v) {
        super.o_Parameters.put(_INDEX_O_CallingPartyNumber, v);
    }

    public OptionalForwardCallIndicators getOptForwardCallIndicators() {
        return (OptionalForwardCallIndicators) super.o_Parameters.get(_INDEX_O_OptionalForwardCallIndicators);
    }

    public void setOptForwardCallIndicators(OptionalForwardCallIndicators v) {
        super.o_Parameters.put(_INDEX_O_OptionalForwardCallIndicators, v);
    }

    public RedirectingNumber getRedirectingNumber() {
        return (RedirectingNumber) super.o_Parameters.get(_INDEX_O_RedirectingNumber);
    }

    public void setRedirectingNumber(RedirectingNumber v) {
        super.o_Parameters.put(_INDEX_O_RedirectingNumber, v);
    }

    public RedirectionInformation getRedirectionInformation() {
        return (RedirectionInformation) super.o_Parameters.get(_INDEX_O_RedirectionInformation);
    }

    public void setRedirectionInformation(RedirectionInformation v) {
        super.o_Parameters.put(_INDEX_O_RedirectionInformation, v);
    }

    public ClosedUserGroupInterlockCode getCUserGroupInterlockCode() {
        return (ClosedUserGroupInterlockCode) super.o_Parameters.get(_INDEX_O_ClosedUserGroupInterlockCode);
    }

    public void setCUserGroupInterlockCode(ClosedUserGroupInterlockCode v) {
        super.o_Parameters.put(_INDEX_O_ClosedUserGroupInterlockCode, v);
    }

    public ConnectionRequest getConnectionRequest() {
        return (ConnectionRequest) super.o_Parameters.get(_INDEX_O_ConnectionRequest);
    }

    public void setConnectionRequest(ConnectionRequest v) {
        super.o_Parameters.put(_INDEX_O_ConnectionRequest, v);
    }

    public OriginalCalledNumber getOriginalCalledNumber() {
        return (OriginalCalledNumber) super.o_Parameters.get(_INDEX_O_OriginalCalledNumber);
    }

    public void setOriginalCalledNumber(OriginalCalledNumber v) {
        super.o_Parameters.put(_INDEX_O_OriginalCalledNumber, v);
    }

    public UserToUserInformation getU2UInformation() {
        return (UserToUserInformation) super.o_Parameters.get(_INDEX_O_UserToUserInformation);
    }

    public void setU2UInformation(UserToUserInformation v) {
        super.o_Parameters.put(_INDEX_O_UserToUserInformation, v);
    }

    public UserServiceInformation getUserServiceInformation() {
        return (UserServiceInformation) super.o_Parameters.get(_INDEX_O_UserServiceInformation);
    }

    public void setUserServiceInformation(UserServiceInformation v) {
        super.o_Parameters.put(_INDEX_O_UserServiceInformation, v);
    }

    public NetworkSpecificFacility getNetworkSpecificFacility() {
        return (NetworkSpecificFacility) super.o_Parameters.get(_INDEX_O_NetworkSPecificFacility);
    }

    public void setNetworkSpecificFacility(NetworkSpecificFacility v) {
        super.o_Parameters.put(_INDEX_O_NetworkSPecificFacility, v);
    }

    public GenericDigits getGenericDigits() {
        return (GenericDigits) super.o_Parameters.get(_INDEX_O_GenericDigits);
    }

    public void setGenericDigits(GenericDigits v) {
        super.o_Parameters.put(_INDEX_O_GenericDigits, v);
    }

    public OriginatingISCPointCode getOriginatingISCPointCode() {
        return (OriginatingISCPointCode) super.o_Parameters.get(_INDEX_O_OriginatingISCPointCode);
    }

    public void setOriginatingISCPointCode(OriginatingISCPointCode v) {
        super.o_Parameters.put(_INDEX_O_OriginatingISCPointCode, v);
    }

    public UserTeleserviceInformation getUserTeleserviceInformation() {
        return (UserTeleserviceInformation) super.o_Parameters.get(_INDEX_O_UserTeleserviceInformation);
    }

    public void setUserTeleserviceInformation(UserTeleserviceInformation v) {
        super.o_Parameters.put(_INDEX_O_UserTeleserviceInformation, v);
    }

    public RemoteOperations getRemoteOperations() {
        return (RemoteOperations) super.o_Parameters.get(_INDEX_O_RemoteOperations);
    }

    public void setRemoteOperations(RemoteOperations v) {
        super.o_Parameters.put(_INDEX_O_RemoteOperations, v);
    }

    public ParameterCompatibilityInformation getParameterCompatibilityInformation() {
        return (ParameterCompatibilityInformation) super.o_Parameters.get(_INDEX_O_ParameterCompatibilityInformation);
    }

    public void setParameterCompatibilityInformation(ParameterCompatibilityInformation v) {
        super.o_Parameters.put(_INDEX_O_ParameterCompatibilityInformation, v);
    }

    public GenericNotificationIndicator getGenericNotificationIndicator() {
        return (GenericNotificationIndicator) super.o_Parameters.get(_INDEX_O_GenericNotificationIndicator);
    }

    public void setGenericNotificationIndicator(GenericNotificationIndicator v) {
        super.o_Parameters.put(_INDEX_O_GenericNotificationIndicator, v);
    }

    public ServiceActivation getServiceActivation() {
        return (ServiceActivation) super.o_Parameters.get(_INDEX_O_ServiceActivation);
    }

    public void setServiceActivation(ServiceActivation v) {
        super.o_Parameters.put(_INDEX_O_ServiceActivation, v);
    }

    public GenericReference getGenericReference() {
        return (GenericReference) super.o_Parameters.get(_INDEX_O_GenericReference);
    }

    public void setGenericReference(GenericReference v) {
        super.o_Parameters.put(_INDEX_O_GenericReference, v);
    }

    public MLPPPrecedence getMLPPPrecedence() {
        return (MLPPPrecedence) super.o_Parameters.get(_INDEX_O_MLPPPrecedence);
    }

    public void setMLPPPrecedence(MLPPPrecedence v) {
        super.o_Parameters.put(_INDEX_O_MLPPPrecedence, v);
    }

    public TransimissionMediumRequierementPrime getTransimissionMediumReqPrime() {
        return (TransimissionMediumRequierementPrime) super.o_Parameters.get(_INDEX_O_TransimissionMediumRequierementPrime);
    }

    public void setTransimissionMediumReqPrime(TransimissionMediumRequierementPrime v) {
        super.o_Parameters.put(_INDEX_O_TransimissionMediumRequierementPrime, v);
    }

    public LocationNumber getLocationNumber() {
        return (LocationNumber) super.o_Parameters.get(_INDEX_O_LocationNumber);
    }

    public void setLocationNumber(LocationNumber v) {
        super.o_Parameters.put(_INDEX_O_LocationNumber, v);
    }

    public ForwardGVNS getForwardGVNS() {
        return (ForwardGVNS) super.o_Parameters.get(_INDEX_O_ForwardGVNS);
    }

    public void setForwardGVNS(ForwardGVNS v) {
        super.o_Parameters.put(_INDEX_O_ForwardGVNS, v);
    }

    public CCSS getCCSS() {
        return (CCSS) super.o_Parameters.get(_INDEX_O_CCSS);
    }

    public void setCCSS(CCSS v) {
        super.o_Parameters.put(_INDEX_O_CCSS, v);
    }

    public NetworkManagementControls getNetworkManagementControls() {
        return (NetworkManagementControls) super.o_Parameters.get(_INDEX_O_NetworkManagementControls);
    }

    public void setNetworkManagementControls(NetworkManagementControls v) {
        super.o_Parameters.put(_INDEX_O_NetworkManagementControls, v);
    }

    /**
     * @param usip
     */
    public void setUserServiceInformationPrime(UserServiceInformationPrime v) {
        super.o_Parameters.put(_INDEX_O_UserServiceInformationPrime, v);
    }

    public UserServiceInformationPrime getUserServiceInformationPrime() {
        return (UserServiceInformationPrime) super.o_Parameters.get(_INDEX_O_UserServiceInformationPrime);
    }

    /**
     * @param pdc
     */
    public void setPropagationDelayCounter(PropagationDelayCounter v) {
        super.o_Parameters.put(_INDEX_O_PropagationDelayCounter, v);

    }

    public PropagationDelayCounter getPropagationDelayCounter() {
        return (PropagationDelayCounter) super.o_Parameters.get(_INDEX_O_PropagationDelayCounter);
    }

    /**
     * @param gn
     */
    public void setGenericNumber(GenericNumber v) {
        super.o_Parameters.put(_INDEX_O_GenericNumber, v);

    }

    public GenericNumber getGenericNumber() {
        return (GenericNumber) super.o_Parameters.get(_INDEX_O_GenericNumber);
    }

    /**
     * @param utui
     */
    public void setU2UIndicators(UserToUserIndicators v) {
        super.o_Parameters.put(_INDEX_O_User2UIndicators, v);

    }

    public UserToUserIndicators getU2UIndicators() {
        return (UserToUserIndicators) super.o_Parameters.get(_INDEX_O_User2UIndicators);
    }

    /**
     * @param at
     */
    public void setAccessTransport(AccessTransport v) {
        super.o_Parameters.put(_INDEX_O_AccessTransport, v);

    }

    public AccessTransport getAccessTransport() {
        return (AccessTransport) super.o_Parameters.get(_INDEX_O_AccessTransport);
    }

    @Override
    public void setCircuitAssigmentMap(CircuitAssigmentMap v) {
        super.o_Parameters.put(_INDEX_O_CircuitAssigmentMap, v);
    }

    @Override
    public CircuitAssigmentMap getCircuitAssigmentMap() {
        return (CircuitAssigmentMap) super.o_Parameters.get(_INDEX_O_CircuitAssigmentMap);
    }

    @Override
    public void setCorrelationID(CorrelationID v) {
        super.o_Parameters.put(_INDEX_O_CorrelationID, v);
    }

    @Override
    public CorrelationID getCorrelationID() {
        return (CorrelationID) super.o_Parameters.get(_INDEX_O_CorrelationID);
    }

    @Override
    public void setCallDiversionTreatmentIndicators(CallDiversionTreatmentIndicators v) {
        super.o_Parameters.put(_INDEX_O_CallDiversionTreatmentIndicators, v);
    }

    @Override
    public CallDiversionTreatmentIndicators getCallDiversionTreatmentIndicators() {
        return (CallDiversionTreatmentIndicators) super.o_Parameters.get(_INDEX_O_CallDiversionTreatmentIndicators);
    }

    @Override
    public void setCalledINNumber(CalledINNumber v) {
        super.o_Parameters.put(_INDEX_O_CalledINNumber, v);
    }

    @Override
    public CalledINNumber getCalledINNumber() {
        return (CalledINNumber) super.o_Parameters.get(_INDEX_O_CalledINNumber);
    }

    @Override
    public void setCallOfferingTreatmentIndicators(CallOfferingTreatmentIndicators v) {
        super.o_Parameters.put(_INDEX_O_CallOfferingTreatmentIndicators, v);
    }

    @Override
    public CallOfferingTreatmentIndicators getCallOfferingTreatmentIndicators() {
        return (CallOfferingTreatmentIndicators) super.o_Parameters.get(_INDEX_O_CallOfferingTreatmentIndicators);
    }

    @Override
    public void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators v) {
        super.o_Parameters.put(_INDEX_O_ConferenceTreatmentIndicators, v);
    }

    @Override
    public ConferenceTreatmentIndicators getConferenceTreatmentIndicators() {
        return (ConferenceTreatmentIndicators) super.o_Parameters.get(_INDEX_O_ConferenceTreatmentIndicators);
    }

    @Override
    public void setSCFID(SCFID v) {
        super.o_Parameters.put(_INDEX_O_SCFID, v);
    }

    @Override
    public SCFID getSCFID() {
        return (SCFID) super.o_Parameters.get(_INDEX_O_SCFID);
    }

    @Override
    public void setUIDCapabilityIndicators(UIDCapabilityIndicators v) {
        super.o_Parameters.put(_INDEX_O_UIDCapabilityIndicators, v);
    }

    @Override
    public UIDCapabilityIndicators getUIDCapabilityIndicators() {
        return (UIDCapabilityIndicators) super.o_Parameters.get(_INDEX_O_UIDCapabilityIndicators);
    }

    @Override
    public void setEchoControlInformation(EchoControlInformation v) {
        super.o_Parameters.put(_INDEX_O_EchoControlInformation, v);
    }

    @Override
    public EchoControlInformation getEchoControlInformation() {
        return (EchoControlInformation) super.o_Parameters.get(_INDEX_O_EchoControlInformation);
    }

    @Override
    public void setHopCounter(HopCounter v) {
        super.o_Parameters.put(_INDEX_O_HopCounter, v);
    }

    @Override
    public HopCounter getHopCounter() {
        return (HopCounter) super.o_Parameters.get(_INDEX_O_HopCounter);
    }

    @Override
    public void setCollectCallRequest(CollectCallRequest v) {
        super.o_Parameters.put(_INDEX_O_CollectCallRequest, v);
    }

    @Override
    public CollectCallRequest getCollectCallRequest() {
        return (CollectCallRequest) super.o_Parameters.get(_INDEX_O_CollectCallRequest);
    }

    @Override
    public void setApplicationTransport(ApplicationTransport v) {
        super.o_Parameters.put(_INDEX_O_ApplicationTransport, v);
    }

    @Override
    public ApplicationTransport getApplicationTransport() {
        return (ApplicationTransport) super.o_Parameters.get(_INDEX_O_ApplicationTransport);
    }

    @Override
    public void setPivotCapability(PivotCapability v) {
        super.o_Parameters.put(_INDEX_O_PivotCapability, v);
    }

    @Override
    public PivotCapability getPivotCapability() {
        return (PivotCapability) super.o_Parameters.get(_INDEX_O_PivotCapability);
    }

    @Override
    public void setCalledDirectoryNumber(CalledDirectoryNumber v) {
        super.o_Parameters.put(_INDEX_O_CalledDirectoryNumber, v);
    }

    @Override
    public CalledDirectoryNumber getCalledDirectoryNumber() {
        return (CalledDirectoryNumber) super.o_Parameters.get(_INDEX_O_CalledDirectoryNumber);
    }

    @Override
    public void setOriginalCalledINNumber(OriginalCalledINNumber v) {
        super.o_Parameters.put(_INDEX_O_OriginalCalledINNumber, v);
    }

    @Override
    public OriginalCalledINNumber getOriginalCalledINNumber() {
        return (OriginalCalledINNumber) super.o_Parameters.get(_INDEX_O_OriginalCalledINNumber);
    }

    @Override
    public void setNetworkRoutingNumber(NetworkRoutingNumber v) {
        super.o_Parameters.put(_INDEX_O_NetworkRoutingNumber, v);
    }

    @Override
    public NetworkRoutingNumber getNetworkRoutingNumber() {
        return (NetworkRoutingNumber) super.o_Parameters.get(_INDEX_O_NetworkRoutingNumber);
    }

    @Override
    public void setQueryOnReleaseCapability(QueryOnReleaseCapability v) {
        super.o_Parameters.put(_INDEX_O_QueryOnReleaseCapability, v);
    }

    @Override
    public QueryOnReleaseCapability getQueryOnReleaseCapability() {
        return (QueryOnReleaseCapability) super.o_Parameters.get(_INDEX_O_QueryOnReleaseCapability);
    }

    @Override
    public void setPivotCounter(PivotCounter v) {
        super.o_Parameters.put(_INDEX_O_PivotCounter, v);
    }

    @Override
    public PivotCounter getPivotCounter() {
        return (PivotCounter) super.o_Parameters.get(_INDEX_O_PivotCounter);
    }

    @Override
    public void setPivotRoutingForwardInformation(PivotRoutingForwardInformation v) {
        super.o_Parameters.put(_INDEX_O_PivotRoutingForwardInformation, v);
    }

    @Override
    public PivotRoutingForwardInformation getPivotRoutingForwardInformation() {
        return (PivotRoutingForwardInformation) super.o_Parameters.get(_INDEX_O_PivotRoutingForwardInformation);
    }

    @Override
    public void setRedirectCapability(RedirectCapability v) {
        super.o_Parameters.put(_INDEX_O_RedirectCapability, v);
    }

    @Override
    public RedirectCapability getRedirectCapability() {
        return (RedirectCapability) super.o_Parameters.get(_INDEX_O_RedirectCapability);
    }

    @Override
    public void setRedirectCounter(RedirectCounter v) {
        super.o_Parameters.put(_INDEX_O_RedirectCounter, v);
    }

    @Override
    public RedirectCounter getRedirectCounter() {
        return (RedirectCounter) super.o_Parameters.get(_INDEX_O_RedirectCounter);
    }

    @Override
    public void setRedirectStatus(RedirectStatus v) {
        super.o_Parameters.put(_INDEX_O_RedirectStatus, v);
    }

    @Override
    public RedirectStatus getRedirectStatus() {
        return (RedirectStatus) super.o_Parameters.get(_INDEX_O_RedirectStatus);
    }

    @Override
    public void setRedirectForwardInformation(RedirectForwardInformation v) {
        super.o_Parameters.put(_INDEX_O_RedirectForwardInformation, v);
    }

    @Override
    public RedirectForwardInformation getRedirectForwardInformation() {
        return (RedirectForwardInformation) super.o_Parameters.get(_INDEX_O_RedirectForwardInformation);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.impl.ISUPMessageImpl# mandatoryVariablePartPossible()
     */

    protected boolean mandatoryVariablePartPossible() {

        return this.getNumberOfMandatoryVariableLengthParameters() != 0;
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
