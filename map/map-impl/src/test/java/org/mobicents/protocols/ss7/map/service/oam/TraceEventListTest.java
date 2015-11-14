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
import org.mobicents.protocols.ss7.map.api.service.oam.BMSCEventList;
import org.mobicents.protocols.ss7.map.api.service.oam.GGSNEventList;
import org.mobicents.protocols.ss7.map.api.service.oam.MGWEventList;
import org.mobicents.protocols.ss7.map.api.service.oam.MMEEventList;
import org.mobicents.protocols.ss7.map.api.service.oam.MSCSEventList;
import org.mobicents.protocols.ss7.map.api.service.oam.PGWEventList;
import org.mobicents.protocols.ss7.map.api.service.oam.SGSNEventList;
import org.mobicents.protocols.ss7.map.api.service.oam.SGWEventList;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class TraceEventListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 32, (byte) 128, 2, 3, (byte) 128, (byte) 129, 2, 7, (byte) 128, (byte) 130, 2, 4, 64, (byte) 131, 2, 6, 64, (byte) 132, 2, 7,
                (byte) 128, (byte) 133, 2, 2, 36, (byte) 134, 2, 5, 32, (byte) 135, 2, 5, 64 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        TraceEventListImpl asc = new TraceEventListImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(asc.getMscSList().getMoMtCall());
        assertFalse(asc.getMscSList().getSs());

        assertTrue(asc.getMgwList().getContext());

        assertTrue(asc.getSgsnList().getMoMtSms());
        assertFalse(asc.getSgsnList().getMbmsContext());

        assertTrue(asc.getGgsnList().getMbmsContext());
        assertFalse(asc.getGgsnList().getPdpContext());

        assertTrue(asc.getBmscList().getMbmsMulticastServiceActivation());

        assertTrue(asc.getMmeList().getInitialAttachTrackingAreaUpdateDetach());
        assertTrue(asc.getMmeList().getHandover());
        assertFalse(asc.getMmeList().getServiceRequestts());

        assertTrue(asc.getSgwList().getBearerActivationModificationDeletion());
        assertFalse(asc.getSgwList().getPdnConnectionCreation());

        assertTrue(asc.getPgwList().getPdnConnectionTermination());
        assertFalse(asc.getPgwList().getBearerActivationModificationDeletion());

    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {

        MSCSEventList mscSList = new MSCSEventListImpl(true, false, false, false, false);
        MGWEventList mgwList = new MGWEventListImpl(true);
        SGSNEventList sgsnList = new SGSNEventListImpl(false, true, false, false);
        GGSNEventList ggsnList = new GGSNEventListImpl(false, true);
        BMSCEventList bmscList = new BMSCEventListImpl(true);
        MMEEventList mmeList = new MMEEventListImpl(false, false, true, false, false, true);
        SGWEventList sgwList = new SGWEventListImpl(false, false, true);
        PGWEventList pgwList = new PGWEventListImpl(false, true, false);
        TraceEventListImpl asc = new TraceEventListImpl(mscSList, mgwList, sgsnList, ggsnList, bmscList, mmeList, sgwList, pgwList);
//        MSCSEventList mscSList, MGWEventList mgwList, SGSNEventList sgsnList, GGSNEventList ggsnList,
//        BMSCEventList bmscList, MMEEventList mmeList, SGWEventList sgwList, PGWEventList pgwList

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

}
