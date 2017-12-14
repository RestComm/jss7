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

package org.mobicents.protocols.ss7.sccp.impl.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SccpDataMessageTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("SccpDataMessageTestStack");
    private MessageFactoryImpl messageFactory;

    @BeforeMethod
    public void setUp() {
        this.stack.setPersistDir(Util.getTmpTestDir());
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = Logger.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @AfterMethod
    public void tearDown() {
    }

    public byte[] getDataUdt() {
        return new byte[] { 9, 0x01, 0x03, 0x05, 0x09, 0x02, 0x42, 0x08, 0x04, 0x43, 0x01, 0x00, 0x08, 0x5D, 0x62, 0x5B, 0x48,
                0x04, 0x00, 0x02, 0x00, 0x30, 0x6B, 0x1A, 0x28, 0x18, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01,
                0x01, (byte) 0xA0, 0x0D, 0x60, 0x0B, (byte) 0xA1, 0x09, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x00, 0x19, 0x02,
                0x6C, 0x37, (byte) 0xA1, 0x35, 0x02, 0x01, 0x01, 0x02, 0x01, 0x2E, 0x30, 0x2D, (byte) 0x80, 0x05, (byte) 0x89,
                0x67, 0x45, 0x23, (byte) 0xF1, (byte) 0x84, 0x06, (byte) 0xA1, 0x21, 0x43, 0x65, (byte) 0x87, (byte) 0xF9,
                0x04, 0x1C, 0x2C, 0x09, 0x04, 0x21, 0x43, 0x65, (byte) 0x87, (byte) 0xF9, 0x04, 0x00, 0x11, 0x30, (byte) 0x92,
                0x60, 0x60, 0x62, 0x00, 0x0B, (byte) 0xC8, 0x32, (byte) 0x9B, (byte) 0xFD, 0x06, 0x5D, (byte) 0xDF, 0x72, 0x36,
                0x19 };
    }

    public byte[] getDataUdtSrc() {
        return new byte[] { 0x62, 0x5B, 0x48, 0x04, 0x00, 0x02, 0x00, 0x30, 0x6B, 0x1A, 0x28, 0x18, 0x06, 0x07, 0x00, 0x11,
                (byte) 0x86, 0x05, 0x01, 0x01, 0x01, (byte) 0xA0, 0x0D, 0x60, 0x0B, (byte) 0xA1, 0x09, 0x06, 0x07, 0x04, 0x00,
                0x00, 0x01, 0x00, 0x19, 0x02, 0x6C, 0x37, (byte) 0xA1, 0x35, 0x02, 0x01, 0x01, 0x02, 0x01, 0x2E, 0x30, 0x2D,
                (byte) 0x80, 0x05, (byte) 0x89, 0x67, 0x45, 0x23, (byte) 0xF1, (byte) 0x84, 0x06, (byte) 0xA1, 0x21, 0x43,
                0x65, (byte) 0x87, (byte) 0xF9, 0x04, 0x1C, 0x2C, 0x09, 0x04, 0x21, 0x43, 0x65, (byte) 0x87, (byte) 0xF9, 0x04,
                0x00, 0x11, 0x30, (byte) 0x92, 0x60, 0x60, 0x62, 0x00, 0x0B, (byte) 0xC8, 0x32, (byte) 0x9B, (byte) 0xFD, 0x06,
                0x5D, (byte) 0xDF, 0x72, 0x36, 0x19 };
    }

    public byte[] getDataUdt1() {
        return new byte[] { 9, 0x00, 0x03, 0x05, 0x09, 0x02, 0x42, 0x01, 0x04, 0x43, 0x01, 0x00, 0x01, 0x05, 0x03, 0x08, 0x02,
                0x00, 0x00 };
    }

    public byte[] getDataUdt2() {
        return new byte[] { 9, (byte) 129, 0x03, 0x05, 0x09, 2, 66, 8, 4, 67, 1, 0, 6, 5, 11, 12, 13, 14, 15 };
    }

    public byte[] getDataUdtSrc1() {
        return new byte[] { 0x03, 0x08, 0x02, 0x00, 0x00 };
    }

    public byte[] getDataXudt() {
        return new byte[] { 0x11, (byte) 0x81, 0x0f, 0x2b, 0x03, 0x0f, 0x00, 0x0c, 0x12, 0x08, 0x00, 0x11, 0x04, 0x32,
                (byte) 0x84, 0x30, 0x00, (byte) 0x80, (byte) 0x81, 0x00, 0x19, 0x64, 0x17, 0x49, 0x03, 0x03, (byte) 0xb5,
                (byte) 0xd7, 0x6c, 0x10, (byte) 0xa2, 0x0e, 0x02, 0x01, 0x01, 0x30, 0x09, 0x02, 0x01, 0x2c, 0x30, 0x04, 0x04,
                0x02, 0x00, 0x00, 0x0c, 0x12, 0x08, 0x00, 0x11, 0x04, 0x32, (byte) 0x84, 0x30, 0x00, 0x00, (byte) 0x84, 0x00 };
    }

    public byte[] getDataXudt1() {
        return new byte[] { 17, (byte) 129, 15, 4, 6, 10, 15, 2, 66, 8, 4, 67, 1, 0, 6, 5, 11, 12, 13, 14, 15, 16, 4,
                (byte) 194, 100, 0, 0, 18, 1, 7, 0 };
    }

    public byte[] getDataXudt2() {
        return new byte[] { 17, (byte) 129, 15, 4, 6, 10, 15, 2, 66, 8, 4, 67, 1, 0, 6, 5, 11, 12, 13, 14, 15, 18, 1, 7, 0 };
    }

    public byte[] getDataXudt3() {
        return new byte[] { 17, (byte) 129, 15, 4, 6, 10, 0, 2, 66, 8, 4, 67, 1, 0, 6, 5, 11, 12, 13, 14, 15 };
    }

    public static byte[] getDataXudt1Src() {
        return new byte[] { 11, 12, 13, 14, 15 };
    }

    public byte[] getDataLudt1() {
        return new byte[] { 19, 1, 10, 7, 00, 8, 00, 11, 00, 16, 00, 2, 66, 8, 4, 67, 1, 0, 6, 5, 00, 11, 12, 13, 14, 15, 16,
                4, (byte) 192, 1, 0, 0, 18, 1, 7, 0 };
    }

    public byte[] getDataLudt2() {
        return new byte[] { 19, 1, 10, 7, 00, 8, 00, 11, 00, 00, 00, 2, 66, 8, 4, 67, 1, 0, 6, 5, 00, 11, 12, 13, 14, 15 };
    }

    public byte[] getDataLudt3() {
        return new byte[] { 19, 1, 10, 7, 00, 8, 00, 11, 00, 55, 01, 2, 66, 8, 4, 67, 1, 0, 6, 44, 01, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
                37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64,
                65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92,
                93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115,
                116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42,
                43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
                71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98,
                99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
                121, 122, 123, 124, 125, 126, 127, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 18, 1, 7, 0 };
    }

    public byte[] getDataLudt3Src() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
                28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55,
                56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83,
                84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108,
                109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 0, 1, 2, 3, 4,
                5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33,
                34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61,
                62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89,
                90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113,
                114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                41, 42, 43, 44, };
    }

    @Test(groups = { "SccpMessage", "functional.decode" })
    public void testDecode() throws Exception {

        // ---- UDT
        // This is data comes from Dialogic MTU test sending the SMS message
        byte[] b = this.getDataUdt();

        ByteArrayInputStream buf = new ByteArrayInputStream(b);
        int type = buf.read();
        SccpDataMessage testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(testObjectDecoded);
        assertNotNull(testObjectDecoded);

        SccpAddress calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertNull(calledAdd.getGlobalTitle());

        SccpAddress callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 8);
        assertNull(callingAdd.getGlobalTitle());

        // ---- UDT Management message
        // This is data comes from Dialogic MTU test
        b = this.getDataUdt1();
        buf = new ByteArrayInputStream(b);
        type = buf.read();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(testObjectDecoded);
        assertNotNull(testObjectDecoded);

        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSubsystemNumber(), 1);
        assertNull(calledAdd.getGlobalTitle());

        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 1);
        assertNull(callingAdd.getGlobalTitle());

        // ---- XUDT from trace
        b = this.getDataXudt();
        buf = new ByteArrayInputStream(b);
        type = buf.read();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(testObjectDecoded);
        assertNotNull(testObjectDecoded);
        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertTrue(calledAdd.getGlobalTitle().getDigits().equals("2348030000480"));
        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 0);
        assertEquals(callingAdd.getSubsystemNumber(), 8);
        assertTrue(callingAdd.getGlobalTitle().getDigits().equals("2348030008180"));
        assertNull(testObjectDecoded.getSegmentation());
        assertNull(testObjectDecoded.getImportance());

        // ---- XUDT all param
        b = this.getDataXudt1();
        buf = new ByteArrayInputStream(b);
        type = buf.read();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(testObjectDecoded);
        assertNotNull(testObjectDecoded);

        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 1);
        assertTrue(testObjectDecoded.getProtocolClass().getReturnMessageOnError());
        assertEquals(testObjectDecoded.getHopCounter().getValue(), 15);
        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertNull(calledAdd.getGlobalTitle());
        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 6);
        assertNull(callingAdd.getGlobalTitle());
        assertTrue(testObjectDecoded.getSegmentation().isFirstSegIndication());
        assertTrue(testObjectDecoded.getSegmentation().isClass1Selected());
        assertEquals(testObjectDecoded.getSegmentation().getRemainingSegments(), 2);
        assertEquals(testObjectDecoded.getSegmentation().getSegmentationLocalRef(), 100);
        assertEquals(testObjectDecoded.getImportance().getValue(), 7);
        assertTrue(Arrays.equals(testObjectDecoded.getData(), getDataXudt1Src()));

        // ---- XUDT without segm
        b = this.getDataXudt2();
        buf = new ByteArrayInputStream(b);
        type = buf.read();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(testObjectDecoded);
        assertNotNull(testObjectDecoded);

        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 1);
        assertTrue(testObjectDecoded.getProtocolClass().getReturnMessageOnError());
        assertEquals(testObjectDecoded.getHopCounter().getValue(), 15);
        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertNull(calledAdd.getGlobalTitle());
        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 6);
        assertNull(callingAdd.getGlobalTitle());
        assertNull(testObjectDecoded.getSegmentation());
        assertEquals(testObjectDecoded.getImportance().getValue(), 7);
        assertTrue(Arrays.equals(testObjectDecoded.getData(), getDataXudt1Src()));

        // ---- XUDT without segm & importance
        b = this.getDataXudt3();
        buf = new ByteArrayInputStream(b);
        type = buf.read();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(testObjectDecoded);
        assertNotNull(testObjectDecoded);

        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 1);
        assertTrue(testObjectDecoded.getProtocolClass().getReturnMessageOnError());
        assertEquals(testObjectDecoded.getHopCounter().getValue(), 15);
        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertNull(calledAdd.getGlobalTitle());
        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 6);
        assertNull(callingAdd.getGlobalTitle());
        assertNull(testObjectDecoded.getSegmentation());
        assertNull(testObjectDecoded.getImportance());
        assertTrue(Arrays.equals(testObjectDecoded.getData(), getDataXudt1Src()));

        // ---- LUDT all param
        b = this.getDataLudt1();
        buf = new ByteArrayInputStream(b);
        type = buf.read();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(testObjectDecoded);
        assertNotNull(testObjectDecoded);

        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 1);
        assertFalse(testObjectDecoded.getProtocolClass().getReturnMessageOnError());
        assertEquals(testObjectDecoded.getHopCounter().getValue(), 10);
        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertNull(calledAdd.getGlobalTitle());
        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 6);
        assertNull(callingAdd.getGlobalTitle());
        assertTrue(testObjectDecoded.getSegmentation().isFirstSegIndication());
        assertTrue(testObjectDecoded.getSegmentation().isClass1Selected());
        assertEquals(testObjectDecoded.getSegmentation().getRemainingSegments(), 0);
        assertEquals(testObjectDecoded.getSegmentation().getSegmentationLocalRef(), 1);
        assertEquals(testObjectDecoded.getImportance().getValue(), 7);
        assertTrue(Arrays.equals(testObjectDecoded.getData(), getDataXudt1Src()));

        // ---- LUDT no segm and importance par
        b = this.getDataLudt2();
        buf = new ByteArrayInputStream(b);
        type = buf.read();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(testObjectDecoded);
        assertNotNull(testObjectDecoded);

        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 1);
        assertFalse(testObjectDecoded.getProtocolClass().getReturnMessageOnError());
        assertEquals(testObjectDecoded.getHopCounter().getValue(), 10);
        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertNull(calledAdd.getGlobalTitle());
        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 6);
        assertNull(callingAdd.getGlobalTitle());
        assertNull(testObjectDecoded.getSegmentation());
        assertNull(testObjectDecoded.getImportance());
        assertTrue(Arrays.equals(testObjectDecoded.getData(), getDataXudt1Src()));

        // ---- LUDT param importance (no segm) - long 300 bytes data
        b = this.getDataLudt3();
        buf = new ByteArrayInputStream(b);
        type = buf.read();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(testObjectDecoded);
        assertNotNull(testObjectDecoded);

        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 1);
        assertFalse(testObjectDecoded.getProtocolClass().getReturnMessageOnError());
        assertEquals(testObjectDecoded.getHopCounter().getValue(), 10);
        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertNull(calledAdd.getGlobalTitle());
        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 6);
        assertNull(callingAdd.getGlobalTitle());
        assertNull(testObjectDecoded.getSegmentation());
        assertEquals(testObjectDecoded.getImportance().getValue(), 7);
        assertTrue(Arrays.equals(testObjectDecoded.getData(), getDataLudt3Src()));

    }

    @Test(groups = { "SccpMessage", "functional.encode" })
    public void testEncode() throws Exception {

        // ---- UDT
        SccpAddress calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,0,  8);
        SccpAddress callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,1, 8);
        SccpDataMessageImpl msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd,
                getDataUdtSrc(), 0, 8, false, null, null);
        // SccpAddress calledParty, SccpAddress callingParty, byte[] data, int sls, int localSsn, boolean returnMessageOnError,
        // HopCounter hopCounter, Importance importance

        EncodingResultData res = msg.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertTrue(Arrays.equals(res.getSolidData(), getDataUdt()));

        // ---- UDT Management message
        calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0, 1);
        callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 1);
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass0(calledAdd, callingAdd, getDataUdtSrc1(), 1, false,
                null, null);

        res = msg.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertTrue(Arrays.equals(res.getSolidData(), getDataUdt1()));

        // ---- XUDT without segm
        calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0,  8);
        callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 6);
        HopCounter hc = new HopCounterImpl(15);
        Importance imp = new ImportanceImpl((byte) 7);
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getDataXudt1Src(), 5, 1,
                true, hc, imp);

        res = msg.encode(stack,LongMessageRuleType.XUDT_ENABLED, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertTrue(Arrays.equals(res.getSolidData(), getDataUdt2()));

        // ---- XUDT without segm & importance
        calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,0, 8);
        callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,1, 6);
        hc = new HopCounterImpl(15);
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getDataXudt1Src(), 5, 1,
                true, hc, null);

        res = msg.encode(stack,LongMessageRuleType.XUDT_ENABLED, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertTrue(Arrays.equals(res.getSolidData(), getDataUdt2()));

        // ---- LUDT all param
        calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0, 8);
        callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 6);
        hc = new HopCounterImpl(10);
        imp = new ImportanceImpl((byte) 7);
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getDataXudt1Src(), 5, 1,
                false, hc, imp);

        res = msg.encode(stack,LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION, 2000, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertTrue(Arrays.equals(res.getSolidData(), getDataLudt1()));

        // ---- LUDT no segm and importance par
        calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0, 8);
        callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 6);
        hc = new HopCounterImpl(10);
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getDataXudt1Src(), 5, 1,
                false, hc, null);

        res = msg.encode(stack,LongMessageRuleType.LUDT_ENABLED, 2000, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertTrue(Arrays.equals(res.getSolidData(), getDataLudt2()));

        // ---- LUDT param importance (no segm) - long 300 bytes data
        calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0, 8);
        callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 6);
        hc = new HopCounterImpl(10);
        imp = new ImportanceImpl((byte) 7);
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getDataLudt3Src(), 5, 1,
                false, hc, imp);

        res = msg.encode(stack,LongMessageRuleType.LUDT_ENABLED, 2000, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertTrue(Arrays.equals(res.getSolidData(), getDataLudt3()));

    }
}
