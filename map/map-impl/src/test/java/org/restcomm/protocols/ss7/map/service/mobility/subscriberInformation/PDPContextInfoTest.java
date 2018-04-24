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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.TEIDImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.TransactionIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PDPContextInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 105, -128, 1, 10, -127, 0, -126, 2, 11, 12, -125, 1, 21, -124, 2, 22, 23, -123, 2, 24, 25,
                -122, 1, 11, -121, 1, 26, -120, 4, 27, 28, 29, 30, -119, 4, 31, 32, 33, 34, -118, 5, 35, 36, 37, 38, 39, -117,
                1, 15, -116, 1, 16, -115, 1, 17, -114, 4, 41, 42, 43, 44, -113, 2, 45, 46, -112, 5, 47, 48, 49, 50, 51, -110,
                1, 52, -109, 1, 53, -108, 1, 54, -107, 1, 55, -106, 1, 56, -105, 1, 57, -103, 1, 91, -102, 1, 92, -101, 1, 93,
                -100, 2, 58, 59, -99, 1, 60 };
    }

    private byte[] getEncodedPDPType() {
        return new byte[] { 11, 12 };
    }

    private byte[] getEncodedPDPAddress() {
        return new byte[] { 21 };
    }

    private byte[] getEncodedapnSubscribed() {
        return new byte[] { 22, 23 };
    }

    private byte[] getEncodedgetapnInUse() {
        return new byte[] { 24, 25 };
    }

    private byte[] getEncodedTransactionId() {
        return new byte[] { 26 };
    }

    private byte[] getEncodedTEID_1() {
        return new byte[] { 27, 28, 29, 30 };
    }

    private byte[] getEncodedTEID_2() {
        return new byte[] { 31, 32, 33, 34 };
    }

    private byte[] getEncodedggsnAddress() {
        return new byte[] { 35, 36, 37, 38, 39 };
    }

    private byte[] getEncodedggsnAddress2() {
        return new byte[] { (byte) 192, (byte) 168, 0, 1 };
    }

    private byte[] getEncodedqosSubscribed() {
        return new byte[] { 15 };
    }

    private byte[] getEncodedqosRequested() {
        return new byte[] { 16 };
    }

    private byte[] getEncodedqosNegotiated() {
        return new byte[] { 17 };
    }

    private byte[] getEncodedchargingId() {
        return new byte[] { 41, 42, 43, 44 };
    }

    private byte[] getEncodedchargingCharacteristics() {
        return new byte[] { 45, 46 };
    }

    private byte[] getEncodedrncAddress() {
        return new byte[] { 47, 48, 49, 50, 51 };
    }

    private byte[] getEncodedrncAddress2() {
        return new byte[] { (byte) 192, (byte) 168, 5, 51 };
    }

    private byte[] getEncodedqos2Subscribed() {
        return new byte[] { 52 };
    }

    private byte[] getEncodedqos2Requested() {
        return new byte[] { 53 };
    }

    private byte[] getEncodedqos2Negotiated() {
        return new byte[] { 54 };
    }

    private byte[] getEncodedqos3Subscribed() {
        return new byte[] { 55 };
    }

    private byte[] getEncodedqos3Requested() {
        return new byte[] { 56 };
    }

    private byte[] getEncodedqos3Negotiated() {
        return new byte[] { 57 };
    }

    private byte[] getEncodedExtPDPType() {
        return new byte[] { 58, 59 };
    }

    private byte[] getEncodedExtPdpAddress() {
        return new byte[] { 60 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        PDPContextInfoImpl impl = new PDPContextInfoImpl();
        impl.decodeAll(asn);
        assertEquals(tag, Tag.SEQUENCE);

        assertEquals((int) impl.getPdpContextIdentifier(), 10);
        assertTrue(impl.getPdpContextActive());
        assertTrue(Arrays.equals(impl.getPdpType().getData(), this.getEncodedPDPType()));
        assertTrue(Arrays.equals(impl.getPdpAddress().getData(), this.getEncodedPDPAddress()));
        assertTrue(Arrays.equals(impl.getApnSubscribed().getData(), this.getEncodedapnSubscribed()));
        assertTrue(Arrays.equals(impl.getApnInUse().getData(), this.getEncodedgetapnInUse()));
        assertEquals((int) impl.getNsapi(), 11);
        assertTrue(Arrays.equals(impl.getTransactionId().getData(), this.getEncodedTransactionId()));
        assertTrue(Arrays.equals(impl.getTeidForGnAndGp().getData(), this.getEncodedTEID_1()));
        assertTrue(Arrays.equals(impl.getTeidForIu().getData(), this.getEncodedTEID_2()));
        assertTrue(Arrays.equals(impl.getGgsnAddress().getData(), this.getEncodedggsnAddress()));
        assertTrue(Arrays.equals(impl.getQosSubscribed().getData(), this.getEncodedqosSubscribed()));
        assertTrue(Arrays.equals(impl.getQosRequested().getData(), this.getEncodedqosRequested()));
        assertTrue(Arrays.equals(impl.getQosNegotiated().getData(), this.getEncodedqosNegotiated()));
        assertTrue(Arrays.equals(impl.getChargingId().getData(), this.getEncodedchargingId()));
        assertTrue(Arrays.equals(impl.getChargingCharacteristics().getData(), this.getEncodedchargingCharacteristics()));
        assertTrue(Arrays.equals(impl.getRncAddress().getData(), this.getEncodedrncAddress()));
        assertNull(impl.getExtensionContainer());
        assertTrue(Arrays.equals(impl.getQos2Subscribed().getData(), this.getEncodedqos2Subscribed()));
        assertTrue(Arrays.equals(impl.getQos2Requested().getData(), this.getEncodedqos2Requested()));
        assertTrue(Arrays.equals(impl.getQos2Negotiated().getData(), this.getEncodedqos2Negotiated()));
        assertTrue(Arrays.equals(impl.getQos3Subscribed().getData(), this.getEncodedqos3Subscribed()));
        assertTrue(Arrays.equals(impl.getQos3Requested().getData(), this.getEncodedqos3Requested()));
        assertTrue(Arrays.equals(impl.getQos3Negotiated().getData(), this.getEncodedqos3Negotiated()));
        assertEquals(impl.getQos4Subscribed().getData(), 91);
        assertEquals(impl.getQos4Requested().getData(), 92);
        assertEquals(impl.getQos4Negotiated().getData(), 93);
        assertTrue(Arrays.equals(impl.getExtPdpType().getData(), this.getEncodedExtPDPType()));
        assertTrue(Arrays.equals(impl.getExtPdpAddress().getData(), this.getEncodedExtPdpAddress()));

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        PDPTypeImpl pdpType = new PDPTypeImpl(getEncodedPDPType());
        PDPAddressImpl pdpAddress = new PDPAddressImpl(getEncodedPDPAddress());
        APNImpl apnSubscribed = new APNImpl(getEncodedapnSubscribed());
        APNImpl apnInUse = new APNImpl(getEncodedgetapnInUse());
        TransactionIdImpl transactionId = new TransactionIdImpl(getEncodedTransactionId());
        TEIDImpl teidForGnAndGp = new TEIDImpl(getEncodedTEID_1());
        TEIDImpl teidForIu = new TEIDImpl(getEncodedTEID_2());
        GSNAddressImpl ggsnAddress = new GSNAddressImpl(getEncodedggsnAddress());
        ExtQoSSubscribedImpl qosSubscribed = new ExtQoSSubscribedImpl(getEncodedqosSubscribed());
        ExtQoSSubscribedImpl qosRequested = new ExtQoSSubscribedImpl(getEncodedqosRequested());
        ExtQoSSubscribedImpl qosNegotiated = new ExtQoSSubscribedImpl(getEncodedqosNegotiated());
        GPRSChargingIDImpl chargingId = new GPRSChargingIDImpl(getEncodedchargingId());
        ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(
                getEncodedchargingCharacteristics());
        GSNAddressImpl rncAddress = new GSNAddressImpl(getEncodedrncAddress());
        Ext2QoSSubscribedImpl qos2Subscribed = new Ext2QoSSubscribedImpl(getEncodedqos2Subscribed());
        Ext2QoSSubscribedImpl qos2Requested = new Ext2QoSSubscribedImpl(getEncodedqos2Requested());
        Ext2QoSSubscribedImpl qos2Negotiated = new Ext2QoSSubscribedImpl(getEncodedqos2Negotiated());
        Ext3QoSSubscribedImpl qos3Subscribed = new Ext3QoSSubscribedImpl(getEncodedqos3Subscribed());
        Ext3QoSSubscribedImpl qos3Requested = new Ext3QoSSubscribedImpl(getEncodedqos3Requested());
        Ext3QoSSubscribedImpl qos3Negotiated = new Ext3QoSSubscribedImpl(getEncodedqos3Negotiated());
        Ext4QoSSubscribedImpl qos4Subscribed = new Ext4QoSSubscribedImpl(91);
        Ext4QoSSubscribedImpl qos4Requested = new Ext4QoSSubscribedImpl(92);
        Ext4QoSSubscribedImpl qos4Negotiated = new Ext4QoSSubscribedImpl(93);
        ExtPDPTypeImpl extPdpType = new ExtPDPTypeImpl(getEncodedExtPDPType());
        PDPAddressImpl extPdpAddress = new PDPAddressImpl(getEncodedExtPdpAddress());

        PDPContextInfoImpl impl = new PDPContextInfoImpl(10, true, pdpType, pdpAddress, apnSubscribed, apnInUse, 11,
                transactionId, teidForGnAndGp, teidForIu, ggsnAddress, qosSubscribed, qosRequested, qosNegotiated, chargingId,
                chargingCharacteristics, rncAddress, null, qos2Subscribed, qos2Requested, qos2Negotiated, qos3Subscribed,
                qos3Requested, qos3Negotiated, qos4Subscribed, qos4Requested, qos4Negotiated, extPdpType, extPdpAddress);
        // int pdpContextIdentifier, boolean pdpContextActive, PDPType pdpType, PDPAddress pdpAddress, APN apnSubscribed, APN
        // apnInUse,
        // Integer asapi, TransactionId transactionId, TEID teidForGnAndGp, TEID teidForIu, GSNAddress ggsnAddress,
        // ExtQoSSubscribed qosSubscribed,
        // ExtQoSSubscribed qosRequested, ExtQoSSubscribed qosNegotiated, GPRSChargingID chargingId, ChargingCharacteristics
        // chargingCharacteristics,
        // GSNAddress rncAddress, MAPExtensionContainer extensionContainer, Ext2QoSSubscribed qos2Subscribed, Ext2QoSSubscribed
        // qos2Requested,
        // Ext2QoSSubscribed qos2Negotiated, Ext3QoSSubscribed qos3Subscribed, Ext3QoSSubscribed qos3Requested,
        // Ext3QoSSubscribed qos3Negotiated,
        // Ext4QoSSubscribed qos4Subscribed, Ext4QoSSubscribed qos4Requested, Ext4QoSSubscribed qos4Negotiated, ExtPDPType
        // extPdpType, PDPAddress extPdpAddress
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "subscriberInformation" })
    public void testXMLSerialize() throws Exception {

        PDPTypeImpl pdpType = new PDPTypeImpl(PDPTypeValue.PPP);
        PDPAddressImpl pdpAddress = new PDPAddressImpl(getEncodedPDPAddress());
        APNImpl apnSubscribed = new APNImpl("aaa.com");
        APNImpl apnInUse = new APNImpl("bbb.com");
        TransactionIdImpl transactionId = new TransactionIdImpl(getEncodedTransactionId());
        TEIDImpl teidForGnAndGp = new TEIDImpl(getEncodedTEID_1());
        TEIDImpl teidForIu = new TEIDImpl(getEncodedTEID_2());
        GSNAddressImpl ggsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, getEncodedggsnAddress2());
        ExtQoSSubscribedImpl qosSubscribed = new ExtQoSSubscribedImpl(getEncodedqosSubscribed());
        ExtQoSSubscribedImpl qosRequested = new ExtQoSSubscribedImpl(getEncodedqosRequested());
        ExtQoSSubscribedImpl qosNegotiated = new ExtQoSSubscribedImpl(getEncodedqosNegotiated());
        GPRSChargingIDImpl chargingId = new GPRSChargingIDImpl(getEncodedchargingId());
        ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(
                getEncodedchargingCharacteristics());
        GSNAddressImpl rncAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, getEncodedrncAddress2());
        Ext2QoSSubscribedImpl qos2Subscribed = new Ext2QoSSubscribedImpl(getEncodedqos2Subscribed());
        Ext2QoSSubscribedImpl qos2Requested = new Ext2QoSSubscribedImpl(getEncodedqos2Requested());
        Ext2QoSSubscribedImpl qos2Negotiated = new Ext2QoSSubscribedImpl(getEncodedqos2Negotiated());
        Ext3QoSSubscribedImpl qos3Subscribed = new Ext3QoSSubscribedImpl(getEncodedqos3Subscribed());
        Ext3QoSSubscribedImpl qos3Requested = new Ext3QoSSubscribedImpl(getEncodedqos3Requested());
        Ext3QoSSubscribedImpl qos3Negotiated = new Ext3QoSSubscribedImpl(getEncodedqos3Negotiated());
        Ext4QoSSubscribedImpl qos4Subscribed = new Ext4QoSSubscribedImpl(91);
        Ext4QoSSubscribedImpl qos4Requested = new Ext4QoSSubscribedImpl(92);
        Ext4QoSSubscribedImpl qos4Negotiated = new Ext4QoSSubscribedImpl(93);
        ExtPDPTypeImpl extPdpType = new ExtPDPTypeImpl(getEncodedExtPDPType());
        PDPAddressImpl extPdpAddress = new PDPAddressImpl(getEncodedExtPdpAddress());

