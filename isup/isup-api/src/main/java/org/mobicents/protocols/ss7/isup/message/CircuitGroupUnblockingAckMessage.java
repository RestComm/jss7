package org.mobicents.protocols.ss7.isup.message;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table23" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * <TABLE id="Table52" style="WIDTH: 575px; COLOR: navy; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * Circuit Group Unblocking Ack (CGUA)</TD>
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt" colSpan="3">
 * <FONT face="Times New Roman" size="3">Circuit Group Unblocking
 * Acknowledgement (CGUA)</FONT> message sent in response to a Circuit/CIC Group
 * Unblocking message to indicate that the requested group of circuits/CICs has
 * been unblocked.</TD>
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
public interface CircuitGroupUnblockingAckMessage extends ISUPMessage {

	/**
	 * Circuit Group Unblocking Ack Message, Q.763 reference table 40 <br>
	 * {@link CircuitGroupUnblockingAckMessage}
	 */
	public static final int MESSAGE_CODE = 0x1B;
}
