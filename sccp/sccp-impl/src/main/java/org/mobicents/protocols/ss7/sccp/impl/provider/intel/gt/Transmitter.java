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

package org.mobicents.protocols.ss7.sccp.impl.provider.intel.gt;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Oleg Kulikov
 */
public class Transmitter implements Runnable {
    
    private DatagramSocket socket = null;
    private InterProcessCommunicator board = null;
    
    /** Creates a new instance of Transmitter */
    public Transmitter(DatagramSocket socket, InterProcessCommunicator board) {
        this.socket = socket;
        this.board = board;
    }
    
    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[500];
                DatagramPacket packet = new DatagramPacket(buffer, 500);
                socket.receive(packet);
                
                int len = packet.getLength();
                byte[] data = new byte[len];
                
                System.arraycopy(buffer, 0, data, 0, len);
                board.send(data);
                Thread.currentThread().sleep(10);
            } catch (Exception e) {
                System.err.println("I/O Exception occured: Caused by " + e);
            }
        }
    }
    
}
