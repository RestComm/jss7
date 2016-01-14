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

package org.mobicents.protocols.ss7.m3ua.impl.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.AssociationImpl;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.testng.annotations.Test;

/**
 * @author sergey vetyutnev
 *
 */
public class AspFactoryPayloadTest {

    @Test
    public void testReceive() throws Exception {
        byte[] data = new byte[] { 0x01, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x3c, 0x02, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x06, 0x00, 0x08, 0x00, 0x00, 0x00, 0x19, 0x02, 0x10, 0x00, 0x21, 0x00, 0x00, 0x17, (byte) 0x9d,
                0x00, 0x00, 0x18, 0x1c, 0x03, 0x03, 0x00, 0x02, 0x09, 0x00, 0x03, 0x05, 0x07, 0x02, 0x42, 0x01, 0x02, 0x42,
                0x01, 0x05, 0x03, (byte) 0xd5, 0x1c, 0x18, 0x00, 0x00, 0x00, 0x00 };

        byte[] plData = new byte[] { 9, 0, 3, 5, 7, 2, 66, 1, 2, 66, 1, 5, 3, -43, 28, 24, 0 };

        // SCTP - SCTP layer netty support
        AspFactoryImplProxy aspFactory = new AspFactoryImplProxy(true);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(data);
        org.mobicents.protocols.api.PayloadData pd = new org.mobicents.protocols.api.PayloadData(byteBuf.capacity(), byteBuf,
                true, false, 0, 0);
        AssociationImpl association = new AssociationImpl("hostAddress", 1111, "peerAddress", 1112, "assocName",
                IpChannelType.SCTP, null);
        aspFactory.onPayload(association, pd);

        assertEquals(aspFactory.lstReadMessage.size(), 1);
        M3UAMessage messageImpl = aspFactory.lstReadMessage.get(0);

        assertEquals(MessageType.PAYLOAD, messageImpl.getMessageType());
        PayloadData payloadData = (PayloadData) messageImpl;
        assertEquals(0l, payloadData.getNetworkAppearance().getNetApp());
        assertEquals(1, payloadData.getRoutingContext().getRoutingContexts().length);
        assertEquals(25l, payloadData.getRoutingContext().getRoutingContexts()[0]);
        ProtocolData protocolData = payloadData.getData();
        assertNotNull(protocolData);
        assertEquals(6045, protocolData.getOpc());
        assertEquals(6172, protocolData.getDpc());
        assertEquals(3, protocolData.getSI());
        assertEquals(2, protocolData.getSLS());
        assertEquals(3, protocolData.getNI());
        assertEquals(0, protocolData.getMP());
        assertEquals(plData, protocolData.getData());

        // TCP - SCTP layer netty support
        aspFactory = new AspFactoryImplProxy(true);
        byteBuf = Unpooled.wrappedBuffer(data);
        pd = new org.mobicents.protocols.api.PayloadData(byteBuf.capacity(), byteBuf, true, false, 0, 0);
        association = new AssociationImpl("hostAddress", 1111, "peerAddress", 1112, "assocName", IpChannelType.TCP, null);
        aspFactory.onPayload(association, pd);

        assertEquals(aspFactory.lstReadMessage.size(), 1);
        messageImpl = aspFactory.lstReadMessage.get(0);

        assertEquals(MessageType.PAYLOAD, messageImpl.getMessageType());
        payloadData = (PayloadData) messageImpl;
        assertEquals(0l, payloadData.getNetworkAppearance().getNetApp());
        assertEquals(1, payloadData.getRoutingContext().getRoutingContexts().length);
        assertEquals(25l, payloadData.getRoutingContext().getRoutingContexts()[0]);
        protocolData = payloadData.getData();
        assertNotNull(protocolData);
        assertEquals(6045, protocolData.getOpc());
        assertEquals(6172, protocolData.getDpc());
        assertEquals(3, protocolData.getSI());
        assertEquals(2, protocolData.getSLS());
        assertEquals(3, protocolData.getNI());
        assertEquals(0, protocolData.getMP());

        // SCTP - SCTP layer netty NOT support
        aspFactory = new AspFactoryImplProxy(false);
        byteBuf = Unpooled.wrappedBuffer(data);
        pd = new org.mobicents.protocols.api.PayloadData(byteBuf.capacity(), byteBuf, true, false, 0, 0);
        association = new AssociationImpl("hostAddress", 1111, "peerAddress", 1112, "assocName", IpChannelType.SCTP, null);
        aspFactory.onPayload(association, pd);

        assertEquals(aspFactory.lstReadMessage.size(), 1);
        messageImpl = aspFactory.lstReadMessage.get(0);

        assertEquals(MessageType.PAYLOAD, messageImpl.getMessageType());
        payloadData = (PayloadData) messageImpl;
        assertEquals(0l, payloadData.getNetworkAppearance().getNetApp());
        assertEquals(1, payloadData.getRoutingContext().getRoutingContexts().length);
        assertEquals(25l, payloadData.getRoutingContext().getRoutingContexts()[0]);
        protocolData = payloadData.getData();
        assertNotNull(protocolData);
        assertEquals(6045, protocolData.getOpc());
        assertEquals(6172, protocolData.getDpc());
        assertEquals(3, protocolData.getSI());
        assertEquals(2, protocolData.getSLS());
        assertEquals(3, protocolData.getNI());
        assertEquals(0, protocolData.getMP());

        // TCP - SCTP layer netty NOT support
        aspFactory = new AspFactoryImplProxy(false);
        byteBuf = Unpooled.wrappedBuffer(data);
        pd = new org.mobicents.protocols.api.PayloadData(byteBuf.capacity(), byteBuf, true, false, 0, 0);
        association = new AssociationImpl("hostAddress", 1111, "peerAddress", 1112, "assocName", IpChannelType.TCP, null);
        aspFactory.onPayload(association, pd);

        assertEquals(aspFactory.lstReadMessage.size(), 1);
        messageImpl = aspFactory.lstReadMessage.get(0);

        assertEquals(MessageType.PAYLOAD, messageImpl.getMessageType());
        payloadData = (PayloadData) messageImpl;
        assertEquals(0l, payloadData.getNetworkAppearance().getNetApp());
        assertEquals(1, payloadData.getRoutingContext().getRoutingContexts().length);
        assertEquals(25l, payloadData.getRoutingContext().getRoutingContexts()[0]);
        protocolData = payloadData.getData();
        assertNotNull(protocolData);
        assertEquals(6045, protocolData.getOpc());
        assertEquals(6172, protocolData.getDpc());
        assertEquals(3, protocolData.getSI());
        assertEquals(2, protocolData.getSLS());
        assertEquals(3, protocolData.getNI());
        assertEquals(0, protocolData.getMP());
    }

