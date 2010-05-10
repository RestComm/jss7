/**
 * 
 */
package org.mobicents.protocols.ss7.stream.tcp;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.mobicents.protocols.ss7.stream.MTPListener;
import org.mobicents.protocols.ss7.stream.MTPProvider;


/**
 * Pipe Mtp provider
 * @author baranowb
 *
 */
public class PipeMtpProviderImpl implements MTPProvider {

	
	private PipeMtpProviderImpl other;
	private MTPListener listener;
	
	
	
	
	
	public PipeMtpProviderImpl() {
		super();
		this.other = new PipeMtpProviderImpl(this);
	}

	
	
	private PipeMtpProviderImpl(PipeMtpProviderImpl other) {
		super();
		this.other = other;
	}



	/**
	 * @return the other
	 */
	public PipeMtpProviderImpl getOther() {
		return other;
	}



	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.MTPProvider#addMtpListener(org.mobicents.protocols.ss7.stream.MTPListener)
	 */
	public void addMtpListener(MTPListener lst) {
		listener = lst;

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.MTPProvider#removeMtpListener(org.mobicents.protocols.ss7.stream.MTPListener)
	 */
	public void removeMtpListener(MTPListener lst) {
		if(lst == this.listener)
			this.listener = null;

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.MTPProvider#send(byte[])
	 */
	public void send(byte[] msg) throws IOException {
		this.other.receive(msg);

	}

	

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.MTPProvider#stop()
	 */
	public void stop() throws IllegalStateException {
		// TODO Auto-generated method stub

	}
	private ScheduledExecutorService _EXECUTOR = Executors.newSingleThreadScheduledExecutor();
	private void receive(byte[] msg) {
		
		_EXECUTOR.schedule(new DataPasser(msg), 50,TimeUnit.MILLISECONDS);
	}
	private class DataPasser implements Runnable
	{

		private byte[] data;
		
		private DataPasser(byte[] data) {
			super();

			this.data = new byte[data.length];
			System.arraycopy(data, 0, this.data, 0, data.length);
		}

		public void run() {
			try{
				listener.receive(data);
			}catch(Exception e )
			{
				e.printStackTrace();
			}
		}
		
	}
}
