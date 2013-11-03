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

package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class TCUserAbortRequestImpl extends DialogRequestImpl implements TCUserAbortRequest {

    private boolean returnMessageOnError;

    // fields
    private ApplicationContextName applicationContextName;
    private UserInformation userInformation;

    private DialogServiceUserType dialogServiceUserType;

    TCUserAbortRequestImpl() {
        super(EventType.UAbort);
    }

    // public External getAbortReason() {
    // return this.abortReason;
    // }

    public ApplicationContextName getApplicationContextName() {
        return this.applicationContextName;
    }

    public UserInformation getUserInformation() {
        return this.userInformation;
    }

    // public void setAbortReason(External abortReason) {
    // this.abortReason = abortReason;
    // }

    public void setApplicationContextName(ApplicationContextName acn) {
        this.applicationContextName = acn;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;

    }

    public void setDialogServiceUserType(DialogServiceUserType dialogServiceUserType) {
        this.dialogServiceUserType = dialogServiceUserType;
    }

    public DialogServiceUserType getDialogServiceUserType() {
        return this.dialogServiceUserType;
    }

    public void setReturnMessageOnError(boolean val) {
        returnMessageOnError = val;
    }

    public boolean getReturnMessageOnError() {
        return returnMessageOnError;
    }

}
