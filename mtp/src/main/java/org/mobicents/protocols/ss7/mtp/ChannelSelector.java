package org.mobicents.protocols.ss7.mtp;

import java.util.Collection;

public interface ChannelSelector {

    /**
     * Register specfield channel for IO.
     *
     * @param channel the channel to be registered.
     */
    public void register(Mtp1 channel);
    
    /**
     * Unregisters channels
     *
     * @param channel to be unregistered 
     */
    public void unregister(Mtp1 channel);
    
    /**
     * Gets channels ready for IO operations.
     *
     * @param key IO operations.
     * @param timeout the wait timeout.
     */
    public Collection<Mtp1> select(int key, int timeout);
}