    @Test
    public void test2TCPPartial() throws Exception {
        // Only header
        byte[] header = new byte[] { 0x01, 0x00, 0x01, 0x01, 0x00, 0x00, 0x01, 0x08 };

        byte[] bodyStart = new byte[] { 0x00, 0x06, 0x00, 0x08, 0x00, 0x00, 0x00, 0x01, 0x02, 0x10, 0x00, (byte) 0xf8, 0x00,
                0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x01, 0x03, 0x02, 0x00, 0x01, 0x09, 0x01, 0x03, 0x10, 0x1d, 0x0d, 0x53,
                0x01, 0x00, (byte) 0x91, 0x00, 0x12, 0x04, 0x19, 0x09, 0x31, (byte) 0x91, 0x39, 0x08, 0x0d, 0x53, 0x02, 0x00,
                (byte) 0x92, 0x00, 0x12, 0x04, 0x19, 0x09, 0x31, (byte) 0x91, 0x39, 0x09, (byte) 0xc6, 0x62, (byte) 0x81,
                (byte) 0xc3, 0x48, 0x04, 0x00, 0x08, 0x00, 0x10, 0x6b, 0x1a, 0x28, 0x18, 0x06, 0x07, 0x00, 0x11, (byte) 0x86,
                0x05, 0x01, 0x01, 0x01, (byte) 0xa0, 0x0d, 0x60, 0x0b, (byte) 0xa1, 0x09, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01,
                0x00, 0x32, 0x01, 0x6c, (byte) 0x81, (byte) 0x9e, (byte) 0xa1, (byte) 0x81, (byte) 0x9b, 0x02, 0x01, 0x01,
                0x02, 0x01, 0x00, 0x30, (byte) 0x81, (byte) 0x92, (byte) 0x80, 0x01, 0x0c, (byte) 0x82, 0x09, 0x03, 0x10, 0x13,
                0x60, (byte) 0x99, (byte) 0x86, 0x00, 0x00, 0x02, (byte) 0x83, 0x08, 0x04, 0x13, 0x19, (byte) 0x89, 0x17,
                (byte) 0x97, 0x31, 0x72, (byte) 0x85, 0x01, 0x0a, (byte) 0x88, 0x01, 0x01, (byte) 0x8a, 0x05, 0x04, 0x13, 0x19,
                (byte) 0x89, (byte) 0x86, (byte) 0xbb, 0x04, (byte) 0x80, 0x02, (byte) 0x80, };

        byte[] bodyEndWithOtherMessage = { (byte) 0x90, (byte) 0x9c, 0x01, 0x0c, (byte) 0x9f, 0x32, 0x08, 0x04, 0x64, 0x58,
                0x05, (byte) 0x94, 0x74, 0x34, (byte) 0xf3, (byte) 0xbf, 0x33, 0x02, (byte) 0x80, 0x00, (byte) 0xbf, 0x34,
                0x2b, 0x02, 0x01, 0x23, (byte) 0x80, 0x08, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x81, 0x07,
                (byte) 0x91, 0x19, (byte) 0x89, (byte) 0x86, (byte) 0x91, 0x01, (byte) 0x82, (byte) 0x82, 0x08, 0x04,
                (byte) 0x97, 0x19, (byte) 0x89, (byte) 0x86, (byte) 0x91, 0x01, (byte) 0x82, (byte) 0xa3, 0x09, (byte) 0x80,
                0x07, 0x04, (byte) 0xf4, (byte) 0x86, 0x00, 0x65, 0x18, (byte) 0xd1, (byte) 0xbf, 0x35, 0x03, (byte) 0x83,
                0x01, 0x11, (byte) 0x9f, 0x36, 0x08, (byte) 0xd2, 0x25, 0x00, 0x00, 0x0d, 0x62, 0x0b, (byte) 0x88, (byte) 0x9f,
                0x37, 0x07, (byte) 0x91, 0x19, (byte) 0x89, (byte) 0x86, (byte) 0x95, (byte) 0x99, (byte) 0x89, (byte) 0x9f,
                0x39, 0x08, 0x02, 0x11, 0x20, 0x10, (byte) 0x91, 0x45, 0x51, 0x23, 0x01, 0x00, 0x01, 0x01, 0x00, 0x00, 0x01,
                0x08, 0x00, 0x06, 0x00, 0x08, 0x00, 0x00, 0x00, 0x01, 0x02, 0x10, 0x00, (byte) 0xf8, 0x00, 0x00, 0x00, 0x02,
                0x00, 0x00, 0x00, 0x01, 0x03, 0x02, 0x00, 0x01, 0x09, 0x01, 0x03, 0x10, 0x1d, 0x0d, 0x53, 0x01, 0x00,
                (byte) 0x91, 0x00, 0x12, 0x04, 0x19, 0x09, 0x31, (byte) 0x91, 0x39, 0x08, 0x0d, 0x53, 0x02, 0x00, (byte) 0x92,
                0x00, 0x12, 0x04, 0x19, 0x09, 0x31, (byte) 0x91, 0x39, 0x09, (byte) 0xc6, 0x62, (byte) 0x81, (byte) 0xc3, 0x48,
                0x04, 0x00, 0x08, 0x00, 0x10, 0x6b, 0x1a, 0x28, 0x18, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01,
                0x01, (byte) 0xa0, 0x0d, 0x60, 0x0b, (byte) 0xa1, 0x09, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x00, 0x32, 0x01,
                0x6c, (byte) 0x81, (byte) 0x9e, (byte) 0xa1, (byte) 0x81, (byte) 0x9b, 0x02, 0x01, 0x01, 0x02, 0x01, 0x00,
                0x30, (byte) 0x81, (byte) 0x92, (byte) 0x80, 0x01, 0x0c, (byte) 0x82, 0x09, 0x03, 0x10, 0x13, 0x60,
                (byte) 0x99, (byte) 0x86, 0x00, 0x00, 0x02, (byte) 0x83, 0x08, 0x04, 0x13, 0x19, (byte) 0x89, 0x17,
                (byte) 0x97, 0x31, 0x72, (byte) 0x85, 0x01, 0x0a, (byte) 0x88, 0x01, 0x01, (byte) 0x8a, 0x05, 0x04, 0x13, 0x19,
                (byte) 0x89, (byte) 0x86, (byte) 0xbb, 0x04, (byte) 0x80, 0x02, (byte) 0x80, (byte) 0x90, (byte) 0x9c, 0x01,
                0x0c, (byte) 0x9f, 0x32, 0x08, 0x04, 0x64, 0x58, 0x05, (byte) 0x94, 0x74, 0x34, (byte) 0xf3, (byte) 0xbf, 0x33,
                0x02, (byte) 0x80, 0x00, (byte) 0xbf, 0x34, 0x2b, 0x02, 0x01, 0x23, (byte) 0x80, 0x08, 0x10, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, (byte) 0x81, 0x07, (byte) 0x91, 0x19, (byte) 0x89, (byte) 0x86, (byte) 0x91, 0x01,
                (byte) 0x82, (byte) 0x82, 0x08, 0x04, (byte) 0x97, 0x19, (byte) 0x89, (byte) 0x86, (byte) 0x91, 0x01,
                (byte) 0x82, (byte) 0xa3, 0x09, (byte) 0x80, 0x07, 0x04, (byte) 0xf4, (byte) 0x86, 0x00, 0x65, 0x18,
                (byte) 0xd1, (byte) 0xbf, 0x35, 0x03, (byte) 0x83, 0x01, 0x11, (byte) 0x9f, 0x36, 0x08, (byte) 0xd2, 0x25,
                0x00, 0x00, 0x0d, 0x62, 0x0b, (byte) 0x88, (byte) 0x9f, 0x37, 0x07, (byte) 0x91, 0x19, (byte) 0x89,
                (byte) 0x86, (byte) 0x95, (byte) 0x99, (byte) 0x89, (byte) 0x9f, 0x39, 0x08, 0x02, 0x11, 0x20, 0x10,
                (byte) 0x91, 0x45, 0x51, 0x23 };

        AspFactoryImplProxy aspFactory = new AspFactoryImplProxy(true);
        AssociationImpl association = new AssociationImpl("hostAddress", 1111, "peerAddress", 1112, "assocName",
                IpChannelType.TCP, null);

        ByteBuf byteBuf = Unpooled.wrappedBuffer(header);
        org.mobicents.protocols.api.PayloadData pd = new org.mobicents.protocols.api.PayloadData(byteBuf.capacity(), byteBuf,
                true, false, 0, 0);
        aspFactory.onPayload(association, pd);        

        assertEquals(aspFactory.lstReadMessage.size(), 0);

        // Now lets read only first half of body and yet we have null
        // messageImpl
        byteBuf = Unpooled.wrappedBuffer(bodyStart);
        pd = new org.mobicents.protocols.api.PayloadData(byteBuf.capacity(), byteBuf,
                true, false, 0, 0);
        aspFactory.onPayload(association, pd);        
        assertEquals(aspFactory.lstReadMessage.size(), 0);

        // Now lets read other half
        byteBuf = Unpooled.wrappedBuffer(bodyEndWithOtherMessage);
        pd = new org.mobicents.protocols.api.PayloadData(byteBuf.capacity(), byteBuf,
                true, false, 0, 0);
        aspFactory.onPayload(association, pd);        
        assertEquals(aspFactory.lstReadMessage.size(), 2);
        M3UAMessage messageImpl = aspFactory.lstReadMessage.get(0);

        assertEquals(MessageType.PAYLOAD, messageImpl.getMessageType());
        PayloadData payloadData = (PayloadData) messageImpl;
        assertNull(payloadData.getNetworkAppearance());
        assertEquals(1, payloadData.getRoutingContext().getRoutingContexts().length);
        assertEquals(1l, payloadData.getRoutingContext().getRoutingContexts()[0]);
        ProtocolData protocolData = payloadData.getData();
        assertNotNull(protocolData);
        assertEquals(2, protocolData.getOpc());
        assertEquals(1, protocolData.getDpc());
        assertEquals(3, protocolData.getSI());
        assertEquals(1, protocolData.getSLS());
        assertEquals(2, protocolData.getNI());
        assertEquals(0, protocolData.getMP());

        messageImpl = aspFactory.lstReadMessage.get(1);

        assertEquals(MessageType.PAYLOAD, messageImpl.getMessageType());
        payloadData = (PayloadData) messageImpl;
        assertNull(payloadData.getNetworkAppearance());
        assertEquals(1, payloadData.getRoutingContext().getRoutingContexts().length);
        assertEquals(1l, payloadData.getRoutingContext().getRoutingContexts()[0]);
        protocolData = payloadData.getData();
        assertNotNull(protocolData);
        assertEquals(2, protocolData.getOpc());
        assertEquals(1, protocolData.getDpc());
        assertEquals(3, protocolData.getSI());
        assertEquals(1, protocolData.getSLS());
        assertEquals(2, protocolData.getNI());
        assertEquals(0, protocolData.getMP());        
        
    }

