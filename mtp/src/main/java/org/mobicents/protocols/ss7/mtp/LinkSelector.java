package org.mobicents.protocols.ss7.mtp;

import java.util.ArrayList;
import java.util.Collection;

public class LinkSelector {
    
    public final static int READ = 0x01;
    public final static int WRITE = 0x02;
    
    private ChannelSelector selector;
    private ArrayList<Mtp2> selected = new ArrayList();        
    
    public LinkSelector(ChannelSelector selector) {
	this.selector = selector;
    }
    
    /**
     * Register specfield channel for IO.
     *
     * @param channel the channel to be registered.
     */
    public void register(Mtp2 link) {
	selector.register(link.getChannel());
    }
    
    /**
     * Unregisters channels
     *
     * @param channel to be unregistered 
     */
    public void unregister(Mtp2 link) {
	selector.unregister(link.getChannel());
    }
    
    /**
     * Gets channels ready for IO operations.
     *
     * @param key IO operations.
     * @param timeout the wait timeout.
     */
    public Collection<Mtp2> select(int key, int timeout) {
	Collection<Mtp1> channels = selector.select(key, timeout);
	selected.clear();
	for (Mtp1 channel : channels) {
	    selected.add(channel.getLink());
	}
	return selected;
    }
}