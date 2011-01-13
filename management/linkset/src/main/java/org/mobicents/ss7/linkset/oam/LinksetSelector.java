package org.mobicents.ss7.linkset.oam;

import java.io.IOException;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 * the stream of Linkset
 * 
 * @author amit bhayani
 * 
 */
public class LinksetSelector implements StreamSelector {

    private static final Logger logger = Logger
            .getLogger(LinksetSelector.class);

    private FastList<Stream> registered = new FastList<Stream>();

    /** array of selected channels */
    private FastList<SelectorKey> selected = new FastList<SelectorKey>();

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

    public FastList<Stream> getRegisteredStreams() {
        return registered;
    }

    public boolean isClosed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FastList<SelectorKey> selectNow(int operation, int timeout)
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
