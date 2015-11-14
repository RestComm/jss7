/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.tcapAnsi.api.asn;

/**
 *
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *

DialoguePortion ::= [PRIVATE 25] IMPLICIT SEQUENCE {
    version         ProtocolVersion OPTIONAL,
    ApplicationContext  CHOICE {
        integerApplicationId    IntegerApplicationContext,
        objectApplicationId ObjectIDApplicationContext
    } OPTIONAL,
    userInformation     UserInformation OPTIONAL,
    securityContext     CHOICE {
        integerSecurityId   [0] IMPLICIT INTEGER,
        objectSecurityId    [1] IMPLICIT OBJECT IDENTIFIER
    } OPTIONAL,
    confidentiality     [2] IMPLICIT Confidentiality OPTIONAL
}

 */
public interface DialogPortion extends Encodable {

    int _TAG_DIALOG_PORTION = 25;


    ProtocolVersion getProtocolVersion();

    void setProtocolVersion(ProtocolVersion val);

    ApplicationContext getApplicationContext();

    void setApplicationContext(ApplicationContext val);

    UserInformation getUserInformation();

    void setUserInformation(UserInformation val);

    SecurityContext getSecurityContext();

    void setSecurityContext(SecurityContext val);

    Confidentiality getConfidentiality();

    void setConfidentiality(Confidentiality val);

}
