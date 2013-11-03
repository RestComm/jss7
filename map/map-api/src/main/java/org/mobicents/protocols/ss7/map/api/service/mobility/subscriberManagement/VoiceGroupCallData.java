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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 VoiceGroupCallData ::= SEQUENCE { groupId GroupId, -- groupId shall be filled with six TBCD fillers (1111)if the longGroupId
 * is present extensionContainer ExtensionContainer OPTIONAL, ..., additionalSubscriptions AdditionalSubscriptions OPTIONAL,
 * additionalInfo [0] AdditionalInfo OPTIONAL, longGroupId [1] Long-GroupId OPTIONAL }
 *
 * -- VoiceGroupCallData containing a longGroupId shall not be sent to VLRs that did not -- indicate support of long Group IDs
 * within the Update Location or Restore Data -- request message
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface VoiceGroupCallData extends Serializable {

    GroupId getGroupId();

    MAPExtensionContainer getExtensionContainer();

    AdditionalSubscriptions getAdditionalSubscriptions();

    AdditionalInfo getAdditionalInfo();

    LongGroupId getLongGroupId();

}
