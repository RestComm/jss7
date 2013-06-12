/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.cap.api.primitives;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;

/**
 *
 CalledPartyBCDNumber {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minCalledPartyBCDNumberLength ..
 * bound.&maxCalledPartyBCDNumberLength)) -- Indicates the Called Party Number, including service selection information. --
 * Refer to 3GPP TS 24.008 [9] for encoding. -- This data type carries only the 'type of number', 'numbering plan --
 * identification' and 'number digit' fields defined in 3GPP TS 24.008 [9]; -- it does not carry the 'called party BCD number
 * IEI' or 'length of called -- party BCD number contents'. -- In the context of the DestinationSubscriberNumber field in
 * ConnectSMSArg or -- InitialDPSMSArg, a CalledPartyBCDNumber may also contain an alphanumeric -- character string. In this
 * case, type-of-number '101'B is used, in accordance -- with 3GPP TS 23.040 [6]. The address is coded in accordance with the --
 * GSM 7-bit default alphabet definition and the SMS packing rules -- as specified in 3GPP TS 23.038 [15] in this case.
 *
 * minCalledPartyBCDNumberLength ::= 1 maxCalledPartyBCDNumberLength ::= 41
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CalledPartyBCDNumber extends AddressString {

    byte[] getData();

    AddressNature getAddressNature();

    NumberingPlan getNumberingPlan();

    String getAddress();

    boolean isExtension();

}
