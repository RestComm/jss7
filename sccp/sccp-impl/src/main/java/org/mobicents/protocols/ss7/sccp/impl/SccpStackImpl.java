package org.mobicents.protocols.ss7.sccp.impl;

import java.util.Properties;

import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

/**
 * @author baranowb
 * 
 */
public class SccpStackImpl implements SccpStack {

	private State state = State.IDLE;

	private SccpProviderImpl sccpProvider;

	private final static SccpProviderFactory factory = new SccpProviderFactory();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.sccp.SccpStack#configure(java.util.Properties
	 * )
	 */
	public void configure(Properties properties) {
		if (state != State.IDLE) {
			throw new IllegalStateException("Stack already been configured or is already running!");
		}
		this.sccpProvider = (SccpProviderImpl) factory.getProvider(properties);
		this.state = State.CONFIGURED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#getSccpProvider()
	 */
	public SccpProvider getSccpProvider() {
		// TODO Auto-generated method stub
		return sccpProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#start()
	 */
	public void start() throws IllegalStateException, StartFailedException {
		if (state != State.CONFIGURED) {
			throw new IllegalStateException("Stack has not been configured or is already running!");
		}
		this.sccpProvider.start();
		this.state = State.RUNNING;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#stop()
	 */
	public void stop() {
		if (state != State.RUNNING) {
			throw new IllegalStateException("Stack is not running!");
		}
		this.sccpProvider.stop();
		this.state = State.CONFIGURED;
	}

	private enum State {
		IDLE, CONFIGURED, RUNNING;
	}

}