    @Test
    public void testSend() throws Exception {
        byte[] data = new byte[] { 0x01, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x3c, 0x02, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x06, 0x00, 0x08, 0x00, 0x00, 0x00, 0x19, 0x02, 0x10, 0x00, 0x21, 0x00, 0x00, 0x17, (byte) 0x9d,
                0x00, 0x00, 0x18, 0x1c, 0x03, 0x03, 0x00, 0x02, 0x09, 0x00, 0x03, 0x05, 0x07, 0x02, 0x42, 0x01, 0x02, 0x42,
                0x01, 0x05, 0x03, (byte) 0xd5, 0x1c, 0x18, 0x00, 0x00, 0x00, 0x00 };

        byte[] plData = new byte[] { 9, 0, 3, 5, 7, 2, 66, 1, 2, 66, 1, 5, 3, -43, 28, 24, 0 };

        // SCTP - SCTP layer netty support
        AspFactoryImplProxy aspFactory = new AspFactoryImplProxy(true);
        AssociationImplProxy association = new AssociationImplProxy();
        aspFactory.setAssociation(association);

        ParameterFactoryImpl parameterFactory = new ParameterFactoryImpl();

        PayloadDataImpl message = new PayloadDataImpl();
        NetworkAppearance na = parameterFactory.createNetworkAppearance(0l);
        RoutingContext rc = parameterFactory.createRoutingContext(new long[] { 25l });
        ProtocolData p = parameterFactory.createProtocolData(6045, 6172, 3, 3, 0, 2, plData);
        
        message.setNetworkAppearance(na);
        message.setRoutingContext(rc);
        message.setData(p);

        aspFactory.doWrite(message);

        assertEquals(association.lstWriteMessage.size(), 1);
        org.mobicents.protocols.api.PayloadData pl2 = association.lstWriteMessage.get(0);
        assertEquals(pl2.getData(), data);

        
        // SCTP - SCTP layer NOT netty support
        aspFactory = new AspFactoryImplProxy(false);
        association = new AssociationImplProxy();
        aspFactory.setAssociation(association);

        message = new PayloadDataImpl();
        na = parameterFactory.createNetworkAppearance(0l);
        rc = parameterFactory.createRoutingContext(new long[] { 25l });
        p = parameterFactory.createProtocolData(6045, 6172, 3, 3, 0, 2, plData);

        message.setNetworkAppearance(na);
        message.setRoutingContext(rc);
        message.setData(p);

        aspFactory.doWrite(message);

        assertEquals(association.lstWriteMessage.size(), 1);
        pl2 = association.lstWriteMessage.get(0);
        assertEquals(pl2.getData(), data);
    }

