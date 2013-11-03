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
package org.mobicents.protocols.ss7.cap.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.SMSAddressStringImpl;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class ConnectSMSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 48, -128, 9, -111, 33, 67, 101, -121, 25, 50, 84, 118, -127, 7, -111, 20, -121, 8, 80,
                64, -9, -126, 6, -111, 34, 112, 87, 0, -128, -86, 18, 48, 5, 2, 1, 2, -127, 0, 48, 9, 2, 1, 3, 10, 1,
                1, -127, 1, -1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ConnectSMSRequestImpl prim = new ConnectSMSRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        CalledPartyBCDNumber destinationSubscriberNumber = prim.getDestinationSubscriberNumber();
        SMSAddressString callingPartyNumber = prim.getCallingPartysNumber();

        assertNotNull(destinationSubscriberNumber);
        assertTrue(destinationSubscriberNumber.getAddress().equals("41788005047"));

        assertNotNull(callingPartyNumber);
        assertTrue(callingPartyNumber.getAddress().equals("1234567891234567"));

        ISDNAddressString smscAddress = prim.getSMSCAddress();
        assertNotNull(smscAddress);
        assertTrue(smscAddress.getAddress().equals("2207750008"));

        // extensions
        CAPExtensionsTest.checkTestCAPExtensions(prim.getExtensions());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        SMSAddressString callingPartysNumber = new SMSAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "1234567891234567");
        CalledPartyBCDNumber destinationSubscriberNumber = new CalledPartyBCDNumberImpl(
                AddressNature.international_number, NumberingPlan.ISDN, "41788005047");
        ISDNAddressString smscAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750008");
        CAPExtensions extensions = CAPExtensionsTest.createTestCAPExtensions();

        ConnectSMSRequestImpl prim = new ConnectSMSRequestImpl(callingPartysNumber, destinationSubscriberNumber,
                smscAddress, extensions);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
