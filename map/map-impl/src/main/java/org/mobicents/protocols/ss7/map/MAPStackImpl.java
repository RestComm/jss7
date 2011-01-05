package org.mobicents.protocols.ss7.map;

import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPStackImpl implements MAPStack {
	
	private TCAPStack tcapStack = null;
	
	private MAPProviderImpl mapProvider = null;

	private State state = State.IDLE;

	public MAPStackImpl(SccpProvider sccpPprovider, SccpAddress address) {
		this.tcapStack = new TCAPStackImpl(sccpPprovider, address);
		TCAPProvider tcapProvider = tcapStack.getProvider();
		mapProvider = new MAPProviderImpl(tcapProvider);
		
		this.state = State.CONFIGURED;
	}

	public MAPProvider getMAPProvider() {
		return this.mapProvider;
	}

	public void start() throws IllegalStateException, StartFailedException {
		if (state != State.CONFIGURED) {
			throw new IllegalStateException("Stack has not been configured or is already running!");
		}
		if(tcapStack!=null)
		{
			//this is null in junits!
			this.tcapStack.start();
		}
		this.mapProvider.start();

		this.state = State.RUNNING;

	}

	public void stop() {
		if (state != State.RUNNING) {
			throw new IllegalStateException("Stack is not running!");
		}
		this.mapProvider.stop();
		if(tcapStack!=null)
		{
			this.tcapStack.stop();
		}
		
		this.state = State.CONFIGURED;
	}

//	// ///////////////
//	// CONF METHOD //
//	// ///////////////
//	/**
//	 * @throws ConfigurationException 
//     *
//     */
//	public void configure(Properties props) throws ConfigurationException {
//		if (state != State.IDLE) {
//			throw new IllegalStateException("Stack already been configured or is already running!");
//		}
//		tcapStack.configure(props);
//		TCAPProvider tcapProvider = tcapStack.getProvider();
//		mapProvider = new MAPProviderImpl(tcapProvider);
//		this.state  = State.CONFIGURED;
//	}



	private enum State {
		IDLE, CONFIGURED, RUNNING;
	}

}
