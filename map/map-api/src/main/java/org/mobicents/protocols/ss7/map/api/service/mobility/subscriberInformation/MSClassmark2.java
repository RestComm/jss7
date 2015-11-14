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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.io.Serializable;

/**
 *
<code>
MS-Classmark2 ::= OCTET STRING (SIZE (3))
-- This parameter carries the value part of the MS Classmark 2 IE defined in
-- 3GPP TS 24.008 [35].

10.5.1.6 Mobile Station Classmark 2
The purpose of the Mobile Station Classmark 2 information element is to provide the network with information
concerning aspects of both high and low priority of the mobile station equipment. This affects the manner in which the
network handles the operation of the mobile station. The Mobile Station Classmark information indicates general
mobile station characteristics and it shall therefore, except for fields explicitly indicated, be independent of the
frequency band of the channel it is sent on.
The Mobile Station Classmark 2 information element is coded as shown in figure 10.5.6/3GPP TS 24.008,
table 10.5.6a/3GPP TS 24.008 and table 10.5.6b/3GPP TS 24.008.
The Mobile Station Classmark 2 is a type 4 information element with 5 octets length.

octet 1: Mobile station classmark
octet 2: Length of mobile station classmark 2 contents
octet 3: 8:0 spare - 7,6:Revision level - 5:ES IND 4:A5/1 - 3,2,1: RF power capability
octet 4: 8:0 spare - 7:PS capa. - 6,5:SS Screen.Indicator - 4:SM capabi. - 3:VBS - 2:VGCS - 1:FC
octet 5: 8:CM3 0 - 7:0 spare - 6:LCSVA CAP - 5:UCS2 - 4:SoLSA - 3:CMSP - 2:A5/3 - 1:A5/2
</code>
 *
 * }
 *
 * @author sergey vetyutnev
 *
 */
public interface MSClassmark2 extends Serializable {

    byte[] getData();

    // TODO: implement internal structure from 3GPP TS 24.008

}
