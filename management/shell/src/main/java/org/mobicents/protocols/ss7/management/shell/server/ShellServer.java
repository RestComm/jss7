package org.mobicents.protocols.ss7.management.shell.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.management.shell.AbstractCommand;
import org.mobicents.protocols.ss7.management.shell.ShellCmdListener;
import org.mobicents.protocols.ss7.management.shell.ShellCommand;
import org.mobicents.protocols.ss7.management.shell.ExitCommand;
import org.mobicents.protocols.ss7.management.shell.NoshutdownCommand;
import org.mobicents.protocols.ss7.management.shell.SS7Command;
import org.mobicents.protocols.ss7.management.shell.ShowCommand;
import org.mobicents.protocols.ss7.management.shell.ShutdownCommand;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ShellServer {

	private static final Logger logger = Logger.getLogger(ShellServer.class);

	private java.nio.channels.Selector selector = null;
	private ServerSocketChannel serverChannel = null;

	private InetSocketAddress address = null;

	private volatile boolean isActive = false;

	private boolean cliConnected = false;

	private ByteBuffer protocolHeader = ByteBuffer.allocateDirect(1);
	private ByteBuffer commandHeader = ByteBuffer.allocateDirect(1);
	private ByteBuffer body = ByteBuffer.allocateDirect(1024);

	private ByteBuffer[] buffers = { protocolHeader, commandHeader, body };

	private String localAddress;
	private int localPort;
	private ShellCmdListener cLICmdListener = null;

	// private long executionTime;
	// private long totalExecutionTime = 0l;
	// private long maxExecutionTime = 01;
	//	
	// private int tick = 0;
	// private int maxhappenedOn = tick;
	// private long maxBeginsAfter = 1000000;

	// private long[] executionTimes = new long[1000005];

	private AbstractCommand exitCmd = new ExitCommand();
	private AbstractCommand noShutDwnCmd = new NoshutdownCommand();
	private AbstractCommand shutDwnCmd = new ShutdownCommand();
	private AbstractCommand shwCmd = new ShowCommand();
	private AbstractCommand ss7Cmd = new SS7Command();

	public void setLocalAddress(String address) {
		this.localAddress = address;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalPort(int port) {
		this.localPort = port;
	}

	public int getLocalPort() {
		return localPort;
	}

	public ShellCmdListener getCliCmdListener() {
		return cLICmdListener;
	}

	public void setCliCmdListener(ShellCmdListener cLICmdListener) {
		this.cLICmdListener = cLICmdListener;
	}

	public void start() throws Exception {

		address = new InetSocketAddress(localAddress, localPort);

		selector = java.nio.channels.Selector.open();
		serverChannel = ServerSocketChannel.open();

		ServerSocket serverSocket = serverChannel.socket();

		serverSocket.bind(address);

		serverChannel.configureBlocking(false);

		serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		this.isActive = true;

		this.shwCmd.setCLICmdListener(this.cLICmdListener);
		this.ss7Cmd.setCLICmdListener(this.cLICmdListener);
		this.exitCmd.setCLICmdListener(this.cLICmdListener);
		this.noShutDwnCmd.setCLICmdListener(this.cLICmdListener);
		this.shutDwnCmd.setCLICmdListener(this.cLICmdListener);

		if (logger.isInfoEnabled()) {
			logger.info("Started SS7 Management at " + address);
		}

		this.perform();
	}

	public void stop() {
		if (logger.isInfoEnabled()) {
			logger.info("Stopped SS7 Management");
		}
		this.isActive = false;
	}

	public void perform() {
		
		if (this.isActive) {
			try {

				// TODO : How should server behave?
				// int n = selector.select();
				int n = selector.selectNow();
				
				// executionTime = System.nanoTime();

				if (n > 0) {
					Iterator<SelectionKey> it = selector.selectedKeys()
							.iterator();

					while (it.hasNext()) {
						SelectionKey key = it.next();

						// Is a new connection coming in?
						if (key.isAcceptable()) {
							ServerSocketChannel server = (ServerSocketChannel) key
									.channel();
							SocketChannel channel = server.accept();

							registerChannel(selector, channel,
									SelectionKey.OP_READ);
						}

						// is there data to read on this channel?
						if (key.isReadable()) {
							readDataFromSocket(key);
						}

						// remove key from selected set, it's been handled
						it.remove();
					}
				}// end of if
				// executionTime = (System.nanoTime() - executionTime);
				// executionTimes[tick] = executionTime;
				// totalExecutionTime = totalExecutionTime + executionTime;
				// if(tick> maxBeginsAfter && executionTime > maxExecutionTime
				// ){
				// maxExecutionTime = executionTime;
				// maxhappenedOn = tick;
				// }
				// tick++;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void registerChannel(java.nio.channels.Selector selector,
			SelectableChannel channel, int ops) throws Exception {
		if (channel == null) {
			return; // could happen
		}

		// set the new channel non-blocking
		channel.configureBlocking(false);

		// register it with the selector
		channel.register(selector, ops);

		this.cliConnected = true;
	}

	private void readDataFromSocket(SelectionKey key) throws Exception {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		long count;

		protocolHeader.clear();
		commandHeader.clear();
		body.clear(); // make buffer empty

		// loop while data available, channel is non-blocking
		while (socketChannel.isOpen()
				&& (count = socketChannel.read(buffers)) > 0) {
			protocolHeader.flip();
			commandHeader.flip();
			body.flip(); // make buffer readable

			int protocol = protocolHeader.get();

			if (protocol == ShellCommand.MTP.getCmdInt()) {
				int cmd = commandHeader.get();

				if (cmd == ShellCommand.SHOW.getCmdInt()) {
					shwCmd.decode(body);
					this.write(socketChannel);

				} else if (cmd == ShellCommand.SS7.getCmdInt()) {
					ss7Cmd.decode(body);
					this.write(socketChannel);

				} else if (cmd == ShellCommand.SHUTDOWN.getCmdInt()) {
					shutDwnCmd.decode(body);
					this.write(socketChannel);

				} else if (cmd == ShellCommand.NOSHUTDOWN.getCmdInt()) {
					noShutDwnCmd.decode(body);
					this.write(socketChannel);

				} else {
					logger.error(String.format("Unknown Command %s ", cmd));
				}
			} else if (protocol == ShellCommand.EXIT.getCmdInt()) {				
				exitCmd.decode(body);
				this.write(socketChannel);
				socketChannel.close();
			} else {
				logger.error(String.format("Unknown Protocol %d ", protocol));
			}

		}
	}

	private void write(SocketChannel socketChannel) throws Exception {
		body.flip();
		socketChannel.write(body);
	}

	public boolean isActive() {
		return isActive;
	}

	// public long getTotalExecutionTime(){
	// return this.totalExecutionTime;
	// }
	//
	// public long getMaxExecutionTime() {
	// return maxExecutionTime;
	// }
	//
	// public long getMaxhappenedOn() {
	// return maxhappenedOn;
	// }

	// public long[] getExecutionTimes() {
	// return executionTimes;
	// }

	// public int getTick() {
	// return tick;
	// }

}
