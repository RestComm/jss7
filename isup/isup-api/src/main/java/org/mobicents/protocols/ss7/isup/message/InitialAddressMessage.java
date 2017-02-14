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

package org.mobicents.protocols.ss7.isup.message;

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
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table15" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px" align="center" colSpan="3">
 * <TABLE id="Table37" style="WIDTH: 575px; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 *
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * IAM (Initial Address Message)</TD>
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; COLOR: navy" colSpan="3">
 * <P>
 * An Initial Address Message (IAM) is sent in the "forward" direction by each switch needed to complete the circuit between the
 * calling party and called party until the circuit connects to the destination switch. An IAM contains the called party number
 * in the mandatory variable part and may contain the calling party name and number in the optional part.
 * </P>
 * </TD>
 * </TR>
 * </TABLE>
 *
 * </TD>
 * </TR>
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 283px; HEIGHT: 30px; TEXT-ALIGN: center">
 * Parameter</TD>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 145px; HEIGHT: 30px">Type</TD>
 * <TD style="FONT-WEIGHT: bold; HEIGHT: 30px">Length (octet)</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Message type</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Nature of Connection Indicators</TD>
 *
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Forward&nbsp;Call Indicators</TD>
 * <TD style="WIDTH: 145px">F</TD>
 *
 * <TD>2</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Calling Party's Category</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; HEIGHT: 12px; TEXT-ALIGN: left">Transmission Medium Requirement</TD>
 * <TD style="WIDTH: 145px; HEIGHT: 12px">F</TD>
 * <TD style="HEIGHT: 12px">1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Called Party Number</TD>
 *
 * <TD style="WIDTH: 145px">V</TD>
 * <TD>4-11</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; HEIGHT: 18px; TEXT-ALIGN: left">Transit Network Selection</TD>
 * <TD style="WIDTH: 145px; HEIGHT: 18px">O</TD>
 * <TD style="HEIGHT: 18px">4-?</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Call Reference</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Calling Party Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-12</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Optional Forward Call Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirecting Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-12</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirection Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-4</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Closed User Group Interlock Code</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>6</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Connection Request</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7-9</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Origional Called Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-12</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; HEIGHT: 17px; TEXT-ALIGN: left">User to User Information</TD>
 * <TD style="WIDTH: 145px; HEIGHT: 17px">O</TD>
 * <TD style="HEIGHT: 17px">3-131</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Access Transport</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User Service Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-13</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User to User Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Number</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>5-13</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Propagation Delay Counter</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User Service Information Prime</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-13</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Network Specific Facility</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Digit</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Originating ISC Point Code</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User Service Information Prime</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Remote Operations</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Parameter Compatibility Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Notification</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Service Activation</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Reference</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>5-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">MLPP Precedence</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>8</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Transmission Medium Requirement Prime</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Location Number</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>5-12</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>1</TD>
 *
 * </TR>
 * </TABLE>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface InitialAddressMessage extends ISUPMessage {

    // FIXME: add CallingGeodeticLocation & NumberPortabilityForwardInformation
    /**
     * Initial Address Message, Q.763 reference table 32 <br>
     * {@link InitialAddressMessage}
     */
    int MESSAGE_CODE = 0x01;

    NatureOfConnectionIndicators getNatureOfConnectionIndicators();

    void setNatureOfConnectionIndicators(NatureOfConnectionIndicators v);

    ForwardCallIndicators getForwardCallIndicators();

    void setForwardCallIndicators(ForwardCallIndicators v);

    CallingPartyCategory getCallingPartCategory();

    void setCallingPartCategory(CallingPartyCategory v);

    TransmissionMediumRequirement getTransmissionMediumRequirement();

    void setTransmissionMediumRequirement(TransmissionMediumRequirement v);

    CalledPartyNumber getCalledPartyNumber();

    void setCalledPartyNumber(CalledPartyNumber v);

    TransitNetworkSelection getTransitNetworkSelection();

    void setTransitNetworkSelection(TransitNetworkSelection v);

    CallReference getCallReference();

    void setCallReference(CallReference v);

    CallingPartyNumber getCallingPartyNumber();

    void setCallingPartyNumber(CallingPartyNumber v);

    OptionalForwardCallIndicators getOptForwardCallIndicators();

    void setOptForwardCallIndicators(OptionalForwardCallIndicators v);

    RedirectingNumber getRedirectingNumber();

    void setRedirectingNumber(RedirectingNumber v);

    RedirectionInformation getRedirectionInformation();

    void setRedirectionInformation(RedirectionInformation v);

    ClosedUserGroupInterlockCode getCUserGroupInterlockCode();

    void setCUserGroupInterlockCode(ClosedUserGroupInterlockCode v);

    ConnectionRequest getConnectionRequest();

    void setConnectionRequest(ConnectionRequest v);

    OriginalCalledNumber getOriginalCalledNumber();

    void setOriginalCalledNumber(OriginalCalledNumber v);

    UserToUserInformation getU2UInformation();

    void setU2UInformation(UserToUserInformation v);

    UserServiceInformation getUserServiceInformation();

    void setUserServiceInformation(UserServiceInformation v);

    NetworkSpecificFacility getNetworkSpecificFacility();

    void setNetworkSpecificFacility(NetworkSpecificFacility v);

    GenericDigits getGenericDigits();

    void setGenericDigits(GenericDigits v);

    OriginatingISCPointCode getOriginatingISCPointCode();

    void setOriginatingISCPointCode(OriginatingISCPointCode v);

    UserTeleserviceInformation getUserTeleserviceInformation();

    void setUserTeleserviceInformation(UserTeleserviceInformation v);

    RemoteOperations getRemoteOperations();

    void setRemoteOperations(RemoteOperations v);

    ParameterCompatibilityInformation getParameterCompatibilityInformation();

    void setParameterCompatibilityInformation(ParameterCompatibilityInformation v);

    GenericNotificationIndicator getGenericNotificationIndicator();

    void setGenericNotificationIndicator(GenericNotificationIndicator v);

    ServiceActivation getServiceActivation();

    void setServiceActivation(ServiceActivation v);

    GenericReference getGenericReference();

    void setGenericReference(GenericReference v);

    MLPPPrecedence getMLPPPrecedence();

    void setMLPPPrecedence(MLPPPrecedence v);

    TransimissionMediumRequierementPrime getTransimissionMediumReqPrime();

    void setTransimissionMediumReqPrime(TransimissionMediumRequierementPrime v);

    LocationNumber getLocationNumber();

    void setLocationNumber(LocationNumber v);

    ForwardGVNS getForwardGVNS();

    void setForwardGVNS(ForwardGVNS v);

    CCSS getCCSS();

    void setCCSS(CCSS v);

    NetworkManagementControls getNetworkManagementControls();

    void setNetworkManagementControls(NetworkManagementControls v);

    /**
     * @param usip
     */
    void setUserServiceInformationPrime(UserServiceInformationPrime v);

    UserServiceInformationPrime getUserServiceInformationPrime();

    /**
     * @param pdc
     */
    void setPropagationDelayCounter(PropagationDelayCounter v);

    PropagationDelayCounter getPropagationDelayCounter();

    /**
     * @param gn
     */
    void setGenericNumber(GenericNumber v);

    GenericNumber getGenericNumber();

    /**
     * @param utui
     */
    void setU2UIndicators(UserToUserIndicators v);

    UserToUserIndicators getU2UIndicators();

    /**
     * @param at
     */
    void setAccessTransport(AccessTransport v);

    AccessTransport getAccessTransport();

    void setCircuitAssigmentMap(CircuitAssigmentMap v);

    CircuitAssigmentMap getCircuitAssigmentMap();

    void setCorrelationID(CorrelationID v);

    CorrelationID getCorrelationID();

    void setCallDiversionTreatmentIndicators(CallDiversionTreatmentIndicators v);

    CallDiversionTreatmentIndicators getCallDiversionTreatmentIndicators();

    void setCalledINNumber(CalledINNumber v);

    CalledINNumber getCalledINNumber();

    void setCallOfferingTreatmentIndicators(CallOfferingTreatmentIndicators v);

    CallOfferingTreatmentIndicators getCallOfferingTreatmentIndicators();

    void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators v);

    ConferenceTreatmentIndicators getConferenceTreatmentIndicators();

    void setSCFID(SCFID v);

    SCFID getSCFID();

    void setUIDCapabilityIndicators(UIDCapabilityIndicators v);

    UIDCapabilityIndicators getUIDCapabilityIndicators();

    void setEchoControlInformation(EchoControlInformation v);

    EchoControlInformation getEchoControlInformation();

    void setHopCounter(HopCounter v);

    HopCounter getHopCounter();

    void setCollectCallRequest(CollectCallRequest v);

    CollectCallRequest getCollectCallRequest();

    void setApplicationTransport(ApplicationTransport v);

    ApplicationTransport getApplicationTransport();

    void setPivotCapability(PivotCapability v);

    PivotCapability getPivotCapability();

    void setCalledDirectoryNumber(CalledDirectoryNumber v);

    CalledDirectoryNumber getCalledDirectoryNumber();

    void setOriginalCalledINNumber(OriginalCalledINNumber v);

    OriginalCalledINNumber getOriginalCalledINNumber();

    void setNetworkRoutingNumber(NetworkRoutingNumber v);

    NetworkRoutingNumber getNetworkRoutingNumber();

    void setQueryOnReleaseCapability(QueryOnReleaseCapability v);

    QueryOnReleaseCapability getQueryOnReleaseCapability();

    void setPivotCounter(PivotCounter v);

    PivotCounter getPivotCounter();

    void setPivotRoutingForwardInformation(PivotRoutingForwardInformation v);

    PivotRoutingForwardInformation getPivotRoutingForwardInformation();

    void setRedirectCapability(RedirectCapability v);

    RedirectCapability getRedirectCapability();

    void setRedirectCounter(RedirectCounter v);

    RedirectCounter getRedirectCounter();

    void setRedirectStatus(RedirectStatus v);

    RedirectStatus getRedirectStatus();

    void setRedirectForwardInformation(RedirectForwardInformation v);

    RedirectForwardInformation getRedirectForwardInformation();
}
