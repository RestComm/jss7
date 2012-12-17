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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AllocationRetentionPriority;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSClassIdentifier;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class EPSQoSSubscribedTest {
	
	public byte[] getData() {
		return new byte[] { 48, 94, -128, 1, 1, -95, 48, -128, 1, 1, -127, 0,
				-126, 0, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
				14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
				23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 39, -96, 32, 48, 10,
				6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
				11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		EPSQoSSubscribedImpl prim = new EPSQoSSubscribedImpl();
		
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
		
		AllocationRetentionPriority allocationRetentionPriority = prim.getAllocationRetentionPriority();
		MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
		assertEquals(allocationRetentionPriority.getPriorityLevel(), 1);
		assertTrue(allocationRetentionPriority.getPreEmptionCapability());
		assertTrue(allocationRetentionPriority.getPreEmptionVulnerability());
		assertNotNull(allocationRetentionPriority.getExtensionContainer());;
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(allocationRetentionPriority.getExtensionContainer()));
		assertNotNull(extensionContainer);
		assertEquals(prim.getQoSClassIdentifier(), QoSClassIdentifier.QCI_1);
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
		QoSClassIdentifier qoSClassIdentifier = QoSClassIdentifier.QCI_1;
		AllocationRetentionPriority allocationRetentionPriority = new AllocationRetentionPriorityImpl(1, Boolean.TRUE, Boolean.TRUE, extensionContainer);
		EPSQoSSubscribedImpl prim = new EPSQoSSubscribedImpl( qoSClassIdentifier, allocationRetentionPriority, extensionContainer);

		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);
		
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
	}
	
}
