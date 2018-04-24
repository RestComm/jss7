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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed_SourceStatisticsDescriptor;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateExtendedImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class Ext3QoSSubscribedTest {

    public byte[] getData1() {
        return new byte[] { 4, 2, 74, (byte) 250 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 2, 108, 0 };
    };

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {
        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        Ext3QoSSubscribedImpl prim = new Ext3QoSSubscribedImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getMaximumBitRateForUplinkExtended().getBitRate(), 16000);
        assertEquals(prim.getGuaranteedBitRateForUplinkExtended().getBitRate(), 256000);
        assertFalse(prim.getGuaranteedBitRateForUplinkExtended().isUseNonextendedValue());


        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new Ext3QoSSubscribedImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getMaximumBitRateForUplinkExtended().getBitRate(), 50000);
        assertEquals(prim.getGuaranteedBitRateForUplinkExtended().getBitRate(), 0);
        assertTrue(prim.getGuaranteedBitRateForUplinkExtended().isUseNonextendedValue());
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {
        ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(16000, false);
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(256000, false);
        Ext3QoSSubscribedImpl prim = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);
//        ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended,
//        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertEquals(asn.toByteArray(), this.getData1());


        maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(50000, false);
        guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(0, true);
        prim = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertEquals(asn.toByteArray(), this.getData2());
    }
    
    @Test(groups = { "functional.xml.serialize", "subscriberInformation" })
    public void testXMLSerialize() throws Exception {

        ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(16000, false);
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(256000, false);
        Ext3QoSSubscribedImpl original = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "ext3QoSSubscribed", Ext3QoSSubscribedImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        Ext3QoSSubscribedImpl copy = reader.read("ext3QoSSubscribed", Ext3QoSSubscribedImpl.class);

        assertEquals(copy.getGuaranteedBitRateForUplinkExtended().getBitRate(), original.getGuaranteedBitRateForUplinkExtended().getBitRate());
        assertEquals(copy.getMaximumBitRateForUplinkExtended().getBitRate(), original.getMaximumBitRateForUplinkExtended().getBitRate());

    }
}
