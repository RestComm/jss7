package org.mobicents.ss7.management.transceiver;

import java.nio.channels.SelectionKey;

public class ChannelSelectionKey {
	private ShellSelectableChannel shellSelectableChannel = null;
	private SelectionKey key = null;

	protected ChannelSelectionKey(ShellSelectableChannel shellSelectableChannel,
			SelectionKey k) {
		this.shellSelectableChannel = shellSelectableChannel;
		this.key = k;
	}

	public ShellSelectableChannel channel() {
		return shellSelectableChannel;
	}

	public boolean isAcceptable() {
		return key.isAcceptable();
	}

	public boolean isReadable() {
		return ((ShellChannel) shellSelectableChannel).isReadable();
	}

	public boolean isWritable() {
		return ((ShellChannel) shellSelectableChannel).isWritable();
	}

	public void cancel() {
		key.cancel();
	}
}
