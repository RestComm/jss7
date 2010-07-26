/**
 * 
 */
package org.mobicents.protocols.ss7.tcap;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;

/**
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class TCAPStackImpl implements TCAPStack {
	
	private static final Logger logger = Logger.getLogger(TCAPStackImpl.class);

	private TCAPProviderImpl tcapProvider;
	private State state = State.IDLE;
	private SccpStackImpl sccpStack;

	public TCAPStackImpl() {
		super();

	}
	//for tests only
	public TCAPStackImpl(SccpProvider sccpPprovider) {
		this.tcapProvider = new TCAPProviderImpl(sccpPprovider,this);		
		this.sccpStack = new SccpStackImpl(sccpPprovider);
		this.state = State.CONFIGURED;
	}

	public void start() throws IllegalStateException, StartFailedException {
		logger.info("Starting ...");
		if (state != State.CONFIGURED) {
			throw new IllegalStateException("Stack has not been configured or is already running!");
		}
		if(sccpStack!=null)
		{
			//this is null in junits!
			this.sccpStack.start();
		}
		this.tcapProvider.start();

		this.state = State.RUNNING;

	}

	public void stop() {
		if (state != State.RUNNING) {
			throw new IllegalStateException("Stack is not running!");
		}
		this.tcapProvider.stop();
		if(sccpStack!=null)
		{
			this.sccpStack.stop();
		}
		this.state = State.CONFIGURED;
	}

	// ///////////////
	// CONF METHOD //
	// ///////////////
	/**
     *
     */
	public void configure(Properties props) throws ConfigurationException{
		if (state != State.IDLE) {
			throw new IllegalStateException("Stack already been configured or is already running!");
		}
		this.sccpStack = new SccpStackImpl();
		this.sccpStack.configure(props);
		this.tcapProvider = new TCAPProviderImpl(this.sccpStack.getSccpProvider(), this);
		this.state = State.CONFIGURED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getProvider()
	 */
	public TCAPProvider getProvider() {

		return tcapProvider;
	}

	private enum State {
		IDLE, CONFIGURED, RUNNING;
	}

}
