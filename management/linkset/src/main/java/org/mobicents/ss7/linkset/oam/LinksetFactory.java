package org.mobicents.ss7.linkset.oam;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class LinksetFactory {


    public LinksetFactory() {
    }

    public abstract Linkset createLinkset(String[] options);
    
    public abstract String getName();

}
