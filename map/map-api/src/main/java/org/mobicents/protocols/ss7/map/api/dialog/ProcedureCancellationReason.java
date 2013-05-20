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

package org.mobicents.protocols.ss7.map.api.dialog;

/**
 * See ETS 300974 Table 5.3/7
 *
 * ProcedureCancellationReason ::= ENUMERATED { handoverCancellation (0), radioChannelRelease (1), networkPathRelease (2),
 * callRelease (3), associatedProcedureFailure (4), tandemDialogueRelease (5), remoteOperationsFailure (6)}
 *
 *
 * @author amit bhayani
 *
 */
public enum ProcedureCancellationReason {
    handoverCancellation(0), radioChannelRelease(1), networkPathRelease(2), callRelease(3), associatedProcedureFailure(4), tandemDialogueRelease(
            5), remoteOperationsFailure(6);

    private int code;

    private ProcedureCancellationReason(int code) {
        this.code = code;
    }

    public static ProcedureCancellationReason getInstance(int code) {
        switch (code) {
            case 0:
                return handoverCancellation;
            case 1:
                return radioChannelRelease;
            case 2:
                return networkPathRelease;
            case 3:
                return callRelease;
            case 4:
                return associatedProcedureFailure;
            case 5:
                return tandemDialogueRelease;
            case 6:
                return remoteOperationsFailure;
            default:
                return null;
        }
    }

    public int getCode() {
        return code;
    }

}
