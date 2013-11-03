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
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.primitives.MAPPrivateExtensionImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SLRArgExtensionContainerTest {

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
        return new byte[] { 48, 14, -96, 10, 48, 8, 6, 3, 42, 22, 33, 1, 2, 3, -95, 0 };
    }

    public long[] getDataOId() {
        return new long[] { 1, 2, 22, 33 };
    }

    public byte[] getDataPe() {
        return new byte[] { 1, 2, 3 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SLRArgExtensionContainerImpl imp = new SLRArgExtensionContainerImpl();
        imp.decodeAll(asn);

        assertEquals(imp.getPrivateExtensionList().size(), 1);
        assertTrue(Arrays.equals(imp.getPrivateExtensionList().get(0).getOId(), getDataOId()));
        assertTrue(Arrays.equals(imp.getPrivateExtensionList().get(0).getData(), getDataPe()));
        assertFalse(imp.getSlrArgPcsExtensions().getNaEsrkRequest());

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        byte[] data = getEncodedData();

        ArrayList<MAPPrivateExtension> privateExtensionList = new ArrayList<MAPPrivateExtension>();
        MAPPrivateExtensionImpl pe = new MAPPrivateExtensionImpl(getDataOId(), getDataPe());
        privateExtensionList.add(pe);
        SLRArgPCSExtensionsImpl slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(false);

        SLRArgExtensionContainerImpl imp = new SLRArgExtensionContainerImpl(privateExtensionList, slrArgPcsExtensions);
        // ArrayList<MAPPrivateExtension> privateExtensionList, SLRArgPCSExtensions slrArgPcsExtensions

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

    }
}
