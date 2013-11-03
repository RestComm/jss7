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

package org.mobicents.protocols.ss7.isup.message;

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
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table6" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * <TABLE id="Table38" style="WIDTH: 575px; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * Call Progress Message (CPG)</TD>
 *
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; COLOR: navy" colSpan="3">
 * <P>
 * CPG is sent in either direction during the set-up or active phase of the call, indicating that an event, which is of
 * significance, and should be relayed to the originating or terminating access, has occurred.
 * </P>
 * </TD>
 * </TR>
 * </TABLE>
 * </TD>
 *
 * </TR>
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 283px; HEIGHT: 30px; TEXT-ALIGN: center">
 * Parameter</TD>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 145px; HEIGHT: 30px">Type</TD>
 * <TD style="FONT-WEIGHT: bold; HEIGHT: 30px">Length (octet)</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Message type</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Event Information</TD>
 * <TD style="WIDTH: 145px">F</TD>
 *
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Cause Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Backward Call Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Optional Backward Call Indicators</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Call Reference</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User to User Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User to User Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-131</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Connected Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>4-12</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Access Transport</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Generic Notification Indicator</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Call Diversion Information</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Transmission Medium Used</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Access Delivery Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirection Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>5-12</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Parameter Compatibility Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Network Specific Facility</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Remote Operations</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Service Activation</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirection Number Restriction</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>1</TD>
 * </TR>
 * </TABLE>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallProgressMessage extends ISUPMessage {
    /**
     * Call Progres Message, Q.763 reference table 23 <br>
     * {@link CallProgressMessage}
     */
    int MESSAGE_CODE = 0x2C;

    void setRedirectStatus(RedirectStatus rS);

    void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation pRBI);

    void setCCNRPossibleIndicator(CCNRPossibleIndicator cCNR);

    void setApplicationTransportParameter(ApplicationTransport aTP);

    void setUIDActionIndicators(UIDActionIndicators uIDA);

    void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators cTI);

    void setCallHistoryInformation(CallHistoryInformation cHI);

    void setGenericNumber(GenericNumber gN);

    void setBackwardGVNS(BackwardGVNS bGVNS);

    void setConnectedNumber(ConnectedNumber cN);

    void setEchoControlInformation(EchoControlInformation eCI);

    void setCallTransferNumber(CallTransferNumber cTR);

    void setRedirectionNumberRestriction(RedirectionNumberRestriction rNR);

    void setServiceActivation(ServiceActivation sA);

    void setCallDiversionInformation(CallDiversionInformation cDI);

    void setParameterCompatibilityInformation(ParameterCompatibilityInformation pCI);

    void setAccessDeliveryInformation(AccessDeliveryInformation aDI);

    void setTransmissionMediumUsed(TransmissionMediumUsed tMU);

    void setRemoteOperations(RemoteOperations rO);

    void setNetworkSpecificFacility(NetworkSpecificFacility nSF);

    void setGenericNotificationIndicator(GenericNotificationIndicator gNI);

    void setUserToUserInformation(UserToUserInformation u2uii);

    void setRedirectionNumber(RedirectionNumber rN);

    void setUserToUserIndicators(UserToUserIndicators u2ui);

    void setAccessTransport(AccessTransport aT);

    void setOptionalBackwardCallIndicators(OptionalBackwardCallIndicators oBCI);

    void setBackwardCallIndicators(BackwardCallIndicators bCMI);

    void setCallReference(CallReference cR);

    void setCauseIndicators(CauseIndicators cI);

    RedirectStatus getRedirectStatus();

    PivotRoutingBackwardInformation getPivotRoutingBackwardInformation();

    CCNRPossibleIndicator getCCNRPossibleIndicator();

    ApplicationTransport getApplicationTransportParameter();

    UIDActionIndicators getUIDActionIndicators();

    ConferenceTreatmentIndicators getConferenceTreatmentIndicators();

    CallHistoryInformation getCallHistoryInformation();

    GenericNumber getGenericNumber();

    BackwardGVNS getBackwardGVNS();

    ConnectedNumber getConnectedNumber();

    EchoControlInformation getEchoControlInformation();

    CallTransferNumber getCallTransferNumber();

    RedirectionNumberRestriction getRedirectionNumberRestriction();

    ServiceActivation getServiceActivation();

    CallDiversionInformation getCallDiversionInformation();

    ParameterCompatibilityInformation getParameterCompatibilityInformation();

    AccessDeliveryInformation getAccessDeliveryInformation();

    TransmissionMediumUsed getTransmissionMediumUsed();

    RemoteOperations getRemoteOperations();

    NetworkSpecificFacility getNetworkSpecificFacility();

    GenericNotificationIndicator getGenericNotificationIndicator();

    UserToUserInformation getUserToUserInformation();

    RedirectionNumber getRedirectionNumber();

    UserToUserIndicators getUserToUserIndicators();

    AccessTransport getAccessTransport();

    OptionalBackwardCallIndicators getOptionalBackwardCallIndicators();

    BackwardCallIndicators getBackwardCallIndicators();

    CallReference getCallReference();

    CauseIndicators getCauseIndicators();

    void setEventInformation(EventInformation _ei);

    EventInformation getEventInformation();
}
