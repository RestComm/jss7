package org.mobicents.ss7.linkset.oam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 * 
 * @author amit bhayani
 * 
 */
public class LinksetSelector implements StreamSelector {

    private static final Logger logger = Logger
            .getLogger(LinksetSelector.class);

    private ArrayList<Stream> registered = new ArrayList<Stream>();

    /** array of selected channels */
    private ArrayList<SelectorKey> selected = new ArrayList<SelectorKey>();

    public LinksetSelector() {
        // TODO Auto-generated constructor stub
    }

    public SelectorKey register(LinksetStream linkSet) {
        // add LinkSet instance to the collection
        registered.add(linkSet);

        LinksetSelectorKey key = new LinksetSelectorKey(linkSet, this);
        linkSet.selectorKey = key;
        return key;
    }

    public void unregister(LinksetStream linkSet) {
        registered.remove(linkSet);
        linkSet.selectorKey = null;

        // TODO : Should some method be called in LinkSet indicating that its
        // Unregistered for cleanup?
    }

    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Stream> getRegisteredStreams() {
        return registered;
    }

    public boolean isClosed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<SelectorKey> selectNow(int operation, int timeout)
            throws IOException {

        selected.clear();
        for (Stream s : registered) {
            if (((LinksetStream) s).poll(operation, timeout)) {
                selected.add(((LinksetStream) s).selectorKey);
            }
        }
        return selected;
    }

}
