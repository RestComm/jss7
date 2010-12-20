package org.mobicents.protocols.ss7.mtp.oam;

import javolution.text.TextBuilder;

/**
 * 
 * @author amit bhayani
 *
 */
public abstract class LinksetFactory {

	protected final int type;

	public LinksetFactory(int type) {
		this.type = type;
	}

	abstract Linkset createLinkSet(TextBuilder linkSetName);

	public int getType() {
		return this.type;
	}

}
