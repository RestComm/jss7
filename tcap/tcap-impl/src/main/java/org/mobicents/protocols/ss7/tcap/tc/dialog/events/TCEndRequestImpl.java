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
 *
 */
package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class TCEndRequestImpl extends DialogRequestImpl implements TCEndRequest {

    private boolean returnMessageOnError;
    private TerminationType terminationType;

    // fields
    private ApplicationContextName applicationContextName;
    private UserInformation userInformation;

    TCEndRequestImpl() {
        super(EventType.End);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest# getApplicationContextName()
     */
    public ApplicationContextName getApplicationContextName() {
        return applicationContextName;
    }

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
    public void setApplicationContextName(ApplicationContextName acn) {
        this.applicationContextName = acn;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest#
     * setUserInformation(org.mobicents.protocols.ss7.tcap.asn.UserInformation)
     */
    public void setUserInformation(UserInformation acn) {
        this.userInformation = acn;

    }

    public TerminationType getTerminationType() {

        return this.terminationType;
    }

    public void setTermination(TerminationType t) {
        this.terminationType = t;

    }

    public void setReturnMessageOnError(boolean val) {
        returnMessageOnError = val;
    }

    public boolean getReturnMessageOnError() {
        return returnMessageOnError;
    }

}
