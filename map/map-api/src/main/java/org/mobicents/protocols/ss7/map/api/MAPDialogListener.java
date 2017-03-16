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

package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface MAPDialogListener {
    /**
     * Called when all components has been processed. It is equals the MAP-DELIMITER indication primitive
     */
    void onDialogDelimiter(MAPDialog mapDialog);

    /**
     * When T_BEGIN received. If MAP user rejects this dialog it should call MAPDialog.refuse()
     */
    void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer);

    /**
     * When T_BEGIN received. If MAP user rejects this dialog it should call This eveent is the onDialogRequest() method for
     * Ericsson-style ASN.1 syntax MAPDialog.refuse()
     */
    void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            AddressString eriMsisdn, AddressString eriVlrNo);

    /**
     * When T_CONTINUE or T_END received with dialogueResponse DialoguePDU (AARE-apdu) (dialog accepted) this is called before
     * ComponentPortion is called
     */
    void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer);

    /**
     * When T_U_ABORT received as the response to T_BEGIN
     *
     */
    void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer);

    // void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
    // ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer);

    /**
     * When T_ABORT received NOT as the response to T_BEGIN
     *
     */
    void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer);

    /**
     * When T_ABORT received NOT as the response to T_BEGIN
     *
     */
    void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer);

    /**
     * When T_CLOSE received If T_CLOSE is the response to T-BEGIN, onDialogRequest() if called first, then ComponentPortion is
     * called and finally onDialogClose
     */
    void onDialogClose(MAPDialog mapDialog);

    /**
     * This service is used to notify the MAP service-user about protocol problems related to a MAP dialogue not affecting the
     * state of the protocol machines
     */
    void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic);

    /**
     * Called when the MADDialog has been released
     *
     * @param mapDialog
     */
    void onDialogRelease(MAPDialog mapDialog);

    /**
     * Called when the MADDialog is about to aborted because of TimeOut
     *
     * @param mapDialog
     */
    void onDialogTimeout(MAPDialog mapDialog);

}
