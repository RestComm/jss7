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

import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.Confidentiality;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortRequest;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class TCUserAbortRequestImpl extends DialogRequestImpl implements TCUserAbortRequest {

    // fields
    private ApplicationContext applicationContextName;
    private UserInformation userInformation;
    private SecurityContext securityContext;
    private Confidentiality confidentiality;
    private UserInformationElement userAbortInformation;

    TCUserAbortRequestImpl() {
        super(EventType.Abort);
    }

    // public External getAbortReason() {
    // return this.abortReason;
    // }

    public ApplicationContext getApplicationContextName() {
        return this.applicationContextName;
    }

    public UserInformation getUserInformation() {
        return this.userInformation;
    }

    // public void setAbortReason(External abortReason) {
    // this.abortReason = abortReason;
    // }

    public void setApplicationContextName(ApplicationContext acn) {
        this.applicationContextName = acn;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;

    }

    public void setReturnMessageOnError(boolean val) {
        returnMessageOnError = val;
    }

    public boolean getReturnMessageOnError() {
        return returnMessageOnError;
    }

    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    @Override
    public void setSecurityContext(SecurityContext val) {
        securityContext = val;
    }

    @Override
    public Confidentiality getConfidentiality() {
        return confidentiality;
    }

    @Override
    public void setConfidentiality(Confidentiality val) {
        confidentiality = val;
    }

    @Override
    public UserInformationElement getUserAbortInformation() {
        return userAbortInformation;
    }

    @Override
    public void setUserAbortInformation(UserInformationElement val) {
        userAbortInformation = val;
    }

}
