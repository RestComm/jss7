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

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;

/**
 *
 InterrogateSS-Res ::= CHOICE { ss-Status [0] SS-Status, basicServiceGroupList [2] BasicServiceGroupList,
 * forwardingFeatureList [3] ForwardingFeatureList, genericServiceInfo [4] GenericServiceInfo }
 *
 * BasicServiceGroupList ::= SEQUENCE SIZE (1..13) OF BasicServiceCode
 *
 * ForwardingFeatureList ::= SEQUENCE SIZE (1..13) OF ForwardingFeature
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InterrogateSSResponse extends SupplementaryMessage {

    SSStatus getSsStatus();

    ArrayList<BasicServiceCode> getBasicServiceGroupList();

    ArrayList<ForwardingFeature> getForwardingFeatureList();

    GenericServiceInfo getGenericServiceInfo();

}