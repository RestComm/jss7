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

package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.*;import org.testng.*;import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;


import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;

/**
 * @author sergey vetyutnev
 * 
 */
public class MAPExtensionContainerTest {
	MAPParameterFactory mapServiceFactory = new MAPParameterFactoryImpl();
	
	public static MAPExtensionContainer GetTestExtensionContainer() {
		MAPParameterFactory mapServiceFactory = new MAPParameterFactoryImpl(); 
		
		ArrayList<MAPPrivateExtension> al = new ArrayList<MAPPrivateExtension>();
		al.add(mapServiceFactory
				.createMAPPrivateExtension(new long[] { 1, 2, 3, 4 }, new byte[] { 11, 12, 13, 14, 15 }));
		al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 6 }, null));
		al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 5 }, new byte[] { 21, 22, 23, 24, 25,
				26 }));

		MAPExtensionContainer cnt = mapServiceFactory.createMAPExtensionContainer(al, new byte[] { 31, 32, 33 });

		return cnt;
	}

	public static Boolean CheckTestExtensionContainer(MAPExtensionContainer extContainer) {
		if (extContainer == null || extContainer.getPrivateExtensionList().size() != 3)
			return false;

		for (int i = 0; i < 3; i++) {
			MAPPrivateExtension pe = extContainer.getPrivateExtensionList().get(i);
			long[] lx = null;
			byte[] bx = null;

			switch (i) {
			case 0:
				lx = new long[] { 1, 2, 3, 4 };
				bx = new byte[] { 11, 12, 13, 14, 15 };
				break;
			case 1:
				lx = new long[] { 1, 2, 3, 6 };
				bx = null;
				break;
			case 2:
				lx = new long[] { 1, 2, 3, 5 };
				bx = new byte[] { 21, 22, 23, 24, 25, 26 };
				break;
			}

			if (pe.getOId() == null || !Arrays.equals(pe.getOId(), lx))
				return false;
			if (bx == null) {
				if (pe.getData() != null)
					return false;
			} else {
				if (pe.getData() == null || !Arrays.equals(pe.getData(), bx))
					return false;
			}
		}

		byte[] by = new byte[] { 31, 32, 33 };
		if (extContainer.getPcsExtensions() == null || !Arrays.equals(extContainer.getPcsExtensions(), by))
			return false;

		return true;
	}
	

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeTest
	public void setUp() {
	}

	@AfterTest
	public void tearDown() {
	}

	@Test(groups = { "functional.decode","primitives"})
	public void testDecode() throws Exception {

		byte[] data = this.getEncodedData();
		AsnInputStream ais = new AsnInputStream(data);
		int tag = ais.readTag();
		MAPExtensionContainerImpl extCont = new MAPExtensionContainerImpl();
		extCont.decodeAll(ais);

		assertEquals( tag,Tag.SEQUENCE);
		assertEquals( CheckTestExtensionContainer(extCont),Boolean.TRUE);
	}

	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {
		byte[] data = this.getEncodedData();
		
		MAPExtensionContainerImpl extCont = (MAPExtensionContainerImpl)GetTestExtensionContainer();
		AsnOutputStream asnOS = new AsnOutputStream();
		extCont.encodeAll(asnOS);
		byte[] res = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,res));
	}
	
	private byte[] getEncodedData() {
		return new byte[] { 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
				25, 26, (byte) 161, 3, 31, 32, 33 };
	}
}
