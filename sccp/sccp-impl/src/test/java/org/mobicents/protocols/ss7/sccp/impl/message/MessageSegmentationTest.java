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
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReturnCauseImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MessageSegmentationTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("MessageSegmentationTestStack");
    private MessageFactoryImpl messageFactory;
    private static byte[] dataA;

    static {
        dataA = new byte[600];
        int i1 = 0;
        for (int i = 0; i < dataA.length; i++) {
            dataA[i] = (byte) i1;
            if (++i1 >= 256)
                i1 = 0;
        }
    }

    @BeforeMethod
    public void setUp() throws Exception {
        this.stack.setPersistDir(Util.getTmpTestDir());
        this.stack.start();
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = Logger.getLogger(SccpStackImpl.class.getCanonicalName());
        stack.setMaxDataMessage(2000);
    }

    @AfterMethod
    public void tearDown() {
        this.stack.stop();
    }

    public static byte[] getDataA() {
        return dataA;
    }

    public static byte[] getDataSegm1() {
        return new byte[] { 17, (byte) 129, 15, 04, 06, 10, (byte) 250, 02, 66, 8, 04, 67, 2, 0, 8, (byte) 240, 0, 1, 2, 3, 4,
                5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33,
                34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61,
                62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89,
                90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113,
                114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, (byte) 128, (byte) 129, (byte) 130,
                (byte) 131, (byte) 132, (byte) 133, (byte) 134, (byte) 135, (byte) 136, (byte) 137, (byte) 138, (byte) 139,
                (byte) 140, (byte) 141, (byte) 142, (byte) 143, (byte) 144, (byte) 145, (byte) 146, (byte) 147, (byte) 148,
                (byte) 149, (byte) 150, (byte) 151, (byte) 152, (byte) 153, (byte) 154, (byte) 155, (byte) 156, (byte) 157,
                (byte) 158, (byte) 159, (byte) 160, (byte) 161, (byte) 162, (byte) 163, (byte) 164, (byte) 165, (byte) 166,
                (byte) 167, (byte) 168, (byte) 169, (byte) 170, (byte) 171, (byte) 172, (byte) 173, (byte) 174, (byte) 175,
                (byte) 176, (byte) 177, (byte) 178, (byte) 179, (byte) 180, (byte) 181, (byte) 182, (byte) 183, (byte) 184,
                (byte) 185, (byte) 186, (byte) 187, (byte) 188, (byte) 189, (byte) 190, (byte) 191, (byte) 192, (byte) 193,
                (byte) 194, (byte) 195, (byte) 196, (byte) 197, (byte) 198, (byte) 199, (byte) 200, (byte) 201, (byte) 202,
                (byte) 203, (byte) 204, (byte) 205, (byte) 206, (byte) 207, (byte) 208, (byte) 209, (byte) 210, (byte) 211,
                (byte) 212, (byte) 213, (byte) 214, (byte) 215, (byte) 216, (byte) 217, (byte) 218, (byte) 219, (byte) 220,
                (byte) 221, (byte) 222, (byte) 223, (byte) 224, (byte) 225, (byte) 226, (byte) 227, (byte) 228, (byte) 229,
                (byte) 230, (byte) 231, (byte) 232, (byte) 233, (byte) 234, (byte) 235, (byte) 236, (byte) 237, (byte) 238,
                (byte) 239, 16, 4, (byte) 194, 1, 0, 0, 18, 1, 7, 0 };
    }

    public static byte[] getDataSegm2() {
        return new byte[] { 17, (byte) 129, 15, 04, 06, 10, (byte) 250, 02, 66, 8, 04, 67, 2, 0, 8, (byte) 240, (byte) 240,
                (byte) 241, (byte) 242, (byte) 243, (byte) 244, (byte) 245, (byte) 246, (byte) 247, (byte) 248, (byte) 249,
                (byte) 250, (byte) 251, (byte) 252, (byte) 253, (byte) 254, (byte) 255, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
                40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67,
                68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95,
                96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118,
                119, 120, 121, 122, 123, 124, 125, 126, 127, (byte) 128, (byte) 129, (byte) 130, (byte) 131, (byte) 132,
                (byte) 133, (byte) 134, (byte) 135, (byte) 136, (byte) 137, (byte) 138, (byte) 139, (byte) 140, (byte) 141,
                (byte) 142, (byte) 143, (byte) 144, (byte) 145, (byte) 146, (byte) 147, (byte) 148, (byte) 149, (byte) 150,
                (byte) 151, (byte) 152, (byte) 153, (byte) 154, (byte) 155, (byte) 156, (byte) 157, (byte) 158, (byte) 159,
                (byte) 160, (byte) 161, (byte) 162, (byte) 163, (byte) 164, (byte) 165, (byte) 166, (byte) 167, (byte) 168,
                (byte) 169, (byte) 170, (byte) 171, (byte) 172, (byte) 173, (byte) 174, (byte) 175, (byte) 176, (byte) 177,
                (byte) 178, (byte) 179, (byte) 180, (byte) 181, (byte) 182, (byte) 183, (byte) 184, (byte) 185, (byte) 186,
                (byte) 187, (byte) 188, (byte) 189, (byte) 190, (byte) 191, (byte) 192, (byte) 193, (byte) 194, (byte) 195,
                (byte) 196, (byte) 197, (byte) 198, (byte) 199, (byte) 200, (byte) 201, (byte) 202, (byte) 203, (byte) 204,
                (byte) 205, (byte) 206, (byte) 207, (byte) 208, (byte) 209, (byte) 210, (byte) 211, (byte) 212, (byte) 213,
                (byte) 214, (byte) 215, (byte) 216, (byte) 217, (byte) 218, (byte) 219, (byte) 220, (byte) 221, (byte) 222,
                (byte) 223, 16, 4, 65, 1, 0, 0, 18, 1, 7, 0 };
    }

    public static byte[] getDataSegm3() {
        return new byte[] { 17, (byte) 129, 15, 04, 06, 10, (byte) 130, 02, 66, 8, 04, 67, 2, 0, 8, 120, (byte) 224,
                (byte) 225, (byte) 226, (byte) 227, (byte) 228, (byte) 229, (byte) 230, (byte) 231, (byte) 232, (byte) 233,
                (byte) 234, (byte) 235, (byte) 236, (byte) 237, (byte) 238, (byte) 239, (byte) 240, (byte) 241, (byte) 242,
                (byte) 243, (byte) 244, (byte) 245, (byte) 246, (byte) 247, (byte) 248, (byte) 249, (byte) 250, (byte) 251,
                (byte) 252, (byte) 253, (byte) 254, (byte) 255, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45,
                46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73,
                74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 16, 4, 64, 1, 0, 0, 18, 1, 7, 0 };
    }

    public static byte[] getDataSegm1_S() {
        byte[] buf1 = new byte[getDataSegm1().length];
        System.arraycopy(getDataSegm1(), 0, buf1, 0, buf1.length);
        buf1[0] = 18;
        buf1[1] = (byte) ReturnCauseValue.SEG_FAILURE.getValue();
        buf1[buf1.length - 8] = (byte) (((int) buf1[buf1.length - 8]) - 64);
        buf1[buf1.length - 7] = (byte) (buf1[buf1.length - 7] + 1);
        return buf1;
    }

    public static byte[] getDataSegm2_S() {
        byte[] buf1 = new byte[getDataSegm2().length];
        System.arraycopy(getDataSegm2(), 0, buf1, 0, buf1.length);
        buf1[0] = 18;
        buf1[1] = (byte) ReturnCauseValue.SEG_FAILURE.getValue();
        buf1[buf1.length - 8] = (byte) (((int) buf1[buf1.length - 8]) - 64);
        buf1[buf1.length - 7] = (byte) (buf1[buf1.length - 7] + 1);
        return buf1;
    }

    public static byte[] getDataSegm3_S() {
        byte[] buf1 = new byte[getDataSegm3().length];
        System.arraycopy(getDataSegm3(), 0, buf1, 0, buf1.length);
        buf1[0] = 18;
        buf1[1] = (byte) ReturnCauseValue.SEG_FAILURE.getValue();
        buf1[buf1.length - 8] = (byte) (((int) buf1[buf1.length - 8]) - 64);
        buf1[buf1.length - 7] = (byte) (buf1[buf1.length - 7] + 1);
        return buf1;
    }

    @Test(groups = { "SccpMessage", "functional.encode" })
    public void testEncode() throws Exception {
        //stack.getSccpProvider().getParameterFactory().createGlobalTitle
        // -- message length exceeds the max possible length
        byte[] buf = new byte[3000];
        SccpAddress calledAdd = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0, 8);
        SccpAddress callingAdd = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        SccpDataMessageImpl msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, buf, 0,
                8, false, null, null);

        EncodingResultData res = msg.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.DataMaxLengthExceeded);

        // -- UDT message: message length exceeds the max possible length for UDT
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getDataA(), 0, 8, false,
                null, null);

        res = msg.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 2000, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.ReturnFailure);
        assertEquals(res.getReturnCause(), ReturnCauseValue.SEG_NOT_SUPPORTED);

        // -- UDT message: message length exceeds the max possible length for UDT
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getDataA(), 0, 8, false,
                null, null);

        res = msg.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 2000, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.ReturnFailure);
        assertEquals(res.getReturnCause(), ReturnCauseValue.SEG_NOT_SUPPORTED);

        // -- XUDT message: a splitted to the 3 segments message
        Importance imp = new ImportanceImpl((byte) 7);
        SccpAddress callingAdd2 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,  2, 8);

        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd2, getDataA(), 0, 8, true,
                null, imp);

        res = msg.encode(stack,LongMessageRuleType.XUDT_ENABLED, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertEquals(res.getSegementedData().size(), 3);
        assertTrue(Arrays.equals(res.getSegementedData().get(0), getDataSegm1()));
        assertTrue(Arrays.equals(res.getSegementedData().get(1), getDataSegm2()));
        assertTrue(Arrays.equals(res.getSegementedData().get(2), getDataSegm3()));

        // -- XUDTS message: a splitted to the 3 segments message
        ReturnCause rc = new ReturnCauseImpl(ReturnCauseValue.SEG_FAILURE);
        SccpNoticeMessageImpl msgNot = (SccpNoticeMessageImpl) messageFactory.createNoticeMessage(
                SccpMessage.MESSAGE_TYPE_XUDT, rc, calledAdd, callingAdd2, getDataA(), null, imp);

        res = msgNot.encode(stack,LongMessageRuleType.XUDT_ENABLED, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertEquals(res.getSegementedData().size(), 3);
        assertTrue(Arrays.equals(res.getSegementedData().get(0), getDataSegm1_S()));
        assertTrue(Arrays.equals(res.getSegementedData().get(1), getDataSegm2_S()));
        assertTrue(Arrays.equals(res.getSegementedData().get(2), getDataSegm3_S()));

        // -- XUDT message: for splitting to the 3 segments message. This message is received from MTP as LUDT without
        // Segmentation field -> Error
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getDataA(), 0, 8, false,
                null, imp);
        res = msg.encode(stack,LongMessageRuleType.LUDT_ENABLED, 2000, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        ByteArrayInputStream in = new ByteArrayInputStream(res.getSolidData());
        int type = in.read();
        msg = (SccpDataMessageImpl) messageFactory.createMessage(type, 1, 2, 0, in, SccpProtocolVersion.ITU, 0);
        res = msg.encode(stack,LongMessageRuleType.XUDT_ENABLED, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.ReturnFailure);
        assertEquals(res.getReturnCause(), ReturnCauseValue.SEG_FAILURE);

        // -- XUDT message: for splitting to the 3 segments message. This message is received from MTP as LUDT with Segmentation
        // field -> OK
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getDataA(), 0, 8, false,
                null, imp);
        res = msg.encode(stack,LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION, 2000, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        in = new ByteArrayInputStream(res.getSolidData());
        type = in.read();
        msg = (SccpDataMessageImpl) messageFactory.createMessage(type, 1, 2, 0, in, SccpProtocolVersion.ITU, 0);
        res = msg.encode(stack,LongMessageRuleType.XUDT_ENABLED, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertEquals(res.getSegementedData().size(), 3);

        // -- XUDT message: big callingPartyAddress and calledPartyAddress fields when there length + data length > 254 and
        // Importance field present
        byte[] bufx = new byte[225];
        GlobalTitle gttt = stack.getSccpProvider().getParameterFactory().createGlobalTitle("1111114444444444",3,NumberingPlan.ISDN_TELEPHONY,  BCDEvenEncodingScheme.INSTANCE,NatureOfAddress.INTERNATIONAL); 
                
        SccpAddress bigAdr = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gttt,0,  8);
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(bigAdr, bigAdr, bufx, 0, 8, true, null, imp);

        res = msg.encode(stack,LongMessageRuleType.XUDT_ENABLED, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertNotNull(res.getSolidData());

        bufx = new byte[226];
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(bigAdr, bigAdr, bufx, 0, 8, true, null, imp);

        res = msg.encode(stack,LongMessageRuleType.XUDT_ENABLED, 191, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        assertEquals(res.getSegementedData().size(), 2);

        // StringBuilder sb = new StringBuilder();
        // for (byte bt : res.getSegementedData().get(2)) {
        // sb.append(", ");
        // int i1 = (bt & 0xFF);
        // if (i1 > 127)
        // sb.append("(byte)");
        // sb.append(i1);
        // }
        // String s = sb.toString();

    }

    private byte[] getEncodedSegmentation() {
        return new byte[] { -127, -96, -122, 1 };
    }

    @Test(groups = { "SccpMessage", "functional.encode" })
    public void testLocalRef() throws Exception {
        SegmentationImpl segm = new SegmentationImpl(true, false, (byte) 1, 100000);
        byte[] buf = segm.encode(false, SccpProtocolVersion.ITU);
        assertTrue(Arrays.equals(buf, getEncodedSegmentation()));

        segm = new SegmentationImpl();
        segm.decode(buf,null, SccpProtocolVersion.ITU);
        assertEquals(segm.getRemainingSegments(), 1);
        assertEquals(segm.getSegmentationLocalRef(), 100000);
        assertTrue(segm.isFirstSegIndication());
        assertFalse(segm.isClass1Selected());
    }
}
