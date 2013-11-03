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

import org.mobicents.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table23" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * <TABLE id="Table52" style="WIDTH: 575px; COLOR: navy; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * Circuit Group Blocking (CGB)</TD>
 * </TR>
 * <TR>
 *
 * <TD style="FONT-SIZE: 9pt; BORDER-BOTTOM: silver thin solid" colSpan="3">
 * <FONT face="Times New Roman" size="3">Circuit Group Blocking (CGB)&nbsp;message sent to the node to permit the switching
 * equipment or maintenance system to remove from (and return to) traffic a group of circuits/CICs. A node receiving a
 * Circuit/CIC Group Blocking message must be able to accept incoming calls on the group of blocked circuits/CICs unless it has
 * also sent a Circuit/CIC Group Blocking message. </FONT></TD>
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
 * <TD style="WIDTH: 283px; HEIGHT: 18px; TEXT-ALIGN: left">Circuit group supervision message&nbsp;type indicators&nbsp;</TD>
 *
 * <TD style="WIDTH: 145px; HEIGHT: 18px">F</TD>
 * <TD style="HEIGHT: 18px">1</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Range and Status</TD>
 * <TD style="WIDTH: 145px">V</TD>
 * <TD>3-34</TD>
 *
 * </TR>
 * </TABLE>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CircuitGroupBlockingMessage extends ISUPMessage {
    /**
     * Circuit Group Blocking Message, Q.763 reference table 40 <br>
     * {@link CircuitGroupBlockingMessage}
     */
    int MESSAGE_CODE = 0x18;

    void setSupervisionType(CircuitGroupSuperVisionMessageType ras);

    CircuitGroupSuperVisionMessageType getSupervisionType();

    void setRangeAndStatus(RangeAndStatus ras);

    RangeAndStatus getRangeAndStatus();

}
