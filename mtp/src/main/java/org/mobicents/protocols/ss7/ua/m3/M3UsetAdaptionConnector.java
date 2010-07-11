/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.ua.m3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;

import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.StreamSelector;
import org.mobicents.protocols.stream.api.tcp.StreamState;
import org.mobicents.protocols.stream.api.tcp.TCPStream;
import org.mobicents.protocols.stream.impl.SelectorProvider;
import org.mobicents.protocols.stream.impl.tcp.TCPStreameFactory;

/**
 * This class allows ASP and other "users" to connect to M3UA layer. Acts like a client
 * 
 * @author baranowb
 * 
 */
public class M3UsetAdaptionConnector implements MtpProvider{

	private static final Logger logger = Logger.getLogger(M3UsetAdaptionConnector.class);
	
	//addresses
	private InetSocketAddress localAddress = new InetSocketAddress("127.0.0.1", 4531);
	private InetSocketAddress remoteAddress = new InetSocketAddress("127.0.0.1", 1345);
	
	//stream part
	private StreamSelector inetConnectionSelector;
	private TCPStream inetConnectionStream;
	private byte[] BUFFER = new byte[8192];
	private LinkedList<byte[]> toStreamBuffer = new LinkedList<byte[]>();
	//mtp consumer
	private Mtp3Listener consumer;
	
	//thread
	private Thread runThread;
	private boolean run = false;
	/////////////////////
	// Setters/Getters //
	/////////////////////
	/**
	 * @return the hostName
	 */
	public String getM3UAHostName() {
		return remoteAddress.getHostName();
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setM3UAHostName(String hostName) {
		this.remoteAddress = new InetSocketAddress(hostName, this.remoteAddress.getPort());
	}

	/**
	 * @return the hostPort
	 */
	public int getM3UAHostPort() {
		return this.remoteAddress.getPort();
	}

	/**
	 * @param hostPort
	 *            the hostPort to set
	 */
	public void setM3UAHostPort(int hostPort) {
		this.remoteAddress = new InetSocketAddress(this.remoteAddress.getAddress(), hostPort);
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return localAddress.getHostName();
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.localAddress = new InetSocketAddress(hostName, this.localAddress.getPort());
	}

	/**
	 * @return the hostPort
	 */
	public int getHostPort() {
		return this.localAddress.getPort();
	}

	/**
	 * @param hostPort
	 *            the hostPort to set
	 */
	public void setHostPort(int hostPort) {
		this.localAddress = new InetSocketAddress(this.localAddress.getAddress(), hostPort);
	}
	

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#getMtpListener()
	 */
	public Mtp3Listener getMtpListener() {
		return this.consumer;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#removeMtpListener()
	 */
	public Mtp3Listener removeMtpListener() {
		Mtp3Listener l = this.consumer;
		this.consumer = null;
		return l;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#setMtpListener(org.mobicents.protocols.ss7.mtp.Mtp3Listener)
	 */
	public void setMtpListener(Mtp3Listener lst) {
		if(lst == null)
		{
			throw new IllegalArgumentException("Null value as MtpListener is not allowed.");
		}
		this.consumer = lst;
	}

	
	////////////////
	// Start/Stop //
	////////////////
	
	public void start() throws IOException, StartFailedException {
		try{
			this.inetConnectionSelector = SelectorProvider.create();
			this.inetConnectionStream = TCPStreameFactory.create(localAddress, remoteAddress);
			this.inetConnectionSelector.register(this.inetConnectionStream);
			
			this.inetConnectionStream.open();
			Runner r = new Runner();
			this.run = true;
			this.runThread = new Thread(r,"org.mobicents.ss7.M3-UA-Connector");
			this.runThread.start();
		}catch(IOException e)
		{
			e.printStackTrace();
			stop();
			throw new IOException();
		}catch(Exception e)
		{
			e.printStackTrace();
			stop();
			throw new StartFailedException(e);
		}
		
	}

	public void stop() {
		this.run = false;
		//give chance to end loop.
		try {
			Thread.currentThread().sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(this.inetConnectionStream!=null)
		{
			try{
			this.inetConnectionStream.close();
			//this.inetConnectionSelector.close();
			this.inetConnectionStream = null;
			//this.inetConnectionSelector = null;
			}catch(Exception e)
			{
				//
			}
		}
		
		if(this.inetConnectionSelector!=null)
		{
			try{
				this.inetConnectionSelector.close();
				this.inetConnectionSelector = null;
			}catch(Exception e)
			{
				//
			}
		}
		
		this.runThread = null;
	}

	
	
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#send(byte[])
	 */
	public int send(byte[] msu) {
		//FIXME: should this copy?
		if(this.inetConnectionStream.getState()!= StreamState.INSERVICE )
		{
			throw new IllegalStateException("Transport resource is not in service: "+this.inetConnectionStream.getState());
		}
		this.toStreamBuffer.add(msu);
		
		return msu.length;
	}




	private class Runner implements Runnable
	{

		public void run() {
			Collection<SelectorKey> selected;
			//Iterator<SelectorKey> iterator;
			while(run)
			{
				try
				{
					selected = inetConnectionSelector.selectNow();
					if(selected.size()>0 && inetConnectionStream.getState() == StreamState.INSERVICE)
					{
						//rock
						//single stream
						SelectorKey key = selected.iterator().next();
						if(key.isReadable())
						{
							//read data from mtp3
							int count = inetConnectionStream.read(BUFFER);
							
							if(inetConnectionStream.getState() == StreamState.INSERVICE)
							{
								byte[] data = new byte[count];
								System.arraycopy(BUFFER, 0, data, 0, count);
								try
								{
									consumer.receive(data);
								}catch(Exception e)
								{
									logger.warn("Caught exception while passing data to mtp user!",e);
								}
							}else
							{
								logger.warn("Received data over mtp, but stream is not up!");
							}
						}
						
						if(key.isWriteable())
						{
							//JIC check, so we dont have bad state.
							if(inetConnectionStream.getState() == StreamState.INSERVICE)
							{
								while(toStreamBuffer.size()>0)
								{
									byte[] data = toStreamBuffer.removeFirst();
								
									try
									{
										int writeCount = inetConnectionStream.write(data);
										if(writeCount!=data.length)
										{
											//this wont happen, unless Oleg optimizes something.
											logger.error("Wrote: "+writeCount+" to stream, required: "+writeCount);
										}
									}catch(Exception e)
									{
										logger.warn("Caught exception while passing data to mtp user!",e);
									}
								}
							}else
							{
								logger.warn("Received data over mtp, but stream is not up!");
								toStreamBuffer.clear();
							}
						}
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	
}
