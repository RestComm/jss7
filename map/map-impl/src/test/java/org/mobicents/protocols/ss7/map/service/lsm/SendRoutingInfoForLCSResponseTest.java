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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Trace is from Brazil Operator
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SendRoutingInfoForLCSResponseTest {
    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        // The trace is from Brazilian operator
        return new byte[] { 0x30, 0x14, (byte) 0xa0, 0x09, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x16, 0x28, (byte) 0x81, 0x00,
                0x70, (byte) 0xa1, 0x07, 0x04, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x00 };
    }

    public byte[] getEncodedDataFull() {
        return new byte[] { 48, 89, -96, 9, -127, 7, -111, 85, 22, 40, -127, 0, 112, -95, 7, 4, 5, -111, 85, 22, 9, 0, -94, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, -125, 5, 11, 12, 13, 14, 15, -124, 5, 21, 22, 23, 24, 25, -123, 5, 31, 32, 33,
                34, 35, -122, 5, 41, 42, 43, 44, 45 };
    }

    public byte[] getEncodedGSNAddress1() {
        return new byte[] { 11, 12, 13, 14, 15 };
    }

    public byte[] getEncodedGSNAddress2() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    public byte[] getEncodedGSNAddress3() {
        return new byte[] { 31, 32, 33, 34, 35 };
    }

    public byte[] getEncodedGSNAddress4() {
        return new byte[] { 41, 42, 43, 44, 45 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SendRoutingInfoForLCSResponseImpl impl = new SendRoutingInfoForLCSResponseImpl();
        impl.decodeAll(asn);

        SubscriberIdentity subsIdent = impl.getTargetMS();
        assertNotNull(subsIdent);

        IMSI imsi = subsIdent.getIMSI();
        ISDNAddressString msisdn = subsIdent.getMSISDN();

        assertNotNull(msisdn);
        assertNull(imsi);

        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(msisdn.getAddress().equals("556182180007"));

        LCSLocationInfo lcsLocInfo = impl.getLCSLocationInfo();
        assertNotNull(lcsLocInfo);

        ISDNAddressString networkNodeNumber = lcsLocInfo.getNetworkNodeNumber();
        assertNotNull(networkNodeNumber);
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(networkNodeNumber.getAddress().equals("55619000"));

        assertNull(impl.getExtensionContainer());
        assertNull(impl.getVgmlcAddress());
        assertNull(impl.getHGmlcAddress());
        assertNull(impl.getPprAddress());
        assertNull(impl.getAdditionalVGmlcAddress());

        data = getEncodedDataFull();

        asn = new AsnInputStream(data);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        impl = new SendRoutingInfoForLCSResponseImpl();
        impl.decodeAll(asn);

        subsIdent = impl.getTargetMS();
        assertNotNull(subsIdent);

        imsi = subsIdent.getIMSI();
        msisdn = subsIdent.getMSISDN();

        assertNotNull(msisdn);
        assertNull(imsi);

        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(msisdn.getAddress().equals("556182180007"));

        lcsLocInfo = impl.getLCSLocationInfo();
        assertNotNull(lcsLocInfo);

        networkNodeNumber = lcsLocInfo.getNetworkNodeNumber();
        assertNotNull(networkNodeNumber);
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(networkNodeNumber.getAddress().equals("55619000"));

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));
        assertTrue(Arrays.equals(impl.getVgmlcAddress().getData(), getEncodedGSNAddress1()));
        assertTrue(Arrays.equals(impl.getHGmlcAddress().getData(), getEncodedGSNAddress2()));
        assertTrue(Arrays.equals(impl.getPprAddress().getData(), getEncodedGSNAddress3()));
        assertTrue(Arrays.equals(impl.getAdditionalVGmlcAddress().getData(), getEncodedGSNAddress4()));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        byte[] data = getEncodedData();

        ISDNAddressString msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "556182180007");
        SubscriberIdentity subsIdent = new SubscriberIdentityImpl(msisdn);

        ISDNAddressString networkNodeNumber = this.MAPParameterFactory.createISDNAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "55619000");

        LCSLocationInfo lcsLocInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, false, null, null, null, null, null);

        SendRoutingInfoForLCSResponseImpl impl = new SendRoutingInfoForLCSResponseImpl(subsIdent, lcsLocInfo, null, null, null,
                null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        data = getEncodedDataFull();

        GSNAddressImpl vgmlcAddress = new GSNAddressImpl(getEncodedGSNAddress1());
        GSNAddressImpl hGmlcAddress = new GSNAddressImpl(getEncodedGSNAddress2());
        GSNAddressImpl pprAddress = new GSNAddressImpl(getEncodedGSNAddress3());
        GSNAddressImpl additionalVGmlcAddress = new GSNAddressImpl(getEncodedGSNAddress4());

        impl = new SendRoutingInfoForLCSResponseImpl(subsIdent, lcsLocInfo,
                MAPExtensionContainerTest.GetTestExtensionContainer(), vgmlcAddress, hGmlcAddress, pprAddress,
                additionalVGmlcAddress);
        // SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo, MAPExtensionContainer extensionContainer,
        // GSNAddress vgmlcAddress, GSNAddress hGmlcAddress, GSNAddress pprAddress, GSNAddress additionalVGmlcAddress

        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }
}
