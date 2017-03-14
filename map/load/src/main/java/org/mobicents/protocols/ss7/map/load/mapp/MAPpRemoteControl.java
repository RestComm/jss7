package org.mobicents.protocols.ss7.map.load.mapp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MAPpRemoteControl implements Runnable {
    protected DatagramSocket socket = null;
    private MAPpContext ctx;


    public MAPpRemoteControl(MAPpContext ctx) throws IOException {
        this.ctx=ctx;
        socket = new DatagramSocket(4445);

    }
    
    public void stop() {
        socket.close();        
        
    }

    public void run() {
        while (!ctx.getCurrentState().equals(MAPpState.COMPLETED)) {
            try {
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                // figure out response
                String dString = "200";
                buf = dString.getBytes();

		// send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }    
}
