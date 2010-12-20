package org.mobicents.protocols.ss7.mtp.oam;

import javolution.text.TextBuilder;

/**
 * 
 * @author amit bhayani
 *
 */
public class DialogicLinksetFactory extends LinksetFactory {

	public DialogicLinksetFactory() {
		super(LinksetType.DIALOGI);
	}

	@Override
	Linkset createLinkSet(TextBuilder linkSetName) {
		return new DialogicLinkset(linkSetName, this.type);
	}

}
