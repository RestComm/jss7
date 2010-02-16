/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.protocols.ss7.sccp.impl.intel.gt;

import java.io.IOException;

/**
 *
 * @author Oleg Kulikov
 */
public class InterProcessCommunicator {
    /** The identifier of the originated module */
    private int source;
    /** The identifier of the destination module */
    private int destination;
    
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
    public byte[] receive() throws IOException {
        byte[] buffer = new byte[1000];
        int len = receive(source,  buffer);
        
        if (len == -1) {
            throw new IOException("Unable to read message from IPC");
        }
        
        byte[] message = new byte[len];
        System.arraycopy(buffer, 0, message, 0, len);
        
        return message;
    }
    
    
    /**
     * Sends datagram to the destination module.
     *
     * @param packet the datagram to be sent.
     */
    public void send(byte[] packet) throws IOException {
        int status = send(source, destination, packet);
        if (status != 0) {
            throw new IOException("Can not send packet");
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
        System.loadLibrary("ipc");
    }    
}
