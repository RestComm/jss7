package org.mobicents.protocols.ss7.management.shell;

import java.nio.ByteBuffer;

/**
 * 
 * @author amit bhayani
 * 
 */
public class ExitCommand extends AbstractCommand {

	public static final String BYE = "Bye";
	public static final byte[] BYE_BYTES = BYE.getBytes();

	public ExitCommand() {
	}

	public ExitCommand(String[] ss7Commands) {
		super(ss7Commands);
	}

	@Override
	public boolean encode(ByteBuffer byteBuffer) {
		byteBuffer.put((byte) ShellCommand.EXIT.getCmdInt());
		return true;
	}

	@Override
	public void decode(ByteBuffer byteBuffer) {
		super.decode(byteBuffer);
		byteBuffer.clear();
		byteBuffer.put(BYE_BYTES);
	}

}