    private class AspFactoryImplProxy extends AspFactoryImpl {
        protected ArrayList<M3UAMessage> lstReadMessage = new ArrayList<M3UAMessage>();

        public AspFactoryImplProxy(boolean nettySupport) {
            super("M3uaAspFact", 16, 1, false);
            // int maxSequenceNumber, long aspId
            M3UAManagementImpl m3uaManagement = new M3UAManagementImplProxy("Test", "m3ua", nettySupport);
            this.setM3UAManagement(m3uaManagement);
            this.createSLSTable(8);
        }

        protected void read(M3UAMessage message) {
            lstReadMessage.add(message);
        }

        public void doWrite(M3UAMessage message) {
            super.write(message);
        }

        public void setAssociation(Association association) {
            super.setAssociation(association);
        }
    }
    
    private class M3UAManagementImplProxy extends M3UAManagementImpl {
        public M3UAManagementImplProxy(String name, String productName, boolean sctpLibNettySupport) {
            super(name, productName);
            this.sctpLibNettySupport = sctpLibNettySupport;
        }
    }

    private class AssociationImplProxy extends AssociationImpl {
        protected ArrayList<org.mobicents.protocols.api.PayloadData> lstWriteMessage = new ArrayList<org.mobicents.protocols.api.PayloadData>();

        public AssociationImplProxy() throws Exception {
            super("hostAddress", 1111, "peerAddress", 1112, "assocName", IpChannelType.SCTP, null);
        }

        @Override
        public void send(org.mobicents.protocols.api.PayloadData payloadData) throws Exception {
            lstWriteMessage.add(payloadData);
        }
    }
}
