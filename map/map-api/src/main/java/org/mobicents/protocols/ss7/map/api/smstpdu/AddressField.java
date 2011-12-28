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

package org.mobicents.protocols.ss7.map.api.smstpdu;

import java.io.OutputStream;

import org.mobicents.protocols.ss7.map.api.MAPException;

/**
Each address field of the SM-TL consists of the following sub-fields: An Address-Length field of one octet, a
Type-of-Address field of one octet, and one Address-Value field of variable length

The Type-of-Address field format is as follows:
Bit 7 = 1
Type-of-number:
Bits 6 5 4
0 0 0 Unknown 1)
0 0 1 International number 2)
0 1 0 National number 3)
0 1 1 Network specific number 4)
1 0 0 Subscriber number 5)
1 0 1 Alphanumeric, (coded according to 3GPP TS 23.038 [9] GSM 7-bit default alphabet)
1 1 0 Abbreviated number
1 1 1 Reserved for extension
Numbering-plan-identification
Bits 3 2 1 0
0 0 0 0 Unknown
0 0 0 1 ISDN/telephone numbering plan (E.164 [17]/E.163[18])
0 0 1 1 Data numbering plan (X.121)
0 1 0 0 Telex numbering plan
0 1 0 1 Service Centre Specific plan 1)
0 1 1 0 Service Centre Specific plan 1)
1 0 0 0 National numbering plan
1 0 0 1 Private numbering plan
1 0 1 0 ERMES numbering plan (ETSI DE/PS 3 01-3)
1 1 1 1 Reserved for extension
All other values are reserved.
 * 
 * @author sergey vetyutnev
 * 
 */
public interface AddressField {

	public TypeOfNumber getTypeOfNumber();

	public NumberingPlanIdentification getNumberingPlanIdentification();

	public String getAddressValue();

	public void encodeData(OutputStream stm) throws MAPException;

}
