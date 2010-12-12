package org.mobicents.protocols.ss7.mtp.cli;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface CLICmdListener {

	public void showLinkSet(boolean statistics, TextBuilder linksetName,
			ByteBuffer byteBuffer);

	public void shutdownLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer);

	public void shutdownLink(TextBuilder linkName, ByteBuffer byteBuffer);

	public void noshutdownLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer);

	public void noshutdownLink(TextBuilder linkName, ByteBuffer byteBuffer);

	public void addLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer);

	public void deleteLinkSet(TextBuilder linksetName, ByteBuffer byteBuffer);

	public void deleteLink(TextBuilder linkName, ByteBuffer byteBuffer);

	public void networkIndicator(TextBuilder linksetName,
			CliCmdEnum networkInd, ByteBuffer byteBuffer);

	public void localPointCode(TextBuilder linksetName, TextBuilder localPC,
			ByteBuffer byteBuffer);

	public void adjacentPointCode(TextBuilder linksetName,
			TextBuilder adjacentPC, ByteBuffer byteBuffer);

	public void localIpPort(TextBuilder linksetName, TextBuilder localIp,
			int localPort, ByteBuffer byteBuffer);

	public void addLink(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer);

	public void span(TextBuilder linkName, int span, ByteBuffer byteBuffer);

	public void channel(TextBuilder linkName, int channel, ByteBuffer byteBuffer);

	public void code(TextBuilder linkName, int code, ByteBuffer byteBuffer);

	public void inhibit(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer);

	public void uninhibit(TextBuilder linksetName, TextBuilder linkName,
			ByteBuffer byteBuffer);

}
