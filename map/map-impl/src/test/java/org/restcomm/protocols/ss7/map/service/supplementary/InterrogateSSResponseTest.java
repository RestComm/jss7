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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ForwardingFeatureImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.GenericServiceInfoImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.InterrogateSSResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSStatusImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class InterrogateSSResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { (byte) 128, 1, 1 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { (byte) 162, 3, (byte) 131, 1, 96 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { (byte) 163, 5, 48, 3, (byte) 132, 1, 1 };
    }

    private byte[] getEncodedData4() {
        return new byte[] { (byte) 164, 3, 4, 1, 1 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, InterrogateSSResponseImpl._TAG_ssStatus);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        InterrogateSSResponseImpl impl = new InterrogateSSResponseImpl();
        impl.decodeAll(asn);

        assertTrue(impl.getSsStatus().getABit());
        assertFalse(impl.getSsStatus().getPBit());
        assertFalse(impl.getSsStatus().getQBit());
        assertFalse(impl.getSsStatus().getRBit());

        assertNull(impl.getBasicServiceGroupList());
        assertNull(impl.getForwardingFeatureList());
        assertNull(impl.getGenericServiceInfo());


        rawData = getEncodedData2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, InterrogateSSResponseImpl._TAG_basicServiceGroupList);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        impl = new InterrogateSSResponseImpl();
        impl.decodeAll(asn);

        assertNull(impl.getSsStatus());

        assertEquals(impl.getBasicServiceGroupList().size(), 1);
        assertEquals(impl.getBasicServiceGroupList().get(0).getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allFacsimileTransmissionServices);

        assertNull(impl.getForwardingFeatureList());
        assertNull(impl.getGenericServiceInfo());


        rawData = getEncodedData3();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, InterrogateSSResponseImpl._TAG_forwardingFeatureList);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        impl = new InterrogateSSResponseImpl();
        impl.decodeAll(asn);

        assertNull(impl.getSsStatus());
        assertNull(impl.getBasicServiceGroupList());

        assertEquals(impl.getForwardingFeatureList().size(), 1);
        assertTrue(impl.getForwardingFeatureList().get(0).getSsStatus().getABit());
        assertFalse(impl.getForwardingFeatureList().get(0).getSsStatus().getPBit());

        assertNull(impl.getGenericServiceInfo());


        rawData = getEncodedData4();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, InterrogateSSResponseImpl._TAG_genericServiceInfo);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        impl = new InterrogateSSResponseImpl();
        impl.decodeAll(asn);

        assertNull(impl.getSsStatus());
        assertNull(impl.getBasicServiceGroupList());
        assertNull(impl.getForwardingFeatureList());

        assertTrue(impl.getGenericServiceInfo().getSsStatus().getABit());
        assertFalse(impl.getGenericServiceInfo().getSsStatus().getPBit());
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {

//        private SSStatus ssStatus;
//        private ArrayList<BasicServiceCode> basicServiceGroupList;
//        private ArrayList<ForwardingFeature> forwardingFeatureList;
//        private GenericServiceInfo genericServiceInfo;
        SSStatus ssStatus = new SSStatusImpl(false, false, false, true);
        InterrogateSSResponseImpl impl = new InterrogateSSResponseImpl(ssStatus);
        AsnOutputStream asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        ArrayList<BasicServiceCode> basicServiceGroupList = new ArrayList<BasicServiceCode>();
        TeleserviceCode teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.allFacsimileTransmissionServices);
        BasicServiceCodeImpl item = new BasicServiceCodeImpl(teleservice);
        basicServiceGroupList.add(item);
        impl = new InterrogateSSResponseImpl(basicServiceGroupList, false);
        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));


        ArrayList<ForwardingFeature> forwardingFeatureList = new ArrayList<ForwardingFeature>();
        ForwardingFeature item2 = new ForwardingFeatureImpl(null, ssStatus, null, null, null, null, null);
        forwardingFeatureList.add(item2);
        impl = new InterrogateSSResponseImpl(forwardingFeatureList);
        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));


        GenericServiceInfoImpl genericServiceInfo = new GenericServiceInfoImpl(ssStatus, null, null, null, null, null, null, null);
        impl = new InterrogateSSResponseImpl(genericServiceInfo);
        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData4();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
