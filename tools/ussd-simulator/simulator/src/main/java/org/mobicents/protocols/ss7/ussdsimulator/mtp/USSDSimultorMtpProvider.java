/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.ussdsimulator.mtp;


import java.util.Properties;
import javax.swing.JTextField;


import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.mtp.provider.m3ua.M3UAProvider;


/**
 * 
 * @author baranowb
 */
public class USSDSimultorMtpProvider extends M3UAProvider {//re-use M3UA, it should work ok :)

	private static final String _DEFAULT_ADDRESS = "127.0.0.1";
	private static final int _DEFAULT_PORT = 1345;

	
	private JTextField jtaddress;
	private JTextField jtport;

        //lets reuse provider


	public USSDSimultorMtpProvider(JTextField address, JTextField port) {

		this.jtaddress = address;
		this.jtport = port;

	}

    @Override
    public void configure(Properties p) throws ConfigurationException {
        p.put(this.PROPERTY_LADDRESS, _DEFAULT_ADDRESS+":"+_DEFAULT_PORT);
        p.put(this.PROPERTY_RADDRESS, jtaddress.getText()+":"+jtport.getText());
        super.configure(p);

    }

   
    @Override
    public void start() throws StartFailedException {
        super.start();

         super.remotePeerStream.activate();
    }



}
