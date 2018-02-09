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

package org.mobicents.protocols.ss7.mtp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.testng.annotations.Test;

/**
 * @author sergey vetyutnev
 *
 */
public class Mtp3TransferMessageTest {

    private byte[] getMsg() {
        return new byte[] { (byte) 0x83, (byte) 232, 3, (byte) 244, (byte) 161, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    private byte[] getAnsiMsg(boolean is8Bit) {
        if(is8Bit)
            return new byte[] { (byte) 0x83, (byte) 232, 3, 0,  (byte) 208, 7, 0, 33, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        else
            return new byte[] { (byte) 0x83, (byte) 232, 3, 0,  (byte) 208, 7, 0, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    private byte[] getData() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }

    // si = 3 (SCCP)
    // ni = 2
    // mp = 0
    // sio = (si + (ni << 6) + (mt << 4))
    // dpc = 1000
    // opc = 2000
    // sls = 10

    @Test(groups = { "Mtp3TransferMessageTest", "decodeItu" })
    public void testItuDecode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ITU);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(getMsg());

        assertEquals(msg.getSi(), 3);
        assertEquals(msg.getNi(), 2);
        assertEquals(msg.getMp(), 0);
        assertEquals(msg.getDpc(), 1000);
        assertEquals(msg.getOpc(), 2000);
        assertEquals(msg.getSls(), 10);
        assertTrue(Arrays.equals(msg.getData(), this.getData()));

    }

    @Test(groups = { "Mtp3TransferMessageTest", "decodeAnsiSls8Bit" })
    public void testAnsiSls8BitDecode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ANSI_Sls8Bit);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(getAnsiMsg(true));

        assertEquals(msg.getSi(), 3);
        assertEquals(msg.getNi(), 2);
        assertEquals(msg.getMp(), 0);
        assertEquals(msg.getDpc(), 1000);
        assertEquals(msg.getOpc(), 2000);
        assertEquals(msg.getSls(), 33);
        assertTrue(Arrays.equals(msg.getData(), this.getData()));

    }

    @Test(groups = { "Mtp3TransferMessageTest", "decodeAnsiSls5Bit" })
    public void testAnsiSls5BitDecode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ANSI_Sls5Bit);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(getAnsiMsg(false));

        assertEquals(msg.getSi(), 3);
        assertEquals(msg.getNi(), 2);
        assertEquals(msg.getMp(), 0);
        assertEquals(msg.getDpc(), 1000);
        assertEquals(msg.getOpc(), 2000);
        assertEquals(msg.getSls(), 10);
        assertTrue(Arrays.equals(msg.getData(), this.getData()));
    }

    @Test(groups = { "Mtp3TransferMessageTest", "encodeItu" })
    public void testItuEncode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ITU);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(3, 2, 0, 2000, 1000, 10, this.getData());

        byte[] res = msg.encodeMtp3();

        assertTrue(Arrays.equals(res, this.getMsg()));

    }
    
    @Test(groups = { "Mtp3TransferMessageTest", "encodeAnsiSls8Bit" })
    public void testAnsiSls8BitEncode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ANSI_Sls8Bit);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(3, 2, 0, 2000, 1000, 33, this.getData());

        byte[] res = msg.encodeMtp3();

        assertTrue(Arrays.equals(res, this.getAnsiMsg(true)));
    }
  
    @Test(groups = { "Mtp3TransferMessageTest", "encodeAnsiSls5Bit" })
    public void testAnsiSls5BitEncode() throws Exception {
        Mtp3TransferPrimitiveFactory factory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ANSI_Sls5Bit);
        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(3, 2, 0, 2000, 1000, 10, this.getData());

        byte[] res = msg.encodeMtp3();

        assertTrue(Arrays.equals(res, this.getAnsiMsg(false)));
  }

}
