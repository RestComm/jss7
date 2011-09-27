/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
 * A Release Message (REL) is sent in either direction indicating that the
 * circuit is being released due to the <B>cause indicator</B> specified. An REL
 * is sent when either the calling or called party "hangs up" the call (cause =
 * 16). An REL is also sent in the backward direction if the called party line
 * is busy (cause = 17).
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
 * <TD style="WIDTH: 283px; TEXT-ALIGN: left">Parameter Compatibility
 * Information</TD>
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
	 * Release Message, Q.763 reference table 33 <br> {@link ReleaseMessage}
	 */
	public static final int MESSAGE_CODE = 0x0C;
	
	public CauseIndicators getCauseIndicators();

	public void setCauseIndicators(CauseIndicators v);

	public RedirectionInformation getRedirectionInformation();

	public void setRedirectionInformation(RedirectionInformation v);

	public RedirectionNumber getRedirectionNumber();

	public void setRedirectionNumber(RedirectionNumber v);

	public AccessTransport getAccessTransport();

	public void setAccessTransport(AccessTransport v);

	public SignalingPointCode getSignalingPointCode();

	public void setSignalingPointCode(SignalingPointCode v);

	public UserToUserInformation getU2UInformation();

	public void setU2UInformation(UserToUserInformation v);

	public AutomaticCongestionLevel getAutomaticCongestionLevel();

	public void setAutomaticCongestionLevel(AutomaticCongestionLevel v);

	public NetworkSpecificFacility getNetworkSpecificFacility();

	public void setNetworkSpecificFacility(NetworkSpecificFacility v);

	public AccessDeliveryInformation getAccessDeliveryInformation();

	public void setAccessDeliveryInformation(AccessDeliveryInformation v);

	public ParameterCompatibilityInformation getParameterCompatibilityInformation();

	public void setParameterCompatibilityInformation(ParameterCompatibilityInformation v);

	public UserToUserIndicators getU2UIndicators();

	public void setU2UIndicators(UserToUserIndicators v);

	public DisplayInformation getDisplayInformation();

	public void setDisplayInformation(DisplayInformation v);

	public RemoteOperations getRemoteOperations();

	public void setRemoteOperations(RemoteOperations v);

	public HTRInformation getHTRInformation();

	public void setHTRInformation(HTRInformation v);

	public RedirectCounter getRedirectCounter();

	public void setRedirectCounter(RedirectCounter v);

	public RedirectBackwardInformation getRedirectBackwardInformation();

	public void setRedirectBackwardInformation(RedirectBackwardInformation v);
}
