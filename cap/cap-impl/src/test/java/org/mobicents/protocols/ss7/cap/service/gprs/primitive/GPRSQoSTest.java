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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class GPRSQoSTest {

    public byte[] getData() {
        return new byte[] { -128, 3, 4, 7, 7 };
    };

    public byte[] getData2() {
        return new byte[] { -127, 2, 1, 7 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // Option 1
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        GPRSQoSImpl prim = new GPRSQoSImpl();
        prim.decodeAll(asn);
        assertEquals(tag, GPRSQoSImpl._ID_shortQoSFormat);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertTrue(Arrays.equals(prim.getShortQoSFormat().getData(), this.getQoSSubscribedData()));
        assertNull(prim.getLongQoSFormat());

        // Option 2
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new GPRSQoSImpl();
        prim.decodeAll(asn);
        assertEquals(tag, GPRSQoSImpl._ID_longQoSFormat);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertTrue(Arrays.equals(prim.getLongQoSFormat().getData(), this.getExtQoSSubscribedData()));
        assertNull(prim.getShortQoSFormat());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        // Option 1
        QoSSubscribed qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        GPRSQoSImpl prim = new GPRSQoSImpl(qosSubscribed);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // Option 2
        ExtQoSSubscribed extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
        prim = new GPRSQoSImpl(extQoSSubscribed);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
    }

}
