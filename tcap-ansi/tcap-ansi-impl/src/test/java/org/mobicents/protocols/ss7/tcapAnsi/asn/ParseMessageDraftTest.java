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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.IOException;

import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.tcapAnsi.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcapAnsi.api.MessageType;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.DraftParsedMessage;
import org.testng.annotations.Test;

@Test(groups = { "asn" })
public class ParseMessageDraftTest {

    private byte[] dataTcQuery = new byte[] {
            (byte) 0xe2, 0x33, (byte) 0xc7, 0x04, 0x00, 0x00, 0x01, 0x00, (byte) 0xe8, 0x2b, (byte) 0xe9, 0x29, (byte) 0xcf, 0x01,
            0x00, (byte) 0xd1, 0x02, 0x09, 0x35, (byte) 0xf2, 0x20, (byte) 0x9f, 0x69, 0x00, (byte) 0x9f, 0x74, 0x00, (byte) 0x9f, (byte) 0x81, 0x00, 0x01,
            0x08, (byte) 0x88, 0x05, 0x16, 0x19, 0x32, 0x04, 0x00, (byte) 0x9f, (byte) 0x81, 0x41, 0x01, 0x01, (byte) 0x9f, (byte) 0x81, 0x43, 0x05, 0x22,
            0x22, 0x22, 0x22, 0x22

    };

    private byte[] dataTcConversation = new byte[] { -27, 30, -57, 8, 1, 1, 2, 2, 3, 3, 4, 4, -24, 18, -23, 16, -49, 1, 0, -47,
            2, 9, 53, -14, 7, 1, 2, 3, 4, 5, 6, 7 };

    private byte[] dataTcResponse = new byte[] { (byte) 0xe4, 0x3c, (byte) 0xc7, 0x04, 0x14, 0x00, 0x00, 0x00, (byte) 0xe8,
            0x34, (byte) 0xea, 0x32, (byte) 0xcf, 0x01, 0x01, (byte) 0xf2, 0x2d, (byte) 0x96, 0x01, 0x13, (byte) 0x8e, 0x02,
            0x06, 0x00, (byte) 0x95, 0x03, 0x00, 0x0c, 0x10, (byte) 0x9f, 0x4e, 0x01, 0x01, (byte) 0x99, 0x03, 0x7a, 0x0d,
            0x11, (byte) 0x9f, 0x5d, 0x07, 0x00, 0x00, 0x21, 0x06, 0x36, 0x54, 0x10, (byte) 0x97, 0x01, 0x07, (byte) 0x9f,
            0x73, 0x01, 0x00, (byte) 0x9f, 0x75, 0x01, 0x00, (byte) 0x98, 0x01, 0x02 };

    private byte[] dataTcAbort = new byte[] { -10, 28, -57, 4, 20, 0, 0, 0, -8, 20, 40, 18, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 7, 1, 2, 3, 4, 5, 6, 7 };

    private byte[] dataTcUnidirectional = new byte[] { -31, 22, -57, 0, -24, 18, -23, 16, -49, 1, 0, -47, 2, 9, 53, -14, 7, 1, 2, 3, 4, 5, 6, 7 };

    @Test(groups = { "functional.decode" })
    public void testTCQuery() throws IOException, EncodeException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(dataTcQuery);

        assertEquals(msg.getMessageType(), MessageType.QueryWithPerm);
        assertEquals((long) msg.getOriginationDialogId(), 256);
        assertNull(msg.getDestinationDialogId());
        assertNull(msg.getParsingErrorReason());
    }

    @Test(groups = { "functional.decode" })
    public void testTCConversation() throws IOException, EncodeException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(dataTcConversation);

        assertEquals(msg.getMessageType(), MessageType.ConversationWithPerm);
        assertEquals((long) msg.getOriginationDialogId(), 16843266);
        assertEquals((long) msg.getDestinationDialogId(), 50529284);
        assertNull(msg.getParsingErrorReason());
    }

    @Test(groups = { "functional.decode" })
    public void testTCResponse() throws IOException, EncodeException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(dataTcResponse);

        assertEquals(msg.getMessageType(), MessageType.Response);
        assertNull(msg.getOriginationDialogId());
        assertEquals((long) msg.getDestinationDialogId(), 335544320);
        assertNull(msg.getParsingErrorReason());
    }

    @Test(groups = { "functional.decode" })
    public void testTCAbort() throws IOException, EncodeException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(dataTcAbort);

        assertEquals(msg.getMessageType(), MessageType.Abort);
        assertNull(msg.getOriginationDialogId());
        assertEquals((long) msg.getDestinationDialogId(), 335544320);
        assertNull(msg.getParsingErrorReason());
    }

    @Test(groups = { "functional.decode" })
    public void testTCUnidirectional() throws IOException, EncodeException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(dataTcUnidirectional);

        assertEquals(msg.getMessageType(), MessageType.Unidirectional);
        assertNull(msg.getOriginationDialogId());
        assertNull(msg.getDestinationDialogId());
        assertNull(msg.getParsingErrorReason());
    }

}
