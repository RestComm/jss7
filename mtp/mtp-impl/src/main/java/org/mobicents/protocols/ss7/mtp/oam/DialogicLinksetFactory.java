package org.mobicents.protocols.ss7.mtp.oam;


/**
 * 
 * @author amit bhayani
 *
 */
public class DialogicLinksetFactory extends LinksetFactory {

    private static final String NAME = "dialogic";
    
	public DialogicLinksetFactory() {
		super();
	}
	
    @Override
    String getName() {
        return NAME ;
    }

    @Override
    Linkset createLinkset(String[] options) {
        return null;
    }

}
