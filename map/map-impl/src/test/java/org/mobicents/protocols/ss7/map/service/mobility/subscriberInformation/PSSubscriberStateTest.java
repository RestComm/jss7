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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoice;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PSSubscriberStateTest {

    private byte[] getEncodedDataDetached() {
        return new byte[] { -127, 0 };
    }

    private byte[] getEncodedDataActiveReachableForPaging() {
        return new byte[] { -91, 10, 48, 3, -128, 1, 5, 48, 3, -128, 1, 6 };
    }

    private byte[] getEncodedDataNotReachableReason() {
        return new byte[] { 10, 1, 0 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedDataDetached();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        PSSubscriberStateImpl impl = new PSSubscriberStateImpl();
        assertEquals(tag, PSSubscriberStateImpl._ID_ps_Detached);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertTrue(asn.isTagPrimitive());
        impl.decodeAll(asn);
        assertEquals(impl.getChoice(), PSSubscriberStateChoice.psDetached);
        assertNull(impl.getPDPContextInfoList());
        assertNull(impl.getNetDetNotReachable());

        rawData = getEncodedDataActiveReachableForPaging();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new PSSubscriberStateImpl();
        assertEquals(tag, PSSubscriberStateImpl._ID_ps_PDP_ActiveReachableForPaging);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertFalse(asn.isTagPrimitive());
        impl.decodeAll(asn);
        assertEquals(impl.getChoice(), PSSubscriberStateChoice.psPDPActiveReachableForPaging);
        assertEquals(impl.getPDPContextInfoList().size(), 2);
        assertEquals(impl.getPDPContextInfoList().get(0).getPdpContextIdentifier(), 5);
        assertEquals(impl.getPDPContextInfoList().get(1).getPdpContextIdentifier(), 6);
        assertNull(impl.getNetDetNotReachable());

        rawData = getEncodedDataNotReachableReason();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new PSSubscriberStateImpl();
        assertEquals(tag, Tag.ENUMERATED);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(asn.isTagPrimitive());
        impl.decodeAll(asn);
        assertEquals(impl.getChoice(), PSSubscriberStateChoice.netDetNotReachable);
        assertNull(impl.getPDPContextInfoList());
        assertEquals(impl.getNetDetNotReachable(), NotReachableReason.msPurged);

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        PSSubscriberStateImpl impl = new PSSubscriberStateImpl(PSSubscriberStateChoice.psDetached, null, null);
        // PSSubscriberStateChoice choice, NotReachableReason netDetNotReachable, ArrayList<PDPContextInfo> pdpContextInfoList
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataDetached();
        assertTrue(Arrays.equals(rawData, encodedData));

        ArrayList<PDPContextInfo> pdpContextInfoList = new ArrayList<PDPContextInfo>();
        PDPContextInfoImpl ci1 = new PDPContextInfoImpl(5, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        PDPContextInfoImpl ci2 = new PDPContextInfoImpl(6, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        pdpContextInfoList.add(ci1);
        pdpContextInfoList.add(ci2);
        impl = new PSSubscriberStateImpl(PSSubscriberStateChoice.psPDPActiveReachableForPaging, null, pdpContextInfoList);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataActiveReachableForPaging();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new PSSubscriberStateImpl(PSSubscriberStateChoice.netDetNotReachable, NotReachableReason.msPurged, null);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataNotReachableReason();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
