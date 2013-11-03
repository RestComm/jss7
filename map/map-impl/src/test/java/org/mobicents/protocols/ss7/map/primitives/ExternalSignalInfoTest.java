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

package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.primitives.ProtocolId;
import org.mobicents.protocols.ss7.map.api.primitives.SignalInfo;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/*
 *
 * @author cristian veliscu
 *
 */
public class ExternalSignalInfoTest {
    Logger logger = Logger.getLogger(ExternalSignalInfoTest.class);

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

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {
        byte[] data = new byte[] { 48, 9, 10, 1, 2, 4, 4, 10, 20, 30, 40 };
        byte[] data_ = new byte[] { 10, 20, 30, 40 };

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        ExternalSignalInfoImpl extSignalInfo = new ExternalSignalInfoImpl();
        extSignalInfo.decodeAll(asn);

        ProtocolId protocolId = extSignalInfo.getProtocolId();
        byte[] signalInfo = extSignalInfo.getSignalInfo().getData();

        assertTrue(Arrays.equals(data_, signalInfo));
        assertNotNull(protocolId);
        assertTrue(protocolId == ProtocolId.gsm_0806);
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { 48, 9, 10, 1, 2, 4, 4, 10, 20, 30, 40 };
        byte[] data_ = new byte[] { 10, 20, 30, 40 };

        SignalInfo signalInfo = new SignalInfoImpl(data_);
        ProtocolId protocolId = ProtocolId.gsm_0806;
        ExternalSignalInfoImpl extSignalInfo = new ExternalSignalInfoImpl(signalInfo, protocolId, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        extSignalInfo.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.callhandling" })
    public void testSerialization() throws Exception {

    }
}
