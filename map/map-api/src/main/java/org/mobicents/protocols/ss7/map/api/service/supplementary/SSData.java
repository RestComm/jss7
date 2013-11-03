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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

import java.io.Serializable;
import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;

/**
 *
 SS-Data ::= SEQUENCE { ss-Code SS-Code OPTIONAL, ss-Status [4] SS-Status OPTIONAL, ss-SubscriptionOption
 * SS-SubscriptionOption OPTIONAL, basicServiceGroupList BasicServiceGroupList OPTIONAL, ..., defaultPriority EMLPP-Priority
 * OPTIONAL, nbrUser [5] MC-Bearers OPTIONAL }
 *
 * BasicServiceGroupList ::= SEQUENCE SIZE (1..13) OF BasicServiceCode
 *
 * MC-Bearers ::= INTEGER (1..7)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SSData extends Serializable {

    SSCode getCode();

    SSStatus getSsStatus();

    SSSubscriptionOption getSsSubscriptionOption();

    ArrayList<BasicServiceCode> getBasicServiceGroupList();

    EMLPPPriority getDefaultPriority();

    Integer getNbrUser();

}