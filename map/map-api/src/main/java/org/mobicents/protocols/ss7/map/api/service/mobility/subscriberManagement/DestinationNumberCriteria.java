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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;

/**
 *
 DestinationNumberCriteria ::= SEQUENCE { matchType [0] MatchType, destinationNumberList [1] DestinationNumberList OPTIONAL,
 * destinationNumberLengthList [2] DestinationNumberLengthList OPTIONAL, -- one or both of destinationNumberList and
 * destinationNumberLengthList -- shall be present ...}
 *
 * DestinationNumberList ::= SEQUENCE SIZE (1..10) OF ISDN-AddressString -- The receiving entity shall not check the format of a
 * number in -- the dialled number list
 *
 * DestinationNumberLengthList ::= SEQUENCE SIZE (1..3) OF INTEGER(1..15)
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface DestinationNumberCriteria {

    MatchType getMatchType();

    ArrayList<ISDNAddressString> getDestinationNumberList();

    ArrayList<Integer> getDestinationNumberLengthList();

}
