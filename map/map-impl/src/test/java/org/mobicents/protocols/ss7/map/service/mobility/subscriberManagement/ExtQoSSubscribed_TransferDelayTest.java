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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class ExtQoSSubscribed_TransferDelayTest {

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        ExtQoSSubscribed_TransferDelayImpl prim = new ExtQoSSubscribed_TransferDelayImpl(0, true);
        assertEquals(prim.getTransferDelay(), 0);

        prim = new ExtQoSSubscribed_TransferDelayImpl(1, true);
        assertEquals(prim.getTransferDelay(), 10);

        prim = new ExtQoSSubscribed_TransferDelayImpl(15, true);
        assertEquals(prim.getTransferDelay(), 150);

        prim = new ExtQoSSubscribed_TransferDelayImpl(16, true);
        assertEquals(prim.getTransferDelay(), 200);

        prim = new ExtQoSSubscribed_TransferDelayImpl(17, true);
        assertEquals(prim.getTransferDelay(), 250);

        prim = new ExtQoSSubscribed_TransferDelayImpl(31, true);
        assertEquals(prim.getTransferDelay(), 950);

        prim = new ExtQoSSubscribed_TransferDelayImpl(32, true);
        assertEquals(prim.getTransferDelay(), 1000);

        prim = new ExtQoSSubscribed_TransferDelayImpl(33, true);
        assertEquals(prim.getTransferDelay(), 1100);

        prim = new ExtQoSSubscribed_TransferDelayImpl(62, true);
        assertEquals(prim.getTransferDelay(), 4000);
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        ExtQoSSubscribed_TransferDelayImpl prim = new ExtQoSSubscribed_TransferDelayImpl(0, false);
        assertEquals(prim.getSourceData(), 0);

        prim = new ExtQoSSubscribed_TransferDelayImpl(10, false);
        assertEquals(prim.getSourceData(), 1);

        prim = new ExtQoSSubscribed_TransferDelayImpl(150, false);
        assertEquals(prim.getSourceData(), 15);

        prim = new ExtQoSSubscribed_TransferDelayImpl(200, false);
        assertEquals(prim.getSourceData(), 16);

        prim = new ExtQoSSubscribed_TransferDelayImpl(250, false);
        assertEquals(prim.getSourceData(), 17);

        prim = new ExtQoSSubscribed_TransferDelayImpl(950, false);
        assertEquals(prim.getSourceData(), 31);

        prim = new ExtQoSSubscribed_TransferDelayImpl(1000, false);
        assertEquals(prim.getSourceData(), 32);

        prim = new ExtQoSSubscribed_TransferDelayImpl(1100, false);
        assertEquals(prim.getSourceData(), 33);

        prim = new ExtQoSSubscribed_TransferDelayImpl(4000, false);
        assertEquals(prim.getSourceData(), 62);
    }

}
