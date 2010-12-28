package org.mobicents.ss7.linkset.oam;

import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 * 
 * @author amit bhayani
 *
 */
public class LinksetSelectorKey implements SelectorKey {

	private boolean isValid;
	private boolean isReadable;
	private boolean isWritable;

	private LinksetStream linkSet = null;
	private LinksetSelector linkSetSelector = null;

	private Object attachment;

	public LinksetSelectorKey(LinksetStream linkSet, LinksetSelector linkSetSelector) {
		this.linkSet = linkSet;
		this.linkSetSelector = linkSetSelector;
	}

	public void attach(Object paramObject) {
		this.attachment = paramObject;
	}

	public Object attachment() {
		return this.attachment;
	}

	public void cancel() {
		linkSetSelector.unregister(linkSet);
	}

	public Stream getStream() {
		return this.linkSet;
	}

	public StreamSelector getStreamSelector() {
		return this.linkSetSelector;
	}

	public boolean isReadable() {
		return isReadable;
	}

	public boolean isValid() {
		return isValid;
	}

	public boolean isWriteable() {
		return isWritable;
	}

}
