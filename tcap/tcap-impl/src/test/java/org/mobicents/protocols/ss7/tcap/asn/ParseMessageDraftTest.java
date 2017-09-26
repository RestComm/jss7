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

package org.mobicents.protocols.ss7.tcap.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.IOException;

import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.MessageType;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.DraftParsedMessage;
import org.testng.annotations.Test;

@Test(groups = { "asn" })
public class ParseMessageDraftTest {

    private byte[] dataTcBegin = new byte[] {
            // TCBegin
            0x62, 38,
            // oidTx
            // OrigTran ID (full)............ 145031169
            0x48, 4, 8, (byte) 0xA5, 0, 1,
            // dialog portion
            0x6B, 30,
            // extrnal tag
            0x28, 28,
            // oid
            0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) // asn
            160, 17,
            // DialogPDU - Request
            0x60, 15, (byte) // protocol version
            0x80, 2, 7, (byte) 0x80, (byte) // acn
            161, 9,
            // oid
            6, 7, 4, 0, 1, 1, 1, 3, 0

    };

    private byte[] dataTcContinue = new byte[] { 0x65, 0x16,
            // org txid
            0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,
            // dtx
            0x49, 0x04, 8, (byte) 0xA4, 0, 1,
            // comp portion
            0x6C, 8,
            // invoke
            (byte) 0xA1, 6,
            // invoke ID
            0x02, 0x01, 0x01,
            // op code
            0x02, 0x01, 0x37 };

    private byte[] dataTcEnd = new byte[] {
            // TCEnd
            0x64, 65,
            // dialog portion
            // empty
            // dtx
            // DestTran ID (full)............ 144965633
            0x49, 4, 8, (byte) 0xA4, 0, 1,
            // comp portion
            0x6C, 57,
            // invoke
            (byte) 0xA1, 6,
            // invoke ID
            0x02, 0x01, 0x01,
            // op code
            0x02, 0x01, 0x37,
            // return result last
            (byte) 0xA2, 47,
            // inoke id
            0x02, 0x01, 0x02,
            // sequence start
            0x30, 42,
            // local operation
            0x02, 0x02, 0x00, (byte) 0xFF,
            // parameter
            0x30, 36, (byte) 0xA0,// some tag.1
            17, (byte) 0x80,// some tag.1.1
            2, 0x11, 0x11, (byte) 0xA1,// some tag.1.2
            04, (byte) 0x82, // some tag.1.3 ?
            2, 0x00, 0x00, (byte) 0x82,
            // some tag.1.4
            1, 12, (byte) 0x83, // some tag.1.5
            2, 0x33, 0x33, (byte) 0xA1,// some trash here
            // tension indicator 2........ ???
            // use value.................. ???
            // some tag.2
            3, (byte) 0x80,// some tag.2.1
            1, -1, (byte) 0xA2, // some tag.3
            3, (byte) 0x80,// some tag.3.1
            1, -1, (byte) 0xA3,// some tag.4
            5, (byte) 0x82,// some tag.4.1
            3, (byte) 0xAB,// - 85 serviceKey................... 123456 // dont care about this content, lets just make len
                           // correct
            (byte) 0xCD, (byte) 0xEF };

    private byte[] dataTcAbort = new byte[] { 103, 9, 73, 4, 123, -91, 52, 19, 74, 1, 126 };

    private byte[] dataTcUnidirectional = new byte[] { 97, 45, 107, 27, 40, 25, 6, 7, 0, 17, -122, 5, 1, 2, 1, -96, 14, 96, 12,
            -128, 2, 7, -128, -95, 6, 6, 4, 40, 2, 3, 4, 108, 14, -95, 12, 2, 1, -128, 2, 2, 2, 79, 4, 3, 1, 2, 3 };

    @Test(groups = { "functional.decode" })
    public void testTCBegin() throws IOException, EncodeException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(dataTcBegin);

        assertEquals(msg.getMessageType(), MessageType.Begin);
        assertEquals((long) msg.getOriginationDialogId(), 145031169);
        assertNull(msg.getDestinationDialogId());
        assertNull(msg.getParsingErrorReason());
    }

    @Test(groups = { "functional.decode" })
    public void testTCContinue() throws IOException, EncodeException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(dataTcContinue);

        assertEquals(msg.getMessageType(), MessageType.Continue);
        assertEquals((long) msg.getOriginationDialogId(), 145031169);
        assertEquals((long) msg.getDestinationDialogId(), 144965633);
        assertNull(msg.getParsingErrorReason());
    }

    @Test(groups = { "functional.decode" })
    public void testTCEnd() throws IOException, EncodeException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(dataTcEnd);

        assertEquals(msg.getMessageType(), MessageType.End);
        assertNull(msg.getOriginationDialogId());
        assertEquals((long) msg.getDestinationDialogId(), 144965633);
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
        assertEquals((long) msg.getDestinationDialogId(), 2074424339);
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
