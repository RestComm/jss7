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

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class RejectTest {

    private byte[] data1 = new byte[] { -20, 9, -49, 1, 5, -43, 2, 2, 3, -16, 0 };

    private byte[] data2 = new byte[] { -20, 8, -49, 0, -43, 2, 2, 3, -16, 0 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        // 1
        AsnInputStream ais = new AsnInputStream(this.data1);
        int tag = ais.readTag();
        assertEquals(tag, Reject._TAG_REJECT);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        Reject rej = TcapFactory.createComponentReject();
        rej.decode(ais);

        assertEquals((long) rej.getCorrelationId(), 5);
        assertEquals(rej.getProblem(), RejectProblem.invokeIncorrectParameter);
        assertFalse(rej.isLocalOriginated());

        // 2
        ais = new AsnInputStream(this.data2);
        tag = ais.readTag();
        assertEquals(tag, Reject._TAG_REJECT);
        assertEquals(ais.getTagClass(), Tag.CLASS_PRIVATE);

        rej = TcapFactory.createComponentReject();
        rej.decode(ais);

        assertNull(rej.getCorrelationId());
        assertEquals(rej.getProblem(), RejectProblem.invokeIncorrectParameter);
        assertFalse(rej.isLocalOriginated());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        // 1
        Reject rej = TcapFactory.createComponentReject();
        rej.setCorrelationId(5L);
        rej.setProblem(RejectProblem.invokeIncorrectParameter);

        AsnOutputStream aos = new AsnOutputStream();
        rej.encode(aos);
        byte[] encodedData = aos.toByteArray();
        byte[] expectedData = data1;
        assertEquals(encodedData, expectedData);

        // 2
        rej = TcapFactory.createComponentReject();
        rej.setProblem(RejectProblem.invokeIncorrectParameter);

        aos = new AsnOutputStream();
        rej.encode(aos);
        encodedData = aos.toByteArray();
        expectedData = data2;
        assertEquals(encodedData, expectedData);
    }
}
