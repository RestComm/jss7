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

package org.mobicents.protocols.ss7.map.smstpdu;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriodFormat;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsSubmitTpduTest {

    public byte[] getData1() {
        return new byte[] { 81, 83, 11, -111, -103, -119, -120, 119, 7, -16, 0, 0, -83, 41, 5, 0, 3, -17, 2, 1, -112, 101, 54,
                -5, 13, 10, -123, 66, 33, 80, 44, 22, 3, -55, 100, 50, -48, 108, 54, 3, -47, 104, 52, 80, -83, 86, 3, -39, 108,
                54 };
    }

    public byte[] getData1A() {
        return new byte[] { -17, 2, 1 };
    }

    public byte[] getData2() {
        return new byte[] { -99, -31, 15, -111, 17, 17, 17, 17, 33, 34, 0, -16, 1, 8, 17, 16, 3, 32, 3, -123, 72, 102, 0, 65,
                0, 100, 0, 100, 0, 114, 0, 101, 0, 115, 0, 115, 0, 70, 0, 105, 0, 101, 0, 108, 0, 100, 0, 73, 0, 109, 0, 112,
                0, 108, 0, 32, 0, 100, 0, 101, 0, 115, 0, 116, 0, 65, 0, 100, 0, 100, 0, 114, 0, 101, 0, 115, 0, 115, 0, 32, 0,
                61, 0, 32, 0, 110, 0, 101, 0, 119, 0, 32, 0, 65, 0, 100, 0, 100, 0, 114, 0, 101, 0, 115, 0, 115, 0, 70, 0, 105,
                0, 101, 0, 108, 0, 100, 0, 73, 0, 109, 0, 112, 0, 108 };
    }

    public byte[] getData3() {
        return new byte[] { -87, 0, 3, -29, 0, -16, 2, 4, 1, 2, 3, 4, 5, 6, 7, 51, 65, 100, 100, 114, 101, 115, 115, 70, 105,
                101, 108, 100, 73, 109, 112, 108, 32, 100, 101, 115, 116, 65, 100, 100, 114, 101, 115, 115, 32, 61, 32, 110,
                101, 119, 32, 65, 100, 100, 114, 101, 115, 115, 70, 105, 101, 108, 100, 73, 109, 112, 108 };
    }

    public byte[] getData3B() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7 };
    }

    @Test(groups = { "functional.decode", "smstpdu" })
    public void testDecode() throws Exception {

        SmsSubmitTpduImpl impl = new SmsSubmitTpduImpl(this.getData1(), null);
        ;
        impl.getUserData().decode();
        assertFalse(impl.getRejectDuplicates());
        assertEquals(impl.getValidityPeriodFormat(), ValidityPeriodFormat.fieldPresentRelativeFormat);
        assertFalse(impl.getReplyPathExists());
        assertTrue(impl.getUserDataHeaderIndicator());
        assertFalse(impl.getStatusReportRequest());
        assertEquals(impl.getMessageReference(), 83);
        assertEquals(impl.getDestinationAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(impl.getDestinationAddress().getNumberingPlanIdentification(),
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertTrue(impl.getDestinationAddress().getAddressValue().equals("99988877700"));
        assertEquals(impl.getProtocolIdentifier().getCode(), 0);
        assertEquals(impl.getDataCodingScheme().getCode(), 0);
        assertEquals((int) impl.getValidityPeriod().getRelativeFormatValue(), 173);
        assertEquals(impl.getUserDataLength(), 41);
        assertTrue(impl.getUserData().getDecodedMessage().equals("Hello !!!! 111 222 333 444 555 666"));
        assertTrue(Arrays.equals(impl.getUserData().getDecodedUserDataHeader().getInformationElementData(0), this.getData1A()));

        impl = new SmsSubmitTpduImpl(this.getData2(), null);
        ;
        impl.getUserData().decode();
        assertTrue(impl.getRejectDuplicates());
        assertEquals(impl.getValidityPeriodFormat(), ValidityPeriodFormat.fieldPresentAbsoluteFormat);
        assertTrue(impl.getReplyPathExists());
        assertFalse(impl.getUserDataHeaderIndicator());
        assertFalse(impl.getStatusReportRequest());
        assertEquals(impl.getMessageReference(), 225);
        assertEquals(impl.getDestinationAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(impl.getDestinationAddress().getNumberingPlanIdentification(),
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertTrue(impl.getDestinationAddress().getAddressValue().equals("111111111222000"));
        assertEquals(impl.getProtocolIdentifier().getCode(), 1);
        assertEquals(impl.getDataCodingScheme().getCode(), 8);
        assertEquals((int) impl.getValidityPeriod().getAbsoluteFormatValue().getYear(), 11);
        assertEquals((int) impl.getValidityPeriod().getAbsoluteFormatValue().getMonth(), 1);
        assertEquals((int) impl.getValidityPeriod().getAbsoluteFormatValue().getDay(), 30);
        assertEquals((int) impl.getValidityPeriod().getAbsoluteFormatValue().getHour(), 2);
        assertEquals((int) impl.getValidityPeriod().getAbsoluteFormatValue().getMinute(), 30);
        assertEquals((int) impl.getValidityPeriod().getAbsoluteFormatValue().getSecond(), 58);
        assertEquals((int) impl.getValidityPeriod().getAbsoluteFormatValue().getTimeZone(), -4);
        assertEquals(impl.getUserDataLength(), 102);
        assertTrue(impl.getUserData().getDecodedMessage().equals("AddressFieldImpl destAddress = new AddressFieldImpl"));
        assertNull(impl.getUserData().getDecodedUserDataHeader());

        impl = new SmsSubmitTpduImpl(this.getData3(), Charset.forName("US-ASCII"));
        impl.getUserData().decode();
        assertFalse(impl.getRejectDuplicates());
        assertEquals(impl.getValidityPeriodFormat(), ValidityPeriodFormat.fieldPresentEnhancedFormat);
        assertTrue(impl.getReplyPathExists());
        assertFalse(impl.getUserDataHeaderIndicator());
        assertTrue(impl.getStatusReportRequest());
        assertEquals(impl.getMessageReference(), 0);
        assertEquals(impl.getDestinationAddress().getTypeOfNumber(), TypeOfNumber.AbbreviatedNumber);
        assertEquals(impl.getDestinationAddress().getNumberingPlanIdentification(),
                NumberingPlanIdentification.DataNumberingPlan);
        assertTrue(impl.getDestinationAddress().getAddressValue().equals("000"));
        assertEquals(impl.getProtocolIdentifier().getCode(), 2);
        assertEquals(impl.getDataCodingScheme().getCode(), 4);
        assertTrue(Arrays.equals(impl.getValidityPeriod().getEnhancedFormatValue().getData(), this.getData3B()));
        assertEquals(impl.getUserDataLength(), 51);
        assertTrue(impl.getUserData().getDecodedMessage().equals("AddressFieldImpl destAddress = new AddressFieldImpl"));
        assertNull(impl.getUserData().getDecodedUserDataHeader());
    }

    @Test(groups = { "functional.encode", "smstpdu" })
    public void testEncode() throws Exception {

        // SortedMap<String,Charset> rr = Charset.availableCharsets();

        UserDataHeaderImpl udh = new UserDataHeaderImpl();
        udh.addInformationElement(0, this.getData1A());
        UserDataImpl ud = new UserDataImpl("Hello !!!! 111 222 333 444 555 666", new DataCodingSchemeImpl(0), udh, null);
        AddressFieldImpl destAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "99988877700");
        ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(0);
        ValidityPeriodImpl vp = new ValidityPeriodImpl(173);
        SmsSubmitTpduImpl impl = new SmsSubmitTpduImpl(false, false, false, 83, destAddress, pi, vp, ud);
        byte[] enc = impl.encodeData();
        assertTrue(Arrays.equals(enc, this.getData1()));

        ud = new UserDataImpl("AddressFieldImpl destAddress = new AddressFieldImpl", new DataCodingSchemeImpl(8), null, null);
        destAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "111111111222000");
        pi = new ProtocolIdentifierImpl(1);
        AbsoluteTimeStampImpl ts = new AbsoluteTimeStampImpl(11, 1, 30, 2, 30, 58, -4);
        vp = new ValidityPeriodImpl(ts);
        impl = new SmsSubmitTpduImpl(true, true, false, 225, destAddress, pi, vp, ud);
        enc = impl.encodeData();
        assertTrue(Arrays.equals(enc, this.getData2()));

        ud = new UserDataImpl("AddressFieldImpl destAddress = new AddressFieldImpl", new DataCodingSchemeImpl(4), null,
                Charset.forName("US-ASCII"));
        destAddress = new AddressFieldImpl(TypeOfNumber.AbbreviatedNumber, NumberingPlanIdentification.DataNumberingPlan, "000");
        pi = new ProtocolIdentifierImpl(2);
        ValidityEnhancedFormatDataImpl efd = new ValidityEnhancedFormatDataImpl(this.getData3B());
        vp = new ValidityPeriodImpl(efd);
        impl = new SmsSubmitTpduImpl(false, true, true, 0, destAddress, pi, vp, ud);
        enc = impl.encodeData();
        assertTrue(Arrays.equals(enc, this.getData3()));

        // boolean rejectDuplicates, boolean replyPathExists, boolean statusReportRequest
    }
}
