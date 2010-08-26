/**
 * 
 */
package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Pipe provider for tests, it connected to another instance intance, provides async transport of data between this and this.other.
 * <br> P === P
 * 
 * @author baranowb
 *
 */
public class PipeSccpProviderImpl extends SccpProviderImpl implements SccpProvider {

	private PipeSccpProviderImpl other;
	private SccpListener listener;//this is our test tcap stack

	/**
	 * 
	 */
	public PipeSccpProviderImpl() {
		this.other = new PipeSccpProviderImpl(this);

	}

	private PipeSccpProviderImpl(PipeSccpProviderImpl other) {
		super();
		this.other = other;
	
	}



	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpProvider#configure(java.util.Properties)
	 */
	public void configure(Properties p) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpProvider#send(org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, byte[])
	 */
	public void send(SccpAddress calledParty, SccpAddress callingParty,	byte[] data, RoutingLabel ar) throws IOException{
		this.other.receiveData(calledParty,callingParty,data);

	}

	@Override
	public void start() throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpProvider#shutdown()
	 */
	public void stop() {
		// TODO Auto-generated method stub

	}

	
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpProvider#addSccpListener(org.mobicents.protocols.ss7.sccp.SccpListener)
	 */
	public void addSccpListener(SccpListener listener) {
		this.listener = listener;
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpProvider#removeSccpListener(org.mobicents.protocols.ss7.sccp.SccpListener)
	 */
	public void removeSccpListener(SccpListener listener) {
		this.listener = null;
		
	}

	/**
	 * @return the other
	 */
	public PipeSccpProviderImpl getOther() {
		return other;
	}

	private ScheduledExecutorService _EXECUTOR = Executors.newSingleThreadScheduledExecutor();
	private void receiveData(SccpAddress calledParty, SccpAddress callingParty, byte[] data) {
		_EXECUTOR.schedule(new DataPasser(calledParty,callingParty,data), 50,TimeUnit.MILLISECONDS);
		
	}
	
	private class DataPasser implements Runnable
	{
		private SccpAddress calledParty;
		private SccpAddress callingParty;
		private byte[] data;
		
		private DataPasser(SccpAddress calledParty, SccpAddress callingParty, byte[] data) {
			super();
			this.calledParty = calledParty;
			this.callingParty = callingParty;
			this.data = new byte[data.length];
			System.arraycopy(data, 0, this.data, 0, data.length);
		}

		public void run() {
			try{
				listener.onMessage(calledParty, callingParty, data, null);
			}catch(Exception e )
			{
				e.printStackTrace();
				//test.fail("Failed on error: "+e);
			}
		}
		
	}

}
