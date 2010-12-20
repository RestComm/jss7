package org.mobicents.protocols.ss7.mtp.oam;

import javolution.text.TextBuilder;

/**
 * 
 * @author amit bhayani
 *
 */
public class DahdiLinksetFactory extends LinksetFactory {

	public DahdiLinksetFactory() {
		super(LinksetType.DAHDI);
	}

	@Override
	Linkset createLinkSet(TextBuilder linkSetName) {
		return new DahdiLinkset(linkSetName, this.type);
	}

}
