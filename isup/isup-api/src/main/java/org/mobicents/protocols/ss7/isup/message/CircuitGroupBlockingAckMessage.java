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
 * Circuit Group Blocking Acknowledgmenet(CGBA)</TD>
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; BORDER-BOTTOM: silver thin solid" colSpan="3">
 * <FONT face="Times New Roman" size="3"> <FONT face="Times New Roman"
 * size="3">Circuit Group Blocking Acknowledgement&nbsp;(CGBA) </FONT> message
 * sent in response to a Circuit/CIC Group Blocking message to indicate that the
 * requested group of circuits/CICs has been blocked. </FONT></TD>
 * </TR>
 * 
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
 * <TD style="WIDTH: 283px; HEIGHT: 18px; TEXT-ALIGN: left">Circuit group
 * supervision message&nbsp;type indicators&nbsp;</TD>
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
public interface CircuitGroupBlockingAckMessage extends ISUPMessage {
	/**
	 * ircuit Group Blocking Ack Message, Q.763 reference table 40 <br>
	 * {@link CircuitGroupBlockingAckMessage}
	 */
	public static final int MESSAGE_CODE = 0x1A;

	public void setSupervisionType(CircuitGroupSuperVisionMessageType ras);

	public CircuitGroupSuperVisionMessageType getSupervisionType();

	public void setRangeAndStatus(RangeAndStatus ras);

	public RangeAndStatus getRangeAndStatus();
}
