/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.ss7.hardware.dahdi;

import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 *
 * @author kulikov
 */
public class SelectorKeyImpl implements SelectorKey {

    private boolean isValid;
    private boolean isReadable;
    private boolean isWritable;
    
    private Channel channel;
    private Selector selector;
    
    private Object attachment;
    
    protected SelectorKeyImpl(Channel channel, Selector selector) {
        this.channel = channel;
        this.selector = selector;
    }
    
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isReadable() {
        return isReadable;
    }

    public boolean isWriteable() {
        return isWritable;
    }

    public Stream getStream() {
        return channel;
    }

    public StreamSelector getStreamSelector() {
        return selector;
    }

    public void cancel() {
        selector.unregister(channel);
    }

    public void attach(Object obj) {
        this.attachment = obj;
    }

    public Object attachment() {
        return this.attachment;
    }

}
