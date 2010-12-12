package org.mobicents.protocols.ss7.mtp.cli;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ShowCommand extends AbstractCommand {

	public ShowCommand() {
	}

	ShowCommand(String[] ss7Comands) {
		super(ss7Comands);
	}

	private void showShowCmdHelp() {
		System.out
				.println("show ss7 linkset [linkset-name | statistics ]        Display linkset information");
	}

	public boolean encode(ByteBuffer byteBuffer) {

		if (ss7Commands.length < 3 || ss7Commands.length > 5) {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}

		if (ss7Commands[1].compareTo("ss7") != 0) {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}

		if (ss7Commands[2].compareTo("linkset") != 0) {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}
		// Header
		byteBuffer.put((byte) CliCmdEnum.SHOW.getCmdInt());

		// Body
		byteBuffer.put((byte) CliCmdEnum.SS7.getCmdInt());
		byteBuffer.put(ZERO_LENGTH);
		byteBuffer.put((byte) CliCmdEnum.LINKSET.getCmdInt());

		if (ss7Commands.length >= 4) {
			if (ss7Commands[3].compareTo(CliCmdEnum.STATISTICS.getCmdStr()) == 0) {

				if (ss7Commands.length == 5) {
					System.out.println("Invalid command");
					this.showShowCmdHelp();
					return false;
				}

				byteBuffer.put(ZERO_LENGTH);
				byteBuffer.put((byte) CliCmdEnum.STATISTICS.getCmdInt());

				return true;
			} else {
				byte[] linkSetName = ss7Commands[3].getBytes();
				byteBuffer.put((byte) linkSetName.length);
				byteBuffer.put(linkSetName);
			}
		}

		if (ss7Commands.length == 5) {
			if (ss7Commands[4].compareTo(CliCmdEnum.STATISTICS.getCmdStr()) == 0) {
				byteBuffer.put((byte) CliCmdEnum.STATISTICS.getCmdInt());
			} else {
				System.out.println("Invalid command");
				this.showShowCmdHelp();
				return false;
			}

		}

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

					boolean statistics = false;

					if (hasMoreBytes(byteBuffer)) {
						cmdLength = byteBuffer.get();

						if (cmdLength > 0) {
							// linkSetName = getString(byteBuffer, 0,
							// cmdLength);
							while (linksetName.length() < cmdLength) {
								linksetName.append((char)byteBuffer.get());
							}
						}

						if (hasMoreBytes(byteBuffer)) {
							// If it has more Byte we know its Statistics
							statistics = true;
						}

					}
					// Call the listener to take appropriate action
					// clean buffer before that
					byteBuffer.clear();
					this.cLICmdListener.showLinkSet(statistics, linksetName,
							byteBuffer);

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
