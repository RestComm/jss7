/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.dialog;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum CAPGeneralAbortReason {
    /**
     * Application context name does not supported
     */
    ACNNotSupported(0),
    /**
     * Other part has been refused the Dialog with AARE apdu with abortReason=null or abortReason=no-reason-given
     */
    DialogRefused(1),
    /**
     * Other part has been refused the Dialog with AARE apdu with abortReason=null or abortReason=NoCommonDialogPortion
     */
    NoCommonDialogPortionReceived(2),
    /**
     * User abort, CAPUserAbortReason is present in the message
     */
    UserSpecific(3);

    private int code;

    private CAPGeneralAbortReason(int code) {
        this.code = code;
    }

    public static CAPGeneralAbortReason getInstance(int code) {
        switch (code) {
            case 0:
                return CAPGeneralAbortReason.ACNNotSupported;
            case 1:
                return CAPGeneralAbortReason.DialogRefused;
            case 2:
                return CAPGeneralAbortReason.NoCommonDialogPortionReceived;
            case 3:
                return CAPGeneralAbortReason.UserSpecific;
            default:
                return null;
        }
    }

    public int getCode() {
        return code;
    }
}
