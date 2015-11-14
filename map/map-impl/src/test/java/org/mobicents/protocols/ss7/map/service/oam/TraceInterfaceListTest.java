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

package org.mobicents.protocols.ss7.map.service.oam;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.oam.BMSCInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.ENBInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.GGSNInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.MGWInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.MMEInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.MSCSInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.PGWInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.RNCInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.SGSNInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.SGWInterfaceList;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class TraceInterfaceListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 42, (byte) 128, 3, 6, 8, 0, (byte) 129, 2, 5, 32, (byte) 130, 3, 5, 64, 0, (byte) 131, 2, 5, (byte) 128, (byte) 132, 2, 4, 16,
                (byte) 133, 2, 7, (byte) 128, (byte) 134, 2, 3, 8, (byte) 135, 2, 3, (byte) 128, (byte) 136, 2, 0, 4, (byte) 137, 2, 5, 64 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        TraceInterfaceListImpl asc = new TraceInterfaceListImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(asc.getMscSList().getMapB());
        assertFalse(asc.getMscSList().getA());

        assertTrue(asc.getMgwList().getIuUp());
        assertFalse(asc.getMgwList().getMc());

        assertTrue(asc.getSgsnList().getIu());
        assertFalse(asc.getSgsnList().getGe());

        assertTrue(asc.getGgsnList().getGn());
        assertFalse(asc.getGgsnList().getGmb());

        assertTrue(asc.getRncList().getUu());
        assertFalse(asc.getRncList().getIub());

        assertTrue(asc.getBmscList().getGmb());

        assertTrue(asc.getMmeList().getS11());
        assertFalse(asc.getMmeList().getS10());

        assertTrue(asc.getSgwList().getS4());
        assertFalse(asc.getSgwList().getS5());

        assertTrue(asc.getPgwList().getGx());
        assertFalse(asc.getPgwList().getS2a());

        assertTrue(asc.getEnbList().getX2());
        assertFalse(asc.getEnbList().getS1Mme());

    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {

        MSCSInterfaceList mscSList = new MSCSInterfaceListImpl(false, false, false, false, true, false, false, false, false, false);
        MGWInterfaceList mgwList = new MGWInterfaceListImpl(false, false, true);
        SGSNInterfaceList sgsnList = new SGSNInterfaceListImpl(false, true, false, false, false, false, false, false, false, false, false);
        GGSNInterfaceList ggsnList = new GGSNInterfaceListImpl(true, false, false);
        RNCInterfaceList rncList = new RNCInterfaceListImpl(false, false, false, true);
        BMSCInterfaceList bmscList = new BMSCInterfaceListImpl(true);
        MMEInterfaceList mmeList = new MMEInterfaceListImpl(false, false, false, false, true);
        SGWInterfaceList sgwList = new SGWInterfaceListImpl(true, false, false, false, false);
        PGWInterfaceList pgwList = new PGWInterfaceListImpl(false, false, false, false, false, true, false, false);
        ENBInterfaceList enbList = new ENBInterfaceListImpl(false, true, false);
        TraceInterfaceListImpl asc = new TraceInterfaceListImpl(mscSList, mgwList, sgsnList, ggsnList, rncList, bmscList, mmeList, sgwList, pgwList, enbList);
//        MSCSInterfaceList mscSList, MGWInterfaceList mgwList, SGSNInterfaceList sgsnList, GGSNInterfaceList ggsnList,
//        RNCInterfaceList rncList, BMSCInterfaceList bmscList, MMEInterfaceList mmeList, SGWInterfaceList sgwList, PGWInterfaceList pgwList,
//        ENBInterfaceList enbList

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

}
