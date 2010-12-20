package org.mobicents.protocols.ss7.mtp.oam;

import javolution.text.TextBuilder;

/**
 * 
 * @author amit bhayani
 *
 */
public class M3UALinksetFactory extends LinksetFactory {

	public M3UALinksetFactory() {
		super(LinksetType.M3UA);
	}

	@Override
	Linkset createLinkSet(TextBuilder linkSetName) {
		return new M3UALinkset(linkSetName, this.type);
	}

}
