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
import org.mobicents.protocols.ss7.management.shell.SS7Command;

public class SS7CommandTest {
	ByteBuffer buffer = null;

	byte ZERO_LENGTH = 0x00;

	SS7Command ss7Cmd = null;

	ShellCmdListener cLICmdListener = null;
	ShellCommand cliCmdEnum = null;

	boolean cliListenerCalled = false;
	String resultLinksetName = null;
	int resultLocalPointCode = -1;
	int resultAdjaPointCode = -1;
	String resultLocalIp = null;
	int resultLocalPort = -1;
	String resultLink = null;
	int resultSpan = -1;
	int resultChannel = -1;
	int resultCode = -1;

	int resultLinkSetType = -1;

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
		resultLocalPointCode = -1;
		resultAdjaPointCode = -1;
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

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.ADDLINKSET.getCmdStr(), linkSetName,
				ShellCommand.LINKSET_DAHDI.getCmdStr() };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.ADDLINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.LINKSET_DAHDI.getCmdInt(), buffer
				.get());

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testaddLinkSetDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) ShellCommand.ADDLINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.put((byte) ShellCommand.LINKSET_DAHDI.getCmdInt());
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(ShellCommand.LINKSET_DAHDI.getCmdInt(), resultLinkSetType);

	}

	@Test
	public void testLinkSetNIEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.NETWORK_INDICATOR.getCmdStr(),
				ShellCommand.NETWORK_INDICATOR_INT.getCmdStr() };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.NETWORK_INDICATOR.getCmdInt(), buffer
				.get());
		assertEquals((byte) ZERO_LENGTH, buffer.get());
		assertEquals((byte) ShellCommand.NETWORK_INDICATOR_INT.getCmdInt(),
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
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) ShellCommand.NETWORK_INDICATOR.getCmdInt());
		buffer.put((byte) ZERO_LENGTH);
		buffer.put((byte) ShellCommand.NETWORK_INDICATOR_INT.getCmdInt());
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(ShellCommand.NETWORK_INDICATOR_INT, cliCmdEnum);

	}

	@Test
	public void testLinkSetLocalPcEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String localPointCode = "123";
		byte[] localPointCodeBytes = localPointCode.getBytes();

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LOCAL_PC.getCmdStr(), localPointCode };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.LOCAL_PC.getCmdInt(), buffer.get());
		assertEquals((byte) 0x03, buffer.get());

		int pointCode = ((buffer.get() << 16) | (buffer.get() << 8) | buffer
				.get());
		assertEquals(123, pointCode);

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkSetLocalPcDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		int pointCode = 123;

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) ShellCommand.LOCAL_PC.getCmdInt());
		buffer.put((byte) 0x03);
		buffer.put((byte) ((pointCode & 0x00ff0000) >> 16)); // value
		buffer.put((byte) ((pointCode & 0x0000ff00) >> 8)); // value
		buffer.put((byte) (pointCode & 0x000000ff)); // value
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(pointCode, resultLocalPointCode);

	}

	@Test
	public void testLinkSetAdjaPcEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String adjPointCode = "123";

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.ADJACENT_PC.getCmdStr(), adjPointCode };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.ADJACENT_PC.getCmdInt(), buffer.get());
		assertEquals((byte) 0x03, buffer.get());

		int pointCode = ((buffer.get() << 16) | (buffer.get() << 8) | buffer
				.get());
		assertEquals(123, pointCode);

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkSetAdjaPcDecode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		int adjPointCode = 123;

		buffer.clear();
		// buffer.put((byte)CliCmdEnum.SHOW.getCmdInt());
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) ShellCommand.ADJACENT_PC.getCmdInt());
		buffer.put((byte) 0x03);
		buffer.put((byte) ((adjPointCode & 0x00ff0000) >> 16)); // value
		buffer.put((byte) ((adjPointCode & 0x0000ff00) >> 8)); // value
		buffer.put((byte) (adjPointCode & 0x000000ff)); // value
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

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LOCAL_IP.getCmdStr(), localIp,
				ShellCommand.LOCAL_PORT.getCmdStr(), localPort };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.LOCAL_IP.getCmdInt(), buffer.get());
		assertEquals((byte) localIp.length(), buffer.get());

		for (int i = 0; i < localIp.length(); i++) {
			assertEquals(localIpBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.LOCAL_PORT.getCmdInt(), buffer.get());
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
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) ShellCommand.LOCAL_IP.getCmdInt());
		buffer.put((byte) localIp.length());
		buffer.put(localIp.getBytes());

		buffer.put((byte) ShellCommand.LOCAL_PORT.getCmdInt());
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

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.DELETELINKSET.getCmdStr(), linkSetName };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.DELETELINKSET.getCmdInt(), buffer
				.get());
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
		buffer.put((byte) ShellCommand.DELETELINKSET.getCmdInt());
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

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.ADDLINK.getCmdStr(), link };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.ADDLINK.getCmdInt(), buffer.get());
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
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) ShellCommand.ADDLINK.getCmdInt());
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

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String span = "4";

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LINK.getCmdStr(), link,
				ShellCommand.SPAN.getCmdStr(), span };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.SPAN.getCmdInt(), buffer.get());
		assertEquals((byte) 0x01, buffer.get());// length
		assertEquals((byte) 0x04, buffer.get());// span

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkSpanDecode() {
		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		buffer.clear();

		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) ShellCommand.LINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);

		buffer.put((byte) ShellCommand.SPAN.getCmdInt());
		buffer.put((byte) 0x01);
		buffer.put((byte) 0x04);
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(link, resultLink);
		assertEquals(4, resultSpan);

	}

	@Test
	public void testLinkChannelEncode() {

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String channel = "16";

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LINK.getCmdStr(), link,
				ShellCommand.CHANNEL.getCmdStr(), channel };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}
		assertEquals((byte) ShellCommand.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.CHANNEL.getCmdInt(), buffer.get());
		assertEquals((byte) 0x01, buffer.get());// length
		assertEquals((byte) 16, buffer.get());// span

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkChannelDecode() {
		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		buffer.clear();

		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.put((byte) ShellCommand.LINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);

		buffer.put((byte) ShellCommand.CHANNEL.getCmdInt());
		buffer.put((byte) 0x01);
		buffer.put((byte) 16);
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(link, resultLink);
		assertEquals(16, resultChannel);

	}

	@Test
	public void testLinkCodeEncode() {
		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String code = "280";

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LINK.getCmdStr(), link,
				ShellCommand.CODE.getCmdStr(), code };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}
		assertEquals((byte) ShellCommand.LINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.CODE.getCmdInt(), buffer.get());
		assertEquals((byte) 0x02, buffer.get());// length
		int codeInt = (buffer.get() << 8 | buffer.get());
		assertEquals(280, codeInt);// code

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testLinkCodeDecode() {
		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		buffer.clear();
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.put((byte) ShellCommand.LINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);

		buffer.put((byte) ShellCommand.CODE.getCmdInt());
		buffer.put((byte) 0x02);
		buffer.put((byte) ((280 & 0x0000ff00) >> 8)); // value
		buffer.put((byte) (280 & 0x000000ff)); // value
		buffer.flip();

		cLICmdListener = new CLICmdListenerImpl();
		ss7Cmd = new SS7Command();
		ss7Cmd.setCLICmdListener(cLICmdListener);

		ss7Cmd.decode(buffer);

		assertTrue(cliListenerCalled);
		assertEquals(linkSetName, resultLinksetName);
		assertEquals(link, resultLink);
		assertEquals(280, resultCode);

	}

	@Test
	public void testDeleteLinkEncode() {
		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.DELETELINK.getCmdStr(), link };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}
		assertEquals((byte) ShellCommand.DELETELINK.getCmdInt(), buffer.get());
		assertEquals((byte) link.length(), buffer.get());

		for (int i = 0; i < link.length(); i++) {
			assertEquals(linkBytes[i], buffer.get());
		}

		// here the ByteBuffer should stop
		assertEquals(buffer.position(), buffer.limit());

	}

	@Test
	public void testDeleteLinkDecode() {
		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		buffer.clear();
		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);
		buffer.put((byte) ShellCommand.DELETELINK.getCmdInt());
		buffer.put((byte) link.length());
		buffer.put(linkBytes);
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
	public void testInhibitEncode() {

		String link = "link1";
		byte[] linkBytes = link.getBytes();

		String linkSetName = "LinkSet1";
		byte[] linkSetNameBytes = linkSetName.getBytes();

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.INHIBIT.getCmdStr(), linkSetName, link };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.INHIBIT.getCmdInt(), buffer.get());
		assertEquals((byte) 0x00, buffer.get());

		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.LINK.getCmdInt(), buffer.get());
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
		buffer.put((byte) ShellCommand.INHIBIT.getCmdInt());
		buffer.put((byte) 0x00);

		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) ShellCommand.LINK.getCmdInt());
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

		String[] str = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.UNINHIBIT.getCmdStr(), linkSetName, link };

		ss7Cmd = new SS7Command(str);
		ss7Cmd.encode(buffer);

		buffer.flip();
		assertEquals((byte) ShellCommand.MTP.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.SS7.getCmdInt(), buffer.get());
		assertEquals((byte) ShellCommand.UNINHIBIT.getCmdInt(), buffer.get());
		assertEquals((byte) 0x00, buffer.get());

		assertEquals((byte) ShellCommand.LINKSET.getCmdInt(), buffer.get());
		assertEquals((byte) linkSetName.length(), buffer.get());

		for (int i = 0; i < linkSetName.length(); i++) {
			assertEquals(linkSetNameBytes[i], buffer.get());
		}

		assertEquals((byte) ShellCommand.LINK.getCmdInt(), buffer.get());
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
		buffer.put((byte) ShellCommand.UNINHIBIT.getCmdInt());
		buffer.put((byte) 0x00);

		buffer.put((byte) ShellCommand.LINKSET.getCmdInt());
		buffer.put((byte) linkSetName.length());
		buffer.put(linkSetNameBytes);

		buffer.put((byte) ShellCommand.LINK.getCmdInt());
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

	private class CLICmdListenerImpl implements ShellCmdListener {

		public void addLink(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			resultLink = linkName.toString();
			resultLinksetName = linksetName.toString();
			cliListenerCalled = true;
		}

		public void addLinkSet(TextBuilder linksetName, int type,
				ByteBuffer byteBuffer) {
			cliListenerCalled = true;
			resultLinksetName = linksetName.toString();
			resultLinkSetType = type;
		}

		public void adjacentPointCode(TextBuilder linksetName, int adjacentPC,
				ByteBuffer byteBuffer) {
			resultAdjaPointCode = adjacentPC;
			cliListenerCalled = true;
			resultLinksetName = linksetName.toString();
		}

		public void channel(TextBuilder linksetName, TextBuilder linkName,
				int channel, ByteBuffer byteBuffer) {
			resultChannel = channel;
			resultLink = linkName.toString();
			resultLinksetName = linksetName.toString();
			cliListenerCalled = true;
		}

		public void code(TextBuilder linksetName, TextBuilder linkName,
				int code, ByteBuffer byteBuffer) {
			resultCode = code;
			resultLink = linkName.toString();
			cliListenerCalled = true;
			resultLinksetName = linksetName.toString();
		}

		public void deleteLink(TextBuilder linksetName, TextBuilder linkName,
				ByteBuffer byteBuffer) {
			resultLink = linkName.toString();
			cliListenerCalled = true;
			resultLinksetName = linksetName.toString();
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

		public void localPointCode(TextBuilder linksetName, int localPC,
				ByteBuffer byteBuffer) {
			resultLocalPointCode = localPC;
			resultLinksetName = linksetName.toString();
			cliListenerCalled = true;
		}

		public void networkIndicator(TextBuilder linksetName,
				ShellCommand networkInd, ByteBuffer byteBuffer) {
			cliListenerCalled = true;
			resultLinksetName = linksetName.toString();
			cliCmdEnum = networkInd;
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
			resultSpan = span;
			resultLink = linkName.toString();
			resultLinksetName = linksetName.toString();
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
