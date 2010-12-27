package org.mobicents.ss7.hardware.dialogic.oam;

import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetFactory;

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
    public String getName() {
        return NAME;
    }

    @Override
    public Linkset createLinkset(String[] options) throws Exception {
        throw new Exception(LinkOAMMessages.NOT_IMPLEMENTED);
    }

}
