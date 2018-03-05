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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.*;

import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateExtendedImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class ExtQoSSubscribed_BitRateExtendedTest {

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        ExtQoSSubscribed_BitRateExtendedImpl prim = new ExtQoSSubscribed_BitRateExtendedImpl(0, true);
        assertEquals(prim.getBitRate(), 0);
        assertTrue(prim.isUseNonextendedValue());

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(1, true);
        assertEquals(prim.getBitRate(), 8700);
        assertFalse(prim.isUseNonextendedValue());

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(10, true);
        assertEquals(prim.getBitRate(), 9600);
        assertFalse(prim.isUseNonextendedValue());

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(74, true);
        assertEquals(prim.getBitRate(), 16000);
        assertFalse(prim.isUseNonextendedValue());

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(75, true);
        assertEquals(prim.getBitRate(), 17000);
        assertFalse(prim.isUseNonextendedValue());

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(158, true);
        assertEquals(prim.getBitRate(), 100000);
        assertFalse(prim.isUseNonextendedValue());

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(186, true);
        assertEquals(prim.getBitRate(), 128000);
        assertFalse(prim.isUseNonextendedValue());

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(187, true);
        assertEquals(prim.getBitRate(), 130000);
        assertFalse(prim.isUseNonextendedValue());

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(192, true);
        assertEquals(prim.getBitRate(), 140000);
        assertFalse(prim.isUseNonextendedValue());

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(250, true);
        assertEquals(prim.getBitRate(), 256000);
        assertFalse(prim.isUseNonextendedValue());

    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        ExtQoSSubscribed_BitRateExtendedImpl prim = new ExtQoSSubscribed_BitRateExtendedImpl(8700, false);
        assertEquals(prim.getSourceData(), 1);

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(9600, false);
        assertEquals(prim.getSourceData(), 10);

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(16000, false);
        assertEquals(prim.getSourceData(), 74);

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(17000, false);
        assertEquals(prim.getSourceData(), 75);

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(100000, false);
        assertEquals(prim.getSourceData(), 158);

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(128000, false);
        assertEquals(prim.getSourceData(), 186);

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(130000, false);
        assertEquals(prim.getSourceData(), 187);

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(140000, false);
        assertEquals(prim.getSourceData(), 192);

        prim = new ExtQoSSubscribed_BitRateExtendedImpl(256000, false);
        assertEquals(prim.getSourceData(), 250);

    }

}
