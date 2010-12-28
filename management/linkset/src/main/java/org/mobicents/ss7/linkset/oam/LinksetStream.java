package org.mobicents.ss7.linkset.oam;

import org.mobicents.protocols.stream.api.Stream;

public abstract class LinksetStream implements Stream {
    
    protected LinksetSelectorKey selectorKey = null;

    /**
     * Poll the respective stream for readiness
     * 
     * @param operation
     * @param timeout
     * @return
     */
    public abstract boolean poll(int operation, int timeout);

}
