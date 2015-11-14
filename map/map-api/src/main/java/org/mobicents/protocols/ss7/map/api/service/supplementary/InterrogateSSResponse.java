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

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;

/**
 *
<code>
InterrogateSS-Res ::= CHOICE {
  ss-Status               [0] SS-Status,
  basicServiceGroupList   [2] BasicServiceGroupList,
  forwardingFeatureList   [3] ForwardingFeatureList,
  genericServiceInfo      [4] GenericServiceInfo
}

BasicServiceGroupList ::= SEQUENCE SIZE (1..13) OF BasicServiceCode

ForwardingFeatureList ::= SEQUENCE SIZE (1..13) OF ForwardingFeature
</code>
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
