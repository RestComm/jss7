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

package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.*;

import org.mobicents.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class GSNAddressAddressTypeTest {

    private int getEncodedData() {
        return 4;
    }

    private int getEncodedData2() {
        return 80;
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        int firstByte = getEncodedData();
        GSNAddressAddressType asc = GSNAddressAddressType.getFromGSNAddressFirstByte(firstByte);

        assertEquals(asc, GSNAddressAddressType.IPv4);


        firstByte = getEncodedData2();
        asc = GSNAddressAddressType.getFromGSNAddressFirstByte(firstByte);

        assertEquals(asc, GSNAddressAddressType.IPv6);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        GSNAddressAddressType asc = GSNAddressAddressType.IPv4;
        int firstByte = asc.createGSNAddressFirstByte();
        assertEquals(firstByte, getEncodedData());

        asc = GSNAddressAddressType.IPv6;
        firstByte = asc.createGSNAddressFirstByte();
        assertEquals(firstByte, getEncodedData2());
    }

}
