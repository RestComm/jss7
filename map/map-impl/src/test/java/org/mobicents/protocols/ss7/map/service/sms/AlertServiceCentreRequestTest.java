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

package org.mobicents.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AlertServiceCentreRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 18, 4, 7, -111, -110, 17, 19, 50, 19, -15, 4, 7, -111, -108, -120, 115, 0, -110, -14 };
    }

    @Test
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        AlertServiceCentreRequestImpl asc = new AlertServiceCentreRequestImpl(MAPOperationCode.alertServiceCentre);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ISDNAddressString msisdn = asc.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "29113123311");

        AddressString sca = asc.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "49883700292");
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "29113123311");
        AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "49883700292");
        AlertServiceCentreRequestImpl asc = new AlertServiceCentreRequestImpl(msisdn, sca);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
