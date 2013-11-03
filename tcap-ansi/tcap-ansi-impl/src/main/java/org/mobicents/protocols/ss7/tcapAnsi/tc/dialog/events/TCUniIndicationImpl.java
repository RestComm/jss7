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

package org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.Confidentiality;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;

/**
 * @author baranowb
 *
 */
public class TCUniIndicationImpl extends DialogIndicationImpl implements TCUniIndication {
    // private Byte qos;
    private SccpAddress originatingAddress, destinationAddress;

    // fields
    private ApplicationContext applicationContextName;
    private UserInformation userInformation;
    private SecurityContext securityContext;
    private Confidentiality confidentiality;

    TCUniIndicationImpl() {
        super(EventType.Uni);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# getApplicationContextName()
     */
    public ApplicationContext getApplicationContextName() {
        return applicationContextName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# getDestinationAddress()
     */
    public SccpAddress getDestinationAddress() {

        return this.destinationAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# getOriginatingAddress()
     */
    public SccpAddress getOriginatingAddress() {

        return this.originatingAddress;
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
    // * getQOS()
    // */
    // public Byte getQOS() {
    //
    // return this.qos;
    // }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# getUserInformation()
     */
    public UserInformation getUserInformation() {

        return this.userInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# setApplicationContextName
     * (org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName)
     */
    public void setApplicationContextName(ApplicationContext acn) {
        this.applicationContextName = acn;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# setDestinationAddress
     * (org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
     */
    public void setDestinationAddress(SccpAddress dest) {
        this.destinationAddress = dest;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# setOriginatingAddress
     * (org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
     */
    public void setOriginatingAddress(SccpAddress dest) {
        this.originatingAddress = dest;

    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // * org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
    // * setQOS(java.lang.Byte)
    // */
    // public void setQOS(Byte b) throws IllegalArgumentException {
    // this.qos = b;
    //
    // }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
     * setUserInformation(org.mobicents.protocols.ss7.tcap.asn.UserInformation)
     */
    public void setUserInformation(UserInformation acn) {
        this.userInformation = acn;

    }

    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public void setSecurityContext(SecurityContext val) {
        securityContext = val;
    }

    @Override
    public Confidentiality getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(Confidentiality val) {
        confidentiality = val;
    }

}
