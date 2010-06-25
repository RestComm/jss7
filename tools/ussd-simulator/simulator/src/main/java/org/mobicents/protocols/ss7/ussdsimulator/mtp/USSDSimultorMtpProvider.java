/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.ussdsimulator.mtp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextField;

import org.mobicents.protocols.ss7.mtp.Mtp2;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.mtp.SelectorFactory;
import org.mobicents.protocols.ss7.stream.tcp.M3UserAgent;
import org.mobicents.protocols.ss7.stream.tcp.MTPProviderImpl;
import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

/**
 * 
 * @author baranowb
 */
public class USSDSimultorMtpProvider extends MTPProviderImpl {

	private static final String _DEFAULT_ADDRESS = "127.0.0.1";
	private static final int _DEFAULT_PORT = 1345;

	// this will start server, to which RAs connect
	private M3UserAgent actualProvider;
	private JTextField address;
	private JTextField port;
	// this is what gets MTP3 data
	private Mtp3Listener listener;
	// this is what goes into agent to make it think its getting data from MTP
	// layers
	private Mtp3Extender mtp3;

	public USSDSimultorMtpProvider(JTextField address, JTextField port) {

		this.address = address;
		this.port = port;

	}

	@Override
	public void start() throws StartFailedException, IllegalStateException {
		try {
			String usedAddress;
			String add = this.address.getText();
			String prt = this.port.getText();
			int port;
			if (add == null || add.equals("")) {
				usedAddress = _DEFAULT_ADDRESS;
			} else {
				usedAddress = add;
			}
			if (prt == null || prt.equals("")) {
				port = _DEFAULT_PORT;
			} else {
				port = Integer.parseInt(prt);
			}

			mtp3 = new Mtp3Extender();

			actualProvider = new M3UserAgent();
			actualProvider.setMtp3(mtp3);
			actualProvider.setLocalAddress(usedAddress);
			actualProvider.setLocalPort(port);
			actualProvider.start();
			// indicate to client linkup, since we mockup Mtps, we all always
			// ready.
			// this.mtp3.agentListener.linkUp();
		} catch (Exception e) {
			throw new StartFailedException(e);
		}

	}

	@Override
	public void stop() throws IllegalStateException {
		if (this.actualProvider != null) {
			this.actualProvider.stop();
			this.actualProvider = null;
			this.mtp3 = null;
		}
	}

	public void addMtp3Listener(Mtp3Listener listener) {
		this.listener = listener;
	}

	public void removeMtp3Listener(Mtp3Listener listener) {
		if (listener == this.listener) {
			this.listener = null;

		}
	}

	public void send(byte[] arg0) throws IOException {
		// trick, stack sends this over agent
		ByteBuffer bb = ByteBuffer.wrap(arg0);

		this.mtp3.agentListener.receive(arg0);

	}

	public void indicateLinkUp() {
		// fake linkup so it goes to other side
		this.mtp3.agentListener.linkUp();
	}

	public boolean isLinkUp() {
		// a bit of hack.
		// FIXME: add monitor for this
		if (this.actualProvider == null) {
			return false;
		} else {
			boolean res = this.actualProvider.isConnected();
			if (res) {
				// this.mtp3.agentListener.linkUp();
				if (this.listener != null) {
					this.listener.linkUp();
				}
			} else {
				if (this.listener != null) {
					this.listener.linkDown();
				}
			}
			return res;
		}
	}

	// fake Mtp3 class: Oleg I have no idea why you removed interfaces...
	private class Mtp3Extender implements Mtp3 {
		private Mtp3Listener agentListener;

		public Mtp3Extender() {

		}

		public boolean send(byte[] msg) {
			// agent received data, listener should get it.
			if (listener != null) {
				
				listener.receive(msg);
				return true;
                        }else
                        {
                            return false;
                        }

			
		}

		public void addMtp3Listener(Mtp3Listener arg0) {
			this.agentListener = arg0;

		}

		public int getDpc() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getOpc() {
			// TODO Auto-generated method stub
			return 0;
		}

		public void removeMtp3Listener(Mtp3Listener arg0) {
			this.agentListener = null;

		}

		public void setDpc(int arg0) {
			// TODO Auto-generated method stub

		}

		public void setLinks(List<Mtp2> arg0) {
			// TODO Auto-generated method stub

		}

		public void setOpc(int arg0) {
			// TODO Auto-generated method stub

		}

		public void setSelectorFactory(SelectorFactory arg0) {
			// TODO Auto-generated method stub

		}

		public void start() throws IOException {
			// this.agentListener.linkUp();

		}

		public void stop() throws IOException {
			this.agentListener.linkDown();

		}
	}
}
