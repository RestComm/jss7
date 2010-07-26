package org.mobicents.protocols.ss7.sccp.impl.provider.stream;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.mtp.provider.MtpListener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.ss7.mtp.provider.MtpProviderFactory;
import org.mobicents.protocols.ss7.sccp.impl.AbstractSccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 * @author baranowb
 */
public class SccpDefaultProviderImpl extends AbstractSccpProviderImpl implements
		MtpListener {
	private static final Logger logger = Logger
			.getLogger(SccpDefaultProviderImpl.class);

	private MtpProvider mtpProvider;
	private boolean linkUp = false;

	public SccpDefaultProviderImpl() {
		super();

	}

	public SccpDefaultProviderImpl(MtpProvider mtpProvider) {
		super();
		this.mtpProvider = mtpProvider;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl#configure(java
	 *      .util.Properties)
	 */
	@Override
	public void configure(Properties props) throws ConfigurationException {

		super.configure(props);
		if (this.mtpProvider == null)// thats for case provider is passed as
		// arg.
		{
			this.mtpProvider = MtpProviderFactory.getInstance().getProvider(
					props);
		}
	}

	public void receive(byte[] arg2) {
		// add check for SIO parts?
		// if(logger.isInfoEnabled())
		// logger.info("Received MSU on L4, service: "+service+",subservice:
		// "+subservice);
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

	public void send(SccpAddress calledParty, SccpAddress callingParty,
			byte[] data, RoutingLabel ar) throws IOException {

		// FIXME:
		if (this.linkUp) {
			byte[] buf = super.encodeToMSU(calledParty, callingParty, data, ar);
			this.mtpProvider.send(buf);
		} else {
			throw new IOException("Link is not up!");
		}

	}

	public void start() throws IllegalStateException, StartFailedException {
		logger.info("Starting ...");
		this.mtpProvider.setMtpListener(this);
		this.mtpProvider.start();
	}

	public void stop() {

		this.mtpProvider.stop();

	}

}
