package org.mobicents.protocols.ss7.isup.message;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table20" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * <TABLE id="Table49" style="WIDTH: 575px; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 * Forward Transfer&nbsp;&nbsp;Message (FOT)</TD>
 * </TR>
 * 
 * <TR>
 * <TD style="FONT-SIZE: 9pt; COLOR: navy" colSpan="3">
 * <FONT face="Times New Roman" size="3">A message sent in the forward direction
 * on semi automatic calls when the outgoing international exchange operator
 * wants the help of an operator at the incoming international exchange. The
 * message will normally serve to bring an assistance operator into the call if
 * the call is automatically set up at the exchange.</FONT></TD>
 * </TR>
 * </TABLE>
 * </TD>
 * </TR>
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 283px; HEIGHT: 30px; TEXT-ALIGN: center">
 * Parameter</TD>
 * 
 * <TD style="FONT-WEIGHT: bold; WIDTH: 145px; HEIGHT: 30px">Type</TD>
 * <TD style="FONT-WEIGHT: bold; HEIGHT: 30px">Length (octet)</TD>
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Message type</TD>
 * <TD style="WIDTH: 145px">F</TD>
 * <TD>1</TD>
 * 
 * </TR>
 * <TR>
 * <TD style="WIDTH: 283px; HEIGHT: 18px; TEXT-ALIGN: left">Call Reference</TD>
 * <TD style="WIDTH: 145px; HEIGHT: 18px">O</TD>
 * <TD style="HEIGHT: 18px">7</TD>
 * </TR>
 * <TR>
 * 
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">End of Optional Parameters</TD>
 * <TD style="WIDTH: 145px">O</TD>
 * <TD>1</TD>
 * </TR>
 * </TABLE>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ForwardTransferMessage extends ISUPMessage {

}
