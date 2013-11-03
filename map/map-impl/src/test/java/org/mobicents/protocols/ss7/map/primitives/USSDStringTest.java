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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSNationalLanguage;
import org.mobicents.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class USSDStringTest {
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

    public static byte[] getDataGSM7() {
        return new byte[] { 0x04, 0x04, 0x2a, 0x1c, 0x6e, (byte) 0x04 };
    }

    public static byte[] getDataUcs2() {
        return new byte[] { 4, 8, 0, 42, 0, 56, 0, 56, 0, 35 };
    }

    public static byte[] getDataUcs2Lang() {
        return new byte[] { 4, 11, -14, -70, 2, 0, 71, 0, 103, 0, 84, 0, 116 };
    }

    public static byte[] getDataGsm7Lang() {
        return new byte[] { 4, 7, -14, -70, -30, 120, -90, -46, 27 };
    }

    public static byte[] getDataGsm7LangArabic() {
        return new byte[] { 4, 8, 16, 80, 68, 50, 1, -123, 55, 65 };
    }

    public static String getStringGsm7LangArabic() {
        return "\u062B \u062C\u0681\u0684 aA";
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = getDataGSM7();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        USSDStringImpl ussdStr = new USSDStringImpl(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7,
                CharacterSet.GSM7, null, null, false));
        ussdStr.decodeAll(asn);

        assertTrue(ussdStr.getString(null).equals("*88#"));

        data = getDataUcs2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        ussdStr = new USSDStringImpl(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralDataCodingIndication,
                CharacterSet.UCS2, null, null, false));
        ussdStr.decodeAll(asn);
        assertTrue(ussdStr.getString(null).equals("*88#"));

        data = getDataUcs2Lang();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        ussdStr = new USSDStringImpl(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication,
                CharacterSet.UCS2, null, null, false));
        ussdStr.decodeAll(asn);
        assertTrue(ussdStr.getString(null).equals("ru\nGgTt"));

        data = getDataGsm7Lang();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        ussdStr = new USSDStringImpl(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication,
                CharacterSet.GSM7, null, null, false));
        ussdStr.decodeAll(asn);
        assertTrue(ussdStr.getString(null).equals("ru\nGgTt"));

        data = getDataGsm7LangArabic();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        ussdStr = new USSDStringImpl(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7,
                CBSNationalLanguage.Arabic, null, false));
        ussdStr.decodeAll(asn);
        assertTrue(ussdStr.getString(null).equals(getStringGsm7LangArabic()));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        byte[] data = getDataGSM7();

        CBSDataCodingScheme dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, null, null,
                false);
        USSDStringImpl ussdStr = new USSDStringImpl("*88#", dcs, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ussdStr.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        data = getDataUcs2();
        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralDataCodingIndication, CharacterSet.UCS2, null, null, false);
        ussdStr = new USSDStringImpl("*88#", dcs, null);
        asnOS = new AsnOutputStream();
        ussdStr.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataUcs2Lang();
        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication, CharacterSet.UCS2, null, null,
                false);
        ussdStr = new USSDStringImpl("ru\nGgTt", dcs, null);
        asnOS = new AsnOutputStream();
        ussdStr.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataGsm7Lang();
        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication, CharacterSet.GSM7, null, null,
                false);
        ussdStr = new USSDStringImpl("ru\nGgTt", dcs, null);
        asnOS = new AsnOutputStream();
        ussdStr.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataGsm7LangArabic();
        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, CBSNationalLanguage.Arabic, null,
                false);
        assertEquals(dcs.getCode(), 34);
        // CBSDataCodingGroup dataCodingGroup, CharacterSet characterSet, CBSNationalLanguage nationalLanguageShiftTable,
        // DataCodingSchemaMessageClass messageClass, boolean isCompressed
        String s1 = getStringGsm7LangArabic();
        ussdStr = new USSDStringImpl(s1, dcs, null);
        asnOS = new AsnOutputStream();
        ussdStr.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "primitives" })
    public void testSerialization() throws Exception {
        CBSDataCodingScheme dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, null, null,
                false);
        USSDStringImpl original = new USSDStringImpl("*88#", dcs, null);
        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        // deserialize
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        USSDStringImpl copy = (USSDStringImpl) o;

        // test result
        assertTrue(copy.getString(null).equals(original.getString(null)));

        // TODO Charset is not Serializable now
        // assertEquals(copy.getCharset(), original.getCharset());
    }
}
