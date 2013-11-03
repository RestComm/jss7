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

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.PlmnIdImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SendAuthenticationInfoRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 21, -128, 6, 17, 33, 34, 51, 67, 68, 2, 1, 4, -125, 1, 0, -124, 3, -71, -2, -59, -122, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 57, -128, 6, 51, 51, 67, 68, 68, -12, 2, 1, 5, 5, 0, -127, 0, 48, 34, 4, 16, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 14, 2, 2, 2, 2, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3, -125, 1, 1, -123, 1, 6 };
    }

    private byte[] getEncodedData_V2() {
        return new byte[] { 4, 8, 82, 0, 7, 34, 2, 35, 103, -9 };
    }

    private byte[] getRequestingPlmnId() {
        return new byte[] { (byte) 185, (byte) 254, (byte) 197 };
    }

    @Test
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        SendAuthenticationInfoRequestImpl asc = new SendAuthenticationInfoRequestImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 3);

        IMSI imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));
        assertEquals(asc.getRequestingNodeType(), RequestingNodeType.vlr);
        assertEquals(asc.getNumberOfRequestedVectors(), 4);

        assertNotNull(asc.getRequestingPlmnId());
        assertTrue(Arrays.equals(asc.getRequestingPlmnId().getData(), getRequestingPlmnId()));

        assertNull(asc.getReSynchronisationInfo());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getNumberOfRequestedAdditionalVectors());

        assertFalse(asc.getSegmentationProhibited());
        assertFalse(asc.getImmediateResponsePreferred());
        assertTrue(asc.getAdditionalVectorsAreForEPS());

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new SendAuthenticationInfoRequestImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 3);

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("33333444444"));
        assertEquals(asc.getRequestingNodeType(), RequestingNodeType.sgsn);
        assertEquals(asc.getNumberOfRequestedVectors(), 5);

        assertNull(asc.getRequestingPlmnId());

        ReSynchronisationInfo rsi = asc.getReSynchronisationInfo();
        assertTrue(Arrays.equals(rsi.getRand(), ReSynchronisationInfoTest.getRandData()));
        assertTrue(Arrays.equals(rsi.getAuts(), ReSynchronisationInfoTest.getAutsData()));

        assertNull(asc.getExtensionContainer());
        assertEquals((int) asc.getNumberOfRequestedAdditionalVectors(), 6);

        assertTrue(asc.getSegmentationProhibited());
        assertTrue(asc.getImmediateResponsePreferred());
        assertFalse(asc.getAdditionalVectorsAreForEPS());

        rawData = getEncodedData_V2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new SendAuthenticationInfoRequestImpl(2);
        asc.decodeAll(asn);
        assertEquals(asc.getMapProtocolVersion(), 2);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("250070222032767"));
        assertNull(asc.getRequestingNodeType());
        assertEquals(asc.getNumberOfRequestedVectors(), 0);

        assertNull(asc.getRequestingPlmnId());

        assertNull(asc.getReSynchronisationInfo());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getNumberOfRequestedAdditionalVectors());

        assertFalse(asc.getSegmentationProhibited());
        assertFalse(asc.getImmediateResponsePreferred());
        assertFalse(asc.getAdditionalVectorsAreForEPS());

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        IMSIImpl imsi = new IMSIImpl("111222333444");
        PlmnIdImpl plmnId = new PlmnIdImpl(getRequestingPlmnId());
        SendAuthenticationInfoRequestImpl asc = new SendAuthenticationInfoRequestImpl(3, imsi, 4, false, false, null, null,
                RequestingNodeType.vlr, plmnId, null, true);
        // long mapProtocolVersion, IMSI imsi, int numberOfRequestedVectors, boolean segmentationProhibited,
        // boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo, MAPExtensionContainer
        // extensionContainer,
        // RequestingNodeType requestingNodeType, PlmnId requestingPlmnId, Integer numberOfRequestedAdditionalVectors, boolean
        // additionalVectorsAreForEPS

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("33333444444");
        ReSynchronisationInfoImpl rsi = new ReSynchronisationInfoImpl(ReSynchronisationInfoTest.getRandData(),
                ReSynchronisationInfoTest.getAutsData());
        asc = new SendAuthenticationInfoRequestImpl(3, imsi, 5, true, true, rsi, null, RequestingNodeType.sgsn, null, 6, false);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("250070222032767");
        asc = new SendAuthenticationInfoRequestImpl(2, imsi, 0, false, false, null, null, null, null, null, false);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_V2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
