/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

/**
 * 
 */
package org.mobicents.protocols.ss7.sccp;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.XUnitDataImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author baranowb
 *
 */
public class XUnitDataTest {
    private static final GlobalTitle _CALLING_PARTY_GLOBAL_TITLE = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL, "1111111");//new GT0100Codec(0, 1, 1, 1, "1111111");
    private static SccpAddress _CALLING_PARTY = null;//new SccpAddressCodec(1, 1, 4, 1, 1234, 1, _CALLING_PARTY_GLOBAL_TITLE);
    private static final GlobalTitle _CALLED_PARTY_GLOBAL_TITLE = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL, "22222222");//new GT0100Codec(1, 1, 4, 2, "22222222");
    private static SccpAddress _CALLED_PARTY = null;//new SccpAddressCodec(1, 1, 4, 1, 10922, 2, _CALLED_PARTY_GLOBAL_TITLE);
    private static final byte[] _DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
    private static final byte[] _SEGMENTATION_REF = new byte[]{10, 11, 12, 45};
    private static final SegmentationImpl _SEGMENTATION = new SegmentationImpl(true, false, (byte) 1, _SEGMENTATION_REF);
    private static final ImportanceImpl _IMPORTANCE = new ImportanceImpl((byte) 4);
    private static final HopCounter _HOPE_COUTNER = new HopCounterImpl(14);
    private static final ProtocolClassImpl _PROTOCOL_CLASS = new ProtocolClassImpl(1,1);
    
    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();

    @BeforeMethod
    public void setUp() {
        _CALLING_PARTY = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, _CALLING_PARTY_GLOBAL_TITLE, 0);
        _CALLED_PARTY = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,_CALLED_PARTY_GLOBAL_TITLE, 0);
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test(groups = { "udt","functional.decode",})
    public void testEncodeDecode() throws Exception {


        XUnitDataImpl testObject = (XUnitDataImpl) messageFactory.createXUnitData(_HOPE_COUTNER, _PROTOCOL_CLASS, _CALLED_PARTY, _CALLING_PARTY);
        testObject.setData(_DATA);
        
        ByteArrayOutputStream bas = new ByteArrayOutputStream(1);
        testObject.encode(bas);
        byte[] encoded = bas.toByteArray();
        assertEquals( (int)encoded[0], XUnitData.MESSAGE_TYPE,"MT indicator must has wrong value.");

        //we loose MT. 
        byte[] toDecode = new byte[encoded.length - 1];
        System.arraycopy(encoded, 1, toDecode, 0, toDecode.length);
        XUnitDataImpl testObjectDecoded = (XUnitDataImpl) messageFactory.createMessage(XUnitData.MESSAGE_TYPE, new ByteArrayInputStream(toDecode));

        assertEquals( testObjectDecoded.getProtocolClass(), _PROTOCOL_CLASS,"Protocol Class does not equal.");
        assertEquals( testObjectDecoded.getCalledPartyAddress().getGlobalTitle().getDigits(), _CALLED_PARTY.getGlobalTitle().getDigits(),"Called Party does not equal.");
        assertEquals( testObjectDecoded.getCallingPartyAddress().getGlobalTitle().getDigits(), _CALLING_PARTY.getGlobalTitle().getDigits(),"Calling Party does not equal.");
        //assertEquals( testObjectDecoded.getData(),_DATA,"Data does not equal.");
        assertTrue( Arrays.equals(_DATA, testObjectDecoded.getData()));
    //There is no optional param here.

    }

    @Test(groups = { "udt", "functional.decode","functional.encode"})
    public void testEncodeDecodeWithOneOptional() throws Exception {


        XUnitDataImpl testObject = (XUnitDataImpl) messageFactory.createXUnitData(_HOPE_COUTNER, _PROTOCOL_CLASS, _CALLED_PARTY, _CALLING_PARTY);
        testObject.setData(_DATA);
        testObject.setImportance(_IMPORTANCE);
        ByteArrayOutputStream bas = new ByteArrayOutputStream(1);
        testObject.encode(bas);
        byte[] encoded = bas.toByteArray();
        assertEquals( encoded[0], XUnitData.MESSAGE_TYPE,"MT indicator must has wrong value.");

        //we loose MT. 
        byte[] toDecode = new byte[encoded.length - 1];
        System.arraycopy(encoded, 1, toDecode, 0, toDecode.length);
        XUnitDataImpl testObjectDecoded = (XUnitDataImpl) messageFactory.createMessage(XUnitData.MESSAGE_TYPE, new ByteArrayInputStream(toDecode));

        testObjectDecoded.decode(new ByteArrayInputStream(toDecode));

        assertEquals( testObjectDecoded.getProtocolClass(), _PROTOCOL_CLASS,"Protocol Class does not equal.");
        assertEquals( testObjectDecoded.getCalledPartyAddress(), _CALLED_PARTY,"Called Party does not equal.");
        assertEquals( testObjectDecoded.getCallingPartyAddress(), _CALLING_PARTY,"Calling Party does not equal.");
        assertTrue( Arrays.equals(_DATA, testObjectDecoded.getData()));
        assertNotNull( testObjectDecoded.getImportance(),"Importance must not be null");
        assertEquals( testObjectDecoded.getImportance(), _IMPORTANCE,"Importance does not match. ");
    }
