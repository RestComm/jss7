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

package org.mobicents.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import junit.framework.Assert;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InformServiceCentreRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 4, 3, 2, 2, 64 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 61, 4, 6, -111, 17, 33, 34, 51, -13, 3, 2, 2, 80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11,
                12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 2,
                2, 2, 43, -128, 2, 1, -68 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        InformServiceCentreRequestImpl isc = new InformServiceCentreRequestImpl();
        isc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        MWStatus mwStatus = isc.getMwStatus();
        assertNotNull(mwStatus);
        assertFalse(mwStatus.getScAddressNotIncluded());
        assertTrue(mwStatus.getMnrfSet());
        assertFalse(mwStatus.getMcefSet());
        assertFalse(mwStatus.getMnrgSet());

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        isc = new InformServiceCentreRequestImpl();
        isc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        MAPExtensionContainer extensionContainer = isc.getExtensionContainer();
        ISDNAddressString storedMSISDN = isc.getStoredMSISDN();
        mwStatus = isc.getMwStatus();
        int absentSubscriberDiagnosticSM = isc.getAbsentSubscriberDiagnosticSM();
        int additionalAbsentSubscriberDiagnosticSM = isc.getAdditionalAbsentSubscriberDiagnosticSM();

        Assert.assertNotNull(storedMSISDN);
        Assert.assertEquals(AddressNature.international_number, storedMSISDN.getAddressNature());
        Assert.assertEquals(NumberingPlan.ISDN, storedMSISDN.getNumberingPlan());
        Assert.assertEquals("111222333", storedMSISDN.getAddress());
        Assert.assertNotNull(mwStatus);
        Assert.assertFalse(mwStatus.getScAddressNotIncluded());
        Assert.assertTrue(mwStatus.getMnrfSet());
        Assert.assertFalse(mwStatus.getMcefSet());
        Assert.assertTrue(mwStatus.getMnrgSet());
        Assert.assertNotNull(absentSubscriberDiagnosticSM);
        Assert.assertEquals(555, (int) absentSubscriberDiagnosticSM);
        Assert.assertNotNull(additionalAbsentSubscriberDiagnosticSM);
        Assert.assertEquals(444, (int) additionalAbsentSubscriberDiagnosticSM);
        Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        MWStatus mwStatus = new MWStatusImpl(false, true, false, false);
        InformServiceCentreRequestImpl isc = new InformServiceCentreRequestImpl(null, mwStatus, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        isc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ISDNAddressString storedMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "111222333");
        mwStatus = new MWStatusImpl(false, true, false, true);
        Integer absentSubscriberDiagnosticSM = 555;
        Integer additionalAbsentSubscriberDiagnosticSM = 444;
        isc = new InformServiceCentreRequestImpl(storedMSISDN, mwStatus, MAPExtensionContainerTest.GetTestExtensionContainer(),
                absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM);

        asnOS.reset();
        isc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
