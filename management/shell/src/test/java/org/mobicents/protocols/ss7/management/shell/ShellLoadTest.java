package org.mobicents.protocols.ss7.management.shell;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.mobicents.protocols.ss7.management.shell.AbstractCommand;
import org.mobicents.protocols.ss7.management.shell.ShellCommand;
import org.mobicents.protocols.ss7.management.shell.ExitCommand;
import org.mobicents.protocols.ss7.management.shell.NoshutdownCommand;
import org.mobicents.protocols.ss7.management.shell.SS7Command;
import org.mobicents.protocols.ss7.management.shell.ShowCommand;
import org.mobicents.protocols.ss7.management.shell.ShutdownCommand;

public class ShellLoadTest {

	private String linkSetName = "LinkSet1";
	private String linkName = "Link1";
	private String channel = "16";
	private String code = "280";

	private SocketChannel socketChannel = null;
	private InetSocketAddress address = null;
	private SelectionKey selectionKey = null;

	public String serverIp = "127.0.0.1";
	public int serverPort = 3435;

	private java.nio.channels.Selector selector = null;

	private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);

	private List<AbstractCommand> commands = new ArrayList<AbstractCommand>();

	private AbstractCommand exit = null;

	int executionIndex = 100000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ShellLoadTest test = new ShellLoadTest();
		test.execute();
	}

	public void execute() {

		String[] exitCmd = new String[] { ShellCommand.EXIT.getCmdStr() };
		exit = new ExitCommand(exitCmd);

		// Noshutdown commands
		String[] noshutdownSs7LinkSetStr = new String[] {
				ShellCommand.NOSHUTDOWN.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName };
		commands.add(new NoshutdownCommand(noshutdownSs7LinkSetStr));

		String[] noshutdownSs7LinkStr = new String[] {
				ShellCommand.NOSHUTDOWN.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LINK.getCmdStr(), linkName };
		commands.add(new NoshutdownCommand(noshutdownSs7LinkStr));

		// show commands
		String[] showSs7LinkSetStr = new String[] { ShellCommand.SHOW.getCmdStr(),
				ShellCommand.SS7.getCmdStr(), ShellCommand.LINKSET.getCmdStr() };
		commands.add(new ShowCommand(showSs7LinkSetStr));

		String[] showSs7LinkSet_LnSeNameStr = new String[] {
				ShellCommand.SHOW.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName };
		commands.add(new ShowCommand(showSs7LinkSet_LnSeNameStr));

		String[] showSs7LinkSet_LnSeName_statsStr = new String[] {
				ShellCommand.SHOW.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.STATISTICS.getCmdStr() };
		commands.add(new ShowCommand(showSs7LinkSet_LnSeName_statsStr));

		// shutdown commands
		String[] shutdownSs7LinkSetStr = new String[] {
				ShellCommand.SHUTDOWN.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName };
		commands.add(new ShutdownCommand(shutdownSs7LinkSetStr));

		String[] shutdownSs7LinkStr = new String[] {
				ShellCommand.SHUTDOWN.getCmdStr(), ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LINK.getCmdStr(), linkName };
		commands.add(new ShutdownCommand(shutdownSs7LinkStr));

		// SS7 Commands
		String[] addLinkSetstr = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.ADDLINKSET.getCmdStr(), linkSetName };
		commands.add(new SS7Command(addLinkSetstr));

		String[] deleteLinkstr = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.DELETELINK.getCmdStr(), linkName };
		commands.add(new SS7Command(deleteLinkstr));

		String[] deleteLinkSetstr = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.DELETELINKSET.getCmdStr(), linkSetName };
		commands.add(new SS7Command(deleteLinkSetstr));

		String[] inhibitstr = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.INHIBIT.getCmdStr(), linkSetName, linkName };
		commands.add(new SS7Command(inhibitstr));

		String[] linkChannelstr = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LINK.getCmdStr(), linkName,
				ShellCommand.CHANNEL.getCmdStr(), channel };
		commands.add(new SS7Command(linkChannelstr));

		String[] codestr = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.LINK.getCmdStr(), linkName, ShellCommand.CODE.getCmdStr(),
				code };
		commands.add(new SS7Command(codestr));

		String[] strAddLink = new String[] { ShellCommand.SS7.getCmdStr(),
				ShellCommand.LINKSET.getCmdStr(), linkSetName,
				ShellCommand.ADDLINK.getCmdStr(), linkName };
		commands.add(new SS7Command(strAddLink));

		try {
			address = new InetSocketAddress(serverIp, serverPort);
			selector = java.nio.channels.Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);

			selectionKey = socketChannel.register(selector,
					SelectionKey.OP_READ);

			socketChannel.connect(address);

			int size = commands.size();
			Random randomGenerator = new Random();

			int randomInt = randomGenerator.nextInt(size);

			// Finish connect first
			while (!socketChannel.finishConnect()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			System.out.println("Socket Connected");

			while (executionIndex > 0) {
				randomInt = randomGenerator.nextInt(size);
				AbstractCommand absCommd = commands.get(randomInt);
				this.execute(absCommd);
				executionIndex--;
			}

			this.execute(exit);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void execute(AbstractCommand cmd) {
		try {
			byteBuffer.clear();
			if (cmd.encode(byteBuffer)) {
				byteBuffer.flip();

				socketChannel.write(byteBuffer);

				int n = selector.select(5000);

				System.out.println("n=" + n);

				int count;
				if (n > 0) {
					Iterator<SelectionKey> it = selector.selectedKeys()
							.iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						it.remove();
						if (key.isReadable()) {
							byteBuffer.clear();
							while ((count = socketChannel.read(byteBuffer)) > 0) {
								byteBuffer.flip(); // make buffer readable

								int readLength = byteBuffer.limit();
								byte[] buff = new byte[readLength];
								byteBuffer.get(buff, 0, readLength);
								// System.out.println(new String(buff));
							}
						}
					}
				} else {
					System.out.println("No response from server");
				}// if n>0

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
