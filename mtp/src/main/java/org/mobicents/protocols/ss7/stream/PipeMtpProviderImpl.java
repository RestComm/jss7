/**
 * 
 */
package org.mobicents.protocols.ss7.stream;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;



/**
 * Pipe Mtp provider
 * @author baranowb
 *
 */
public class PipeMtpProviderImpl implements MTPProvider {

	
	private PipeMtpProviderImpl other;
	private Mtp3Listener listener;
	
	
	
	
	
	public PipeMtpProviderImpl() {
		super();
		this.other = new PipeMtpProviderImpl(this);
		_EXECUTOR= Executors.newSingleThreadScheduledExecutor();
	}

	
	
	private PipeMtpProviderImpl(PipeMtpProviderImpl other) {
		super();
		this.other = other;
		_EXECUTOR= Executors.newSingleThreadScheduledExecutor();
	}

	

	/**
	 * @return the other
	 */
	public PipeMtpProviderImpl getOther() {
		return other;
	}

	public void indicateLinkUp()
	{
		if(this.listener!=null)
			this.listener.linkUp();
	}

	public void indicateLinkDown()
	{
		if(this.listener!=null)
			this.listener.linkDown();
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.MTPProvider#addMtpListener(org.mobicents.protocols.ss7.stream.MTPListener)
	 */
	public void addMtp3Listener(Mtp3Listener lst) {
		listener = lst;

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.MTPProvider#removeMtpListener(org.mobicents.protocols.ss7.stream.MTPListener)
	 */
	public void removeMtp3Listener(Mtp3Listener lst) {
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
		if(_EXECUTOR!=null)
		{
			_EXECUTOR.shutdown();
			_EXECUTOR = null;
		}

	}
	private ScheduledExecutorService _EXECUTOR ;
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
	public void start() throws StartFailedException, IllegalStateException {
		// TODO Auto-generated method stub
		
	}
}
