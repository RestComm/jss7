package org.mobicents.protocols.ss7.management.shell;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * 
 * @author amit bhayani
 * 
 */
public class NoshutdownCommand extends AbstractCommand {

	public NoshutdownCommand() {
	}

	public NoshutdownCommand(String[] ss7Commands) {
		super(ss7Commands);
	}

	private void showShowCmdHelp() {
		System.out
				.println("noshutdown ss7 linkset linkset-name        reactivate a disabled linkset");
		System.out
				.println("      noshutdown ss7 link link-name        reactivate a link");

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
		byteBuffer.put((byte) CmdEnum.NOSHUTDOWN.getCmdInt());

		// Body
		byteBuffer.put((byte) CmdEnum.SS7.getCmdInt());
		byteBuffer.put(ZERO_LENGTH);

		if (ss7Commands[2].compareTo("linkset") == 0) {
			byteBuffer.put((byte) CmdEnum.LINKSET.getCmdInt());
		} else if (ss7Commands[2].compareTo("link") == 0) {
			byteBuffer.put((byte) CmdEnum.LINK.getCmdInt());
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

			if (cmd == CmdEnum.SS7.getCmdInt()) {
				// ZERO_LENGTH
				int cmdLength = byteBuffer.get();

				// Next command
				cmd = byteBuffer.get();
				if (cmd == CmdEnum.LINKSET.getCmdInt()) {
					cmdLength = byteBuffer.get();
					//name = getString(byteBuffer, 0, cmdLength);
					while (linksetName.length() < cmdLength) {
						linksetName.append((char)byteBuffer.get());
					}

					this.cLICmdListener.noshutdownLinkSet(linksetName, byteBuffer);

				} else if (cmd == CmdEnum.LINK.getCmdInt()) {
					cmdLength = byteBuffer.get();
					//name = getString(byteBuffer, 0, cmdLength);
					while (linkName.length() < cmdLength) {
						linkName.append((char)byteBuffer.get());
					}

					this.cLICmdListener.noshutdownLink(linkName, byteBuffer);

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
