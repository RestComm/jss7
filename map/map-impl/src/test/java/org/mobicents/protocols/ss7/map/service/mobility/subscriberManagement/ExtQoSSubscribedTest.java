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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.*;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_DeliveryOfErroneousSdus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_DeliveryOrder;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_ResidualBER;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_SduErrorRatio;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TrafficClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TrafficHandlingPriority;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class ExtQoSSubscribedTest {

    public byte[] getData1() {
        return new byte[] { 4, 9, 3, 115, (byte) 150, (byte) 254, (byte) 254, 116, 3, 0, 0 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 9, 15, (byte) 130, (byte) 151, 23, (byte) 128, 22, 17, 24, 25 };
    };

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {
        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ExtQoSSubscribedImpl prim = new ExtQoSSubscribedImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getAllocationRetentionPriority(), 3);
        assertEquals(prim.getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No);
        assertEquals(prim.getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(prim.getTrafficClass(), ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(prim.getMaximumSduSize().getMaximumSduSize(), 1500);
        assertEquals(prim.getMaximumBitRateForUplink().getBitRate(), 8640);
        assertEquals(prim.getMaximumBitRateForDownlink().getBitRate(), 8640);
        assertEquals(prim.getResidualBER(), ExtQoSSubscribed_ResidualBER._1_10_minus_5);
        assertEquals(prim.getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_4);
        assertEquals(prim.getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(prim.getTransferDelay().getSourceData(), 0);
        assertEquals(prim.getGuaranteedBitRateForUplink().getSourceData(), 0);
        assertEquals(prim.getGuaranteedBitRateForDownlink().getSourceData(), 0);


        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ExtQoSSubscribedImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getAllocationRetentionPriority(), 15);
        assertEquals(prim.getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes);
        assertEquals(prim.getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.subscribeddeliveryOrder_Reserved);
        assertEquals(prim.getTrafficClass(), ExtQoSSubscribed_TrafficClass.backgroundClass);
        assertEquals(prim.getMaximumSduSize().getMaximumSduSize(), 1502);
        assertEquals(prim.getMaximumBitRateForUplink().getBitRate(), 23);
        assertEquals(prim.getMaximumBitRateForDownlink().getBitRate(), 576);
        assertEquals(prim.getResidualBER(), ExtQoSSubscribed_ResidualBER._5_10_minus_2);
        assertEquals(prim.getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_6);
        assertEquals(prim.getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_1);
        assertEquals(prim.getTransferDelay().getTransferDelay(), 40);
        assertEquals(prim.getGuaranteedBitRateForUplink().getBitRate(), 24);
        assertEquals(prim.getGuaranteedBitRateForDownlink().getBitRate(), 25);
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {
        ExtQoSSubscribed_MaximumSduSizeImpl maximumSduSize = new ExtQoSSubscribed_MaximumSduSizeImpl(1500, false);
        ExtQoSSubscribed_BitRateImpl maximumBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(8640, false);
        ExtQoSSubscribed_BitRateImpl maximumBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(8640, false);
        ExtQoSSubscribed_TransferDelayImpl transferDelay = new ExtQoSSubscribed_TransferDelayImpl(0, true);
        ExtQoSSubscribed_BitRateImpl guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(0, true);
        ExtQoSSubscribed_BitRateImpl guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(0, true);
        ExtQoSSubscribedImpl prim = new ExtQoSSubscribedImpl(3, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No,
                ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo, ExtQoSSubscribed_TrafficClass.interactiveClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER._1_10_minus_5, ExtQoSSubscribed_SduErrorRatio._1_10_minus_4,
                ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertEquals(asn.toByteArray(), this.getData1());


        maximumSduSize = new ExtQoSSubscribed_MaximumSduSizeImpl(1502, false);
        maximumBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(23, false);
        maximumBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(576, false);
        transferDelay = new ExtQoSSubscribed_TransferDelayImpl(40, false);
        guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(24, false);
        guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(25, false);
        prim = new ExtQoSSubscribedImpl(15, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes,
                ExtQoSSubscribed_DeliveryOrder.subscribeddeliveryOrder_Reserved, ExtQoSSubscribed_TrafficClass.backgroundClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER._5_10_minus_2, ExtQoSSubscribed_SduErrorRatio._1_10_minus_6,
                ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_1, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertEquals(asn.toByteArray(), this.getData2());
    }

}
