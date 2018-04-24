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


import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRate;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_DeliveryOfErroneousSdus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_DeliveryOrder;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_MaximumSduSize;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_ResidualBER;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_SduErrorRatio;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TrafficClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TrafficHandlingPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TransferDelay;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtQoSSubscribedImpl extends OctetStringBase implements ExtQoSSubscribed {

    private static final String ALLOCATION_RETENTION_PRIORITY = "allocationRetentionPriority";
    private static final String DELIVERY_OF_ERRONEOUS_SDUS = "deliveryOfErroneousSdus";
    private static final String DELIVERY_ORDER = "deliveryOrder";
    private static final String TRAFFIC_CLASS = "trafficClass";
    private static final String MAXIMUM_SDU_SIZE = "maximumSduSize";
    private static final String MAXIMUM_BIT_RATE_FOR_UPLINK = "maximumBitRateForUplink";
    private static final String MAXIMUM_BIT_RATE_FOR_DOWNLINK = "maximumBitRateForDownlink";
    private static final String RESIDUAL_BER = "residualBER";
    private static final String SDU_ERROR_RATIO = "sduErrorRatio";
    private static final String TRAFFIC_HANDLING_PRIORITY = "trafficHandlingPriority";
    private static final String TRANSFER_DELAY = "transferDelay";
    private static final String GUARANTEED_BIT_RATE_FOR_UPLINK = "guaranteedBitRateForUplink";
    private static final String GUARANTEED_BIT_RATE_FOR_DOWNLINK = "guaranteedBitRateForDownlink";

    private static final int DEFAULT_INT_VALUE = 0;

    public ExtQoSSubscribedImpl() {
        super(1, 9, "ExtQoSSubscribed");
    }

    public ExtQoSSubscribedImpl(byte[] data) {
        super(1, 9, "ExtQoSSubscribed", data);
    }

    public ExtQoSSubscribedImpl(int allocationRetentionPriority, ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus,
            ExtQoSSubscribed_DeliveryOrder deliveryOrder, ExtQoSSubscribed_TrafficClass trafficClass, ExtQoSSubscribed_MaximumSduSize maximumSduSize,
            ExtQoSSubscribed_BitRate maximumBitRateForUplink, ExtQoSSubscribed_BitRate maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER residualBER,
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio, ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority,
            ExtQoSSubscribed_TransferDelay transferDelay, ExtQoSSubscribed_BitRate guaranteedBitRateForUplink,
            ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink) {
        super(1, 9, "ExtQoSSubscribed");

        this.setData(allocationRetentionPriority, deliveryOfErroneousSdus, deliveryOrder, trafficClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, residualBER, sduErrorRatio, trafficHandlingPriority, transferDelay, guaranteedBitRateForUplink,
                guaranteedBitRateForDownlink);
    }

    protected void setData(int allocationRetentionPriority, ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus,
            ExtQoSSubscribed_DeliveryOrder deliveryOrder, ExtQoSSubscribed_TrafficClass trafficClass, ExtQoSSubscribed_MaximumSduSize maximumSduSize,
            ExtQoSSubscribed_BitRate maximumBitRateForUplink, ExtQoSSubscribed_BitRate maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER residualBER,
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio, ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority,
            ExtQoSSubscribed_TransferDelay transferDelay, ExtQoSSubscribed_BitRate guaranteedBitRateForUplink,
            ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink) {
        this.data = new byte[9];

        this.data[0] = (byte) allocationRetentionPriority;
        this.data[1] = (byte) ((deliveryOfErroneousSdus != null ? deliveryOfErroneousSdus.getCode() : 0)
                | ((deliveryOrder != null ? deliveryOrder.getCode() : 0) << 3) | ((trafficClass != null ? trafficClass.getCode() : 0) << 5));
        this.data[2] = (byte) (maximumSduSize != null ? maximumSduSize.getSourceData() : 0);
        this.data[3] = (byte) (maximumBitRateForUplink != null ? maximumBitRateForUplink.getSourceData() : 0);
        this.data[4] = (byte) (maximumBitRateForDownlink != null ? maximumBitRateForDownlink.getSourceData() : 0);
        this.data[5] = (byte) ((sduErrorRatio != null ? sduErrorRatio.getCode() : 0) | ((residualBER != null ? residualBER.getCode() : 0) << 4));
        this.data[6] = (byte) ((trafficHandlingPriority != null ? trafficHandlingPriority.getCode() : 0) | ((transferDelay != null ? transferDelay
                .getSourceData() : 0) << 2));
        this.data[7] = (byte) (guaranteedBitRateForUplink != null ? guaranteedBitRateForUplink.getSourceData() : 0);
        this.data[8] = (byte) (guaranteedBitRateForDownlink != null ? guaranteedBitRateForDownlink.getSourceData() : 0);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public int getAllocationRetentionPriority() {
        if (this.data == null || this.data.length < 1)
            return 0;

        return this.data[0] & 0xFF;
    }

    @Override
    public ExtQoSSubscribed_DeliveryOfErroneousSdus getDeliveryOfErroneousSdus() {
        if (this.data == null || this.data.length < 2)
            return null;

        return ExtQoSSubscribed_DeliveryOfErroneousSdus.getInstance(this.data[1] & 0x07);
    }

    @Override
    public ExtQoSSubscribed_DeliveryOrder getDeliveryOrder() {
        if (this.data == null || this.data.length < 2)
            return null;

        return ExtQoSSubscribed_DeliveryOrder.getInstance((this.data[1] & 0x18) >> 3);
    }

    @Override
    public ExtQoSSubscribed_TrafficClass getTrafficClass() {
        if (this.data == null || this.data.length < 2)
            return null;

        return ExtQoSSubscribed_TrafficClass.getInstance((this.data[1] & 0xE0) >> 5);
    }

    @Override
    public ExtQoSSubscribed_MaximumSduSize getMaximumSduSize() {
        if (this.data == null || this.data.length < 3)
            return null;

        return new ExtQoSSubscribed_MaximumSduSizeImpl(this.data[2] & 0xFF, true);
    }

    @Override
    public ExtQoSSubscribed_BitRate getMaximumBitRateForUplink() {
        if (this.data == null || this.data.length < 4)
            return null;

        return new ExtQoSSubscribed_BitRateImpl(this.data[3] & 0xFF, true);
    }

    @Override
    public ExtQoSSubscribed_BitRate getMaximumBitRateForDownlink() {
        if (this.data == null || this.data.length < 5)
            return null;

        return new ExtQoSSubscribed_BitRateImpl(this.data[4] & 0xFF, true);
    }

    @Override
    public ExtQoSSubscribed_ResidualBER getResidualBER() {
        if (this.data == null || this.data.length < 6)
            return null;

        return ExtQoSSubscribed_ResidualBER.getInstance((this.data[5] & 0xF0) >> 4);
    }

    @Override
    public ExtQoSSubscribed_SduErrorRatio getSduErrorRatio() {
        if (this.data == null || this.data.length < 6)
            return null;

        return ExtQoSSubscribed_SduErrorRatio.getInstance(this.data[5] & 0x0F);
    }

    @Override
    public ExtQoSSubscribed_TrafficHandlingPriority getTrafficHandlingPriority() {
        if (this.data == null || this.data.length < 7)
            return null;

        return ExtQoSSubscribed_TrafficHandlingPriority.getInstance(this.data[6] & 0x03);
    }

    @Override
    public ExtQoSSubscribed_TransferDelay getTransferDelay() {
        if (this.data == null || this.data.length < 7)
            return null;

        return new ExtQoSSubscribed_TransferDelayImpl((this.data[6] & 0xFC) >> 2, true);
    }

    @Override
    public ExtQoSSubscribed_BitRate getGuaranteedBitRateForUplink() {
        if (this.data == null || this.data.length < 8)
            return null;

        return new ExtQoSSubscribed_BitRateImpl(this.data[7] & 0xFF, true);
    }

    @Override
    public ExtQoSSubscribed_BitRate getGuaranteedBitRateForDownlink() {
        if (this.data == null || this.data.length < 9)
            return null;

        return new ExtQoSSubscribed_BitRateImpl(this.data[8] & 0xFF, true);
    }

    @Override
    public String toString() {
        if (this.data != null && this.data.length >= 1) {
            int allocationRetentionPriority = getAllocationRetentionPriority();
            ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus = getDeliveryOfErroneousSdus();
            ExtQoSSubscribed_DeliveryOrder deliveryOrder = getDeliveryOrder();
            ExtQoSSubscribed_TrafficClass trafficClass = getTrafficClass();
            ExtQoSSubscribed_MaximumSduSize maximumSduSize = getMaximumSduSize();
            ExtQoSSubscribed_BitRate maximumBitRateForUplink = getMaximumBitRateForUplink();
            ExtQoSSubscribed_BitRate maximumBitRateForDownlink = getMaximumBitRateForDownlink();
            ExtQoSSubscribed_ResidualBER residualBER = getResidualBER();
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio = getSduErrorRatio();
            ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority = getTrafficHandlingPriority();
            ExtQoSSubscribed_TransferDelay transferDelay = getTransferDelay();
            ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = getGuaranteedBitRateForUplink();
            ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = getGuaranteedBitRateForDownlink();

            StringBuilder sb = new StringBuilder();
            sb.append(_PrimitiveName);
            sb.append(" [");

            sb.append("allocationRetentionPriority=");
            sb.append(allocationRetentionPriority);
            sb.append(", ");
            if (deliveryOfErroneousSdus != null) {
                sb.append("deliveryOfErroneousSdus=");
                sb.append(deliveryOfErroneousSdus);
                sb.append(", ");
            }
            if (deliveryOrder != null) {
                sb.append("deliveryOrder=");
                sb.append(deliveryOrder);
                sb.append(", ");
            }
            if (trafficClass != null) {
                sb.append("trafficClass=");
                sb.append(trafficClass);
                sb.append(", ");
            }
            if (maximumSduSize != null) {
                sb.append("maximumSduSize=");
                sb.append(maximumSduSize);
                sb.append(", ");
            }
            if (maximumBitRateForUplink != null) {
                sb.append("maximumBitRateForUplink=");
                sb.append(maximumBitRateForUplink);
                sb.append(", ");
            }
            if (maximumBitRateForDownlink != null) {
                sb.append("maximumBitRateForDownlink=");
                sb.append(maximumBitRateForDownlink);
                sb.append(", ");
            }
            if (residualBER != null) {
                sb.append("residualBER=");
                sb.append(residualBER);
                sb.append(", ");
            }
            if (sduErrorRatio != null) {
                sb.append("sduErrorRatio=");
                sb.append(sduErrorRatio);
                sb.append(", ");
            }
            if (trafficHandlingPriority != null) {
                sb.append("trafficHandlingPriority=");
                sb.append(trafficHandlingPriority);
                sb.append(", ");
            }
            if (transferDelay != null) {
                sb.append("transferDelay=");
                sb.append(transferDelay);
                sb.append(", ");
            }
            if (guaranteedBitRateForUplink != null) {
                sb.append("guaranteedBitRateForUplink=");
                sb.append(guaranteedBitRateForUplink);
                sb.append(", ");
            }
            if (guaranteedBitRateForDownlink != null) {
                sb.append("guaranteedBitRateForDownlink=");
                sb.append(guaranteedBitRateForDownlink);
                sb.append(", ");
            }
            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ExtQoSSubscribedImpl> EXT_QOS_SUBSCRIBED_XML = new XMLFormat<ExtQoSSubscribedImpl>(ExtQoSSubscribedImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ExtQoSSubscribedImpl qosSubscribed) throws XMLStreamException {
            int allocationRetentionPriority = xml.getAttribute(ALLOCATION_RETENTION_PRIORITY, DEFAULT_INT_VALUE);
            ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus =
                    Enum.valueOf(ExtQoSSubscribed_DeliveryOfErroneousSdus.class, xml.getAttribute(DELIVERY_OF_ERRONEOUS_SDUS, "subscribedDeliveryOfErroneousSdus_Reserved"));
            ExtQoSSubscribed_DeliveryOrder deliveryOrder =
                    Enum.valueOf(ExtQoSSubscribed_DeliveryOrder.class, xml.getAttribute(DELIVERY_ORDER, "subscribeddeliveryOrder_Reserved"));
            ExtQoSSubscribed_TrafficClass trafficClass =
                    Enum.valueOf(ExtQoSSubscribed_TrafficClass.class, xml.getAttribute(TRAFFIC_CLASS, "subscribedTrafficClass_Reserved"));
            ExtQoSSubscribed_MaximumSduSize  maximumSduSize = new ExtQoSSubscribed_MaximumSduSizeImpl(xml.getAttribute(MAXIMUM_SDU_SIZE, DEFAULT_INT_VALUE), false);
            ExtQoSSubscribed_BitRate maximumBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(xml.getAttribute(MAXIMUM_BIT_RATE_FOR_UPLINK, DEFAULT_INT_VALUE), false);
            ExtQoSSubscribed_BitRate maximumBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(xml.getAttribute(MAXIMUM_BIT_RATE_FOR_DOWNLINK, DEFAULT_INT_VALUE), false);
            ExtQoSSubscribed_ResidualBER residualBER =
                    Enum.valueOf(ExtQoSSubscribed_ResidualBER.class, xml.getAttribute(RESIDUAL_BER, "subscribedResidualBER_Reserved"));
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio =
                    Enum.valueOf(ExtQoSSubscribed_SduErrorRatio.class, xml.getAttribute(SDU_ERROR_RATIO, "subscribedSduErrorRatio_Reserved"));
            ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority =
                    Enum.valueOf(ExtQoSSubscribed_TrafficHandlingPriority.class, xml.getAttribute(TRAFFIC_HANDLING_PRIORITY, "subscribedTrafficHandlingPriority_Reserved"));
            ExtQoSSubscribed_TransferDelay transferDelay = new ExtQoSSubscribed_TransferDelayImpl(xml.getAttribute(TRANSFER_DELAY, DEFAULT_INT_VALUE),false);
            ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(xml.getAttribute(GUARANTEED_BIT_RATE_FOR_UPLINK, DEFAULT_INT_VALUE),false);
            ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(xml.getAttribute(GUARANTEED_BIT_RATE_FOR_DOWNLINK, DEFAULT_INT_VALUE), false);
            qosSubscribed.setData(allocationRetentionPriority, deliveryOfErroneousSdus, deliveryOrder, trafficClass, maximumSduSize, maximumBitRateForUplink,
                    maximumBitRateForDownlink, residualBER, sduErrorRatio, trafficHandlingPriority, transferDelay, guaranteedBitRateForUplink,
                    guaranteedBitRateForDownlink);
        }

        @Override
        public void write(ExtQoSSubscribedImpl qosSubscribed, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (qosSubscribed.data != null) {
                xml.setAttribute(ALLOCATION_RETENTION_PRIORITY, qosSubscribed.getAllocationRetentionPriority());
                if(qosSubscribed.getDeliveryOfErroneousSdus()!=null)
                    xml.setAttribute(DELIVERY_OF_ERRONEOUS_SDUS, qosSubscribed.getDeliveryOfErroneousSdus().toString());
                if(qosSubscribed.getDeliveryOrder()!=null)
                    xml.setAttribute(DELIVERY_ORDER, qosSubscribed.getDeliveryOrder().toString());
                if(qosSubscribed.getTrafficClass()!=null)
                    xml.setAttribute(TRAFFIC_CLASS, qosSubscribed.getTrafficClass().toString());
                if(qosSubscribed.getMaximumSduSize()!=null)
                    xml.setAttribute(MAXIMUM_SDU_SIZE, qosSubscribed.getMaximumSduSize().getMaximumSduSize());
                if(qosSubscribed.getMaximumBitRateForUplink()!=null)
                    xml.setAttribute(MAXIMUM_BIT_RATE_FOR_UPLINK, qosSubscribed.getMaximumBitRateForUplink().getBitRate());
                if(qosSubscribed.getMaximumBitRateForDownlink()!=null)
                    xml.setAttribute(MAXIMUM_BIT_RATE_FOR_DOWNLINK, qosSubscribed.getMaximumBitRateForDownlink().getBitRate());
                if(qosSubscribed.getResidualBER()!=null)
                    xml.setAttribute(RESIDUAL_BER, qosSubscribed.getResidualBER().toString());
                if(qosSubscribed.getSduErrorRatio()!=null)
                    xml.setAttribute(SDU_ERROR_RATIO, qosSubscribed.getSduErrorRatio().toString());
                if(qosSubscribed.getTrafficHandlingPriority()!=null)
                    xml.setAttribute(TRAFFIC_HANDLING_PRIORITY, qosSubscribed.getTrafficHandlingPriority().toString());
                if(qosSubscribed.getTransferDelay()!=null)
                    xml.setAttribute(TRANSFER_DELAY, qosSubscribed.getTransferDelay().getTransferDelay());
                if(qosSubscribed.getGuaranteedBitRateForUplink()!=null)
                    xml.setAttribute(GUARANTEED_BIT_RATE_FOR_UPLINK, qosSubscribed.getGuaranteedBitRateForUplink().getBitRate());
                if(qosSubscribed.getGuaranteedBitRateForDownlink()!=null)
                    xml.setAttribute(GUARANTEED_BIT_RATE_FOR_DOWNLINK, qosSubscribed.getGuaranteedBitRateForDownlink().getBitRate());
            }
        }
    };
}
