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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

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
public interface SSData {

    SSCode getCode();

    SSStatus getSsStatus();

    SSSubscriptionOption getSsSubscriptionOption();

    ArrayList<BasicServiceCode> getBasicServiceGroupList();

    EMLPPPriority getDefaultPriority();

    Integer getNbrUser();

}