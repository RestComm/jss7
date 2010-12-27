package org.mobicents.ss7.m3ua.oam;

import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetFactory;

/**
 * 
 * @author amit bhayani
 * 
 */
public class M3UALinksetFactory extends LinksetFactory {

    private static final String NAME = "m3ua";

    public M3UALinksetFactory() {
        super();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Linkset createLinkset(String[] options) {
        return null;
    }

}
