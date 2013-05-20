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
