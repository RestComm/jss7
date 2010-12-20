package org.mobicents.protocols.ss7.management.shell;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SS7Command extends AbstractCommand {

	public SS7Command() {
	}

	protected SS7Command(String[] ss7Commands) {
		super(ss7Commands);
	}

	private void showShowCmdHelp() {
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
				.println("                                  ss7 linkset linkset-name link link-name channel channel       configure the channel for link");
		System.out
				.println("                                        ss7 linkset linkset-name link link-name code code       configure the code for link");
		System.out
				.println("                                            ss7 linkset linkset-name deletelink link-name        delete link");
		System.out
				.println("                                                      ss7 inhibit linkset-name link-name        inhibit a link");
		System.out
				.println("                                                    ss7 uninhibit linkset-name link-name        uninhibit a link");

	}

	@Override
	public boolean encode(ByteBuffer byteBuffer) {
		if (ss7Commands.length < 3 || ss7Commands.length > 7) {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}

		// MTP Header
		byteBuffer.put((byte) ShellCommand.MTP.getCmdInt());

		// Header
		byteBuffer.put((byte) ShellCommand.SS7.getCmdInt());

		if (ss7Commands[1].compareTo(ShellCommand.ADDLINKSET.getCmdStr()) == 0) {
			if (ss7Commands.length != 4) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 addlinkset linkset-name {dahdi | dialogic | m3ua }       Add a new linkset");
				return false;
			} else if (!(ss7Commands[3].compareTo(ShellCommand.LINKSET_DAHDI
					.getCmdStr()) == 0
					|| ss7Commands[3].compareTo(ShellCommand.LINKSET_DIALOGIC
							.getCmdStr()) == 0 || ss7Commands[3]
					.compareTo(ShellCommand.LINKSET_M3UA.getCmdStr()) == 0)) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 addlinkset linkset-name {dahdi | dialogic | m3ua }       Add a new linkset");
			}

			// Body
			byteBuffer.put((byte) ShellCommand.ADDLINKSET.getCmdInt());
			byteBuffer.put((byte) ss7Commands[2].length());
			byteBuffer.put(ss7Commands[2].getBytes());

			byteBuffer.put((byte) (ShellCommand.getCommand(ss7Commands[3]))
					.getCmdInt());
			return true;

		} else if (ss7Commands[1].compareTo(ShellCommand.LINKSET.getCmdStr()) == 0) {

			// Body
			byteBuffer.put((byte) ShellCommand.LINKSET.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[2].length()); // length
			byteBuffer.put(ss7Commands[2].getBytes()); // value

			if (ss7Commands[3].compareTo(ShellCommand.NETWORK_INDICATOR
					.getCmdStr()) == 0) {
				if (ss7Commands.length != 5) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name network-indicator {international | national | reserved | spare}        Configure the network indicator for a linkset");
					return false;
				} else if (!(ss7Commands[4]
						.compareTo(ShellCommand.NETWORK_INDICATOR_INT
								.getCmdStr()) == 0
						|| ss7Commands[4]
								.compareTo(ShellCommand.NETWORK_INDICATOR_NAT
										.getCmdStr()) == 0
						|| ss7Commands[4]
								.compareTo(ShellCommand.NETWORK_INDICATOR_RES
										.getCmdStr()) == 0 || ss7Commands[4]
						.compareTo(ShellCommand.NETWORK_INDICATOR_SPA
								.getCmdStr()) == 0)) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name network-indicator {international | national | reserved | spare}        Configure the network indicator for a linkset");

				}

				byteBuffer.put((byte) ShellCommand.NETWORK_INDICATOR
						.getCmdInt()); // command
				byteBuffer.put(ZERO_LENGTH); // length

				byteBuffer.put((byte) ShellCommand.getCommand(ss7Commands[4])
						.getCmdInt()); // command
				return true;

			} else if (ss7Commands[3].compareTo(ShellCommand.LOCAL_PC
					.getCmdStr()) == 0) {

				if (ss7Commands.length != 5) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name local-pc point-code        configure the local point code for a linkset");
					return false;
				}

				int pointCode = Utils.validatePointCode(ss7Commands[4]);
				if (pointCode == -1) {
					System.out.println("Invalid Point Code");
					System.out
							.println("ss7 linkset linkset-name local-pc point-code        configure the local point code for a linkset");
					return false;
				}

				byteBuffer.put((byte) ShellCommand.LOCAL_PC.getCmdInt()); // command
				byteBuffer.put((byte) 0x03); // length
				byteBuffer.put((byte) ((pointCode & 0x00ff0000) >> 16)); // value
				byteBuffer.put((byte) ((pointCode & 0x0000ff00) >> 8)); // value
				byteBuffer.put((byte) (pointCode & 0x000000ff)); // value

				return true;

			} else if (ss7Commands[3].compareTo(ShellCommand.ADJACENT_PC
					.getCmdStr()) == 0) {
				if (ss7Commands.length != 5) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name adjacent-pc point-code        configure the adjacent point code for a linkset");
					return false;
				}

				int pointCode = Utils.validatePointCode(ss7Commands[4]);
				if (pointCode == -1) {
					System.out.println("Invalid Point Code");
					System.out
							.println("ss7 linkset linkset-name local-pc point-code        configure the local point code for a linkset");
					return false;
				}

				byteBuffer.put((byte) ShellCommand.ADJACENT_PC.getCmdInt()); // command
				byteBuffer.put((byte) 0x03); // length
				byteBuffer.put((byte) ((pointCode & 0x00ff0000) >> 16)); // value
				byteBuffer.put((byte) ((pointCode & 0x0000ff00) >> 8)); // value
				byteBuffer.put((byte) (pointCode & 0x000000ff)); // value

				return true;

			} else if (ss7Commands[3].compareTo(ShellCommand.LOCAL_IP
					.getCmdStr()) == 0) {

				if (ss7Commands.length != 7) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
					return false;
				} else if (ss7Commands[5].compareTo(ShellCommand.LOCAL_PORT
						.getCmdStr()) != 0) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");

				} else if (!Utils.validateIp(ss7Commands[4])) {
					System.out.println("Invalid IP Address");
					System.out
							.println("ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
				}

				int port = Utils.validatePort(ss7Commands[6]);
				if (port == -1) {
					System.out.println("Invalid Port");
					System.out
							.println("ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
					return false;
				}

				byteBuffer.put((byte) ShellCommand.LOCAL_IP.getCmdInt()); // command
				byteBuffer.put((byte) ss7Commands[4].length()); // length
				byteBuffer.put(ss7Commands[4].getBytes()); // value

				byteBuffer.put((byte) ShellCommand.LOCAL_PORT.getCmdInt()); // command
				byteBuffer.put((byte) 0x02); // length
				byteBuffer.put((byte) ((port & 0x0000ff00) >> 8)); // value
				byteBuffer.put((byte) (port & 0x000000ff)); // value

				return true;

			} else if (ss7Commands[3].compareTo(ShellCommand.ADDLINK
					.getCmdStr()) == 0) {
				if (ss7Commands.length != 5) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name addlink link-name        add the link to linkset");
					return false;
				}

				byteBuffer.put((byte) ShellCommand.ADDLINK.getCmdInt()); // command
				byteBuffer.put((byte) ss7Commands[4].length()); // length
				byteBuffer.put(ss7Commands[4].getBytes()); // value

				return true;

			} else if (ss7Commands[3].compareTo(ShellCommand.LINK.getCmdStr()) == 0) {
				if (ss7Commands.length != 7) {
					System.out.println("Invalid command");
					System.out
							.println("      ss7 linkset linkset-name link link-name span span        configure the span for link");
					System.out
							.println("ss7 linkset linkset-name link link-name channel channel       configure the channel for link");
					System.out
							.println("      ss7 linkset linkset-name link link-name code code       configure the code for link");

					return false;
				}

				// Body
				byteBuffer.put((byte) ShellCommand.LINK.getCmdInt()); // command
				byteBuffer.put((byte) ss7Commands[4].length()); // length
				byteBuffer.put(ss7Commands[4].getBytes()); // value

				if (ss7Commands[5].compareTo(ShellCommand.SPAN.getCmdStr()) == 0) {
					int span = Utils.validateSpan(ss7Commands[6]);
					if (span == -1) {
						System.out.println("Invalid Span");
						System.out
								.println("ss7 linkset linkset-name link link-name span span        configure the span for link");
						return false;
					}

					// Body
					byteBuffer.put((byte) ShellCommand.SPAN.getCmdInt()); // command
					byteBuffer.put((byte) 0x01); // length
					byteBuffer.put((byte) span); // value
					return true;

				} else if (ss7Commands[5].compareTo(ShellCommand.CHANNEL
						.getCmdStr()) == 0) {
					int channel = Utils.validateChannel(ss7Commands[6]);
					if (channel == -1) {
						System.out.println("Invalid channel");
						System.out
								.println("ss7 linkset linkset-name link link-name channel channel        configure the channel for link");
						return false;
					}

					// Body
					byteBuffer.put((byte) ShellCommand.CHANNEL.getCmdInt()); // command
					byteBuffer.put((byte) 0x01); // length
					byteBuffer.put((byte) channel); // value

					return true;
				} else if (ss7Commands[5].compareTo(ShellCommand.CODE
						.getCmdStr()) == 0) {
					int code = Utils.validateCode(ss7Commands[6]);
					if (code == -1) {
						System.out.println("Invalid code");
						System.out
								.println("ss7 linkset linkset-name link link-name code code        configure the code for link");
						return false;
					}

					// Body
					byteBuffer.put((byte) ShellCommand.CODE.getCmdInt()); // command
					byteBuffer.put((byte) 0x02); // length
					byteBuffer.put((byte) ((code & 0x0000ff00) >> 8)); // value
					byteBuffer.put((byte) (code & 0x000000ff)); // value

					return true;
				} else {
					System.out.println("Invalid command");
					System.out
							.println("      ss7 linkset linkset-name link link-name span span        configure the span for link");
					System.out
							.println("ss7 link linkset linkset-name link-name channel channel        configure the channel for link");
					System.out
							.println("      ss7 linkset linkset-name link link-name code code        configure the code for link");

					return false;
				}
			} else if (ss7Commands[3].compareTo(ShellCommand.DELETELINK
					.getCmdStr()) == 0) {
				if (ss7Commands.length != 5) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name deletelink link-name        delete link");
					return false;
				}

				// Body
				byteBuffer.put((byte) ShellCommand.DELETELINK.getCmdInt());
				byteBuffer.put((byte) ss7Commands[4].length());
				byteBuffer.put(ss7Commands[4].getBytes());
				return true;

			} else {
				System.out.println("Invalid command");
				System.out
						.println("ss7 linkset linkset-name network-indicator {international | national | reserved | spare}        Configure the network indicator for a linkset");
				System.out
						.println("                                            ss7 linkset linkset-name local-pc point-code        configure the local point code for a linkset");
				System.out
						.println("                                         ss7 linkset linkset-name adjacent-pc point-code        configure the adjacent point code for a linkset");
				System.out
						.println("                        ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
				System.out
						.println("                                              ss7 linkset linkset-name addlink link-name        add the link to linkset");
			}
			return false;

		} else if (ss7Commands[1].compareTo(ShellCommand.DELETELINKSET
				.getCmdStr()) == 0) {
			if (ss7Commands.length != 3) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 deletelinkset link-set        delete linkset");
				return false;
			}

			// Body
			byteBuffer.put((byte) ShellCommand.DELETELINKSET.getCmdInt());
			byteBuffer.put((byte) ss7Commands[2].length());
			byteBuffer.put(ss7Commands[2].getBytes());
			return true;

		} else if (ss7Commands[1].compareTo(ShellCommand.INHIBIT.getCmdStr()) == 0) {
			if (ss7Commands.length != 4) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 inhibit linkset-name link-name        inhibit a link");
				return false;
			}

			// Body
			byteBuffer.put((byte) ShellCommand.INHIBIT.getCmdInt()); // command
			byteBuffer.put(ZERO_LENGTH); // length

			byteBuffer.put((byte) ShellCommand.LINKSET.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[2].length()); // length
			byteBuffer.put(ss7Commands[2].getBytes()); // value

			byteBuffer.put((byte) ShellCommand.LINK.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[3].length()); // length
			byteBuffer.put(ss7Commands[3].getBytes()); // value

			return true;

		} else if (ss7Commands[1].compareTo(ShellCommand.UNINHIBIT.getCmdStr()) == 0) {
			if (ss7Commands.length != 4) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 uninhibit linkset-name link-name        uninhibit a link");
				return false;
			}

			// Body
			byteBuffer.put((byte) ShellCommand.UNINHIBIT.getCmdInt()); // command
			byteBuffer.put(ZERO_LENGTH); // length

			byteBuffer.put((byte) ShellCommand.LINKSET.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[2].length()); // length
			byteBuffer.put(ss7Commands[2].getBytes()); // value

			byteBuffer.put((byte) ShellCommand.LINK.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[3].length()); // length
			byteBuffer.put(ss7Commands[3].getBytes()); // value

			return true;
		} else {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}
		// return false;
	}

	@Override
	public void decode(ByteBuffer byteBuffer) {
		super.decode(byteBuffer);
		try {

			int cmd = byteBuffer.get();
			int cmdLength;
			if (cmd == ShellCommand.ADDLINKSET.getCmdInt()) {
				cmdLength = byteBuffer.get();
				while (linksetName.length() < cmdLength) {
					linksetName.append((char) byteBuffer.get());
				}

				int type = byteBuffer.get();
				byteBuffer.clear();
				this.cLICmdListener.addLinkSet(linksetName, type, byteBuffer);

			} else if (cmd == ShellCommand.LINKSET.getCmdInt()) {
				cmdLength = byteBuffer.get();
				while (linksetName.length() < cmdLength) {
					linksetName.append((char) byteBuffer.get());
				}
				// String linksetName = getString(byteBuffer, 0, cmdLength);

				// Net Cmd
				cmd = byteBuffer.get();
				if (cmd == ShellCommand.NETWORK_INDICATOR.getCmdInt()) {
					cmdLength = byteBuffer.get();// zero

					cmd = byteBuffer.get();// international | national |
					// reserved | spare
					ShellCommand networkInd = ShellCommand.getCommand(cmd);
					if (networkInd == null) {
						sendErrorMsg(byteBuffer);
						return;
					}
					byteBuffer.clear();
					this.cLICmdListener.networkIndicator(linksetName,
							networkInd, byteBuffer);
				} else if (cmd == ShellCommand.LOCAL_PC.getCmdInt()) {
					cmdLength = byteBuffer.get();

					pointCode = ((byteBuffer.get() << 16)
							| (byteBuffer.get() << 8) | byteBuffer.get());
					byteBuffer.clear();
					this.cLICmdListener.localPointCode(linksetName, pointCode,
							byteBuffer);

				} else if (cmd == ShellCommand.ADJACENT_PC.getCmdInt()) {
					cmdLength = byteBuffer.get();
					pointCode = ((byteBuffer.get() << 16)
							| (byteBuffer.get() << 8) | byteBuffer.get());
					byteBuffer.clear();
					this.cLICmdListener.adjacentPointCode(linksetName,
							pointCode, byteBuffer);

				} else if (cmd == ShellCommand.LOCAL_IP.getCmdInt()) {
					cmdLength = byteBuffer.get();
					while (localIp.length() < cmdLength) {
						localIp.append((char) byteBuffer.get());
					}

					// String localIp = getString(byteBuffer, 0, cmdLength);

					cmd = byteBuffer.get(); // LOCAL_PORT
					cmdLength = byteBuffer.get();
					int localPort = ((byteBuffer.get() << 8) | byteBuffer.get());
					byteBuffer.clear();
					this.cLICmdListener.localIpPort(linksetName, localIp,
							localPort, byteBuffer);

				} else if (cmd == ShellCommand.ADDLINK.getCmdInt()) {
					cmdLength = byteBuffer.get();
					while (linkName.length() < cmdLength) {
						linkName.append((char) byteBuffer.get());
					}
					byteBuffer.clear();
					this.cLICmdListener.addLink(linksetName, linkName,
							byteBuffer);
				} else if (cmd == ShellCommand.LINK.getCmdInt()) {
					cmdLength = byteBuffer.get();
					while (linkName.length() < cmdLength) {
						linkName.append((char) byteBuffer.get());
					}
					// String linkName = getString(byteBuffer, 0, cmdLength);

					// Net Cmd
					cmd = byteBuffer.get();
					if (cmd == ShellCommand.SPAN.getCmdInt()) {
						cmdLength = byteBuffer.get();
						int span = byteBuffer.get();
						byteBuffer.clear();
						this.cLICmdListener.span(linksetName, linkName, span,
								byteBuffer);
					} else if (cmd == ShellCommand.CHANNEL.getCmdInt()) {
						cmdLength = byteBuffer.get();
						int channel = byteBuffer.get();
						byteBuffer.clear();
						this.cLICmdListener.channel(linksetName, linkName,
								channel, byteBuffer);
					} else if (cmd == ShellCommand.CODE.getCmdInt()) {
						cmdLength = byteBuffer.get();
						int code = ((byteBuffer.get() << 8) | byteBuffer.get());
						byteBuffer.clear();
						this.cLICmdListener.code(linksetName, linkName, code,
								byteBuffer);
					} else {
						sendErrorMsg(byteBuffer);
						return;
					}
				} else if (cmd == ShellCommand.DELETELINK.getCmdInt()) {
					cmdLength = byteBuffer.get();
					while (linkName.length() < cmdLength) {
						linkName.append((char) byteBuffer.get());
					}
					byteBuffer.clear();
					this.cLICmdListener.deleteLink(linksetName, linkName,
							byteBuffer);
				} else {
					sendErrorMsg(byteBuffer);
					return;
				}

			} else if (cmd == ShellCommand.DELETELINKSET.getCmdInt()) {
				cmdLength = byteBuffer.get();
				// String name = getString(byteBuffer, 0, cmdLength);
				while (linksetName.length() < cmdLength) {
					linksetName.append((char) byteBuffer.get());
				}
				byteBuffer.clear();				
				this.cLICmdListener.deleteLinkSet(linksetName, byteBuffer);
			} else if (cmd == ShellCommand.INHIBIT.getCmdInt()) {
				cmdLength = byteBuffer.get();// zero

				cmd = byteBuffer.get();// LINKSET
				cmdLength = byteBuffer.get();
				while (linksetName.length() < cmdLength) {
					linksetName.append((char) byteBuffer.get());
				}
				// String linksetName = getString(byteBuffer, 0, cmdLength);

				cmd = byteBuffer.get();// LINK
				cmdLength = byteBuffer.get();
				while (linkName.length() < cmdLength) {
					linkName.append((char) byteBuffer.get());
				}
				byteBuffer.clear();
				this.cLICmdListener.inhibit(linksetName, linkName, byteBuffer);

			} else if (cmd == ShellCommand.UNINHIBIT.getCmdInt()) {
				cmdLength = byteBuffer.get();// zero

				cmd = byteBuffer.get();// LINKSET
				cmdLength = byteBuffer.get();
				while (linksetName.length() < cmdLength) {
					linksetName.append((char) byteBuffer.get());
				}
				// String linksetName = getString(byteBuffer, 0, cmdLength);

				cmd = byteBuffer.get();// LINK
				cmdLength = byteBuffer.get();
				// String linkName = getString(byteBuffer, 0, cmdLength);
				while (linkName.length() < cmdLength) {
					linkName.append((char) byteBuffer.get());
				}
				byteBuffer.clear();
				this.cLICmdListener
						.uninhibit(linksetName, linkName, byteBuffer);
			} else {
				sendErrorMsg(byteBuffer);
				return;
			}
		} catch (BufferUnderflowException bufe) {
			// ideally should never happen
			sendErrorMsg(byteBuffer);
		}
	}

}
