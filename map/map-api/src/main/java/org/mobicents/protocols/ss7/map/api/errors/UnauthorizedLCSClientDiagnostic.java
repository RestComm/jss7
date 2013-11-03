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

package org.mobicents.protocols.ss7.map.api.errors;

/**
 *
 * UnauthorizedLCSClient-Diagnostic ::= ENUMERATED { noAdditionalInformation (0), clientNotInMSPrivacyExceptionList (1),
 * callToClientNotSetup (2), privacyOverrideNotApplicable (3), disallowedByLocalRegulatoryRequirements (4), ...,
 * unauthorizedPrivacyClass (5), unauthorizedCallSessionUnrelatedExternalClient (6),
 * unauthorizedCallSessionRelatedExternalClient (7) } -- exception handling: -- any unrecognized value shall be ignored
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum UnauthorizedLCSClientDiagnostic {
    noAdditionalInformation(0), clientNotInMSPrivacyExceptionList(1), callToClientNotSetup(2), privacyOverrideNotApplicable(3), disallowedByLocalRegulatoryRequirements(
            4), unauthorizedPrivacyClass(5), unauthorizedCallSessionUnrelatedExternalClient(6), unauthorizedCallSessionRelatedExternalClient(
            7);

    private int code;

    private UnauthorizedLCSClientDiagnostic(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static UnauthorizedLCSClientDiagnostic getInstance(int code) {
        switch (code) {
            case 0:
                return noAdditionalInformation;
            case 1:
                return clientNotInMSPrivacyExceptionList;
            case 2:
                return callToClientNotSetup;
            case 3:
                return privacyOverrideNotApplicable;
            case 4:
                return disallowedByLocalRegulatoryRequirements;
            case 5:
                return unauthorizedPrivacyClass;
            case 6:
                return unauthorizedCallSessionUnrelatedExternalClient;
            case 7:
                return unauthorizedCallSessionRelatedExternalClient;
            default:
                return null;
        }
    }

}
