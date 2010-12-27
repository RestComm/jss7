package org.mobicents.protocols.ss7.mtp.oam;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class LinksetFactory {


    public LinksetFactory() {
    }

    abstract Linkset createLinkset(String[] options);
    
    abstract String getName();

}
