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
package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtension;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class QualityOfServiceTest {

    public byte[] getData() {
        return new byte[] { 48, 35, -96, 5, -128, 3, 4, 7, 7, -95, 4, -127, 2, 1, 7, -94, 5, -128, 3, 4, 7, 7, -93, 3, -128, 1,
                52, -92, 3, -128, 1, 53, -91, 3, -128, 1, 54 };
    };

    private byte[] getEncodedqos2Subscribed1() {
        return new byte[] { 52 };
    }

    private byte[] getEncodedqos2Subscribed2() {
        return new byte[] { 53 };
    }

    private byte[] getEncodedqos2Subscribed3() {
        return new byte[] { 54 };
    }

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        QualityOfServiceImpl prim = new QualityOfServiceImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(Arrays.equals(prim.getRequestedQoS().getShortQoSFormat().getData(), this.getQoSSubscribedData()));
        assertNull(prim.getRequestedQoS().getLongQoSFormat());

        assertTrue(Arrays.equals(prim.getSubscribedQoS().getLongQoSFormat().getData(), this.getExtQoSSubscribedData()));
        assertNull(prim.getSubscribedQoS().getShortQoSFormat());

        assertTrue(Arrays.equals(prim.getNegotiatedQoS().getShortQoSFormat().getData(), this.getQoSSubscribedData()));
        assertNull(prim.getNegotiatedQoS().getLongQoSFormat());

        assertTrue(Arrays.equals(prim.getRequestedQoSExtension().getSupplementToLongQoSFormat().getData(),
                this.getEncodedqos2Subscribed1()));

        assertTrue(Arrays.equals(prim.getSubscribedQoSExtension().getSupplementToLongQoSFormat().getData(),
                this.getEncodedqos2Subscribed2()));

        assertTrue(Arrays.equals(prim.getNegotiatedQoSExtension().getSupplementToLongQoSFormat().getData(),
                this.getEncodedqos2Subscribed3()));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        QoSSubscribed qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        GPRSQoS requestedQoS = new GPRSQoSImpl(qosSubscribed);

        ExtQoSSubscribed extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
        GPRSQoS subscribedQoS = new GPRSQoSImpl(extQoSSubscribed);

        GPRSQoS negotiatedQoS = new GPRSQoSImpl(qosSubscribed);

        Ext2QoSSubscribedImpl qos2Subscribed1 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed1());
        GPRSQoSExtension requestedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed1);

        Ext2QoSSubscribedImpl qos2Subscribed2 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed2());
        GPRSQoSExtension subscribedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed2);

        Ext2QoSSubscribedImpl qos2Subscribed3 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed3());
        GPRSQoSExtension negotiatedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed3);

        QualityOfServiceImpl prim = new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS, requestedQoSExtension,
                subscribedQoSExtension, negotiatedQoSExtension);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
