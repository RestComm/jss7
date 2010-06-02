package org.mobicents.protocols.ss7.isup.message;

/**
 * Start time:09:54:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * <TABLE id="Table22" style="FONT-SIZE: 9pt; WIDTH: 584px; HEIGHT: 72px; TEXT-ALIGN: center" cellSpacing="1" cellPadding="1" width="584" align="center" border="1">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; WIDTH: 328px; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" align="center" colSpan="3">
 * <TABLE id="Table51" style="WIDTH: 575px; COLOR: navy; HEIGHT: 49px" cellSpacing="1" cellPadding="1" width="575" border="0">
 * <TR>
 * <TD style="FONT-WEIGHT: bold; FONT-SIZE: 10pt; COLOR: teal; HEIGHT: 28px; TEXT-ALIGN: center" colSpan="3">
 *Blocking (BLO)
 * </TR>
 * 
 * <TR>
 * <TD style="FONT-SIZE: 9pt; BORDER-BOTTOM: silver thin solid" colSpan="3">
 * Blocking (BLO) message sent only for maintenance purposes to the exchange at
 * the other end of a circuit, to cause an engaged condition of that circuit for
 * subsequent calls outgoing from that exchange. When a circuit is used in the
 * bothway mode of operation, an exchange receiving the blocking message must be
 * capable of accepting incoming calls on the concerned circuit unless it has
 * also sent a blocking message. Under certain conditions, a blocking message is
 * also a proper response to a reset circuit message.</TD>
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
 * </TABLE>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface BlockingMessage extends ISUPMessage {
	/**
	 * Blocking Message, Q.763 reference table 39 <br> {@link BlockingMessage}
	 */
	public static final int MESSAGE_CODE = 0x13;
}
