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

import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table28" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * <TABLE id="Table57" style="WIDTH: 575px; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * Facility&nbsp;(FAC) Message</TD>
 *
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; COLOR: navy" colSpan="3">
 * <FONT face="Times New Roman" size="3">
 * <P class="MsoNormal" style="MARGIN-LEFT: 0cm; TEXT-INDENT: 0cm; TEXT-ALIGN: justify; mso-list: l20 level1 lfo13" * >
 * <SPAN lang="EN-GB">Facility (FAC)&nbsp;message sent in either direction at any phase of the call to request an action at
 * another node. The message is also used to carry the results, error or rejection of a previously requested action. </SPAN>
 * </P>
 * </FONT></TD>
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
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Service Activation</TD>
 *
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>3-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Remote Operations</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>?</TD>
 *
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Message&nbsp;Compatibility Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4</TD>
 * </TR>
 * <TR>
 *
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Parameter Compatibility Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 *
 * <TD>1</TD>
 * </TR>
 * </TABLE>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface FacilityMessage extends ISUPMessage {
    /**
     * Facility Message, Q.763 reference table 45 <br>
     * {@link FacilityMessage}
     */
    int MESSAGE_CODE = 0x33;

    void setMessageCompatibilityInformation(MessageCompatibilityInformation mci);

    MessageCompatibilityInformation getMessageCompatibilityInformation();

    void setParameterCompatibilityInformation(ParameterCompatibilityInformation pci);

    ParameterCompatibilityInformation getParameterCompatibilityInformation();

    void setRemoteOperations(RemoteOperations ro);

    RemoteOperations getRemoteOperations();

    void setServiceActivation(ServiceActivation sa);

    ServiceActivation getServiceActivation();

    void setCallTransferNumber(CallTransferNumber ctn);

    CallTransferNumber getCallTransferNumber();

    void setAccessTransport(AccessTransport at);

    AccessTransport getAccessTransport();

    void setGenericNotificationIndicator(GenericNotificationIndicator gni);

    GenericNotificationIndicator getGenericNotificationIndicator();

    void setRedirectionNumber(RedirectionNumber rn);

    RedirectionNumber getRedirectionNumber();

    void setPivotRoutingIndicators(PivotRoutingIndicators pri);

    PivotRoutingIndicators getPivotRoutingIndicators();

    void setPivotStatus(PivotStatus ps);

    PivotStatus getPivotStatus();

    void setPivotCounter(PivotCounter pc);

    PivotCounter getPivotCounter();

    void setPivotRoutingBackwardInformation(PivotRoutingBackwardInformation prbi);

    PivotRoutingBackwardInformation getPivotRoutingBackwardInformation();

    void setRedirectStatus(RedirectStatus rs);

    RedirectStatus getRedirectStatus();
}
