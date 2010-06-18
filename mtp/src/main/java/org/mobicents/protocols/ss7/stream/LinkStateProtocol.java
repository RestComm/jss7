package org.mobicents.protocols.ss7.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.MtpUser;
import org.mobicents.protocols.ss7.stream.tlv.LinkStatus;
import org.mobicents.protocols.ss7.stream.tlv.TLVInputStream;
import org.mobicents.protocols.ss7.stream.tlv.TLVOutputStream;
import org.mobicents.protocols.ss7.stream.tlv.Tag;

/**
 * Utility class for all logic regarding link state protocol. This class hides
 * TLV details and how messages are encoded. <br>
 * It can be reused by different stream forwarders.
 * 
 * @author baranowb
 * 
 */
public class LinkStateProtocol implements MtpUser {

	private static final Logger logger = Logger.getLogger(LinkStateProtocol.class);
	// /////////////////
	// Some statics //
	// ////////////////
	private static final byte[] _LINK_STATE_UP;
	private static final byte[] _LINK_STATE_DOWN;
	static {
		TLVOutputStream tlv = new TLVOutputStream();
		try {
			tlv.writeLinkStatus(LinkStatus.LinkUp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_LINK_STATE_UP = tlv.toByteArray();
		tlv.reset();
		try {
			tlv.writeLinkStatus(LinkStatus.LinkDown);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_LINK_STATE_DOWN = tlv.toByteArray();

	}

	
	public LinkStateProtocol() {
		super();
		this.reset();
	}

	private StreamForwarder streamForwarder;
	private HDLCHandler hdlcHandler = new HDLCHandler();
	private ByteBuffer txBuff = ByteBuffer.allocate(8192);
	// state of link
	private boolean linkUp = false;
	private boolean transportUp = false;

	// hook for interception :)
	private InterceptorHook hook;

	
	
	
	// ////////////////
	// Provider part //
	// ////////////////
	private ArrayList<MTPListener> mtpListeners = new ArrayList<MTPListener>();

	public void addMTPListener(MTPListener lst) {
		if (lst != null && !this.mtpListeners.contains(lst)) {
			this.mtpListeners.add(lst);
		}
	}

	public void removeMTPListener(MTPListener lst) {
		if (lst != null && this.mtpListeners.contains(lst)) {
			this.mtpListeners.remove(lst);
		}
	}

	// ////////////////
	// MTP3 part... //
	// ////////////////
	private Mtp3 mtp3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.MtpUser#linkDown()
	 */
	public void linkDown() {
		if (this.mtp3 != null) {
			//this is MTP3 side, we need to stream it if we are connected!
			this.pushData(_LINK_STATE_DOWN);
		} else {
			//this is provider side
			for(MTPListener lst:this.mtpListeners)
			{
				try
				{
					lst.linkDown();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		this.linkUp = false;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.MtpUser#linkUp()
	 */
	public void linkUp() {
		if (this.mtp3 != null) {
			//this is MTP3 side, we need to stream it if we are connected!
			this.pushData(_LINK_STATE_UP);
		} else {
			//this is provider side, linkup means also connection up :)
			for(MTPListener lst:this.mtpListeners)
			{
				try
				{
					lst.linkUp();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		this.linkUp = true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.MtpUser#receive(byte[])
	 */
	public void receive(byte[] msgBuff) {
		// here we have proper MSU from mtp3
		try {
			//check hook
			if(this.hook!=null && this.hook.intercepted(msgBuff))
			{
				return;
			}
			// trigger logic to handle that.
			this.streamDataToSend(msgBuff);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.mtp.MtpUser#receive(java.lang.String)
	 */
	public void receive(String msg) {
		// not used, will be removed
		throw new UnsupportedOperationException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.mtp.MtpUser#setMtp3(org.mobicents.protocols
	 * .ss7.mtp.Mtp3)
	 */
	public void setMtp3(Mtp3 mtp) {
		
		if(this.mtp3 == null)
		{
			
		}else
		{
			if(mtp == null)
			{
				this.mtp3.setUserPart(null);
			}else
			{
				this.mtp3.setUserPart(this);
			}
		}
		this.mtp3 = mtp;
		if(this.mtp3!=null)
		{
			this.mtp3.setUserPart(this);
		}
	}

	// /////////////////////////////
	// LinkStateProtocol methods //
	// /////////////////////////////
	/**
	 * Method called by stream handlers, Indicates that it received data from
	 * stream(TCP/UDP) and we have to process it
	 * 
	 * @param data
	 */
	public void streamDataReceived(ByteBuffer data) {
		// we received some data from stream
		// this means that we are connected, lets consume
		// consume;

		ByteBuffer[] readResult = null;

		while ((readResult = this.hdlcHandler.processRx(data)) != null) {
			for (ByteBuffer b : readResult) {

				try {
					TLVInputStream tlvInputStream = new TLVInputStream(new ByteArrayInputStream(b.array()));
					int tag = tlvInputStream.readTag();
					if (tag == Tag._TAG_LINK_DATA) {
						byte[] linkData = tlvInputStream.readLinkData();

						if (this.mtp3 != null) {
							if(linkUp)
							{
								this.mtp3.send(linkData);
							}else
							{
								//it just came after link went down. Do nothing?
							}
						} else {
							if(!linkUp)
							{
								//?
								linkUp();
							}
							for(MTPListener lst:this.mtpListeners)
							{
								try{
									lst.receive(linkData);
								}catch(Exception e)
								{
									e.printStackTrace();
								}
							}
						}

					} else if (tag == Tag._TAG_LINK_STATUS) {
						LinkStatus ls = tlvInputStream.readLinkStatus();
						switch (ls) {
						case LinkDown:
							if (this.mtp3 != null) {
								//should nto happen :)
								logger.warn("MTP3: Received link down!");
							} else {
								linkDown();
							}
							break;
						case LinkUp:
							if (this.mtp3 != null) {
								//should nto happen :)
								logger.warn("MTP3: Received link up!");
							} else {
								linkUp();
							}
							break;
						case Query:
							if (this.mtp3 != null) {
								//querry
								if(this.linkUp)
								{
									pushData(_LINK_STATE_UP);
								}else
								{
									pushData(_LINK_STATE_DOWN);
								}
							} else {
								//should nto happen :)
								logger.warn("Provider: Received link status query!");
							}
							break;
						}
					} else {
						logger.warn("Received weird message! Tag: "+tag);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * This method is called once some entity wants to stream data.
	 * LinkStateProtocol performs following:
	 * <ul>
	 * <li>encode</li> - encode with tlv
	 * <li>pass to streamer</li> - pass encoded data to streamer, which forwards
	 * encoded byte[] to second peer.
	 * </ul>
	 * 
	 * @param data
	 */
	public void streamDataToSend(byte[] data) throws IOException {
		//check some preconditions?
		if (this.mtp3 != null) {
			//we reside on MTP side
			//this is called only when link is up
			if(transportUp)
			{
				TLVOutputStream tlo = new TLVOutputStream();
				tlo.writeData(data);
				pushData(tlo.toByteArray());
			}else
			{
				logger.warn("MTP3: Cannot send data, since transport is not UP! Link state: "+this.linkUp);
			}
		} else {
			//we are on provider side. This means remote is actaull MTP3 layer.
			
			if(this.linkUp && this.transportUp)
			{
				TLVOutputStream tlo = new TLVOutputStream();
				tlo.writeData(data);
				pushData(tlo.toByteArray());
			}else
			{
				throw new IOException("Cannot send data, connection["+transportUp+"] link["+linkUp+"] is down: ");
			}
		}
	}
	/**
	 * This method expects TLV encoded data!
	 * @param data
	 */
	private synchronized void  pushData(byte[] data) {
		//encode
		// this.txBuffer.add(ByteBuffer.wrap(data));
		ByteBuffer bb = ByteBuffer.allocate(data.length);
		bb.put(data);
		bb.flip();
		this.hdlcHandler.addToTxBuffer(bb);
		
		
		// while (!this.hdlcHandler.isTxBufferEmpty()) {
		if (!this.hdlcHandler.isTxBufferEmpty()) {

			// ByteBuffer buf = (ByteBuffer) txBuffer.get(0);
			txBuff.clear();
			try{
				this.hdlcHandler.processTx(txBuff);
			}catch(BufferOverflowException bbbb)
			{
				bbbb.printStackTrace();
			}
			txBuff.flip();
			this.streamForwarder.streamData(txBuff);
			
			//we get one shot? is that ok?
		}
		
	}

	/**
	 * @return the streamForwarder
	 */
	public StreamForwarder getStreamForwarder() {
		return streamForwarder;
	}

	/**
	 * @param streamForwarder
	 *            the streamForwarder to set
	 */
	public void setStreamForwarder(StreamForwarder streamForwarder) {
		this.streamForwarder = streamForwarder;
	}

	public void reset() {
		this.hdlcHandler = new HDLCHandler();
		this.txBuff.clear();
	}

	public void transportUp() {
		this.transportUp = true;
		
		if(this.mtp3!=null)
		{
			
			if(linkUp)
			{
				this.linkUp();
			}
		}else
		{
			if(!linkUp)
			{
				this.linkDown();
			}
		}
	}

	public void transportDown() {
		this.transportUp = false;
		if(this.mtp3!=null)
		{
			//nothing
		}else
		{
			if(linkUp)
			{
				linkDown();
			}
		}
	}
	
	public void setInterceptorHook(InterceptorHook ih)
	{
		this.hook = ih;
	}
	public static ByteBuffer copyToPosition(ByteBuffer data)
	{
		byte[] tw = new byte[data.limit()];
		byte[] source = data.array();
		System.arraycopy(source, 0, tw, 0, tw.length);
		return ByteBuffer.wrap(tw);
	}
}
