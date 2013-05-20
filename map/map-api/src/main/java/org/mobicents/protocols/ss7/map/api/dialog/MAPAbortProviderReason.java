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

package org.mobicents.protocols.ss7.map.api.dialog;

/**
 * Provider reason: This parameter indicates the reason for aborting the MAP dialogue: - provider malfunction; - supporting
 * dialogue/transaction released; - resource limitation; - maintenance activity; - version incompatibility; - abnormal MAP
 * dialogue.
 *
 * @author sergey vetyutnev
 *
 */
public enum MAPAbortProviderReason {
    /**
     * This option is used when other PAbortCauseType options is received from a peer except UnrecognizedTxID or
     * ResourceLimitation
     */
    ProviderMalfunction(0),

    /**
     * This option is used when PAbortCauseType.UnrecognizedTxID is received from a peer (a Dialog has already released at a
     * peer side)
     */
    SupportingDialogueTransactionReleased(1),

    /**
     * This option is used when PAbortCauseType.ResourceLimitation is received from a peer
     */
    ResourceLimitation(2),

    /**
     * This option is not used now.
     */
    MaintenanceActivity(3),

    /**
     * This option is not used now. If a potential version incompatibility is detected (when we should try to use MAP V1)
     * MAPDialogListener.onDialogRequest() is issued with MAPRefuseReason==PotentialVersionIncompatibility
     */
    VersionIncompatibility(4),

    /**
     * Used when local problems with a Dialog detected: - no or bad ACN received at the first TC-CONTINUE or TC-END - no or bad
     * user info is received at the TC-U-ABORT
     */
    AbnormalMAPDialogueLocal(5),

    /**
     * Used when receiving MAP-ProviderAbortInfo from a peer with MAP-ProviderAbortReason = abnormalDialogue
     */
    AbnormalMAPDialogueFromPeer(6),

    /**
     * Used when receiving MAP-ProviderAbortInfo from a peer with MAP-ProviderAbortReason = invalidPDU
     */
    InvalidPDU(7);

    private int code;

    private MAPAbortProviderReason(int code) {
        this.code = code;
    }
}
