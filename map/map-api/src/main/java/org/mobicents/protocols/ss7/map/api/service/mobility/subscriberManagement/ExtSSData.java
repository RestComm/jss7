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
import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;

/**
 *
 Ext-SS-Data ::= SEQUENCE { ss-Code SS-Code, ss-Status [4] Ext-SS-Status, ss-SubscriptionOption SS-SubscriptionOption
 * OPTIONAL, basicServiceGroupList Ext-BasicServiceGroupList OPTIONAL, extensionContainer [5] ExtensionContainer OPTIONAL, ...}
 *
 * Ext-BasicServiceGroupList ::= SEQUENCE SIZE (1..32) OF Ext-BasicServiceCode
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ExtSSData extends Serializable {

    SSCode getSsCode();

    ExtSSStatus getSsStatus();

    SSSubscriptionOption getSSSubscriptionOption();

    ArrayList<ExtBasicServiceCode> getBasicServiceGroupList();

    MAPExtensionContainer getExtensionContainer();

}
