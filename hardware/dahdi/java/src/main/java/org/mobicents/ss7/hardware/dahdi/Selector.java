/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.ss7.hardware.dahdi;

import java.io.IOException;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp1;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 *
 * @author amit bhayani
 *
 */
public class Selector implements StreamSelector {

    private static final String LIB_NAME = "mobicents-dahdi-linux";

    public static final int READ = 0x01;
    public static final int WRITE = 0x02;

    /** array of selected file descriptors */
    private int[] fds = new int[16];

    /** array of registered channels */
    private FastList<Mtp1> registered = new FastList<Mtp1>();

    private int ops;

    /** array of selected channels */
    private FastList<SelectorKey> selected = new FastList<SelectorKey>();
    private static Logger logger = Logger.getLogger(Selector.class);

    static {
        try {
            System.loadLibrary(LIB_NAME);
            System.out.println("Loaded library mobicents-dahdi-linux");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Selector() {

    }

    /**
     * Register channel with this selector.
     *
     * @param channel the channel to register.
     */
    public SelectorKey register(Channel channel) {
        // add channel instance to the collection
        registered.add(channel);
        // perform actual registration
        logger.info("Registering file descriptor:" + ((Channel) channel).fd);
        doRegister(((Channel) channel).fd);

        SelectorKeyImpl key = new SelectorKeyImpl(channel, this);
        channel.selectorKey = key;
        return key;
    }

    /**
     * Unregister channel.
     *
     * @param channel the channel to unregister.
     */
    public void unregister(Channel channel) {
        registered.remove(channel);
        channel.selectorKey = null;
        doUnregister(((Channel) channel).fd);
    }

    /**
     * Registers pipe for polling.
     *
     * @param fd the file descriptor.
     */
    public native void doRegister(int fd);

    /**
     * Unregisters pipe from polling.
     *
     * @param fd the file descriptor.
     */
    public native void doUnregister(int fd);

    /**
     * Delegates select call to unix poll function.
     *
     * @param fds the list of file descriptors.
     * @param key selection key.
     * @return the number of selected channels.
     */
    public native int doPoll(int[] fds, int key, int timeout);

    public FastList<SelectorKey> selectNow(int ops, int timeout) throws IOException {
        int count = doPoll(fds, ops, 1);
        selected.clear();
        for (int i = 0; i < count; i++) {
            for (FastList.Node<Mtp1> n = registered.head(), end = registered.tail(); (n = n.getNext()) != end;) {
                Channel channel = (Channel) n.getValue();
                if (channel.fd == fds[i]) {
                    selected.add(channel.selectorKey);
                }
            }
        }
        return selected;
    }

    public void setOperation(int v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getOperations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isReadOperation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWriteOperation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isClosed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FastList<Stream> getRegisteredStreams() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}