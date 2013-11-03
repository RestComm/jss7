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
import org.mobicents.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.HTRInformation;
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
 * Start time:09:41:44 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table3" style="FONT-SIZE: 9pt; WIDTH: 584px; COLOR: black; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 *
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px" align="center" colSpan="3">
 * <TABLE id="Table33" style="WIDTH: 575px; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * ACM (Address Complete Message)</TD>
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; COLOR: navy" colSpan="3">
 * <P>
 *
 * An Address Complete Message (ACM) is sent in the "backward" direction to indicate that the remote end of a trunk circuit has
 * been reserved.
 * <P>
 * The originating switch responds to an ACM message by connecting the calling party's line to the trunk to complete the voice
 * circuit from the calling party to the called party. The terminating switch sends a ringing tone to the calling party's line.
 * <BR>
 * </P>
 * </TD>
 * </TR>
 * </TABLE>
 * </TD>
 * </TR>
 * <TR>
 *
 * <TD style="FONT-WEIGHT: bold; WIDTH: 239px; HEIGHT: 30px; TEXT-ALIGN: center">
 * Parameter</TD>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 145px; HEIGHT: 30px">Type</TD>
 * <TD style="FONT-WEIGHT: bold; HEIGHT: 30px">Length (octet)</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Message type</TD>
 * <TD style="WIDTH: 145px">F</TD>
 *
 * <TD>1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Backward Call Indicators</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>2</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Optional Backward Call Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Call Reference</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>7</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Cause Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">User to User Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">User to User Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-131</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Access Transport</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Generic Notification Indicator</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Transmission Medium Used</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Echo Control Information</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Access Delivery Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Redirection Number</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>5-12</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Parameter Compatibility Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Call Diversion Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Network Specific Facility</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Remote Operations</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Service Activation</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">Redirection Number Restriction</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 239px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>1</TD>
 * </TR>
 * </TABLE>
 *
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface AddressCompleteMessage extends ISUPMessage {

    /**
     * Address Complete Message, Q.763 reference table 21 <br>
     * {@link AddressCompleteMessage}
     */
    int MESSAGE_CODE = 0x06;

    void setBackwardCallIndicators(BackwardCallIndicators indicators);

    BackwardCallIndicators getBackwardCallIndicators();

    void setOptionalBackwardCallIndicators(OptionalBackwardCallIndicators value);

    OptionalBackwardCallIndicators getOptionalBackwardCallIndicators();

    void setCallReference(CallReference value);

    CallReference getCallReference();

    void setCauseIndicators(CauseIndicators value);

    CauseIndicators getCauseIndicators();

    void setUserToUserIndicators(UserToUserIndicators value);

    UserToUserIndicators getUserToUserIndicators();

    void setUserToUserInformation(UserToUserInformation value);

    UserToUserInformation getUserToUserInformation();

    void setAccessTransport(AccessTransport value);

    AccessTransport getAccessTransport();

    void setGenericNotificationIndicator(GenericNotificationIndicator value);

    GenericNotificationIndicator getGenericNotificationIndicator();

    void setTransmissionMediumUsed(TransmissionMediumUsed value);

    TransmissionMediumUsed getTransmissionMediumUsed();

    void setEchoControlInformation(EchoControlInformation value);

    EchoControlInformation getEchoControlInformation();

    void setAccessDeliveryInformation(AccessDeliveryInformation value);

    AccessDeliveryInformation getAccessDeliveryInformation();

    void setRedirectionNumber(RedirectionNumber value);

    RedirectionNumber getRedirectionNumber();

    void setParameterCompatibilityInformation(ParameterCompatibilityInformation value);

    ParameterCompatibilityInformation getParameterCompatibilityInformation();

    void setCallDiversionInformation(CallDiversionInformation value);

    CallDiversionInformation getCallDiversionInformation();

    void setNetworkSpecificFacility(NetworkSpecificFacility value);

    NetworkSpecificFacility getNetworkSpecificFacility();

    void setRemoteOperations(RemoteOperations value);

    RemoteOperations getRemoteOperations();

    void setServiceActivation(ServiceActivation value);

    RedirectionNumberRestriction getRedirectionNumberRestriction();

    void setRedirectionNumberRestriction(RedirectionNumberRestriction value);

    ServiceActivation getServiceActivation();

    void setConferenceTreatmentIndicators(ConferenceTreatmentIndicators value);

    ConferenceTreatmentIndicators getConferenceTreatmentIndicators();

    void setUIDActionIndicators(UIDActionIndicators value);

    UIDActionIndicators getUIDActionIndicators();

    void setApplicationTransportParameter(ApplicationTransport value);

    ApplicationTransport getApplicationTransportParameter();

    void setCCNRPossibleIndicator(CCNRPossibleIndicator value);

    CCNRPossibleIndicator getCCNRPossibleIndicator();

    void setHTRInformation(HTRInformation value);

    HTRInformation getHTRInformation();

    void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation value);

    PivotRoutingBackwardInformation getPivotRoutingBackwardInformation();

    void setRedirectStatus(RedirectStatus value);

    RedirectStatus getRedirectStatus();

}
