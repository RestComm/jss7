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

package org.mobicents.protocols.ss7.tcap.asn;

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

import junit.framework.TestCase;


/**
 * 
 * @author sergey vetyutnev
 *
 */
public class RejectTest extends TestCase {
	
	private byte[] getData() {
		return new byte[] { (byte) 164, 6, 2, 1, 1, (byte) 129, 1, 2 };
	}
	
	private byte[] getDataNullInvokeId() {
		return new byte[] { -92, 5, 5, 0, -128, 1, 0 };
	}
	
	@org.junit.Test
	public void testDecode() throws IOException, ParseException {

		byte[] b = getData();
		AsnInputStream asnIs = new AsnInputStream(b);
		Component comp = TcapFactory.createComponent(asnIs);

		assertEquals("Wrong component Type", ComponentType.Reject, comp.getType());
		Reject rej = (Reject) comp;
		assertEquals("Wrong invoke ID", new Long(1), rej.getInvokeId());
		Problem prb = rej.getProblem();
		assertEquals(ProblemType.Invoke, prb.getType());
		assertEquals(InvokeProblemType.MistypedParameter, prb.getInvokeProblemType());

		
		b = getDataNullInvokeId();
		asnIs = new AsnInputStream(b);
		comp = TcapFactory.createComponent(asnIs);

		assertEquals("Wrong component Type", ComponentType.Reject, comp.getType());
		rej = (Reject) comp;
		assertNull(rej.getInvokeId());
		prb = rej.getProblem();
		assertEquals(ProblemType.General, prb.getType());
		assertEquals(GeneralProblemType.UnrecognizedComponent, prb.getGeneralProblemType());
	}
	
	
	@org.junit.Test
	public void testEncode() throws IOException, ParseException {

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
