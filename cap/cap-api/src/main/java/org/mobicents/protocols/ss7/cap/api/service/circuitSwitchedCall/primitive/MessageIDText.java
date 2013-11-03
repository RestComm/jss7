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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
 *
 text [1] SEQUENCE { messageContent [0] IA5String (SIZE(bound.&minMessageContentLength .. bound.&maxMessageContentLength)),
 * attributes [1] OCTET STRING (SIZE(bound.&minAttributesLength .. bound.&maxAttributesLength)) OPTIONAL },
 *
 * minMessageContentLength ::= 1 maxMessageContentLength ::= 127 minAttributesLength ::= 2 maxAttributesLength ::= 10
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MessageIDText extends Serializable {

    String getMessageContent();

    byte[] getAttributes();

}