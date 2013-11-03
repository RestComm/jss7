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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class TcAbortTest {

    private byte[] data1 = new byte[] { -10, 9, -57, 4, 20, 0, 0, 0, -41, 1, 6 };

    private byte[] data2 = new byte[] { -10, 13, -57, 4, 20, 0, 0, 0, -7, 3, -37, 1, 111, -8, 0 };

    private byte[] data3 = new byte[] { -10, 28, -57, 4, 20, 0, 0, 0, -8, 20, 40, 18, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 7, 1, 2, 3, 4, 5, 6, 7 };

    private byte[] trId = new byte[] { 20, 0, 0, 0 };

    private byte[] dataValue = new byte[] { 1, 2, 3, 4, 5, 6, 7 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        // 1
        AsnInputStream ais = new AsnInputStream(this.data1);
        int tag = ais.readTag();
        assertEquals(tag, TCAbortMessage._TAG_ABORT);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        TCAbortMessage tcm = TcapFactory.createTCAbortMessage(ais);

        assertEquals(tcm.getDestinationTransactionId(), trId);
        assertNull(tcm.getDialogPortion());
        assertNull(tcm.getUserAbortInformation());
        assertEquals(tcm.getPAbortCause(), PAbortCause.ResourceUnavailable);

        // 2
        ais = new AsnInputStream(this.data2);
        tag = ais.readTag();
        assertEquals(tag, TCAbortMessage._TAG_ABORT);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        tcm = TcapFactory.createTCAbortMessage(ais);

        assertEquals(tcm.getDestinationTransactionId(), trId);
        assertNull(tcm.getUserAbortInformation());
        assertNull(tcm.getPAbortCause());
        DialogPortion dp = tcm.getDialogPortion();
        assertEquals(dp.getApplicationContext().getInteger(), 111);

        // 3
        ais = new AsnInputStream(this.data3);
        tag = ais.readTag();
        assertEquals(tag, TCAbortMessage._TAG_ABORT);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        tcm = TcapFactory.createTCAbortMessage(ais);

        assertEquals(tcm.getDestinationTransactionId(), trId);
        assertNull(tcm.getPAbortCause());
        assertNull(tcm.getDialogPortion());

        UserInformationElement uie = tcm.getUserAbortInformation();
        assertTrue(uie.isOid());
        assertTrue(Arrays.equals(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 }, uie.getOidValue()));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        // 1
        TCAbortMessage tcm = TcapFactory.createTCAbortMessage();
        tcm.setDestinationTransactionId(trId);
        tcm.setPAbortCause(PAbortCause.ResourceUnavailable);

        AsnOutputStream aos = new AsnOutputStream();
        tcm.encode(aos);
        byte[] encodedData = aos.toByteArray();
        byte[] expectedData = data1;
        assertEquals(encodedData, expectedData);

        // 2
        tcm = TcapFactory.createTCAbortMessage();
        tcm.setDestinationTransactionId(trId);
        DialogPortion dp = TcapFactory.createDialogPortion();
        ApplicationContext ac = TcapFactory.createApplicationContext(111);
        dp.setApplicationContext(ac);
        tcm.setDialogPortion(dp);

        aos = new AsnOutputStream();
        tcm.encode(aos);
        encodedData = aos.toByteArray();
        expectedData = data2;
        assertEquals(encodedData, expectedData);

        // 3
        tcm = TcapFactory.createTCAbortMessage();
        tcm.setDestinationTransactionId(trId);
        UserInformationElement uai = new UserInformationElementImpl();
        uai.setOid(true);
        uai.setOidValue(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 });

        uai.setAsn(true);
        uai.setEncodeType(dataValue);
        tcm.setUserAbortInformation(uai);

        aos = new AsnOutputStream();
        tcm.encode(aos);
        encodedData = aos.toByteArray();
        expectedData = data3;
        assertEquals(encodedData, expectedData);

    }

}
