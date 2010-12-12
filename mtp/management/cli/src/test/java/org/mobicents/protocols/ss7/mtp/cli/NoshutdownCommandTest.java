package org.mobicents.protocols.ss7.mtp.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author amit bhayani
 *
 */
public class NoshutdownCommandTest {
	ByteBuffer buffer = null;

	NoshutdownCommand noshutdownCmd = null;

	byte ZERO_LENGTH = 0x00;
	String linkSetName = "LinkSet1";
	byte[] linkSetNameBytes = linkSetName.getBytes();

	String linkName = "Link1";
	byte[] linkNameBytes = linkName.getBytes();

	CLICmdListener cLICmdListener = null;

	boolean noshutdownLinkset = false;
	boolean noshutdownLink = false;
	String lksetName = null;
	String lkName = null;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		buffer = ByteBuffer.allocate(128);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testNoshutdownSs7LinkSetEncode() {
		String[] shutdownSs7LinkSetStr = new String[] {
				CliCmdEnum.NOSHUTDOWN.getCmdStr(), CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr(), linkSetName };

		noshutdownCmd = new NoshutdownCommand(shutdownSs7LinkSetStr);
		noshutdownCmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.NOSHUTDOWN.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testNoshutdownSs7LinkSetDecode() {
		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHUTDOWN.getCmdInt());
		buffer.put((byte) CliCmdEnum.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		noshutdownCmd = new NoshutdownCommand();
		noshutdownCmd.setCLICmdListener(cLICmdListener);

		noshutdownCmd.decode(buffer);

		assertTrue(noshutdownLinkset);
		assertEquals(linkSetName, lksetName);
	}

	@Test
	public void testNoshutdownSs7LinkEncode() {
		String[] shutdownSs7LinkSetStr = new String[] {
				CliCmdEnum.NOSHUTDOWN.getCmdStr(), CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINK.getCmdStr(), linkName };

		noshutdownCmd = new NoshutdownCommand(shutdownSs7LinkSetStr);
		noshutdownCmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.NOSHUTDOWN.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) CliCmdEnum.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) linkName.length(), buffer.get());

		for (int i = 0; i < linkName.length(); i++) {
			assertEquals(linkNameBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testNoshutdownSs7LinkDecode() {
		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHUTDOWN.getCmdInt());
		buffer.put((byte) CliCmdEnum.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) CliCmdEnum.LINK.getCmdInt());
		buffer.put((byte) linkName.length());
		buffer.put(linkNameBytes);

		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		noshutdownCmd = new NoshutdownCommand();
		noshutdownCmd.setCLICmdListener(cLICmdListener);

		noshutdownCmd.decode(buffer);

		assertTrue(noshutdownLink);
		assertEquals(linkName, lkName);
	}

	private class CLICmdListenerImpl implements CLICmdListener {



		public void noshutdownLink(TextBuilder linkName, ByteBuffer byteBuffer) {
			noshutdownLink = true;
			lkName = linkName.toString();
		}

		public void noshutdownLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer) {
			noshutdownLinkset = true;
			lksetName = linksetName.toString();
		}

		public void addLink(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void addLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void adjacentPointCode(TextBuilder linksetName,
				TextBuilder adjacentPC, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void channel(TextBuilder linkName, int channel,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void code(TextBuilder linkName, int code, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void deleteLink(TextBuilder linkName, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void deleteLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void inhibit(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void localIpPort(TextBuilder linksetName, TextBuilder localIp,
				int localPort, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void localPointCode(TextBuilder linksetName,
				TextBuilder localPC, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void networkIndicator(TextBuilder linksetName,
				CliCmdEnum networkInd, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void showLinkSet(boolean statistics, TextBuilder linksetName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void shutdownLink(TextBuilder linkName, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void shutdownLinkSet(TextBuilder linksetName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void span(TextBuilder linkName, int span, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void uninhibit(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}



	}
}
