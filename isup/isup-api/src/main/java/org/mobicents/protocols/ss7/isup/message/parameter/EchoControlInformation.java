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
    int _PARAMETER_CODE = 0x37;

    /**
     * See Q.763 3.19 Outgoing echo control device information indicator : no information
     */
    int _OUTGOING_ECHO_CDII_NOINFO = 0;

    /**
     * See Q.763 3.19 Outgoing echo control device information indicator : outgoing echo control device not included and not
     * available
     */
    int _OUTGOING_ECHO_CDII_NINA = 1;

    /**
     * See Q.763 3.19 Outgoing echo control device information indicator : outgoing echo control device included
     */
    int _OUTGOING_ECHO_CDII_INCLUDED = 2;

    /**
     * See Q.763 3.19 Outgoing echo control device information indicator : outgoing echo control device not included but
     * available
     */
    int _OUTGOING_ECHO_CDII_NIA = 3;

    /**
     * See Q.763 3.19 Incoming echo control device information indicator : no information
     */
    int _INCOMING_ECHO_CDII_NOINFO = 0;

    /**
     * See Q.763 3.19 Incoming echo control device information indicator : incoming echo control device not included and not
     * available
     */
    int _INCOMING_ECHO_CDII_NINA = 1;

    /**
     * See Q.763 3.19 Incoming echo control device information indicator : incoming echo control device included
     */
    int _INCOMING_ECHO_CDII_INCLUDED = 2;

    /**
     * See Q.763 3.19 Incoming echo control device information indicator : incoming echo control device not included but
     * available
     */
    int _INCOMING_ECHO_CDII_NIA = 3;

    /**
     * See Q.763 3.19 Incoming echo control device request indicator : no information
     */
    int _INCOMING_ECHO_CDRI_NOINFO = 0;

    /**
     * See Q.763 3.19 Incoming echo control device request indicator : incoming echo control device activation request
     */
    int _INCOMING_ECHO_CDRI_AR = 1;

    /**
     * See Q.763 3.19 Incoming echo control device request indicator : incoming echo control device deactivation request (Note
     * 2)
     */
    int _INCOMING_ECHO_CDRI_DR = 2;

    /**
     * See Q.763 3.19 Outgoing echo control device request indicator : no information
     */
    int _OUTGOING_ECHO_CDRI_NOINFO = 0;

    /**
     * See Q.763 3.19 Outgoing echo control device request indicator : outgoing echo control device activation request
     */
    int _OUTGOING_ECHO_CDRI_AR = 1;

    /**
     * See Q.763 3.19 Outgoing echo control device request indicator : outgoing echo control device deactivation request (Note
     * 1)
     */
    int _OUTGOING_ECHO_CDRI_DR = 2;

    int getOutgoingEchoControlDeviceInformationIndicator();

    void setOutgoingEchoControlDeviceInformationIndicator(int outgoingEchoControlDeviceInformationIndicator);

    int getIncomingEchoControlDeviceInformationIndicator();

    void setIncomingEchoControlDeviceInformationIndicator(int incomingEchoControlDeviceInformationIndicator);

    int getOutgoingEchoControlDeviceInformationRequestIndicator();

    void setOutgoingEchoControlDeviceInformationRequestIndicator(int outgoingEchoControlDeviceInformationRequestIndicator);

    int getIncomingEchoControlDeviceInformationRequestIndicator();

    void setIncomingEchoControlDeviceInformationRequestIndicator(int incomingEchoControlDeviceInformationRequestIndicator);

}
