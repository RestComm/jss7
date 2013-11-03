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

package org.mobicents.ss7.hardware.dialogic;

import java.io.IOException;

/**
 *
 * @author amit bhayani
 * @author Oleg Kulikov
 * @author sergey vetyutnev
 *
 */
public class InterProcessCommunicator {

    private static final String LIB_NAME = "mobicents-dialogic-linux";

    /** The identifier of the originated module */
    private int source;
    /** The identifier of the destination module */
    private int destination;

    private byte[] readBuffer = new byte[1000];

    /**
     * Creates a new instance of InterProcessCommunicator
     *
     * @param source the integer identifier of the originated module
     * @param destination the integer idenifier of the destination module.
     */
    public InterProcessCommunicator(int source, int destination) {
        this.source = source;
        this.destination = destination;
    }

    /**
     * Receives a datagram from GCT Interprocess.
     *
     * @return received datagram.
     */
    public byte[] read() {
        // TODO Make JNI use native ByteBuffer

        int len = receive(source, readBuffer);

        if (len == -1) {
            // no messages in queue
            return null;
        }

        byte[] tempBuf = new byte[len];
        System.arraycopy(readBuffer, 0, tempBuf, 0, len);
        return tempBuf;
    }

    /**
     * Sends datagram to the destination module.
     *
     * @param packet the datagram to be sent.
     */
    public int write(byte[] msg) throws IOException {
        // TODO Make JNI use native ByteBuffer

        // TODO I do not sure if "synchronized" needs here, may be dialogic GCT_send() method is thread-safe ?
        synchronized (this) {
            int status = send(source, destination, msg);

            if (status != 0) {
                throw new IOException("Dialogic card: Can not send packet by GCT_send() method");
            }

            return status;
        }
    }

    /**
     * Actualy receives datagram using Interprocces communication.
     *
     * @param source indicates the module id wich will receive datagrams.
     * @param buffer the buffer wich should be used to store recevied message.
     * @return the actual length of the received message.
     */
    private native int receive(int source, byte[] buffer);

    /**
     * Actualy sends the message using interprocces communication.
     *
     * @param source the module id wich sends message.
     * @param destionation the module id wich must receive message.
     * @param buffer the buffer contained message.
     * @return the number of actualy bytes sent.
     */
    private native int send(int source, int destination, byte[] buffer);

    static {
        try {
            System.loadLibrary(LIB_NAME);
            System.out.println("Loaded library mobicents-dialogic-linux");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
