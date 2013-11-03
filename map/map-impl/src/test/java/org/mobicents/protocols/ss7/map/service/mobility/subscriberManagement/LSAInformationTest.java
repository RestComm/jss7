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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAAttributes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAOnlyAccessIndicator;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class LSAInformationTest {

    public byte[] getData() {
        return new byte[] { 48, 101, 5, 0, -127, 1, 1, -94, 53, 48, 51, -128, 3, 12, 34, 26, -127, 1, 5, -126, 0, -93, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getDataLSAIdentity() {
        return new byte[] { 12, 34, 26 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        LSAInformationImpl prim = new LSAInformationImpl();

        prim.decodeAll(asn);
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getCompleteDataListIncluded());
        assertEquals(prim.getLSAOnlyAccessIndicator(), LSAOnlyAccessIndicator.accessOutsideLSAsRestricted);

        ArrayList<LSAData> lsaDataList = prim.getLSADataList();
        assertNotNull(lsaDataList);
        assertEquals(lsaDataList.size(), 1);
        LSAData lsaData = lsaDataList.get(0);

        assertTrue(Arrays.equals(lsaData.getLSAIdentity().getData(), this.getDataLSAIdentity()));
        assertEquals(lsaData.getLSAAttributes().getData(), 5);
        assertTrue(lsaData.getLsaActiveModeIndicator());
        assertNotNull(lsaData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lsaData.getExtensionContainer()));

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        boolean completeDataListIncluded = true;
        LSAOnlyAccessIndicator lsaOnlyAccessIndicator = LSAOnlyAccessIndicator.accessOutsideLSAsRestricted;
        ArrayList<LSAData> lsaDataList = new ArrayList<LSAData>();
        LSAIdentity lsaIdentity = new LSAIdentityImpl(this.getDataLSAIdentity());
        LSAAttributes lsaAttributes = new LSAAttributesImpl(5);
        boolean lsaActiveModeIndicator = true;
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        LSAData lsaData = new LSADataImpl(lsaIdentity, lsaAttributes, lsaActiveModeIndicator, extensionContainer);
        lsaDataList.add(lsaData);

        LSAInformationImpl prim = new LSAInformationImpl(completeDataListIncluded, lsaOnlyAccessIndicator, lsaDataList,
                extensionContainer);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
