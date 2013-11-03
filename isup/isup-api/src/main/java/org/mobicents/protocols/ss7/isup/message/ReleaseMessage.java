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
 * Start time:10:06:29 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message;

import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.AutomaticCongestionLevel;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.DisplayInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.HTRInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.SignalingPointCode;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:10:06:29 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table16" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * <TABLE id="Table35" style="WIDTH: 575px; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 *
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * REL (Release Message)</TD>
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; COLOR: navy" colSpan="3">
 * <P>
 * A Release Message (REL) is sent in either direction indicating that the circuit is being released due to the <B>cause
 * indicator</B> specified. An REL is sent when either the calling or called party "hangs up" the call (cause = 16). An REL is
 * also sent in the backward direction if the called party line is busy (cause = 17).
 * </P>
 * </TD>
 *
 * </TR>
 * </TABLE>
 * </TD>
 * </TR>
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 283px; HEIGHT: 30px; TEXT-ALIGN: center">
 * Parameter</TD>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 145px; HEIGHT: 30px">Type</TD>
 * <TD style="FONT-WEIGHT: bold; HEIGHT: 30px">Length (octet)</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Message type</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Cause Indicators</TD>
 * <TD style="WIDTH: 145px">V</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirection Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>3-4</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; HEIGHT: 20px; TEXT-ALIGN: left">Redirection Number</TD>
 * <TD style="WIDTH: 145px; HEIGHT: 20px">O</TD>
 * <TD style="HEIGHT: 20px">5-12</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User to User Indicators</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">User to User Information</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-131</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Access Transport</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Access Delivery Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Parameter Compatibility Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Network Specific Facility</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">signalingPoint Code</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4</TD>
 * </TR>
 *
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Automatic Congestion Level</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Redirection Number Restriction</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>1</TD>
 *
 * </TR>
 * </TABLE>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ReleaseMessage extends ISUPMessage {

    /**
     * Release Message, Q.763 reference table 33 <br>
     * {@link ReleaseMessage}
     */
    int MESSAGE_CODE = 0x0C;

    CauseIndicators getCauseIndicators();

    void setCauseIndicators(CauseIndicators v);

    RedirectionInformation getRedirectionInformation();

    void setRedirectionInformation(RedirectionInformation v);

    RedirectionNumber getRedirectionNumber();

    void setRedirectionNumber(RedirectionNumber v);

    AccessTransport getAccessTransport();

    void setAccessTransport(AccessTransport v);

    SignalingPointCode getSignalingPointCode();

    void setSignalingPointCode(SignalingPointCode v);

    UserToUserInformation getU2UInformation();

    void setU2UInformation(UserToUserInformation v);

    AutomaticCongestionLevel getAutomaticCongestionLevel();

    void setAutomaticCongestionLevel(AutomaticCongestionLevel v);

    NetworkSpecificFacility getNetworkSpecificFacility();

    void setNetworkSpecificFacility(NetworkSpecificFacility v);

    AccessDeliveryInformation getAccessDeliveryInformation();

    void setAccessDeliveryInformation(AccessDeliveryInformation v);

    ParameterCompatibilityInformation getParameterCompatibilityInformation();

    void setParameterCompatibilityInformation(ParameterCompatibilityInformation v);

    UserToUserIndicators getU2UIndicators();

    void setU2UIndicators(UserToUserIndicators v);

    DisplayInformation getDisplayInformation();

    void setDisplayInformation(DisplayInformation v);

    RemoteOperations getRemoteOperations();

    void setRemoteOperations(RemoteOperations v);

    HTRInformation getHTRInformation();

    void setHTRInformation(HTRInformation v);

    RedirectCounter getRedirectCounter();

    void setRedirectCounter(RedirectCounter v);

    RedirectBackwardInformation getRedirectBackwardInformation();

    void setRedirectBackwardInformation(RedirectBackwardInformation v);
}
