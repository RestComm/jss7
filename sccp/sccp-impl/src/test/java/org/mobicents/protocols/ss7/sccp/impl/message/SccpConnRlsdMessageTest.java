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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCauseValue;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class SccpConnRlsdMessageTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("SccpConnRlsdMessageTestStack");
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

    public byte[] getDataRlsdNoOptParams() {
        return new byte[] { 0x04, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03, 0x07, 0x00 };
    }

    public byte[] getDataRlsdOneOptParam() {
        return new byte[] { 0x04, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03, 0x07, 0x01, 0x12, 0x01, 0x02, 0x00 };
    }

    public byte[] getDataRlsdAllParams() {
        return new byte[] {
                0x04, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03, 0x07, 0x01, 0x0F, 0x05, 0x01, 0x02, 0x03, 0x04, 0x05, 0x12,
                0x01, 0x02, 0x00
        };
    }

    @Test(groups = { "SccpMessage", "functional.decode" })
    public void testDecode() throws Exception {
        // ---- no optional params
        ByteArrayInputStream buf = new ByteArrayInputStream(this.getDataRlsdNoOptParams());
        int type = buf.read();
        SccpConnRlsdMessageImpl testObjectDecoded = (SccpConnRlsdMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 3);
        assertEquals(testObjectDecoded.getReleaseCause().getValue(), ReleaseCauseValue.ACCESS_CONGESTION);

        // ---- one optional param
        buf = new ByteArrayInputStream(this.getDataRlsdOneOptParam());
        type = buf.read();
        testObjectDecoded = (SccpConnRlsdMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 3);
        assertEquals(testObjectDecoded.getReleaseCause().getValue(), ReleaseCauseValue.ACCESS_CONGESTION);
        assertEquals(testObjectDecoded.getImportance().getValue(), 2);

        // ---- all param
        buf = new ByteArrayInputStream(this.getDataRlsdAllParams());
        type = buf.read();
        testObjectDecoded = (SccpConnRlsdMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 3);
        assertEquals(testObjectDecoded.getReleaseCause().getValue(), ReleaseCauseValue.ACCESS_CONGESTION);
        assertEquals(testObjectDecoded.getUserData(), new byte[] {1, 2, 3, 4, 5});
        assertEquals(testObjectDecoded.getImportance().getValue(), 2);
    }

    @Test(groups = { "SccpMessage", "functional.encode" })
    public void testEncode() throws Exception {
        // ---- no optional params
        SccpConnRlsdMessageImpl original = new SccpConnRlsdMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(3));
        original.setReleaseCause(new ReleaseCauseImpl(ReleaseCauseValue.ACCESS_CONGESTION));

        EncodingResultData encoded = original.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        assertEquals(encoded.getSolidData(), this.getDataRlsdNoOptParams());

        // ---- one optional param
        original = new SccpConnRlsdMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(3));
        original.setReleaseCause(new ReleaseCauseImpl(ReleaseCauseValue.ACCESS_CONGESTION));
        original.setImportance(new ImportanceImpl((byte)10));

        encoded = original.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        assertEquals(encoded.getSolidData(), this.getDataRlsdOneOptParam());

        // ---- all param
        original = new SccpConnRlsdMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(3));
        original.setReleaseCause(new ReleaseCauseImpl(ReleaseCauseValue.ACCESS_CONGESTION));
        original.setUserData(new byte[] {1, 2, 3, 4, 5});
        original.setImportance(new ImportanceImpl((byte)10));

        encoded = original.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        assertEquals(encoded.getSolidData(), this.getDataRlsdAllParams());
    }
}

