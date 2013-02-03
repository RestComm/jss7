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
package org.mobicents.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.testng.annotations.Test;

/**
 * @author Amit Bhayani
 * 
 */
public class TNoAnswerSpecificInfoTest {

	/**
	 * 
	 */
	public TNoAnswerSpecificInfoTest() {
		// TODO Auto-generated constructor stub
	}

	@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
	public void testXMLSerializaion() throws Exception {
		CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(0, "111222333", 1, 1);
		CalledPartyNumberCapImpl forwardingDestinationNumber = new CalledPartyNumberCapImpl(calledPartyNumber);
		TNoAnswerSpecificInfoImpl original = new TNoAnswerSpecificInfoImpl(true, forwardingDestinationNumber);

		// Writes the area to a file.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
		// writer.setBinding(binding); // Optional.
		writer.setIndentation("\t"); // Optional (use tabulation for
										// indentation).
		writer.write(original, "tNoAnswerSpecificInfo", TNoAnswerSpecificInfoImpl.class);
		writer.close();

		byte[] rawData = baos.toByteArray();
		String serializedEvent = new String(rawData);

		System.out.println(serializedEvent);

		ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
		XMLObjectReader reader = XMLObjectReader.newInstance(bais);
		TNoAnswerSpecificInfoImpl copy = reader.read("tNoAnswerSpecificInfo", TNoAnswerSpecificInfoImpl.class);

		assertEquals(copy.getForwardingDestinationNumber().getCalledPartyNumber().getAddress(), original
				.getForwardingDestinationNumber().getCalledPartyNumber().getAddress());
		assertEquals(copy.getCallForwarded(), original.getCallForwarded());
	}

}
