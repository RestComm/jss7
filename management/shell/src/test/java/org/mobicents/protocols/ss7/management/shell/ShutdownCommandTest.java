package org.mobicents.protocols.ss7.management.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.management.shell.ShellCmdListener;
import org.mobicents.protocols.ss7.management.shell.ShellCommand;
import org.mobicents.protocols.ss7.management.shell.ShutdownCommand;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ShutdownCommandTest {

	ByteBuffer buffer = null;

	ShutdownCommand shutdownCmd = null;

	byte ZERO_LENGTH = 0x00;
	String linkSetName = "LinkSet1";
	byte[] linkSetNameBytes = linkSetName.getBytes();

	String linkName = "Link1";
	byte[] linkNameBytes = linkName.getBytes();

	ShellCmdListener cLICmdListener = null;

	boolean shutdownLinkset = false;
	boolean shutdownLink = false;
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
	public void testShutdownSs7LinkSetEncode() {
		String[] shutdownSs7LinkSetStr = new String[] {
				ShellCommand.SHUTDOWN.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName };

		shutdownCmd = new ShutdownCommand(shutdownSs7LinkSetStr);
		shutdownCmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SHUTDOWN.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testShutdownSs7LinkSetDecode() {
		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHUTDOWN.getCmdInt());
		buffer.put((byte) ShellCommand.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		shutdownCmd = new ShutdownCommand();
		shutdownCmd.setCLICmdListener(cLICmdListener);

		shutdownCmd.decode(buffer);

		assertTrue(shutdownLinkset);
		assertEquals(linkSetName, lksetName);
	}

	@Test
	public void testShutdownSs7LinkEncode() {
		String[] shutdownSs7LinkSetStr = new String[] {
				ShellCommand.SHUTDOWN.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LINK.getCmdStr(), linkName };

		shutdownCmd = new ShutdownCommand(shutdownSs7LinkSetStr);
		shutdownCmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SHUTDOWN.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());

		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) linkName.length(), buffer.get());

		for (int i = 0; i < linkName.length(); i++) {
			assertEquals(linkNameBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testShutdownSs7LinkDecode() {
		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHUTDOWN.getCmdInt());
		buffer.put((byte) ShellCommand.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.put((byte) ShellCommand.LINK.getCmdInt());
		buffer.put((byte) linkName.length());
		buffer.put(linkNameBytes);

		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		shutdownCmd = new ShutdownCommand();
		shutdownCmd.setCLICmdListener(cLICmdListener);

		shutdownCmd.decode(buffer);

		assertTrue(shutdownLink);
		assertEquals(linkName, lkName);
		assertEquals(linkSetName, lksetName);
	}

	private class CLICmdListenerImpl implements ShellCmdListener {

		public void showLinkSet(boolean statistics, String linksetName,
				ByteBuffer byteBuffer) {

		}

		public void shutdownLink(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			shutdownLink = true;
			lkName = linkName.toString();
			lksetName = linksetName.toString();
		}

		public void shutdownLinkSet(TextBuilder linksetName,
				ByteBuffer byteBuffer) {
			shutdownLinkset = true;
			lksetName = linksetName.toString();
		}

		public void addLink(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void addLinkSet(TextBuilder linksetName, int type, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void adjacentPointCode(TextBuilder linksetName,
				int adjacentPC, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void channel(TextBuilder linksetName, TextBuilder linkName,
				int channel, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void code(TextBuilder linksetName, TextBuilder linkName,
				int code, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void deleteLink(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
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
				int localPC, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void networkIndicator(TextBuilder linksetName,
				ShellCommand networkInd, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void noshutdownLink(TextBuilder linksetName,
				TextBuilder linkName, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void noshutdownLinkSet(TextBuilder linksetName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void showLinkSet(boolean statistics, TextBuilder linksetName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void span(TextBuilder linksetName, TextBuilder linkName,
				int span, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void uninhibit(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

	}

}
