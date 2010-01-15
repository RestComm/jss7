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

package org.mobicents.ss7.sccp.impl.intel.gt;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
/**
 *
 * @author Oleg Kulikov
 */
public class Transceiver {
    private DatagramSocket socket = null;
    private InterProcessCommunicator board = null;
    
    private Receiver receiver = null;
    private Transmitter transmitter = null;
    
    /** Creates a new instance of Transceiver */
    public Transceiver() throws SocketException {
        socket = new DatagramSocket(9201);
        board = new InterProcessCommunicator(61,34);
        
        receiver = new Receiver(socket, board);
        transmitter = new Transmitter(socket, board);
    }

    public void start() {
        new Thread(receiver).start();
        new Thread(transmitter).start();
    }
    
    public static void main(String[] args) throws Exception {
        Transceiver transceiver = new Transceiver();
        transceiver.start();
    }
    
}
