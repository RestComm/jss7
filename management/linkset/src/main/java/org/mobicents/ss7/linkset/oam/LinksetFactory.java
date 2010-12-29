package org.mobicents.ss7.linkset.oam;

/**
 * <p>
 * The abstract class represents a factory for creating {@link Linkset}
 * </p>
 * <p>
 * When {@link LinksetManager} receives command to create new {@link Linkset},
 * it uses linkset factory to create new linkset depending on options passed
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public abstract class LinksetFactory {

    public LinksetFactory() {
    }

    /**
     * Create a new linkset analyzing the options passed
     * 
     * @param options
     * @return
     * @throws Exception
     */
    public abstract Linkset createLinkset(String[] options) throws Exception;

    /**
     * Get name of this factory
     * 
     * @return
     */
    public abstract String getName();

}
