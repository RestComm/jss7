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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;
import org.mobicents.protocols.stream.api.tcp.StreamState;
import org.mobicents.protocols.stream.api.tcp.TCPStream;
import org.mobicents.protocols.stream.impl.SelectorProvider;

/**
 * Simple class for M3 User Adaptation layer - commonly refered as M3 User
 * Agent. Instance of this class resides on Signalling Gateway (SG) and relies
 * two types of information to remote peers:
 * <ul>
 * <li>link metadata - state(only for now) congestion indications, availability,
 * route etc.</li>
 * <li>MSUs</li>
 * </ul>
 * 
 * @author baranowb
 * 
 */
public class M3UserAdaptation {

//	private static final Logger logger = Logger.getLogger(M3UserAdaptation.class);
//	
//	private StreamSelector streamSelector;
//	private byte[] BUFFER = new byte[8192];
//	// MTP3 selector and stream
//	//private StreamSelector mtpStreamSelector;
//	private Stream mtpStream;
//	private LinkedList<byte[]> toMtpBuffer = new LinkedList<byte[]>();
//	
//	//mtp
//	private int dpc,opc;
//	private List<Mtp1> linkSet;
//	private String linkSetName;
//
//	// net protocol selector and stream.
//	//private StreamSelector inetConnectionSelector;
//	private TCPStream inetConnectionStream;
//	private InetSocketAddress inetConnectionAddress = new InetSocketAddress("127.0.0.1", 1345);
//	private LinkedList<byte[]> toInetConnectionBuffer = new LinkedList<byte[]>();
//	// private String hostName = "127.0.0.1";
//	// private int hostPort = 1345;
//
//	// run part;
//	private Thread runThread;
//	private boolean run = false;
//
//	protected void initMtp3() throws IOException {
//		// init mtp1;
//		if(this.linkSet==null || this.linkSet.size() ==0)
//		{
//			throw new IllegalArgumentException("Wrong linkset: "+this.linkSet);
//		}
//		// init mtp2;
//		
//		List<Mtp2> mtp2Links = Mtp2Factory.getInstance().createMtpLinkSet(this.linkSet, this.linkSetName);
//		// init mtp3;
//		//this.mtpStream = Mtp3Factory.getInstance().createMtp(mtp2Links, linkSetName, opc, dpc, selectorFactory);
//		
//		// selector and register
//		//this.mtpStreamSelector = SelectorProvider.create();
//		this.streamSelector.register(this.mtpStream);
//		this.mtpStream.open();
//	}
//	
//	protected void initTransport() throws IOException
//	{
//		//init some protocol, for now its tcp
//		//this.inetConnectionSelector = SelectorProvider.create();
//		//create server stream;
//		//this.inetConnectionStream = TCPStreameFactory.create(this.inetConnectionAddress);
//		
//		//register
//		this.streamSelector.register(this.inetConnectionStream);
//		
//		//register listener in mtp3 to get link metadata info
//		//FIXME:
//		
//		//open stream
//		this.inetConnectionStream.open();
//	}
//	
//	/**
//	 * @return the hostName
//	 */
//	public String getHostName() {
//		return inetConnectionAddress.getHostName();
//	}
//
//	/**
//	 * @param hostName
//	 *            the hostName to set
//	 */
//	public void setHostName(String hostName) {
//		this.inetConnectionAddress = new InetSocketAddress(hostName, this.inetConnectionAddress.getPort());
//	}
//
//	/**
//	 * @return the hostPort
//	 */
//	public int getHostPort() {
//		return this.inetConnectionAddress.getPort();
//	}
//
//	/**
//	 * @param hostPort
//	 *            the hostPort to set
//	 */
//	public void setHostPort(int hostPort) {
//		this.inetConnectionAddress = new InetSocketAddress(this.inetConnectionAddress.getAddress(), hostPort);
//	}
//	
//	
//
//	/**
//	 * @return the dpc
//	 */
//	public int getDpc() {
//		return dpc;
//	}
//
//	/**
//	 * @param dpc the dpc to set
//	 */
//	public void setDpc(int dpc) {
//		this.dpc = dpc;
//	}
//
//	/**
//	 * @return the opc
//	 */
//	public int getOpc() {
//		return opc;
//	}
//
//	/**
//	 * @param opc the opc to set
//	 */
//	public void setOpc(int opc) {
//		this.opc = opc;
//	}
//
//	/**
//	 * @return the linkSet
//	 */
//	public Collection<Mtp1> getLinkSet() {
//		return linkSet;
//	}
//
//	/**
//	 * @param linkSet the linkSet to set
//	 */
//	public void setLinkSet(List<Mtp1> linkSet) {
//		this.linkSet = linkSet;
//	}
//
//	/**
//	 * @return the linkSetName
//	 */
//	public String getLinkSetName() {
//		return linkSetName;
//	}
//
//	/**
//	 * @param linkSetName the linkSetName to set
//	 */
//	public void setLinkSetName(String linkSetName) {
//		this.linkSetName = linkSetName;
//	}
//
//	public void start() throws IOException, StartFailedException {
//		try{
//		this.streamSelector = SelectorProvider.create();
//		initMtp3();
//		initTransport();
//		this.run = true;
//		Runner r = new Runner();
//		//FIXME: add ss7 wide thread group!
//		this.runThread = new Thread(r,"org.mobicents.ss7.M3-UA");
//		this.runThread.start();
//		}catch(IOException e)
//		{
//			e.printStackTrace();
//			stop();
//			throw new IOException();
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//			stop();
//			throw new StartFailedException(e);
//		}
//		
//	}
//
//	public void stop() {
//		this.run = false;
//		//give chance to end loop.
//		try {
//			Thread.currentThread().sleep(1500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(this.mtpStream!=null)
//		{
//			try{
//			this.mtpStream.close();
//			//this.mtpStreamSelector.close();
//			this.mtpStream = null;
//			//this.mtpStreamSelector = null;
//			}catch(Exception e)
//			{
//				//
//			}
//		}
//		if(this.inetConnectionStream!=null)
//		{
//			try{
//			this.inetConnectionStream.close();
//			//this.inetConnectionSelector.close();
//			this.inetConnectionStream = null;
//			//this.inetConnectionSelector = null;
//			}catch(Exception e)
//			{
//				//
//			}
//		}
//		
//		if(this.streamSelector!=null)
//		{
//			try{
//				this.streamSelector.close();
//				this.streamSelector = null;
//			}catch(Exception e)
//			{
//				//
//			}
//		}
//		
//		this.runThread = null;
//	}
//
//	
//	
//	private class Runner implements Runnable {
//
//		public void run() {
//			Collection<SelectorKey> selected;
//			Iterator<SelectorKey> iterator;
//			SelectorKey key;
//			Stream s;
//			while(run)
//			{
//				
//				try {
//					selected = streamSelector.selectNow();
//					if(selected.size()>0)
//					{
//						iterator = selected.iterator();
//						//one stream, easy
//						while(iterator.hasNext())
//						{
//							key = iterator.next();
//							s = key.getStream();
//							if(s == mtpStream)
//							{
//								if(key.isReadable())
//								{
//									//read data from mtp3
//									int count = mtpStream.read(BUFFER);
//									
//									if(inetConnectionStream.getState() == StreamState.INSERVICE)
//									{
//										byte[] data = new byte[count];
//										System.arraycopy(BUFFER, 0, data, 0, count);
//										toInetConnectionBuffer.add(data);
//									}else
//									{
//										logger.warn("Received data over mtp, but stream is not up!");
//									}
//								}
//								
//								if(key.isWriteable() && !toMtpBuffer.isEmpty())
//								{
//									//FIXME
//									//check state of ring buffer 
////									int freeSendSpace = mtpStream.getFreeSpace();
////									//if we are here, its not congested!
////									if(mtpStream.isLinkUp())
////									{
////										while(freeSendSpace>0 && toMtpBuffer.size()>0)
////										{
////											byte[] data =  toMtpBuffer.removeFirst();
////											int writeCount = mtpStream.write(data);
////											if(writeCount!=data.length)
////											{
////												logger.error("Wrote: "+writeCount+" to mtp layer, required: "+writeCount);
////											}
////											freeSendSpace --;
////										}
////									}else
////									{
////										//do nothing, link state callback will do all whats needed
////									}
//								}
//								
//								//FIXME add cehck for congestion
//							}else if(s == inetConnectionStream)
//							{
//								if(key.isReadable())
//								{
//									//read data from mtp3
//									int count = inetConnectionStream.read(BUFFER);
//									if(mtpStream.isLinkUp())
//									{
//										byte[] data = new byte[count];
//										System.arraycopy(BUFFER, 0, data, 0, count);
//										toMtpBuffer.add(data);
//									}else
//									{
//										//discard
//										logger.warn("Received data over stream, but mtp is not up!");
//									}
//								}
//								
//								if(key.isWriteable() && !toInetConnectionBuffer.isEmpty())
//								{
//								
//										while(inetConnectionStream.getState() == StreamState.INSERVICE && toInetConnectionBuffer.size()>0)
//										{
//											byte[] data =  toInetConnectionBuffer.removeFirst();
//											int writeCount = inetConnectionStream.write(data);
//											if(writeCount!=data.length)
//											{
//												logger.error("Wrote: "+writeCount+" to stream, required: "+writeCount);
//											}
//										}
//								}
//								
//								//clear buff?
//								if(inetConnectionStream.getState() != StreamState.INSERVICE)
//								{
//									toInetConnectionBuffer.clear();
//								}
//							}else
//							{
//								//ok, this is totaly wrong;
//								logger.error("Unknown stream, this should not happen! "+s);
//							}
//							
//						}
//					}
//				} catch (IOException e) {
//					
//					e.printStackTrace();
//					run = false;
//				}
//				
//		
//			}
//
//		}
//
//	}
}