//	
    @Test(groups = { "udt", "functional.decode","functional.encode"})
    public void testEncodeDecodeWithBothOptional() throws Exception {
        XUnitDataImpl testObject = (XUnitDataImpl) messageFactory.createXUnitData(_HOPE_COUTNER, _PROTOCOL_CLASS, _CALLED_PARTY, _CALLING_PARTY);
        testObject.setData(_DATA);
        testObject.setImportance(_IMPORTANCE);
        testObject.setSegmentation(_SEGMENTATION);
        ByteArrayOutputStream bas = new ByteArrayOutputStream(1);
        testObject.encode(bas);
        byte[] encoded = bas.toByteArray();
        assertEquals( encoded[0], XUnitData.MESSAGE_TYPE,"MT indicator must has wrong value.");

        //we loose MT. 
        byte[] toDecode = new byte[encoded.length - 1];
        System.arraycopy(encoded, 1, toDecode, 0, toDecode.length);
        XUnitDataImpl testObjectDecoded = (XUnitDataImpl) messageFactory.createMessage(XUnitData.MESSAGE_TYPE, new ByteArrayInputStream(toDecode));

        assertEquals( testObjectDecoded.getProtocolClass(), _PROTOCOL_CLASS,"Protocol Class does not equal.");
        assertEquals( testObjectDecoded.getCalledPartyAddress(), _CALLED_PARTY,"Called Party does not equal.");
        assertEquals( testObjectDecoded.getCallingPartyAddress(), _CALLING_PARTY,"Calling Party does not equal.");
        assertTrue( Arrays.equals(_DATA, testObjectDecoded.getData()));
        assertNotNull( testObjectDecoded.getImportance(),"Importance must not be null");
        assertEquals( testObjectDecoded.getImportance(), _IMPORTANCE,"Importance does not match. ");
        assertNotNull( testObjectDecoded.getSegmentation(),"Segmentation must not be null");
        assertEquals( testObjectDecoded.getSegmentation(), _SEGMENTATION,"Segmentation does not match. ");

    }

    @Test(groups = { "udt", "functional.decode"})
    public void testRealDecode() throws Exception {


        //This is real msg dump :)
        byte[] b = new byte[]{(byte) 0x01, (byte) 0x0f, (byte) 0x04, (byte) 0x0f, (byte) 0x1a, (byte) 0xac, (byte) 0x0b, (byte) 0x12, (byte) 0x92, (byte) 0x00, (byte) 0x11, (byte) 0x04, (byte) 0x97, (byte) 0x20, (byte) 0x73, (byte) 0x00, (byte) 0x72, (byte) 0x01, (byte) 0x0b, (byte) 0x12, (byte) 0x92, (byte) 0x00, (byte) 0x11, (byte) 0x04, (byte) 0x97, (byte) 0x20, (byte) 0x73, (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x92, (byte) 0x62, (byte) 0x81, (byte) 0x8f, (byte) 0x48, (byte) 0x04, (byte) 0x22, (byte) 0x00, (byte) 0x01, (byte) 0x04, (byte) 0x6b, (byte) 0x1e, (byte) 0x28, (byte) 0x1c, (byte) 0x06, (byte) 0x07, (byte) 0x00, (byte) 0x11, (byte) 0x86, (byte) 0x05, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0xa0, (byte) 0x11, (byte) 0x60, (byte) 0x0f, (byte) 0x80, (byte) 0x02, (byte) 0x07, (byte) 0x80, (byte) 0xa1, (byte) 0x09, (byte) 0x06, (byte) 0x07, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x32, (byte) 0x01, (byte) 0x6c, (byte) 0x80, (byte) 0xa1, (byte) 0x63, (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0x30, (byte) 0x5b, (byte) 0x80, (byte) 0x01, (byte) 0x08, (byte) 0x82, (byte) 0x08, (byte) 0x84, (byte) 0x90, (byte) 0x97, (byte) 0x20, (byte) 0x83, (byte) 0x20, (byte) 0x68, (byte) 0x06, (byte) 0x83, (byte) 0x07, (byte) 0x03, (byte) 0x13, (byte) 0x09, (byte) 0x32, (byte) 0x26, (byte) 0x59, (byte) 0x18, (byte) 0x85, (byte) 0x01, (byte) 0x0a, (byte) 0x8a, (byte) 0x08, (byte) 0x84, (byte) 0x93, (byte) 0x97, (byte) 0x20, (byte) 0x73, (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0xbb, (byte) 0x05, (byte) 0x80, (byte) 0x03, (byte) 0x80, (byte) 0x90, (byte) 0xa3, (byte) 0x9c, (byte) 0x01, (byte) 0x0c, (byte) 0x9f, (byte) 0x32, (byte) 0x08, (byte) 0x52, (byte) 0x00, (byte) 0x07, (byte) 0x32, (byte) 0x01, (byte) 0x56, (byte) 0x04, (byte) 0xf2, (byte) 0xbf, (byte) 0x35, (byte) 0x03, (byte) 0x83, (byte) 0x01, (byte) 0x11, (byte) 0x9f, (byte) 0x36, (byte) 0x05, (byte) 0x99, (byte) 0x88, (byte) 0x1d, (byte) 0x00, (byte) 0x01, (byte) 0x9f, (byte) 0x37, (byte) 0x07, (byte) 0x91, (byte) 0x97, (byte) 0x20, (byte) 0x73, (byte) 0x00, (byte) 0x02, (byte) 0xf1, (byte) 0x9f, (byte) 0x39, (byte) 0x08, (byte) 0x02, (byte) 0x90, (byte) 0x11, (byte) 0x82, (byte) 0x02, (byte) 0x15, (byte) 0x23, (byte) 0x61, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x01, (byte) 0x06, (byte) 0x00, (byte) 0x21, (byte) 0x7c};

        XUnitDataImpl testObjectDecoded = (XUnitDataImpl) messageFactory.createMessage(XUnitData.MESSAGE_TYPE, new ByteArrayInputStream(b));
    }
}
