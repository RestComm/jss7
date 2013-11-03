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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CancelLocationRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { -93, 72, 4, 5, 17, 17, 33, 34, 34, 10, 1, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6,
                48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -125, 4, -111, 34, 34, -8, -124, 4, -111, 34, 34, -7, -123, 4,
                0, 3, 98, 39 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { -93, 84, 48, 13, 4, 5, 17, 17, 33, 34, 34, 4, 4, 0, 3, 98, 39, 10, 1, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 0, -126, 0, -125, 4, -111, 34,
                34, -8, -124, 4, -111, 34, 34, -7, -123, 4, 0, 3, 98, 39 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 4, 5, 17, 17, 33, 34, 34 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 13, 4, 5, 17, 17, 33, 34, 34, 4, 4, 0, 3, 98, 39 };
    }

    public byte[] getDataLmsi() {
        return new byte[] { 0, 3, 98, 39 };
    }

    @Test(groups = { "functional.decode", "locationManagement" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        CancelLocationRequestImpl asc = new CancelLocationRequestImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, CancelLocationRequestImpl.TAG_cancelLocationRequest);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertEquals(asc.getMapProtocolVersion(), 3);

        IMSI imsi = asc.getImsi();
        IMSIWithLMSI imsiWithLmsi = asc.getImsiWithLmsi();
        CancellationType cancellationType = asc.getCancellationType();
        MAPExtensionContainer extensionContainer = asc.getExtensionContainer();
        TypeOfUpdate typeOfUpdate = asc.getTypeOfUpdate();
        boolean mtrfSupportedAndAuthorized = asc.getMtrfSupportedAndAuthorized();
        boolean mtrfSupportedAndNotAuthorized = asc.getMtrfSupportedAndNotAuthorized();
        ISDNAddressString newMSCNumber = asc.getNewMSCNumber();
        ISDNAddressString newVLRNumber = asc.getNewVLRNumber();
        LMSI newLmsi = asc.getNewLmsi();
        long mapProtocolVersion = asc.getMapProtocolVersion();

        assertTrue(imsi.getData().equals("1111122222"));
        assertNull(imsiWithLmsi);
        assertEquals(cancellationType.getCode(), 1);
        assertNotNull(extensionContainer);
        assertEquals(typeOfUpdate.getCode(), 0);
        assertFalse(mtrfSupportedAndAuthorized);
        assertFalse(mtrfSupportedAndNotAuthorized);
        assertTrue(newMSCNumber.getAddress().equals("22228"));
        assertEquals(newMSCNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(newMSCNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(newVLRNumber.getAddress().equals("22229"));
        assertEquals(newVLRNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(newVLRNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(Arrays.equals(newLmsi.getData(), getDataLmsi()));
        assertEquals(mapProtocolVersion, 3);

        // encode data 1
        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new CancelLocationRequestImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, CancelLocationRequestImpl.TAG_cancelLocationRequest);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertEquals(asc.getMapProtocolVersion(), 3);

        imsi = asc.getImsi();
        imsiWithLmsi = asc.getImsiWithLmsi();
        cancellationType = asc.getCancellationType();
        extensionContainer = asc.getExtensionContainer();
        typeOfUpdate = asc.getTypeOfUpdate();
        mtrfSupportedAndAuthorized = asc.getMtrfSupportedAndAuthorized();
        mtrfSupportedAndNotAuthorized = asc.getMtrfSupportedAndNotAuthorized();
        newMSCNumber = asc.getNewMSCNumber();
        newVLRNumber = asc.getNewVLRNumber();
        newLmsi = asc.getNewLmsi();
        mapProtocolVersion = asc.getMapProtocolVersion();

        assertNull(imsi);
        assertNotNull(imsiWithLmsi);
        assertTrue(imsiWithLmsi.getImsi().getData().equals("1111122222"));
        LMSI lmsi = imsiWithLmsi.getLmsi();
        assertTrue(Arrays.equals(lmsi.getData(), getDataLmsi()));

        assertEquals(cancellationType.getCode(), 1);
        assertNotNull(extensionContainer);
        assertEquals(typeOfUpdate.getCode(), 0);
        assertTrue(mtrfSupportedAndAuthorized);
        assertTrue(mtrfSupportedAndNotAuthorized);
        assertTrue(newMSCNumber.getAddress().equals("22228"));
        assertEquals(newMSCNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(newMSCNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(newVLRNumber.getAddress().equals("22229"));
        assertEquals(newVLRNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(newVLRNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(Arrays.equals(newLmsi.getData(), getDataLmsi()));
        assertEquals(mapProtocolVersion, 3);

        // encode data 2
        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new CancelLocationRequestImpl(2);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 2);

        imsi = asc.getImsi();
        imsiWithLmsi = asc.getImsiWithLmsi();
        cancellationType = asc.getCancellationType();
        extensionContainer = asc.getExtensionContainer();
        typeOfUpdate = asc.getTypeOfUpdate();
        mtrfSupportedAndAuthorized = asc.getMtrfSupportedAndAuthorized();
        mtrfSupportedAndNotAuthorized = asc.getMtrfSupportedAndNotAuthorized();
        newMSCNumber = asc.getNewMSCNumber();
        newVLRNumber = asc.getNewVLRNumber();
        newLmsi = asc.getNewLmsi();
        mapProtocolVersion = asc.getMapProtocolVersion();

        assertTrue(imsi.getData().equals("1111122222"));
        assertNull(imsiWithLmsi);
        assertNull(cancellationType);
        assertNull(extensionContainer);
        assertNull(typeOfUpdate);
        assertFalse(mtrfSupportedAndAuthorized);
        assertFalse(mtrfSupportedAndNotAuthorized);
        assertNull(newMSCNumber);
        assertNull(newVLRNumber);
        assertNull(newLmsi);
        assertEquals(mapProtocolVersion, 2);

        // encode data 3
        rawData = getEncodedData3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new CancelLocationRequestImpl(2);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 2);

        imsi = asc.getImsi();
        imsiWithLmsi = asc.getImsiWithLmsi();
        cancellationType = asc.getCancellationType();
        extensionContainer = asc.getExtensionContainer();
        typeOfUpdate = asc.getTypeOfUpdate();
        mtrfSupportedAndAuthorized = asc.getMtrfSupportedAndAuthorized();
        mtrfSupportedAndNotAuthorized = asc.getMtrfSupportedAndNotAuthorized();
        newMSCNumber = asc.getNewMSCNumber();
        newVLRNumber = asc.getNewVLRNumber();
        newLmsi = asc.getNewLmsi();
        mapProtocolVersion = asc.getMapProtocolVersion();

        assertNull(imsi);
        // assertNotNull(imsiWithLmsi);
        assertTrue(imsiWithLmsi.getImsi().getData().equals("1111122222"));
        lmsi = imsiWithLmsi.getLmsi();
        assertTrue(Arrays.equals(lmsi.getData(), getDataLmsi()));
        assertNull(cancellationType);
        assertNull(extensionContainer);
        assertNull(typeOfUpdate);
        assertFalse(mtrfSupportedAndAuthorized);
        assertFalse(mtrfSupportedAndNotAuthorized);
        assertNull(newMSCNumber);
        assertNull(newVLRNumber);
        assertNull(newLmsi);
        assertEquals(mapProtocolVersion, 2);
    }

    public static MAPExtensionContainer GetTestExtensionContainer() {
        MAPParameterFactory mapServiceFactory = new MAPParameterFactoryImpl();

        ArrayList<MAPPrivateExtension> al = new ArrayList<MAPPrivateExtension>();
        al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 4 }, new byte[] { 11, 12, 13, 14, 15 }));
        al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 6 }, null));
        al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 5 }, new byte[] { 21, 22, 23, 24, 25, 26 }));

        MAPExtensionContainer cnt = mapServiceFactory.createMAPExtensionContainer(al, new byte[] { 31, 32, 33 });

        return cnt;
    }

    @Test(groups = { "functional.encode", "locationManagement" })
    public void testEncode() throws Exception {

        IMSI imsi = new IMSIImpl("1111122222");
        LMSIImpl lmsi = new LMSIImpl(getDataLmsi());
        IMSIWithLMSI imsiWithLmsi = new IMSIWithLMSIImpl(imsi, lmsi);
        CancellationType cancellationType = CancellationType.getInstance(1);
        MAPExtensionContainer extensionContainer = GetTestExtensionContainer();
        TypeOfUpdate typeOfUpdate = TypeOfUpdate.getInstance(0);
        boolean mtrfSupportedAndAuthorized = false;
        boolean mtrfSupportedAndNotAuthorized = false;
        ISDNAddressString newMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNAddressString newVLRNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22229");
        LMSI newLmsi = new LMSIImpl(getDataLmsi());
        long mapProtocolVersion = 3;

        CancelLocationRequestImpl asc = new CancelLocationRequestImpl(imsi, null, cancellationType, extensionContainer,
                typeOfUpdate, mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi,
                mapProtocolVersion);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        mtrfSupportedAndAuthorized = true;
        mtrfSupportedAndNotAuthorized = true;
        asc = new CancelLocationRequestImpl(null, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
                mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi,
                mapProtocolVersion);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        mapProtocolVersion = 2;
        asc = new CancelLocationRequestImpl(imsi, null, null, null, null, false, false, null, null, null, mapProtocolVersion);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        asc = new CancelLocationRequestImpl(null, imsiWithLmsi, null, null, null, false, false, null, null, null,
                mapProtocolVersion);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));

    }
}
