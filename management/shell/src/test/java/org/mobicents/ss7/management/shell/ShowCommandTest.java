package org.mobicents.ss7.management.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.ss7.management.shell.ShellCmdListener;
import org.mobicents.ss7.management.shell.ShellCommand;
import org.mobicents.ss7.management.shell.ShowCommand;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ShowCommandTest {

	ByteBuffer buffer = null;

	byte ZERO_LENGTH = 0x00;

	ShowCommand shwCmd = null;

	String linkSetName = "LinkSet1";
	byte[] linkSetNameBytes = linkSetName.getBytes();

	boolean stats = false;
	String lksetName = null;
	boolean listenerCalled = false;

	ShellCmdListener cLICmdListener = null;

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
	public void testShowSs7LinkSetEncode() {

		String[] showSs7LinkSetStr = new String[] { ShellCommand.SHOW.getCmdStr(),
				ShellCommand.SS7.getCmdStr(), ShellCommand.LINKSET.getCmdStr() };

		shwCmd = new ShowCommand(showSs7LinkSetStr);
		shwCmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SHOW.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testShowSs7LinkSetDecode() {
		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) ShellCommand.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		shwCmd = new ShowCommand();
		shwCmd.setCLICmdListener(cLICmdListener);

		shwCmd.decode(buffer);

		assertTrue(listenerCalled);
		assertEquals("", lksetName);
		assertFalse(stats);

	}

	@Test
	public void testShowSs7LinkSet_LeSetNmEncode() {

		String[] showSs7LinkSet_LnSeNameStr = new String[] {
				ShellCommand.SHOW.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName };

		shwCmd = new ShowCommand(showSs7LinkSet_LnSeNameStr);
		buffer.clear();
		shwCmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SHOW.getCmdInt(), buffer.get());
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
	public void testShowSs7LinkSet_LeSetNmDecode() {

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) ShellCommand.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		shwCmd = new ShowCommand();
		shwCmd.setCLICmdListener(cLICmdListener);

		shwCmd.decode(buffer);

		assertTrue(listenerCalled);
		assertEquals(linkSetName, lksetName);
		assertFalse(stats);

	}

	@Test
	public void testShowSs7LinkSet_LeSetNm_StatsEncode() {

		String[] showSs7LinkSet_LnSeName_statsStr = new String[] {
				ShellCommand.SHOW.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.STATISTICS.getCmdStr() };

		shwCmd = new ShowCommand(showSs7LinkSet_LnSeName_statsStr);
		buffer.clear();
		shwCmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SHOW.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.STATISTICS.getCmdInt(), buffer.get());

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testShowSs7LinkSet_LeSetNm_StatsDecode() {

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) ShellCommand.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.put((byte) ShellCommand.STATISTICS.getCmdInt());
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		shwCmd = new ShowCommand();
		shwCmd.setCLICmdListener(cLICmdListener);

		shwCmd.decode(buffer);

		assertTrue(listenerCalled);
		assertEquals(linkSetName, lksetName);
		assertTrue(stats);

	}

	@Test
	public void testShowSs7LinkSet_StatsEncode() {

		String[] showSs7LinkSetStr = new String[] { ShellCommand.SHOW.getCmdStr(),
				ShellCommand.SS7.getCmdStr(), ShellCommand.LINKSET.getCmdStr(),
				ShellCommand.STATISTICS.getCmdStr() };

		shwCmd = new ShowCommand(showSs7LinkSetStr);
		shwCmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SHOW.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) ShellCommand.STATISTICS.getCmdInt(), buffer.get());

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testShowSs7LinkSet_StatsDecode() {
		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) ShellCommand.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) ShellCommand.STATISTICS.getCmdInt());
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		shwCmd = new ShowCommand();
		shwCmd.setCLICmdListener(cLICmdListener);

		shwCmd.decode(buffer);

		assertTrue(listenerCalled);
		assertEquals("", lksetName);
		assertTrue(stats);

	}

	private class CLICmdListenerImpl implements ShellCmdListener {

		public void showLinkSet(boolean statistics, TextBuilder linksetName,
				ByteBuffer byteBuffer) {
			stats = statistics;
			lksetName = linksetName.toString();
			listenerCalled = true;

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

		public void shutdownLink(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void shutdownLinkSet(TextBuilder linksetName,
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
