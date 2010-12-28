package org.mobicents.ss7.hardware.dialogic.oam;

import org.mobicents.ss7.linkset.oam.LinkOAMMessages;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetState;

/**
 * 
 * @author amit bhayani
 * 
 */
public class DialogicLinkset extends Linkset {

    public DialogicLinkset() {
        super();
    }

    @Override
    protected void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void configure() throws Exception {

    }

    @Override
    public void activate() throws Exception {
        if (this.state == LinksetState.AVAILABLE) {
            throw new Exception(LinkOAMMessages.LINKSET_ALREADY_ACTIVE);
        }

        // TODO Start Dialogic Linkset
    }

    @Override
    public void deactivate() throws Exception {
        throw new Exception(LinkOAMMessages.NOT_IMPLEMENTED);
    }

    @Override
    public void activateLink(String linkName) throws Exception {
        throw new Exception(LinkOAMMessages.OPERATION_NOT_SUPPORTED);
    }

    @Override
    public void deactivateLink(String linkName) throws Exception {
        throw new Exception(LinkOAMMessages.OPERATION_NOT_SUPPORTED);
    }

    @Override
    public void createLink(String[] arg0) throws Exception {
        throw new Exception(LinkOAMMessages.NOT_IMPLEMENTED);
    }

    @Override
    public void deleteLink(String arg0) throws Exception {
        throw new Exception(LinkOAMMessages.NOT_IMPLEMENTED);
    }
}
