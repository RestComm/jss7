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

public class SS7CommandTest {
	ByteBuffer buffer = null;

	byte ZERO_LENGTH = 0x00;

	SS7Command ss7Cmd = null;

	CLICmdListener cLICmdListener = null;
	CliCmdEnum cliCmdEnum = null;

	boolean cliListenerCalled = false;
	String resultLinksetName = null;
	String resultLocalPointCode = null;
	String resultAdjaPointCode = null;
	String resultLocalIp = null;
	int resultLocalPort = -1;
	String resultLink = null;
	int resultSpan = -1;
	int resultChannel = -1;
	int resultCode = -1;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		cliListenerCalled = false;
		resultLinksetName = null;
		resultLocalPointCode = null;
		resultAdjaPointCode = null;
		resultLocalIp = null;
		resultLocalPort = -1;
		resultLink = null;
		buffer = ByteBuffer.allocate(128);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testaddLinkSetEncode() {
		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.ADDLINKSET.getCmdStr(), linkSetName };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.ADDLINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testaddLinkSetDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.ADDLINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);

	}

	@Test
	public void testLinkSetNIEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr(), linkSetName,
				CliCmdEnum.NETWORK_INDICATOR.getCmdStr(),
				CliCmdEnum.NETWORK_INDICATOR_INT.getCmdStr() };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.NETWORK_INDICATOR.getCmdInt(), buffer
				.get());
		assertEquals((byte) ZERO_LENGTH, buffer.get());
		assertEquals((byte) CliCmdEnum.NETWORK_INDICATOR_INT.getCmdInt(),
				buffer.get());

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkSetNIDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) CliCmdEnum.NETWORK_INDICATOR.getCmdInt());
		buffer.put((byte) ZERO_LENGTH);
		buffer.put((byte) CliCmdEnum.NETWORK_INDICATOR_INT.getCmdInt());
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(CliCmdEnum.NETWORK_INDICATOR_INT, cliCmdEnum);

	}

	@Test
	public void testLinkSetLocalPcEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String localPointCode = "123";
		byte[] localPointCodeBytes = localPointCode.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr(), linkSetName,
				CliCmdEnum.LOCAL_PC.getCmdStr(), localPointCode };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.LOCAL_PC.getCmdInt(), buffer.get());
		assertEquals((byte) localPointCode.length(), buffer.get());

		for (int i = 0; i < localPointCode.length(); i++) {
			assertEquals(localPointCodeBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkSetLocalPcDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String localPointCode = "123";

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) CliCmdEnum.LOCAL_PC.getCmdInt());
		buffer.put((byte) localPointCode.length());
		buffer.put(localPointCode.getBytes());
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(localPointCode, resultLocalPointCode);

	}

	@Test
	public void testLinkSetAdjaPcEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String adjPointCode = "123";
		byte[] adjPointCodeBytes = adjPointCode.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr(), linkSetName,
				CliCmdEnum.ADJACENT_PC.getCmdStr(), adjPointCode };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.ADJACENT_PC.getCmdInt(), buffer.get());
		assertEquals((byte) adjPointCode.length(), buffer.get());

		for (int i = 0; i < adjPointCode.length(); i++) {
			assertEquals(adjPointCodeBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkSetAdjaPcDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String adjPointCode = "123";

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) CliCmdEnum.ADJACENT_PC.getCmdInt());
		buffer.put((byte) adjPointCode.length());
		buffer.put(adjPointCode.getBytes());
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(adjPointCode, resultAdjaPointCode);

	}

	@Test
	public void testLinkSetLocalIpPortEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String localIp = "192.168.0.101";
		String localPort = "3344";

		byte[] localIpBytes = localIp.getBytes();
		byte[] localPortBytes = localPort.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr(), linkSetName,
				CliCmdEnum.LOCAL_IP.getCmdStr(), localIp,
				CliCmdEnum.LOCAL_PORT.getCmdStr(), localPort };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.LOCAL_IP.getCmdInt(), buffer.get());
		assertEquals((byte) localIp.length(), buffer.get());

		for (int i = 0; i < localIp.length(); i++) {
			assertEquals(localIpBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.LOCAL_PORT.getCmdInt(), buffer.get());
		assertEquals((byte) 0x02, buffer.get());

		int localPortRes = ((buffer.get() << 8) | buffer.get());

		assertEquals(3344, localPortRes);

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkLocalIpPortDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String localIp = "192.168.0.101";
		int localPort = 3344;

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) CliCmdEnum.LOCAL_IP.getCmdInt());
		buffer.put((byte) localIp.length());
		buffer.put(localIp.getBytes());

		buffer.put((byte) CliCmdEnum.LOCAL_PORT.getCmdInt());
		buffer.put((byte) 0x02); // length
		buffer.put((byte) ((localPort & 0x0000ff00) >> 8)); // value
		buffer.put((byte) (localPort & 0x000000ff)); // value
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(localIp, resultLocalIp);
		assertEquals(localPort, resultLocalPort);

	}

	@Test
	public void testDeleteLinkSetEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.DELETELINKSET.getCmdStr(), linkSetName };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.DELETELINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testDeleteLinkSetDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.DELETELINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);

	}

	@Test
	public void testLinkSetAddLinkEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINKSET.getCmdStr(), linkSetName,
				CliCmdEnum.ADDLINK.getCmdStr(), link };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.ADDLINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkSetAddLinkDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) CliCmdEnum.ADDLINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(link.getBytes());
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(link, resultLink);

	}

	@Test
	public void testLinkSpanEncode() {
		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String span = "4";

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINK.getCmdStr(), link, CliCmdEnum.SPAN.getCmdStr(),
				span };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.SPAN.getCmdInt(), buffer.get());
		assertEquals((byte) 0x01, buffer.get());// length
		assertEquals((byte) 0x04, buffer.get());// span

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkSpanDecode() {
		String link = "link1";
		byte[] linkBytes = link.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.LINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);

		buffer.put((byte) CliCmdEnum.SPAN.getCmdInt());
		buffer.put((byte) 0x01);
		buffer.put((byte) 0x04);
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		// assertTrue(cliListenerCalled);
		assertEquals(link, resultLink);
		assertEquals(4, resultSpan);

	}

	@Test
	public void testLinkChannelEncode() {
		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String channel = "16";

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINK.getCmdStr(), link,
				CliCmdEnum.CHANNEL.getCmdStr(), channel };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.CHANNEL.getCmdInt(), buffer.get());
		assertEquals((byte) 0x01, buffer.get());// length
		assertEquals((byte) 16, buffer.get());// span

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkChannelDecode() {
		String link = "link1";
		byte[] linkBytes = link.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.LINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);

		buffer.put((byte) CliCmdEnum.CHANNEL.getCmdInt());
		buffer.put((byte) 0x01);
		buffer.put((byte) 16);
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(link, resultLink);
		assertEquals(16, resultChannel);

	}

	@Test
	public void testLinkCodeEncode() {
		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String code = "280";

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.LINK.getCmdStr(), link, CliCmdEnum.CODE.getCmdStr(),
				code };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.CODE.getCmdInt(), buffer.get());
		assertEquals((byte) 0x02, buffer.get());// length
		int codeInt = (buffer.get() << 8 | buffer.get());
		assertEquals(280, codeInt);// code

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkCodeDecode() {
		String link = "link1";
		byte[] linkBytes = link.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.LINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);

		buffer.put((byte) CliCmdEnum.CODE.getCmdInt());
		buffer.put((byte) 0x02);
		buffer.put((byte) ((280 & 0x0000ff00) >> 8)); // value
		buffer.put((byte) (280 & 0x000000ff)); // value
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(link, resultLink);
		assertEquals(280, resultCode);

	}

	@Test
	public void testDeleteLinkEncode() {

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.DELETELINK.getCmdStr(), link };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.DELETELINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testDeleteLinkDecode() {

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.DELETELINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(link, resultLink);

	}

	@Test
	public void testInhibitEncode() {

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.INHIBIT.getCmdStr(), linkSetName, link };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.INHIBIT.getCmdInt(), buffer.get());
		assertEquals((byte) 0x00, buffer.get());

		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testInhibitDecode() {

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.INHIBIT.getCmdInt());
		buffer.put((byte) 0x00);

		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) CliCmdEnum.LINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);

		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(link, resultLink);
		assertEquals(linkSetName, resultLinksetName);

	}

	@Test
	public void testUninhibitEncode() {

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String[] str = new String[] { CliCmdEnum.SS7.getCmdStr(),
				CliCmdEnum.UNINHIBIT.getCmdStr(), linkSetName, link };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();

		assertEquals((byte) CliCmdEnum.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) CliCmdEnum.UNINHIBIT.getCmdInt(), buffer.get());
		assertEquals((byte) 0x00, buffer.get());

		assertEquals((byte) CliCmdEnum.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) CliCmdEnum.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testUninhibitDecode() {

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) CliCmdEnum.UNINHIBIT.getCmdInt());
		buffer.put((byte) 0x00);

		buffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) CliCmdEnum.LINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);

		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(link, resultLink);
		assertEquals(linkSetName, resultLinksetName);

	}

	private class CLICmdListenerImpl implements CLICmdListener {

		public void addLink(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			resultLink = linkName.toString();
			resultLinksetName = linksetName.toString();
			cliListenerCalled = true;
		}

		public void addLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer) {
			cliListenerCalled = true;
			resultLinksetName = linksetName.toString();
		}

		public void adjacentPointCode(TextBuilder linksetName,
				TextBuilder adjacentPC, ByteBuffer byteBuffer) {
			resultAdjaPointCode = adjacentPC.toString();
			cliListenerCalled = true;
			resultLinksetName = linksetName.toString();
		}

		public void channel(TextBuilder linkName, int channel,
				ByteBuffer byteBuffer) {
			resultChannel = channel;
			resultLink = linkName.toString();
			cliListenerCalled = true;
		}

		public void code(TextBuilder linkName, int code, ByteBuffer byteBuffer) {
			resultCode = code;
			resultLink = linkName.toString();
			cliListenerCalled = true;
		}

		public void deleteLink(TextBuilder linkName, ByteBuffer byteBuffer) {
			resultLink = linkName.toString();
			cliListenerCalled = true;
		}

		public void deleteLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer) {
			cliListenerCalled = true;
			resultLinksetName = linksetName.toString();
		}

		public void inhibit(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			cliListenerCalled = true;
			resultLink = linkName.toString();
			resultLinksetName = linksetName.toString();
		}

		public void localIpPort(TextBuilder linksetName, TextBuilder localIp,
				int localPort, ByteBuffer byteBuffer) {
			resultLocalIp = localIp.toString();
			resultLocalPort = localPort;
			resultLinksetName = linksetName.toString();
			cliListenerCalled = true;
		}

		public void localPointCode(TextBuilder linksetName,
				TextBuilder localPC, ByteBuffer byteBuffer) {
			resultLocalPointCode = localPC.toString();
			resultLinksetName = linksetName.toString();
			cliListenerCalled = true;
		}

		public void networkIndicator(TextBuilder linksetName,
				CliCmdEnum networkInd, ByteBuffer byteBuffer) {
			cliListenerCalled = true;
			resultLinksetName = linksetName.toString();
			cliCmdEnum = networkInd;
		}

		public void noshutdownLink(TextBuilder linkName, ByteBuffer byteBuffer) {
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

		public void shutdownLink(TextBuilder linkName, ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void shutdownLinkSet(TextBuilder linksetName,
				ByteBuffer byteBuffer) {
			// TODO Auto-generated method stub

		}

		public void span(TextBuilder linkName, int span, ByteBuffer byteBuffer) {
			resultSpan = span;
			resultLink = linkName.toString();
			cliListenerCalled = true;
		}

		public void uninhibit(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			cliListenerCalled = true;
			resultLink = linkName.toString();
			resultLinksetName = linksetName.toString();
		}

	}
}
