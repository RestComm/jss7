package org.mobicents.protocols.ss7.mtp.cli;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ShutdownCommand extends AbstractCommand {

	public ShutdownCommand() {
	}

	public ShutdownCommand(String[] ss7Commands) {
		super(ss7Commands);
	}

	private void showShowCmdHelp() {
		System.out
				.println("shutdown ss7 linkset linkset-name        disable a linkset");
		System.out
				.println("      shutdown ss7 link link-name        disable a link");

	}

	@Override
	public boolean encode(ByteBuffer byteBuffer) {
		if (ss7Commands.length != 4) {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}

		if (ss7Commands[1].compareTo("ss7") != 0) {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}

		// Header
		byteBuffer.put((byte) CliCmdEnum.SHUTDOWN.getCmdInt());

		// Body
		byteBuffer.put((byte) CliCmdEnum.SS7.getCmdInt());
		byteBuffer.put(ZERO_LENGTH);

		if (ss7Commands[2].compareTo("linkset") == 0) {
			byteBuffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());
		} else if (ss7Commands[2].compareTo("link") == 0) {
			byteBuffer.put((byte) CliCmdEnum.LINK.getCmdInt());
		} else {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}

		// Put the length and name for linkset/link
		byteBuffer.put((byte) ss7Commands[3].length());
		byteBuffer.put(ss7Commands[3].getBytes());

		return true;
	}

	@Override
	public void decode(ByteBuffer byteBuffer) {
		super.decode(byteBuffer);
		try {
			int cmd = byteBuffer.get();

			if (cmd == CliCmdEnum.SS7.getCmdInt()) {
				// ZERO_LENGTH
				int cmdLength = byteBuffer.get();

				// Next command
				cmd = byteBuffer.get();
				if (cmd == CliCmdEnum.LINKSET.getCmdInt()) {
					cmdLength = byteBuffer.get();
					//name = getString(byteBuffer, 0, cmdLength);
					while (linksetName.length() < cmdLength) {
						linksetName.append((char)byteBuffer.get());
					}

					this.cLICmdListener.shutdownLinkSet(linksetName, byteBuffer);

				} else if (cmd == CliCmdEnum.LINK.getCmdInt()) {
					cmdLength = byteBuffer.get();
					//name = getString(byteBuffer, 0, cmdLength);
					
					while (linkName.length() < cmdLength) {
						linkName.append((char)byteBuffer.get());
					}

					this.cLICmdListener.shutdownLink(linkName, byteBuffer);

				} else {
					sendErrorMsg(byteBuffer);
					return;
				}

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
