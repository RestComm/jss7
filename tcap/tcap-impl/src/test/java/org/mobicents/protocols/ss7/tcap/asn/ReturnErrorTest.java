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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;

import junit.framework.TestCase;

/**
 * 
 * @author amit bhayani
 *
 */
public class ReturnErrorTest extends TestCase {
	
	@org.junit.Test
	public void testDecodeWithParaSequ() throws IOException, ParseException {
		
		if(true)
		{
			//FIXME:
			return;
		}
		/**
		 * TODO :
		 * This test is half, as the ReturnResultLastImpl and ReturnResultImpl still has ambiguity in decode(). Read comments in  respective 
		 * classes .decode method 
		 */
		
		
		byte[] b = new byte[] { 
				
				//0xA3 - Return ReturnError TAG
				(byte)0xA3,
                //0x06 - Len
				0x06,
                        //0x02 - InvokeID Tag
						0x02,
                        //0x01 - Len
						0x01,
                            //0x05
							0x05,
                        //0x02 - ReturnError Code Tag
						0x02,
                        //0x01 - Len
						0x01,
							//0x0F
							0x0F
				 };
		
		AsnInputStream asnIs = new AsnInputStream(new ByteArrayInputStream(b));

		Component comp = TcapFactory.createComponent(asnIs);

		assertEquals("Wrong component Type",ComponentType.ReturnError, comp.getType());
		ReturnError re = (ReturnError) comp;
		assertEquals("Wrong invoke ID",new Long(5), re.getInvokeId());
		assertNotNull("No error code.",re.getErrorCode());
		ErrorCode ec = re.getErrorCode();
		assertEquals("Wrong error code type.",ErrorCodeType.Local, ec.getErrorType());
		byte[] d = ec.getData();
		assertNotNull("No data.",d);
		assertEquals("Wrong data len.",1, d.length);
		assertEquals("wrong data conent.",new Byte((byte)15), new Byte(d[0]));
		AsnOutputStream aos = new AsnOutputStream();
		comp.encode(aos);
		byte[] encoded  = aos.toByteArray();
		compareArrays(b, encoded);
		
	}
	private void compareArrays(byte[] expected, byte[] encoded) {
		boolean same = Arrays.equals(expected, encoded);
		assertTrue("byte[] dont match, expected|encoded \n"
				+ Arrays.toString(expected) + "\n" + Arrays.toString(encoded),
				same);
	}
}
