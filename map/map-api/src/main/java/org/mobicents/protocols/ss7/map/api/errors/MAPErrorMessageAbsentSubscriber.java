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

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 MAP V2-3 absentSubscriber ERROR ::= { PARAMETER AbsentSubscriberParam -- optional -- AbsentSubscriberParam must not be used
 * in version <3 CODE local:27 }
 *
 * AbsentSubscriberParam ::= SEQUENCE { extensionContainer ExtensionContainer OPTIONAL, ..., absentSubscriberReason [0]
 * AbsentSubscriberReason OPTIONAL}
 *
 * MAP V1 AbsentSubscriber ::= ERROR PARAMETER mwd-Set BOOLEAN -- optional -- mwd-Set must be absent in version greater 1
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPErrorMessageAbsentSubscriber extends MAPErrorMessage {

    // following is for MAP V3 only
    MAPExtensionContainer getExtensionContainer();

    AbsentSubscriberReason getAbsentSubscriberReason();

    void setExtensionContainer(MAPExtensionContainer extensionContainer);

    void setAbsentSubscriberReason(AbsentSubscriberReason absentSubscriberReason);

    // following is for MAP V1 only
    Boolean getMwdSet();

    void setMwdSet(Boolean val);

}
