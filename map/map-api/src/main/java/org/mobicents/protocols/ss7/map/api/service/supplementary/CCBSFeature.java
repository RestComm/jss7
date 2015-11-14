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

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;

/**
 *
<code>
CCBS-Feature ::= SEQUENCE {
  ccbs-Index                [0] CCBS-Index OPTIONAL,
  b-subscriberNumber        [1] ISDN-AddressString OPTIONAL,
  b-subscriberSubaddress    [2] ISDN-SubaddressString OPTIONAL,
  basicServiceGroup         [3] BasicServiceCode OPTIONAL,
  ...
}

CCBS-Index ::= INTEGER (1..5)
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CCBSFeature extends Serializable {

    Integer getCcbsIndex();

    ISDNAddressString getBSubscriberNumber();

    ISDNAddressString getBSubscriberSubaddress();

    BasicServiceCode getBasicServiceCode();

}
