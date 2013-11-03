/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.mobicents.protocols.ss7.map.primitives.PlmnIdImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ReportingPLMNListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 26, -128, 0, -95, 22, 48, 10, -128, 3, 11, 12, 13, -127, 1, 0, -126, 0, 48, 8, -128, 3, 21, 22,
                33, -127, 1, 1 };
    }

    private byte[] getDataPlmnId1() {
        return new byte[] { 11, 12, 13 };
    }

    private byte[] getDataPlmnId2() {
        return new byte[] { 21, 22, 33 };
    }

    @Test(groups = { "functional.decode", "service.lms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ReportingPLMNListImpl imp = new ReportingPLMNListImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(imp.getPlmnListPrioritized());

        ArrayList<ReportingPLMN> al = imp.getPlmnList();
        assertEquals(al.size(), 2);
        ReportingPLMN p1 = al.get(0);
        ReportingPLMN p2 = al.get(1);
        assertTrue(Arrays.equals(p1.getPlmnId().getData(), getDataPlmnId1()));
        assertTrue(Arrays.equals(p2.getPlmnId().getData(), getDataPlmnId2()));
        assertEquals(p1.getRanTechnology(), RANTechnology.gsm);
        assertEquals(p2.getRanTechnology(), RANTechnology.umts);
        assertTrue(p1.getRanPeriodicLocationSupport());
        assertFalse(p2.getRanPeriodicLocationSupport());
    }

    @Test(groups = { "functional.encode", "service.lms" })
    public void testEncode() throws Exception {

        PlmnId plmnId = new PlmnIdImpl(getDataPlmnId1());
        ReportingPLMN rp1 = new ReportingPLMNImpl(plmnId, RANTechnology.gsm, true);
        plmnId = new PlmnIdImpl(getDataPlmnId2());
        ReportingPLMN rp2 = new ReportingPLMNImpl(plmnId, RANTechnology.umts, false);

        ArrayList<ReportingPLMN> plmnList = new ArrayList<ReportingPLMN>();
        plmnList.add(rp1);
        plmnList.add(rp2);

        ReportingPLMNListImpl imp = new ReportingPLMNListImpl(true, plmnList);
        // boolean plmnListPrioritized, ArrayList<ReportingPLMN> plmnList

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
