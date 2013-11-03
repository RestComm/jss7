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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.mobicents.protocols.ss7.map.primitives.PlmnIdImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ReportingPLMNTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 10, -128, 3, 1, 2, 3, -127, 1, 1, -126, 0 };
    }

    private byte[] getDataPlmnId() {
        return new byte[] { 1, 2, 3 };
    }

    @Test(groups = { "functional.decode", "service.lms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ReportingPLMNImpl imp = new ReportingPLMNImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(Arrays.equals(imp.getPlmnId().getData(), getDataPlmnId()));
        assertEquals(imp.getRanTechnology(), RANTechnology.umts);
        assertTrue(imp.getRanPeriodicLocationSupport());
    }

    @Test(groups = { "functional.encode", "service.lms" })
    public void testEncode() throws Exception {

        PlmnId plmnId = new PlmnIdImpl(getDataPlmnId());
        ReportingPLMNImpl imp = new ReportingPLMNImpl(plmnId, RANTechnology.umts, true);
        // PlmnId plmnId, RANTechnology ranTechnology, boolean ranPeriodicLocationSupport

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
