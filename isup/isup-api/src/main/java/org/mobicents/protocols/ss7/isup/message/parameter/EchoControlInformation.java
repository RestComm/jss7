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
 * Start time:12:44:34 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:44:34 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface EchoControlInformation extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x37;

	
	/**
	 * See Q.763 3.19 Outgoing echo control device information indicator : no
	 * information
	 */
	public static final int _OUTGOING_ECHO_CDII_NOINFO = 0;

	/**
	 * See Q.763 3.19 Outgoing echo control device information indicator :
	 * outgoing echo control device not included and not available
	 */
	public static final int _OUTGOING_ECHO_CDII_NINA = 1;

	/**
	 * See Q.763 3.19 Outgoing echo control device information indicator :
	 * outgoing echo control device included
	 */
	public static final int _OUTGOING_ECHO_CDII_INCLUDED = 2;

	/**
	 * See Q.763 3.19 Outgoing echo control device information indicator :
	 * outgoing echo control device not included but available
	 */
	public static final int _OUTGOING_ECHO_CDII_NIA = 3;

	/**
	 * See Q.763 3.19 Incoming echo control device information indicator : no
	 * information
	 */
	public static final int _INCOMING_ECHO_CDII_NOINFO = 0;

	/**
	 * See Q.763 3.19 Incoming echo control device information indicator :
	 * incoming echo control device not included and not available
	 */
	public static final int _INCOMING_ECHO_CDII_NINA = 1;

	/**
	 * See Q.763 3.19 Incoming echo control device information indicator :
	 * incoming echo control device included
	 */
	public static final int _INCOMING_ECHO_CDII_INCLUDED = 2;

	/**
	 * See Q.763 3.19 Incoming echo control device information indicator :
	 * incoming echo control device not included but available
	 */
	public static final int _INCOMING_ECHO_CDII_NIA = 3;

	/**
	 * See Q.763 3.19 Incoming echo control device request indicator : no
	 * information
	 */
	public static final int _INCOMING_ECHO_CDRI_NOINFO = 0;

	/**
	 * See Q.763 3.19 Incoming echo control device request indicator : incoming
	 * echo control device activation request
	 */
	public static final int _INCOMING_ECHO_CDRI_AR = 1;

	/**
	 * See Q.763 3.19 Incoming echo control device request indicator : incoming
	 * echo control device deactivation request (Note 2)
	 */
	public static final int _INCOMING_ECHO_CDRI_DR = 2;

	/**
	 * See Q.763 3.19 Outgoing echo control device request indicator : no
	 * information
	 */
	public static final int _OUTGOING_ECHO_CDRI_NOINFO = 0;

	/**
	 * See Q.763 3.19 Outgoing echo control device request indicator : outgoing
	 * echo control device activation request
	 */
	public static final int _OUTGOING_ECHO_CDRI_AR = 1;

	/**
	 * See Q.763 3.19 Outgoing echo control device request indicator : outgoing
	 * echo control device deactivation request (Note 1)
	 */
	public static final int _OUTGOING_ECHO_CDRI_DR = 2;
	
	
	public int getOutgoingEchoControlDeviceInformationIndicator();

	public void setOutgoingEchoControlDeviceInformationIndicator(int outgoingEchoControlDeviceInformationIndicator);

	public int getIncomingEchoControlDeviceInformationIndicator();

	public void setIncomingEchoControlDeviceInformationIndicator(int incomingEchoControlDeviceInformationIndicator);

	public int getOutgoingEchoControlDeviceInformationRequestIndicator();

	public void setOutgoingEchoControlDeviceInformationRequestIndicator(int outgoingEchoControlDeviceInformationRequestIndicator);

	public int getIncomingEchoControlDeviceInformationRequestIndicator();

	public void setIncomingEchoControlDeviceInformationRequestIndicator(int incomingEchoControlDeviceInformationRequestIndicator);

}
