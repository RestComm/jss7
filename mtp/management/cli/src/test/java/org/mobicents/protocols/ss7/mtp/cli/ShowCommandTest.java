package org.mobicents.protocols.ss7.mtp.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
public class ShowCommandTest {

	ByteBuffer buffer = null;

	byte ZERO_LENGTH = 0x00;

	ShowCommand shwCmd = null;

	String linkSetName = "LinkSet1";
	byte[] linkSetNameBytes = linkSetName.getBytes();

	boolean stats = false;
	String lksetName = null;
	boolean listenerCalled = false;

	CLICmdListener cLICmdListener = null;

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

		String[] showSs7LinkSetStr = new String[] {
				CliCmdEnum.SHOW.getCmdStr(), CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr() };

		shwCmd = new ShowCommand(showSs7LinkSetStr);
		shwCmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SHOW.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testShowSs7LinkSetDecode() {
		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
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
				CliCmdEnum.SHOW.getCmdStr(), CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr(), linkSetName };

		shwCmd = new ShowCommand(showSs7LinkSet_LnSeNameStr);
		buffer.clear();
		shwCmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SHOW.getCmdInt(), buffer.get());
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
	public void testShowSs7LinkSet_LeSetNmDecode() {

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
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
				CliCmdEnum.SHOW.getCmdStr(), CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr(), linkSetName,
				CliCmdEnum.STATISTICS.getCmdStr() };

		shwCmd = new ShowCommand(showSs7LinkSet_LnSeName_statsStr);
		buffer.clear();
		shwCmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SHOW.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.STATISTICS.getCmdInt(), buffer.get());

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testShowSs7LinkSet_LeSetNm_StatsDecode() {

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.put((byte) CliCmdEnum.STATISTICS.getCmdInt());
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

		String[] showSs7LinkSetStr = new String[] {
				CliCmdEnum.SHOW.getCmdStr(), CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr(),
				CliCmdEnum.STATISTICS.getCmdStr() };

		shwCmd = new ShowCommand(showSs7LinkSetStr);
		shwCmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SHOW.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals(ZERO_LENGTH, buffer.get());
		assertEquals((byte) CliCmdEnum.STATISTICS.getCmdInt(), buffer.get());

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}
	
	@Test
	public void testShowSs7LinkSet_StatsDecode() {
		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.SS7.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put(ZERO_LENGTH);
		buffer.put((byte) CliCmdEnum.STATISTICS.getCmdInt());		
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		shwCmd = new ShowCommand();
		shwCmd.setCLICmdListener(cLICmdListener);

		shwCmd.decode(buffer);

		assertTrue(listenerCalled);
		assertEquals("",lksetName);
		assertTrue(stats);

	}	

	private class CLICmdListenerImpl implements CLICmdListener {

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

		public void noshutdownLink(TextBuilder linkName, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub
			
		}

		public void noshutdownLinkSet(TextBuilder linksetName,
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
