/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.tcap.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.TCAPTestUtils;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.testng.annotations.Test;

@Test(groups = { "asn" })
public class TcEndTest {

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testTCEndMessage_No_Dialog() throws IOException, ParseException, EncodeException {

        // no idea how to check rest...?

        // created by hand
        byte[] b = new byte[] {
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

        AsnInputStream ais = new AsnInputStream(b);
        int tag = ais.readTag();
        assertEquals(TCEndMessage._TAG, tag, "Expected TCEnd");
        TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);

        assertNull(tcm.getDialogPortion(), "Dialog portion should not be present");
        // assertEquals(144965633L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");
        assertTrue(Arrays.equals(tcm.getDestinationTransactionId(), new byte[] { 8, (byte) 0xA4, 0, 1, }),
                "Destination transaction id does not match");

        // comp portion
        assertNotNull(tcm.getComponent(), "Component portion should be present");
        assertEquals(2, tcm.getComponent().length, "Component count is wrong");
        Component c = tcm.getComponent()[0];
        assertEquals(ComponentType.Invoke, c.getType(), "Wrong component type");
        Invoke i = (Invoke) c;
        assertEquals(new Long(1), i.getInvokeId(), "Wrong invoke ID");
        assertNull(i.getLinkedId(), "Linked ID is not null");

        c = tcm.getComponent()[1];
        assertEquals(ComponentType.ReturnResultLast, c.getType(), "Wrong component type");
        ReturnResultLast rrl = (ReturnResultLast) c;
        assertEquals(new Long(2), rrl.getInvokeId(), "Wrong invoke ID");
        assertNotNull(rrl.getOperationCode(), "Operation code should not be null");

        OperationCode ocs = rrl.getOperationCode();

        assertEquals(OperationCodeType.Local, ocs.getOperationType(), "Wrong Operation Code type");
        assertEquals(new Long(0x00FF), ocs.getLocalOperationCode(), "Wrong Operation Code");

        assertNotNull(rrl.getParameter(), "Parameter should not be null");

        AsnOutputStream aos = new AsnOutputStream();
        tcm.encode(aos);
        byte[] encoded = aos.toByteArray();

        TCAPTestUtils.compareArrays(b, encoded);

    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testTCEndMessage_No_Component() throws IOException, EncodeException, ParseException {

        // created by hand
        byte[] b = new byte[] {
                // TCEnd
                0x64, 50,
                // oidTx
                // OrigTran ID (full)............ 145031169
                0x49, 4, 8, (byte) 0xA5, 0, 1,
                // dialog portion
                0x6B, 42,
                // extrnal tag
                0x28, 40,
                // oid
                0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, // asn

                29, 0x61, // dialog response
                27,
                // protocol version
                (byte) 0x80, // protocol version

                2, 7, (byte) 0x80, (byte) 161,// acn
                9,
                // oid
                6, 7, 4, 0, 1, 1, 1, 3, 0,
                // result
                (byte) 0xA2, 0x03, 0x2, 0x1, (byte) 0x0,
                // result source diagnostic
                (byte) 0xA3, 5, (byte) 0x0A2, // provider
                3, 0x02,// int 2
                0x01, (byte) 0x2
        // no user info?
        };

        AsnInputStream ais = new AsnInputStream(b);
        int tag = ais.readTag();
        assertEquals(TCEndMessage._TAG, tag, "Expected TCEnd");
        TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);
        assertNull(tcm.getComponent(), "Component portion should not be present");
        assertNotNull(tcm.getDialogPortion(), "Dialog portion should not be null");
        // assertEquals(145031169L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");
        assertTrue(Arrays.equals(tcm.getDestinationTransactionId(), new byte[] { 8, (byte) 0xA5, 0, 1, }),
                "Destination transaction id does not match");

        assertFalse(tcm.getDialogPortion().isUnidirectional(), "Dialog should not be Uni");
        DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
        assertEquals(DialogAPDUType.Response, _dapd.getType(), "Wrong dialog APDU type!");

        DialogResponseAPDU dapd = (DialogResponseAPDU) _dapd;

        // check nulls first
        assertNull(dapd.getUserInformation(), "UserInformation should not be present");

        // not nulls
        assertNotNull(dapd.getResult(), "Result should not be null");
        Result r = dapd.getResult();
        assertEquals(ResultType.Accepted, r.getResultType(), "Wrong result");

        assertNotNull(dapd.getResultSourceDiagnostic(), "Result Source Diagnostic should not be null");

        ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
        assertNull(rsd.getDialogServiceUserType(), "User diagnostic should not be present");
        assertEquals(DialogServiceProviderType.NoCommonDialogPortion, rsd.getDialogServiceProviderType(),
                "Wrong provider diagnostic type");

        AsnOutputStream aos = new AsnOutputStream();
        tcm.encode(aos);
        byte[] encoded = aos.toByteArray();

        TCAPTestUtils.compareArrays(b, encoded);

    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testTCEndMessage_No_Nothing() throws IOException, EncodeException, ParseException {

        // no idea how to check rest...?

        // created by hand
        byte[] b = new byte[] {
                // TCEnd
                0x64, 6,
                // oidTx
                // OrigTran ID (full)............ 145031169
                0x49, 4, 8, (byte) 0xA5, 0, 1,

        };

        AsnInputStream ais = new AsnInputStream(b);
        int tag = ais.readTag();
        assertEquals(TCEndMessage._TAG, tag, "Expected TCEnd");
        TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);

        assertNull(tcm.getDialogPortion(), "Dialog portion should be null");
        assertNull(tcm.getComponent(), "Component portion should not be present");
        // assertEquals(145031169L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");
        assertTrue(Arrays.equals(tcm.getDestinationTransactionId(), new byte[] { 8, (byte) 0xA5, 0, 1, }),
                "Destination transaction id does not match");

        AsnOutputStream aos = new AsnOutputStream();
        tcm.encode(aos);
        byte[] encoded = aos.toByteArray();

        TCAPTestUtils.compareArrays(b, encoded);

    }

