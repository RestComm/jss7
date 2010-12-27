/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.ss7.management.shell;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

import org.mobicents.ss7.management.console.Version;

/**
 * 
 * @author amit bhayani
 * 
 */
public class Shell {

	// .read() should block for max 10 seconds
	public static final int SO_TIMEOUT = 10 * 1000;

	public static final String EXIT = "exit";

	Version version = Version.instance;

	public final String WELCOME_MESSAGE = version.toString()
			+ "\n"
			+ "This is free software, with components licensed under the GNU General Public License\n"
			+ "version 2 and other licenses. For further details visit http://mobicents.org\n"
			+ "=======================================================================================";

	public static final String CONNECTED_MESSAGE = "Connected to %s currently running on %s";

	public static final String cliPrefix = "Mobicents-SS7*CLI>";

	public String serverIp = "127.0.0.1";
	public int serverPort = 1110;

	private java.nio.channels.Selector selector = null;

	private SocketChannel socketChannel = null;
	private InetSocketAddress address = null;
	private SelectionKey selectionKey = null;

	private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);

	public static void main(String args[]) {
		Shell ss7Cli = new Shell();
		ss7Cli.beginCli(args);
	}

	private void showCliHelp() {
		System.out.println(version.toString());
		System.out.println("Usage: SS7 [OPTIONS]");
		System.out.println("Valid Options");
		System.out.println("-v           Display version number and exit");
		System.out.println("-h           This help screen");
		System.out
				.println(String
						.format(
								"-s <socket>  The Server Socket where this CLI connects. By default it assumes server port is %s "
										+ "\n             If this is not true, pass socket:port ",
								serverPort));
	}

	private void showSS7Help() {
		System.out
				.println("                                                                                    help        This help screen");
		System.out
				.println("                                           show ss7 linkset [linkset-name | statistics ]        Display linkset information");
		System.out
				.println("                                  ss7 addlinkset linkset-name {dahdi | dialogic | m3ua }        Add a new linkset");
		System.out
				.println("ss7 linkset linkset-name network-indicator {international | national | reserved | spare}        Configure the network indicator for a linkset");
		System.out
				.println("                                            ss7 linkset linkset-name local-pc point-code        configure the local point code for a linkset");
		System.out
				.println("                                         ss7 linkset linkset-name adjacent-pc point-code        configure the adjacent point code for a linkset");
		System.out
				.println("                        ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
		System.out
				.println("                                                              ss7 deletelinkset link-set        delete linkset");
		System.out
				.println("                                              ss7 linkset linkset-name addlink link-name        add the link to linkset");
		System.out
				.println("                                       ss7 linkset linkset-name link link-name span span        configure the span for link");
		System.out
				.println("                                 ss7 linkset linkset-name link link-name channel channel        configure the channel for link");
		System.out
				.println("                                       ss7 linkset linkset-name link link-name code code        configure the code for link");
		System.out
				.println("                                           ss7 linkset linkset-name deletelink link-name        delete link");
		System.out
				.println("                                                       shutdown ss7 linkset linkset-name        disable a linkset");
		System.out
				.println("                                                     noshutdown ss7 linkset linkset-name        reactivate a disabled linkset");
		System.out
				.println("                                        shutdown ss7 linkset linkset-name link link-name        disable a link");
		System.out
				.println("                                      noshutdown ss7 linkset linkset-name link link-name        reactivate a link");
		System.out
				.println("                                                      ss7 inhibit linkset-name link-name        inhibit a link");
		System.out
				.println("                                                    ss7 uninhibit linkset-name link-name        uninhibit a link");
		System.out
				.println("                                                                                    exit        gracefully exit from CLI");
	}

	public void beginCli(String args[]) {
		try {
			if (args == null || args.length == 0) {
				this.showCliHelp();
				System.exit(0);
			}

			String cmd = args[0];

			if (cmd.compareTo("-v") == 0) {
				System.out.println(version.toString());
				System.exit(0);
			}

			if (cmd.compareTo("-h") == 0) {
				this.showCliHelp();
				System.exit(0);
			}

			if (cmd.compareTo("-s") == 0) {

				if (args.length < 2) {
					System.out.println("You need an argument for option s");
					System.exit(0);
				} else {
					String[] serverArgs = args[1].split(":");
					serverIp = serverArgs[0];
					if (serverArgs.length == 2) {
						try {
							serverPort = Integer.parseInt(serverArgs[1]);
						} catch (NumberFormatException nfe) {
							System.out
									.println("Wrong argument passed for option s");
							System.exit(0);
						}
					}
				}
				System.out.println(WELCOME_MESSAGE);

				address = new InetSocketAddress(serverIp, serverPort);
				selector = java.nio.channels.Selector.open();
				socketChannel = SocketChannel.open();
				socketChannel.configureBlocking(false);

				selectionKey = socketChannel.register(selector,
						SelectionKey.OP_READ);

				socketChannel.connect(address);

				// Finish connect first
				while (!socketChannel.finishConnect()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// TODO : The VERSION here should be of Server Side SS7, may be
				// Server Side SS7 sends
				// back the ver once connected?
				System.out.println(String.format(CONNECTED_MESSAGE, version
						.toString(), serverIp));
				System.out.print(cliPrefix);
				Scanner in = new Scanner(System.in);

				String data = in.nextLine();

				String[] ss7Commands = data.split(" ");
				while (ss7Commands[0].compareTo(EXIT) != 0) {

					if (ss7Commands == null || ss7Commands.length == 0) {
						showSS7Help();
					} else if (ss7Commands[0].compareTo("show") == 0) {
						AbstractCommand shwCmd = new ShowCommand(ss7Commands);
						this.execute(shwCmd);

					} else if (ss7Commands[0].compareTo("ss7") == 0) {
						AbstractCommand ss7Cmd = new SS7Command(ss7Commands);
						this.execute(ss7Cmd);
					} else if (ss7Commands[0].compareTo("shutdown") == 0) {
						AbstractCommand shutDwnCmd = new ShutdownCommand(
								ss7Commands);
						this.execute(shutDwnCmd);
					} else if (ss7Commands[0].compareTo("noshutdown") == 0) {
						AbstractCommand noShutDwnCmd = new NoshutdownCommand(
								ss7Commands);
						this.execute(noShutDwnCmd);
					} else if (ss7Commands[0].compareTo("help") == 0) {
						showSS7Help();
					} else {
						System.out
								.println(String
										.format(
												"Invalid command %s. execute help command to know valid options",
												ss7Commands[0]));
					}

					System.out.print(cliPrefix);
					data = in.nextLine();
					ss7Commands = data.split(" ");

				}// end of while

				AbstractCommand exitCmd = new ExitCommand(ss7Commands);
				this.execute(exitCmd);

			} else {
				System.out.println(String.format("Invalid option %s", cmd));

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (this.socketChannel != null) {
				try {
					this.socketChannel.close();
				} catch (IOException e) {
				}
			}
		}// finally
	}

	protected void execute(AbstractCommand cmd) {
		try {
			byteBuffer.clear();
			if (cmd.encode(byteBuffer)) {
				byteBuffer.flip();

				socketChannel.write(byteBuffer);

				int n = selector.select(5000);

				// System.out.println("n=" + n);

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
								System.out.println(new String(buff));
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
