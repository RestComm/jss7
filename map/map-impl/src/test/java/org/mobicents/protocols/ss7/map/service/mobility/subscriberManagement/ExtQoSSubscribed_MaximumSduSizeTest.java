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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class ExtQoSSubscribed_MaximumSduSizeTest {

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        ExtQoSSubscribed_MaximumSduSizeImpl prim = new ExtQoSSubscribed_MaximumSduSizeImpl(0, true);
        assertEquals(prim.getMaximumSduSize(), 0);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(1, true);
        assertEquals(prim.getMaximumSduSize(), 10);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(149, true);
        assertEquals(prim.getMaximumSduSize(), 1490);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(150, true);
        assertEquals(prim.getMaximumSduSize(), 1500);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(151, true);
        assertEquals(prim.getMaximumSduSize(), 1502);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(152, true);
        assertEquals(prim.getMaximumSduSize(), 1510);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(153, true);
        assertEquals(prim.getMaximumSduSize(), 1520);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(154, true);
        assertEquals(prim.getMaximumSduSize(), 0);
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        ExtQoSSubscribed_MaximumSduSizeImpl prim = new ExtQoSSubscribed_MaximumSduSizeImpl(0, false);
        assertEquals(prim.getSourceData(), 0);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(10, false);
        assertEquals(prim.getSourceData(), 1);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(1490, false);
        assertEquals(prim.getSourceData(), 149);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(1500, false);
        assertEquals(prim.getSourceData(), 150);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(1502, false);
        assertEquals(prim.getSourceData(), 151);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(1510, false);
        assertEquals(prim.getSourceData(), 152);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(1520, false);
        assertEquals(prim.getSourceData(), 153);

        prim = new ExtQoSSubscribed_MaximumSduSizeImpl(2000, false);
        assertEquals(prim.getSourceData(), 0);
    }

}
