/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AccessType;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class AuthenticationFailureReportRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 11, 4, 6, 17, 33, 34, 51, 67, 68, 10, 1, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 88, 4, 6, 17, 33, 34, 51, 67, 68, 10, 1, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6,
                48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 1, 1, -1, 10, 1, 2, 4, 16, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                15, 16, -128, 4, -111, 17, 17, 51, -127, 4, -111, 17, 17, 68 };
    }

    private byte[] getRandBalue() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
    }

    @Test
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        AuthenticationFailureReportRequestImpl asc = new AuthenticationFailureReportRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        IMSI imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));
        assertEquals(asc.getFailureCause(), FailureCause.wrongUserResponse);
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getReAttempt());
        assertNull(asc.getAccessType());
        assertNull(asc.getRand());
        assertNull(asc.getVlrNumber());
        assertNull(asc.getSgsnNumber());


        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new AuthenticationFailureReportRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));
        assertEquals(asc.getFailureCause(), FailureCause.wrongNetworkSignature);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));
        assertTrue(asc.getReAttempt());
        assertEquals(asc.getAccessType(), AccessType.locationUpdating);
        assertEquals(asc.getRand(), getRandBalue());
        assertEquals(asc.getVlrNumber().getAddress(), "111133");
        assertEquals(asc.getSgsnNumber().getAddress(), "111144");

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        IMSIImpl imsi = new IMSIImpl("111222333444");
        AuthenticationFailureReportRequestImpl asc = new AuthenticationFailureReportRequestImpl(imsi, FailureCause.wrongUserResponse, null, null, null, null,
                null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ISDNAddressString vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "111133");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "111144");
        asc = new AuthenticationFailureReportRequestImpl(imsi, FailureCause.wrongNetworkSignature, MAPExtensionContainerTest.GetTestExtensionContainer(), true,
                AccessType.locationUpdating, getRandBalue(), vlrNumber, sgsnNumber);
        // IMSI imsi, FailureCause failureCause, MAPExtensionContainer
        // extensionContainer, Boolean reAttempt,
        // AccessType accessType, byte[] rand, ISDNAddressString vlrNumber,
        // ISDNAddressString sgsnNumber

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
