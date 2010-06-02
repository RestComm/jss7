package org.mobicents.protocols.ss7.isup.message;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table24" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * <TABLE id="Table53" style="WIDTH: 575px; COLOR: navy; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * 
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * Circuit Group Query (CQM) Message</TD>
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt" colSpan="3">
 * <FONT face="Times New Roman" size="3"> <SPAN lang="EN-GB" style="FONT-SIZE: 12pt; FONT-FAMILY: 'Times New Roman'; mso-bidi-font-size: 10.0pt; mso-fareast-font-family: 'Times New Roman'; mso-ansi-language: EN-GB; mso-fareast-language: EN-US; mso-bidi-language: AR-SA"
 * >Circuit Group Query (CQM)&nbsp;message sent on a routine or demand basis to
 * request the far-end node to give the state of all circuits/CICs in a
 * particular range. </SPAN> </FONT></TD>
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
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Range and Status</TD>
 * <TD style="WIDTH: 145px">V</TD>
 * <TD>2</TD>
 * </TR>
 * </TABLE>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CircuitGroupQueryMessage extends ISUPMessage {
	/**
	 * Circuit Group Query Message, Q.763 reference table 21 <br>
	 * {@link CircuitGroupQueryMessage}
	 */
	public static final int MESSAGE_CODE = 0x2A;

}
