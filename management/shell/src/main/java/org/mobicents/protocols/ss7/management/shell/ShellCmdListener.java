package org.mobicents.protocols.ss7.management.shell;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface ShellCmdListener {

	public void showLinkSet(boolean statistics, TextBuilder linksetName,
			ByteBuffer byteBuffer);

	public void shutdownLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer);

	public void shutdownLink(TextBuilder linksetName, TextBuilder linkName, ByteBuffer byteBuffer);

	public void noshutdownLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer);

	public void noshutdownLink(TextBuilder linksetName, TextBuilder linkName, ByteBuffer byteBuffer);

	public void addLinkSet(TextBuilder linksetName, int type, ByteBuffer byteBuffer);

	public void deleteLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer);

	public void deleteLink(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer);

	public void networkIndicator(TextBuilder linksetName, ShellCommand networkInd,
			ByteBuffer byteBuffer);

	public void localPointCode(TextBuilder linksetName, int localPC,
			ByteBuffer byteBuffer);

	public void adjacentPointCode(TextBuilder linksetName,
			int adjacentPC, ByteBuffer byteBuffer);

	public void localIpPort(TextBuilder linksetName, TextBuilder localIp,
			int localPort, ByteBuffer byteBuffer);

	public void addLink(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer);

	public void span(TextBuilder linksetName, TextBuilder linkName, int span,
			ByteBuffer byteBuffer);

	public void channel(TextBuilder linksetName, TextBuilder linkName,
			int channel, ByteBuffer byteBuffer);

	public void code(TextBuilder linksetName, TextBuilder linkName, int code,
			ByteBuffer byteBuffer);

	public void inhibit(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer);

	public void uninhibit(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer);

}
