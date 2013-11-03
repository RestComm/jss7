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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
@Test(groups = { "asn" })
public class RejectTest {

    private byte[] getData() {
        return new byte[] { (byte) 164, 6, 2, 1, 1, (byte) 129, 1, 2 };
    }

    private byte[] getDataNullInvokeId() {
        return new byte[] { -92, 5, 5, 0, -128, 1, 0 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws IOException, ParseException {

        byte[] b = getData();
        AsnInputStream asnIs = new AsnInputStream(b);
        Component comp = TcapFactory.createComponent(asnIs);

        assertEquals(ComponentType.Reject, comp.getType(), "Wrong component Type");
        Reject rej = (Reject) comp;
        assertEquals(new Long(1), rej.getInvokeId(), "Wrong invoke ID");
        Problem prb = rej.getProblem();
        assertEquals(ProblemType.Invoke, prb.getType());
        assertEquals(InvokeProblemType.MistypedParameter, prb.getInvokeProblemType());

        b = getDataNullInvokeId();
        asnIs = new AsnInputStream(b);
        comp = TcapFactory.createComponent(asnIs);

        assertEquals(ComponentType.Reject, comp.getType(), "Wrong component Type");
        rej = (Reject) comp;
        assertNull(rej.getInvokeId());
        prb = rej.getProblem();
        assertEquals(ProblemType.General, prb.getType());
        assertEquals(GeneralProblemType.UnrecognizedComponent, prb.getGeneralProblemType());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws IOException, EncodeException {

        byte[] expected = this.getData();
        Reject rej = TcapFactory.createComponentReject();
        rej.setInvokeId(1L);
        Problem prb = TcapFactory.createProblem(ProblemType.Invoke);
        prb.setInvokeProblemType(InvokeProblemType.MistypedParameter);
        rej.setProblem(prb);

        AsnOutputStream asnos = new AsnOutputStream();
        rej.encode(asnos);
        byte[] encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));

        expected = this.getDataNullInvokeId();
        rej = TcapFactory.createComponentReject();
        prb = TcapFactory.createProblem(ProblemType.General);
        prb.setGeneralProblemType(GeneralProblemType.UnrecognizedComponent);
        rej.setProblem(prb);

        asnos = new AsnOutputStream();
        rej.encode(asnos);
        encodedData = asnos.toByteArray();
        assertTrue(Arrays.equals(expected, encodedData));
    }
}
