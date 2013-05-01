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

package org.mobicents.protocols.ss7.sccp.impl.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SccpRemoveSpcTest {

	private Logger logger;
	private SccpStackImpl stack = new SccpStackImpl("SccpRemoveSpcTest");
	private MessageFactoryImpl messageFactory;

	@BeforeMethod
	public void setUp() {
		this.stack.setPersistDir(Util.getTmpTestDir());
		this.messageFactory = new MessageFactoryImpl(stack);
		this.logger = Logger.getLogger(SccpStackImpl.class.getCanonicalName());
	}

	@AfterMethod
	public void tearDown() {
	}

	public byte[] getDataUdt_GT_WithDpc() {
		return new byte[] { 9, 1, 3, 11, 19, 8, 11, -89, 5, 8, 0, 68, 68, 68, 8, 11, -90, 5, 8, 0, 85, 85, 85, 5, 22, 22, 22, 22, 22 };
	}

	public byte[] getDataUdt_GT_WithOutDpc() {
		return new byte[] { 9, 1, 3, 9, 15, 6, 10, 8, 0, 68, 68, 68, 6, 10, 8, 0, 85, 85, 85, 5, 22, 22, 22, 22, 22};
	}

	public byte[] getDataUdt_DpcSsn() {
		return new byte[] { 9, 1, 3, 7, 11, 4, 67, -89, 5, 8, 4, 67, -90, 5, 8, 5, 22, 22, 22, 22, 22 };
	}

	public byte[] getData() {
		return new byte[] { 22, 22, 22, 22, 22 };
	}

	@Test(groups = { "SccpMessage", "functional.decode"})
	public void testDecode() throws Exception {

		// ---- Encoding based on GT - removeSpc on
		this.stack.setRemoveSpc(true);
		byte[] b = this.getDataUdt_GT_WithOutDpc(); 

		ByteArrayInputStream buf = new ByteArrayInputStream(b);
		int type = buf.read();
		SccpDataMessage testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf);
		assertNotNull(testObjectDecoded);

		SccpAddress calledAdd = testObjectDecoded.getCalledPartyAddress();
		assertNotNull(calledAdd);
		assertEquals( calledAdd.getSubsystemNumber(),8);
		assertEquals( calledAdd.getSignalingPointCode(),0);
		GlobalTitle gt = calledAdd.getGlobalTitle();
		assertTrue(gt.getDigits().equals("444444"));

		SccpAddress callingAdd = testObjectDecoded.getCallingPartyAddress();
		assertNotNull(callingAdd);
		assertEquals( callingAdd.getSubsystemNumber(),8);
		assertEquals( callingAdd.getSignalingPointCode(),0);
		gt = callingAdd.getGlobalTitle();
		assertTrue(gt.getDigits().equals("555555"));

		// ---- Encoding based on GT - removeSpc off 
		this.stack.setRemoveSpc(false);
		b = this.getDataUdt_GT_WithDpc();

		buf = new ByteArrayInputStream(b);
		type = buf.read();
		testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf);
		assertNotNull(testObjectDecoded);

		calledAdd = testObjectDecoded.getCalledPartyAddress();
		assertNotNull(calledAdd);
		assertEquals( calledAdd.getSubsystemNumber(),8);
		assertEquals( calledAdd.getSignalingPointCode(),1447);
		gt = calledAdd.getGlobalTitle();
		assertTrue(gt.getDigits().equals("444444"));

		callingAdd = testObjectDecoded.getCallingPartyAddress();
		assertNotNull(callingAdd);
		assertEquals( callingAdd.getSubsystemNumber(),8);
		assertEquals( callingAdd.getSignalingPointCode(),1446);
		gt = callingAdd.getGlobalTitle();
		assertTrue(gt.getDigits().equals("555555"));

		// ---- Encoding based on DPC+SSN - removeSpc on 
		this.stack.setRemoveSpc(true);
		b = this.getDataUdt_DpcSsn();

		buf = new ByteArrayInputStream(b);
		type = buf.read();
		testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf);
		assertNotNull(testObjectDecoded);

		calledAdd = testObjectDecoded.getCalledPartyAddress();
		assertNotNull(calledAdd);
		assertEquals( calledAdd.getSubsystemNumber(),8);
		assertEquals( calledAdd.getSignalingPointCode(),1447);
		gt = calledAdd.getGlobalTitle();
		assertNull(gt);

		callingAdd = testObjectDecoded.getCallingPartyAddress();
		assertNotNull(callingAdd);
		assertEquals( callingAdd.getSubsystemNumber(),8);
		assertEquals( callingAdd.getSignalingPointCode(),1446);
		gt = callingAdd.getGlobalTitle();
		assertNull(gt);

		// ---- Encoding based on DPC+SSN - removeSpc off 
		this.stack.setRemoveSpc(false);
		b = this.getDataUdt_DpcSsn();

		buf = new ByteArrayInputStream(b);
		type = buf.read();
		testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf);
		assertNotNull(testObjectDecoded);

		calledAdd = testObjectDecoded.getCalledPartyAddress();
		assertNotNull(calledAdd);
		assertEquals( calledAdd.getSubsystemNumber(),8);
		assertEquals( calledAdd.getSignalingPointCode(),1447);
		gt = calledAdd.getGlobalTitle();
		assertNull(gt);

		callingAdd = testObjectDecoded.getCallingPartyAddress();
		assertNotNull(callingAdd);
		assertEquals( callingAdd.getSubsystemNumber(),8);
		assertEquals( callingAdd.getSignalingPointCode(),1446);
		gt = callingAdd.getGlobalTitle();
		assertNull(gt);

	}

	@Test(groups = { "SccpMessage", "functional.encode"})
	public void testEncode() throws Exception {

		// ---- Encoding based on GT - removeSpc on
		// ---- removeSpc on
		this.stack.setRemoveSpc(true);
		GlobalTitle gt1 = GlobalTitle.getInstance(0, "444444");
		GlobalTitle gt2 = GlobalTitle.getInstance(0, "555555");
		SccpAddress calledAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 1447, gt1, 8);
		SccpAddress callingAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 1446, gt2, 8);
		SccpDataMessageImpl msg = (SccpDataMessageImpl)messageFactory.createDataMessageClass1(calledAdd, callingAdd, getData(), 0, 8, false, null, null);

		EncodingResultData res = msg.encode(LongMessageRuleType.LongMessagesForbidden, 272, logger);
		assertEquals(res.getEncodingResult(), EncodingResult.Success);
		
		logger.debug("*************\n"+Arrays.toString(res.getSolidData()));
		
		assertTrue(Arrays.equals(res.getSolidData(), this.getDataUdt_GT_WithOutDpc()));

		// ---- removeSpc off
		this.stack.setRemoveSpc(false);

		res = msg.encode(LongMessageRuleType.LongMessagesForbidden, 272, logger);
		assertEquals(res.getEncodingResult(), EncodingResult.Success);
		assertTrue(Arrays.equals(res.getSolidData(), this.getDataUdt_GT_WithDpc()));

		// ---- Encoding based on DPC+SSN - removeSpc on
		// ---- removeSpc on
		this.stack.setRemoveSpc(true);
		calledAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1447, null, 8);
		callingAdd = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1446, null, 8);
		msg = (SccpDataMessageImpl)messageFactory.createDataMessageClass1(calledAdd, callingAdd, getData(), 0, 8, false, null, null);

		res = msg.encode(LongMessageRuleType.LongMessagesForbidden, 272, logger);
		assertEquals(res.getEncodingResult(), EncodingResult.Success);
		assertTrue(Arrays.equals(res.getSolidData(), this.getDataUdt_DpcSsn()));

		// ---- removeSpc off
		this.stack.setRemoveSpc(false);

		res = msg.encode(LongMessageRuleType.LongMessagesForbidden, 272, logger);
		assertEquals(res.getEncodingResult(), EncodingResult.Success);
		assertTrue(Arrays.equals(res.getSolidData(), this.getDataUdt_DpcSsn()));
	}
}

