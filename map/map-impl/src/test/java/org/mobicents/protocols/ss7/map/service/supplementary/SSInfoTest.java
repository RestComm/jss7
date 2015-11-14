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

package org.mobicents.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class SSInfoTest {

    private byte[] getEncodedData1() {
        return new byte[] { (byte) 160, 7, 48, 5, 48, 3, (byte) 132, 1, 4 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { (byte) 161, 7, 48, 5, 48, 3, (byte) 132, 1, 4 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { (byte) 163, 3, (byte) 132, 1, 4 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData1();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, SSInfoImpl._TAG_forwardingInfo);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        SSInfoImpl impl = new SSInfoImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getForwardingInfo().getForwardingFeatureList().size(), 1);
        assertFalse(impl.getForwardingInfo().getForwardingFeatureList().get(0).getSsStatus().getQBit());
        assertTrue(impl.getForwardingInfo().getForwardingFeatureList().get(0).getSsStatus().getPBit());
        assertFalse(impl.getForwardingInfo().getForwardingFeatureList().get(0).getSsStatus().getRBit());
        assertFalse(impl.getForwardingInfo().getForwardingFeatureList().get(0).getSsStatus().getABit());

        assertNull(impl.getCallBarringInfo());
        assertNull(impl.getSsData());


        rawData = getEncodedData2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, SSInfoImpl._TAG_callBarringInfo);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        impl = new SSInfoImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getCallBarringInfo().getCallBarringFeatureList().size(), 1);
        assertFalse(impl.getCallBarringInfo().getCallBarringFeatureList().get(0).getSsStatus().getQBit());
        assertTrue(impl.getCallBarringInfo().getCallBarringFeatureList().get(0).getSsStatus().getPBit());
        assertFalse(impl.getCallBarringInfo().getCallBarringFeatureList().get(0).getSsStatus().getRBit());
        assertFalse(impl.getCallBarringInfo().getCallBarringFeatureList().get(0).getSsStatus().getABit());

        assertNull(impl.getForwardingInfo());
        assertNull(impl.getSsData());


        rawData = getEncodedData3();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, SSInfoImpl._TAG_ssData);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        impl = new SSInfoImpl();
        impl.decodeAll(asn);

        assertFalse(impl.getSsData().getSsStatus().getQBit());
        assertTrue(impl.getSsData().getSsStatus().getPBit());
        assertFalse(impl.getSsData().getSsStatus().getRBit());
        assertFalse(impl.getSsData().getSsStatus().getABit());

        assertNull(impl.getCallBarringInfo());
        assertNull(impl.getForwardingInfo());
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {

        ArrayList<ForwardingFeature> forwardingFeatureList = new ArrayList<ForwardingFeature>();
        SSStatus ssStatus = new SSStatusImpl(false, true, false, false);
        ForwardingFeature ff = new ForwardingFeatureImpl(null, ssStatus, null, null, null, null, null);
        forwardingFeatureList.add(ff);
        ForwardingInfo forwardingInfo = new ForwardingInfoImpl(null, forwardingFeatureList);
        SSInfoImpl impl = new SSInfoImpl(forwardingInfo);
//        private ForwardingInfo forwardingInfo;
//        private CallBarringInfo callBarringInfo;
//        private SSData ssData;

        AsnOutputStream asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));


        ArrayList<CallBarringFeature> callBarringFeatureList = new ArrayList<CallBarringFeature>();
        CallBarringFeatureImpl cbf = new CallBarringFeatureImpl(null, ssStatus);
        callBarringFeatureList.add(cbf);
        CallBarringInfoImpl callBarringInfo = new CallBarringInfoImpl(null, callBarringFeatureList);
        impl = new SSInfoImpl(callBarringInfo);

        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));


        SSDataImpl ssData = new SSDataImpl(null, ssStatus, null, null, null, null);
        impl = new SSInfoImpl(ssData);

        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
