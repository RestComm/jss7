package org.mobicents.protocols.ss7.isup.message;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table27" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * 
 * <TABLE id="Table56" style="WIDTH: 575px; COLOR: navy; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * User Part Test (UPT)&nbsp;Message</TD>
 * </TR>
 * <TR>
 * <TD style="FONT-SIZE: 9pt; BORDER-BOTTOM: silver thin solid" colSpan="3">
 * <FONT face="Times New Roman" size="3"> <P class="MsoNormal" style="MARGIN-LEFT: 0cm; TEXT-INDENT: 0cm; TEXT-ALIGN: justify; mso-list: l20 level1 lfo13"
 * > <SPAN lang="EN-GB">User Part Test (UPT)&nbsp;message sent in either
 * direction to test the status of a user part marked as unavailable for a
 * signalingpoint. </SPAN> </P>
 * 
 * </FONT></TD>
 * </TR>
 * 
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
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Parameter Compatibility
 * Information</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * 
 * <TD>4-?</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>1</TD>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface UserPartTestMessage extends ISUPMessage {

}
