package org.mobicents.ss7.linkset.oam;

/**
 * <p>
 * Listener interface for receiving
 * {@link org.mobicents.ss7.linkset.oam.Linkset} event. The class that is
 * interested in linkset for it's operation implements this interface.
 * </p>
 * <p>
 * {@linkset LinksetManager} calls {@link #add add} method when it adds new
 * Linkset
 * <p>
 * 
 * <p>
 * {@linkset LinksetManager} calls {@link #remove remove} method when it remove
 * existing Linkset
 * <p>
 * 
 * @author amit bhayani
 * 
 */
public interface Layer4 {

    /**
     * New linkset added
     * 
     * @param linkset
     */
    public void add(Linkset linkset);

    /**
     * Existing linkset removed
     * 
     * @param linkset
     */
    public void remove(Linkset linkset);

}