    @Test(groups = { "functional.encode", "functional.decode" })
    public void testTCEndMessage_All() throws IOException, EncodeException, ParseException {

        // no idea how to check rest...?

        // created by hand
        byte[] b = new byte[] {
                // TCEnd
                0x64, 68 + 41,
                // dtx
                // DestTran ID (full)............ 144965633
                0x49, 4, 8, (byte) 0xA4, 0, 1,
                // dialog portion
                0x6B, 42,
                // extrnal tag
                0x28, 40,
                // oid
                0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, // asn

                29, 0x61, // dialog response
                27,
                // protocol version
                (byte) 0x80, // protocol version

                2, 7, (byte) 0x80, (byte) 161,// acn
                9,
                // oid
                6, 7, 4, 0, 1, 1, 1, 3, 0,
                // result
                (byte) 0xA2, 0x03, 0x2, 0x1, (byte) 0x01,
                // result source diagnostic
                (byte) 0xA3, 5, (byte) 0x0A2, // provider
                3, 0x02,// int 2
                0x01, (byte) 0x00,
                // no user info?
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
                0x02, 0x02, 0x01, (byte) 0xFF,
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

        AsnInputStream ais = new AsnInputStream(b);
        int tag = ais.readTag();
        assertEquals(TCEndMessage._TAG, tag, "Expected TCEnd");
        TCEndMessage tcm = TcapFactory.createTCEndMessage(ais);

        // universal
        // assertEquals(144965633L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");
        assertTrue(Arrays.equals(tcm.getDestinationTransactionId(), new byte[] { 8, (byte) 0xA4, 0, 1, }),
                "Destination transaction id does not match");

        // dialog portion
        assertNotNull(tcm.getDialogPortion(), "Dialog portion should not be null");
        // assertEquals(144965633L, tcm.getDestinationTransactionId(),"Destination transaction id does not match");

        assertFalse(tcm.getDialogPortion().isUnidirectional(), "Dialog should not be Uni");
        DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
        assertEquals(DialogAPDUType.Response, _dapd.getType(), "Wrong dialog APDU type!");

        DialogResponseAPDU dapd = (DialogResponseAPDU) _dapd;

        // check nulls first
        assertNull(dapd.getUserInformation(), "UserInformation should not be present");

        // not nulls
        assertNotNull(dapd.getResult(), "Result should not be null");
        Result r = dapd.getResult();
        assertEquals(ResultType.RejectedPermanent, r.getResultType(), "Wrong result");

        assertNotNull(dapd.getResultSourceDiagnostic(), "Result Source Diagnostic should not be null");

        ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
        assertNull(rsd.getDialogServiceUserType(), "User diagnostic should not be present");
        assertEquals(DialogServiceProviderType.Null, rsd.getDialogServiceProviderType(), "Wrong provider diagnostic type");

        // comp portion
        assertNotNull(tcm.getComponent(), "Component portion should be present");
        assertEquals(2, tcm.getComponent().length, "Component count is wrong");
        Component c = tcm.getComponent()[0];
        assertEquals(ComponentType.Invoke, c.getType(), "Wrong component type");
        Invoke i = (Invoke) c;
        assertEquals(new Long(1), i.getInvokeId(), "Wrong invoke ID");
        assertNull(i.getLinkedId(), "Linked ID is not null");

        c = tcm.getComponent()[1];
        assertEquals(ComponentType.ReturnResultLast, c.getType(), "Wrong component type");
        ReturnResultLast rrl = (ReturnResultLast) c;
        assertEquals(new Long(2), rrl.getInvokeId(), "Wrong invoke ID");
        assertNotNull(rrl.getOperationCode(), "Operation code should not be null");

        OperationCode ocs = rrl.getOperationCode();

        assertEquals(OperationCodeType.Local, ocs.getOperationType(), "Wrong Operation Code type");
        assertEquals(new Long(511), ocs.getLocalOperationCode(), "Wrong Operation Code");

        assertNotNull(rrl.getParameter(), "Parameter should not be null");

        AsnOutputStream aos = new AsnOutputStream();
        tcm.encode(aos);
        byte[] encoded = aos.toByteArray();

        TCAPTestUtils.compareArrays(b, encoded);

    }
}