//        PDPContextInfoImpl original = new PDPContextInfoImpl(10, true, pdpType, pdpAddress, apnSubscribed, apnInUse, 11,
//                transactionId, teidForGnAndGp, teidForIu, ggsnAddress, qosSubscribed, qosRequested, qosNegotiated, chargingId,
//                chargingCharacteristics, rncAddress, null, qos2Subscribed, qos2Requested, qos2Negotiated, qos3Subscribed,
//                qos3Requested, qos3Negotiated, qos4Subscribed, qos4Requested, qos4Negotiated, extPdpType, extPdpAddress);
        PDPContextInfoImpl original = new PDPContextInfoImpl(10, true, pdpType, pdpAddress, apnSubscribed, apnInUse, 11,
                transactionId, teidForGnAndGp, teidForIu, ggsnAddress, qosSubscribed, qosRequested, qosNegotiated, chargingId,
                chargingCharacteristics, rncAddress, null, qos2Subscribed, qos2Requested, qos2Negotiated, qos3Subscribed,
                qos3Requested, qos3Negotiated, qos4Subscribed, qos4Requested, qos4Negotiated, extPdpType, extPdpAddress);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "pdpContextInfo", PDPContextInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        PDPContextInfoImpl copy = reader.read("pdpContextInfo", PDPContextInfoImpl.class);

        assertEquals(copy.getPdpContextIdentifier(), original.getPdpContextIdentifier());
        assertEquals(copy.getPdpContextActive(), original.getPdpContextActive());

        assertEquals(copy.getPdpType().getData(), original.getPdpType().getData());
        assertEquals(copy.getPdpAddress().getData(), original.getPdpAddress().getData());

        assertEquals(copy.getApnSubscribed().getApn(), original.getApnSubscribed().getApn());
        assertEquals(copy.getApnInUse().getApn(), original.getApnInUse().getApn());
        assertEquals(copy.getNsapi(), original.getNsapi());
        assertEquals(copy.getTransactionId().getData(), original.getTransactionId().getData());

        assertEquals(copy.getTeidForGnAndGp().getData(), original.getTeidForGnAndGp().getData());
        assertEquals(copy.getTeidForIu().getData(), original.getTeidForIu().getData());
        assertEquals(copy.getGgsnAddress().getGSNAddressAddressType(), original.getGgsnAddress().getGSNAddressAddressType());
        assertEquals(copy.getGgsnAddress().getGSNAddressData(), original.getGgsnAddress().getGSNAddressData());
        
        assertEquals(copy.getChargingId().getData(), original.getChargingId().getData());
        
        assertEquals(copy.getChargingCharacteristics().isNormalCharging(), original.getChargingCharacteristics().isNormalCharging());
        assertEquals(copy.getChargingCharacteristics().isPrepaidCharging(), original.getChargingCharacteristics().isPrepaidCharging());
        assertEquals(copy.getChargingCharacteristics().isFlatRateChargingCharging(), original.getChargingCharacteristics().isFlatRateChargingCharging());
        assertEquals(copy.getChargingCharacteristics().isChargingByHotBillingCharging(), original.getChargingCharacteristics().isChargingByHotBillingCharging());
        
        assertEquals(copy.getQosSubscribed().getAllocationRetentionPriority(), original.getQosSubscribed().getAllocationRetentionPriority());
        assertEquals(copy.getQosRequested().getAllocationRetentionPriority(), original.getQosRequested().getAllocationRetentionPriority());
        assertEquals(copy.getQosNegotiated().getAllocationRetentionPriority(), original.getQosNegotiated().getAllocationRetentionPriority());
        
        assertEquals(copy.getRncAddress().getData(), original.getRncAddress().getData());
        assertEquals(copy.getRncAddress().getGSNAddressAddressType(), original.getRncAddress().getGSNAddressAddressType());
        
        assertEquals(copy.getQos2Subscribed().getSourceStatisticsDescriptor(), original.getQos2Subscribed().getSourceStatisticsDescriptor());
        assertEquals(copy.getQos2Subscribed().isOptimisedForSignallingTraffic(), original.getQos2Subscribed().isOptimisedForSignallingTraffic());
        assertEquals(copy.getQos2Requested().getSourceStatisticsDescriptor(), original.getQos2Requested().getSourceStatisticsDescriptor());
        assertEquals(copy.getQos2Requested().isOptimisedForSignallingTraffic(), original.getQos2Requested().isOptimisedForSignallingTraffic());
        assertEquals(copy.getQos2Negotiated().getSourceStatisticsDescriptor(), original.getQos2Negotiated().getSourceStatisticsDescriptor());
        assertEquals(copy.getQos2Negotiated().isOptimisedForSignallingTraffic(), original.getQos2Negotiated().isOptimisedForSignallingTraffic());
        
        assertEquals(copy.getQos3Subscribed().getMaximumBitRateForUplinkExtended().getBitRate(), original.getQos3Subscribed().getMaximumBitRateForUplinkExtended().getBitRate());
        assertEquals(copy.getQos3Requested().getMaximumBitRateForUplinkExtended().getBitRate(), original.getQos3Requested().getMaximumBitRateForUplinkExtended().getBitRate());
        assertEquals(copy.getQos3Negotiated().getMaximumBitRateForUplinkExtended().getBitRate(), original.getQos3Negotiated().getMaximumBitRateForUplinkExtended().getBitRate());
        
        assertEquals(copy.getQos4Subscribed().getData(), original.getQos4Subscribed().getData());
        assertEquals(copy.getQos4Requested().getData(), original.getQos4Requested().getData());
        assertEquals(copy.getQos4Negotiated().getData(), original.getQos4Negotiated().getData());
        
        assertEquals(copy.getExtPdpType().getData(), original.getExtPdpType().getData());
        
        assertEquals(copy.getPdpAddress().getData(), original.getPdpAddress().getData());
        
        
        
    }

}
