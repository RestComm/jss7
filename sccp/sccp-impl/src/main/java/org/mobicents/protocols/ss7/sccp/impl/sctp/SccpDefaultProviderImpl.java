package org.mobicents.protocols.ss7.sccp.impl.sctp;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.stream.MTPProvider;
import org.mobicents.protocols.ss7.stream.MTPProviderFactory;
import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

public class SccpDefaultProviderImpl extends SccpProviderImpl implements Mtp3Listener {
	private static final Logger logger = Logger.getLogger(SccpDefaultProviderImpl.class);

	private MTPProvider mtpProvider;
	private boolean linkUp = false;

	public SccpDefaultProviderImpl(Properties props) {
		super(props);
		this.mtpProvider = MTPProviderFactory.getInstance().getProvider(props);

	}
	public SccpDefaultProviderImpl(MTPProvider mtpProvider,Properties props) {
		super(props);
		this.mtpProvider =mtpProvider;

	}
	public void receive(byte[] arg2) {
		// add check for SIO parts?
		// if(logger.isInfoEnabled())
		// logger.info("Received MSU on L4, service: "+service+",subservice: "+subservice);
		super.receive(arg2);

	}

	public void linkDown() {
		// add more?
		if (linkUp) {
			this.linkUp = false;
			if (listener != null)
				listener.linkDown();
		}

	}

	public void linkUp() {
		if (!linkUp) {
			this.linkUp = true;
			if (listener != null)
				listener.linkUp();
		}

	}

	public void send(SccpAddress calledParty, SccpAddress callingParty, byte[] data, RoutingLabel ar) throws IOException {

		// FIXME:
		if (this.linkUp) {
			byte[] buf = super.encodeToMSU(calledParty, callingParty, data, ar);
			this.mtpProvider.send(buf);
		} else {
			throw new IOException("Link is not up!");
		}

	}

	public void start() throws IllegalStateException, StartFailedException {
		this.mtpProvider.addMtp3Listener(this);
		this.mtpProvider.start();
	}

	public void stop() {

		this.mtpProvider.removeMtp3Listener(this);
		this.mtpProvider.stop();

	}

	

}
