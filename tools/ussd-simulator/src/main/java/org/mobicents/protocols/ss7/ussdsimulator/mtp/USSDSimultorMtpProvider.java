/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.ussdsimulator.mtp;

import java.io.IOException;
import javax.swing.JTextField;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.stream.MTPListener;
import org.mobicents.protocols.ss7.stream.tcp.M3UserAgent;
import org.mobicents.protocols.ss7.stream.tcp.MTPProviderImpl;
import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

/**
 *
 * @author baranowb
 */
public class USSDSimultorMtpProvider extends MTPProviderImpl{

    private static final String _DEFAULT_ADDRESS = "127.0.0.1";
    private static final int _DEFAULT_PORT = 1345;

    //this will start server, to which RAs connect
    private M3UserAgent actualProvider;
    private JTextField address;
    private JTextField port;
    private MTPListener listener;
    private Mtp3Extender mtp3;



    public USSDSimultorMtpProvider(JTextField address,JTextField port)
    {

        this.address = address;
        this.port = port;

    }

    @Override
    public void start() throws StartFailedException, IllegalStateException {
        try{
           String usedAddress;
           String add = this.address.getText();
           String prt = this.port.getText();
           int port;
           if(add ==null || add.equals(""))
           {
               usedAddress = _DEFAULT_ADDRESS;
           }else
           {
               usedAddress = add;
           }
           if(prt ==null || prt.equals(""))
           {
               port = _DEFAULT_PORT;
           }else
           {
               port = Integer.parseInt(prt);
           }

           mtp3 = new Mtp3Extender();

           actualProvider = new M3UserAgent();
           actualProvider.setMtp3(mtp3);
           actualProvider.setAddress(usedAddress);
           actualProvider.setPort(port);
           actualProvider.start();
           //indicate to client linkup, since we mockup Mtps, we all always ready.
           actualProvider.linkUp();
        }catch(Exception e)
        {
            throw new StartFailedException(e);
        }

    }

    @Override
    public void stop() throws IllegalStateException {
        if(this.actualProvider!=null)
        {
            this.actualProvider.stop();
            this.actualProvider = null;
            this.mtp3 = null;
        }
    }

    public void addMtpListener(MTPListener listener) {
       this.listener = listener;
    }

    public void removeMtpListener(MTPListener listener) {
        if(listener == this.listener)
        {
            this.listener = null;

        }
    }

    public void send(byte[] arg0) throws IOException {
       this.actualProvider.receive(arg0);
    }


    public boolean isLinkUp()
    {
        //a bit of hack.
        //FIXME: add monitor for this
        if(this.actualProvider==null)
        {
            return false;
        }else
        {
            boolean res = this.actualProvider.isConnected();
            if(res)
            {
                if(this.listener!=null)
                {
                    this.listener.linkUp();
                }
            }else
            {
                if(this.listener!=null)
                {
                    this.listener.linkDown();
                }
            }
            return res;
        }
    }

    //fake Mtp3 class: Oleg I have no idea why you removed interfaces...
    private class Mtp3Extender extends Mtp3
    {
        public Mtp3Extender()
        {
            super("FakeMTP3!");
        }

        @Override
        public boolean send(byte[] msg) {
            //agent received data, listener should get it.
            if(listener!=null)
            {
                listener.receive(msg);
                return true;
            }

            return false;
        }
    }
}
