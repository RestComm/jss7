package org.mobicents.protocols.ss7.stream.tcp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.MtpUser;
import org.mobicents.protocols.ss7.stream.HDLCHandler;
import org.mobicents.protocols.ss7.stream.StreamForwarder;
import org.mobicents.protocols.ss7.stream.tlv.LinkStatus;
import org.mobicents.protocols.ss7.stream.tlv.TLVInputStream;
import org.mobicents.protocols.ss7.stream.tlv.TLVOutputStream;
import org.mobicents.protocols.ss7.stream.tlv.Tag;

public class M3UserAgent implements StreamForwarder, MtpUser, Runnable, M3UserAgentMBean {
	private static final Logger logger = Logger.getLogger(M3UserAgent.class);
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private int port = 1345;
	private InetAddress address;
	private ServerSocketChannel serverSocketChannel;
	private SocketChannel channel;
	private Selector readSelector;
	private Selector writeSelector;
	private Selector connectSelector;
	// we accept only one connection
	private boolean connected = false;
	private ByteBuffer readBuff = ByteBuffer.allocate(8192);
	private ByteBuffer txBuff = ByteBuffer.allocate(8192);

	private Mtp3 mtp;
	// private MTP mtp;
	private boolean linkUp = false;
	private Future runFuture;

	//
	private HDLCHandler hdlcHandler = new HDLCHandler();
	private boolean runnable;

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

	public M3UserAgent() {
		super();
		// wont send empty buffer
		this.txBuff.limit(0);
	}

	///////////////////////
	// Setters & Getters //
	///////////////////////
	
	public String getAddress() {
		return this.address.toString();
	}

	public int getPort() {
		return this.port;
	}

	public void setAddress(String address) throws UnknownHostException {
		this.address = InetAddress.getAllByName(address)[0];

	}

	public void setPort(int port) {
		if (port > 0) {
			this.port = port;
		} else {
			// do nothing, use def
		}

	}

	

	public void setMtp3(Mtp3 mtp) {
		this.mtp = mtp;
		if (mtp != null) {
			this.mtp.setUserPart(this);
		}

	}
	
	
	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}
	
	public void start() throws IOException {
		this.connectSelector = SelectorProvider.provider().openSelector();
		this.serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);

		// Bind the server socket to the specified address and port
		InetSocketAddress isa = new InetSocketAddress(this.address, this.port);
		serverSocketChannel.socket().bind(isa);

		// Register the server socket channel, indicating an interest in
		// accepting new connections
		serverSocketChannel.register(this.connectSelector, SelectionKey.OP_ACCEPT);
		if(logger.isInfoEnabled())
			{
				logger.info("Initiaited server on: " + this.address + ":" + this.port);
			}

		runnable = true;
		this.runFuture = this.executor.submit(this);

	}

	public void stop() {
		if (this.runFuture != null) {
			this.runFuture.cancel(false);
			this.runFuture = null;
			runnable = false;
		}
		if (this.connectSelector != null && this.connectSelector.isOpen()) {
			try {
				this.connectSelector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (this.serverSocketChannel != null) {
			try {
				this.serverSocketChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		disconnect();
	}

	public void run()
	{
		while(runnable)
		{
			try
			{
				Iterator selectedKeys = null;

				// Wait for an event one of the registered channels
				if (!connected) {

					
					// block till we have someone subscribing for data.
					this.connectSelector.select();

					selectedKeys = this.connectSelector.selectedKeys().iterator();
					// operate on keys set
					performKeyOperations(selectedKeys);

				//} else if (linkUp) {
				} else {
					// else we try I/O ops.
				
					if (this.readSelector.selectNow() > 0) {
						selectedKeys = this.readSelector.selectedKeys().iterator();
						// operate on keys set

						performKeyOperations(selectedKeys);

					}

					if (this.writeSelector.selectNow() > 0) {
						selectedKeys = this.writeSelector.selectedKeys().iterator();
						// operate on keys set

						performKeyOperations(selectedKeys);

					}

					if (hdlcHandler.isTxBufferEmpty()) {
//						synchronized(this.writeSelector)
//						{
//							this.writeSelector.wait(5);
//						}
						
					}
				}
			}catch(ClosedSelectorException cse)
			{
				cse.printStackTrace();
				//check for server selector?
				disconnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	private void performKeyOperations(Iterator<SelectionKey> selectedKeys) throws IOException {

		while (selectedKeys.hasNext()) {

			SelectionKey key = selectedKeys.next();
			//THIS MUST BE PRESENT!
			selectedKeys.remove();

			if (!key.isValid()) {
				// handle disconnect here?
				if(logger.isInfoEnabled()){
					logger.info("Key became invalid: "+key);
				}
				continue;
			}

			// Check what event is available and deal with it
			if (key.isAcceptable()) {

				this.accept(key);
			} else if (key.isReadable()) {

				this.read(key);
			} else if (key.isWritable()) {

				this.write(key);
			}
		}

	}
	
	private void accept(SelectionKey key) throws IOException {
		if(connected)
		{
			if(logger.isInfoEnabled())
			{
				logger.info("Second client not supported yet.");
			}
			
			return;
		}
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		
		
		this.channel = serverSocketChannel.accept();
		this.writeSelector = SelectorProvider.provider().openSelector();
		this.readSelector = SelectorProvider.provider().openSelector();
		Socket socket = channel.socket();
		
		this.channel.configureBlocking(false);
		this.channel.register(this.readSelector, SelectionKey.OP_READ);
		this.channel.register(this.writeSelector, SelectionKey.OP_WRITE);
		

		this.connected = true;
		
		if (logger.isInfoEnabled()) {
			logger.info("Estabilished connection with: " + socket.getInetAddress() + ":" + socket.getPort());
			
		}
		
		//if (connected) {
		//	serverSocketChannel.close();
		//	return;
		//}
		//lets strean state
		if(linkUp)
		{
			this.streamData(_LINK_STATE_UP);
		}else
		{
			//this.streamData(_LINK_STATE_DOWN);
		}
	}
	
	private void write(SelectionKey key) throws IOException {
		
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Write until there's not more data ?
		
		//while (!txBuffer.isEmpty()) {
		if(txBuff.remaining()>0)
		{
			int  sentDataCount = socketChannel.write(txBuff);
			
			if(txBuff.remaining()>0)
			{
				//buffer filled.
				return;
			}else
			{
				
			}
		}
		//while (!this.hdlcHandler.isTxBufferEmpty()) {
		if (!this.hdlcHandler.isTxBufferEmpty()) {

			//ByteBuffer buf = (ByteBuffer) txBuffer.get(0);
			txBuff.clear();

			this.hdlcHandler.processTx(txBuff);
			txBuff.flip();
			socketChannel.write(txBuff);

			//if (buf.remaining() > 0) {
			if(txBuff.remaining()>0)
			{
				// ... or the socket's buffer fills up
				return;
			}
			//buf.clear();
			//txBuff.clear();
			//txBuffer.remove(0);
			

		}

	}
	
	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// FIXME: we must ensure that we have whole frame here?
		// Clear out our read buffer so it's ready for new data
		this.readBuff.clear();

		// Attempt to read off the channel
		int numRead = -1;
		try {
			numRead = socketChannel.read(this.readBuff);
		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel
			e.printStackTrace();
			handleClose(key);
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			handleClose(key);
			return;
		}
		//pass it on.
		ByteBuffer[] readResult = null;

		//This will read everything, and if there is incomplete frame, it will retain its partial content
		//so on next read it can continue to decode!
		this.readBuff.flip();
		
		while((readResult = this.hdlcHandler.processRx(this.readBuff))!=null)
		{
			for(ByteBuffer b:readResult)
			{
				
				//byte sls = b.get();
				//byte linksetId = b.get();
				//this.layer3.send(sls,linksetId,si, ssf, b.array());
				TLVInputStream tlvInputStream = new TLVInputStream(new ByteArrayInputStream(b.array()));
				int tag = tlvInputStream.readTag();
				if(tag == Tag._TAG_LINK_DATA)
				{
					byte[] data = tlvInputStream.readLinkData();
					
					this.mtp.send( data);
				}else if (tag == Tag._TAG_LINK_STATUS)
				{
					LinkStatus ls = tlvInputStream.readLinkStatus();
					switch(ls)
					{
						case Query:
							if(this.linkUp)
							{
								this.streamData(_LINK_STATE_UP);
							}else
							{
								this.streamData(_LINK_STATE_DOWN);
							}
						
					}
				}else
				{
					logger.warn("Received weird message!");
				}
			}
		}
		this.readBuff.clear();
		//this.layer3.send(si, ssf, this.readBuff.array());

	}
	private void handleClose(SelectionKey key) {
		if(logger.isDebugEnabled())
        {
        	logger.debug("Handling key close operations: " + key);
		}
		linkDown();
		try {
			disconnect();
		} finally {
			// linkDown();
			//connected = false;
			// synchronized (this.hdlcHandler) {
			synchronized (this.writeSelector) {
				// this is to ensure buffer does not have any bad data.
				// this.txBuffer.clear();
				this.hdlcHandler.clearTxBuffer();

			}
		}
		return;
	}
	
	private void disconnect() {
		
		if (this.channel != null) {
			try {
				this.channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.channel = null;

		if (this.readSelector != null && this.readSelector.isOpen()) {
			try {
				this.readSelector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (this.writeSelector != null && this.writeSelector.isOpen()) {
			try {
				this.writeSelector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.connected = false;

		
	}
	/**
	 * Method used internaly - this one sends actuall data to remote end.
	 * @param data
	 */
	public void streamData(byte[] data) {
		//if(this.channel!=null)
		//	connected = this.channel.isConnected();
		
		if (!connected) {
			if (logger.isInfoEnabled()) {
				logger.info("There is no client interested in data stream, ignoring. Message should be retransmited.");

			}
			return;
		}

		// And queue the data we want written
		//synchronized (this.txBuffer) {
		//synchronized (this.hdlcHandler) {
		synchronized (this.writeSelector) {

			//this.txBuffer.add(ByteBuffer.wrap(data));
			ByteBuffer bb = ByteBuffer.allocate(data.length);
			bb.put(data);
			bb.flip();
			this.hdlcHandler.addToTxBuffer(bb);
			// Finally, wake up our selecting thread so it can make the required
			// changes
			this.writeSelector.wakeup();
		}
		
		
	}
	////////////
	// LAYER4 //
	////////////
	public void linkDown() {
		if (logger.isInfoEnabled()) {
			logger.info("Received L4 Down event from layer3.");
		}
		this.linkUp = false;
		//FIXME: proper actions here.
		//this.txBuff.clear();
		//this.txBuff.limit(0);
		//this.readBuff.clear();
		this.streamData(_LINK_STATE_DOWN);
	}

	public void linkUp() {
		if (logger.isInfoEnabled()) {
			logger.info("Received L4 Up event from layer3.");
		}
		this.linkUp = true;
		this.streamData(_LINK_STATE_UP);
	}
	public void receive(String msg)
	{
		this.receive(msg.getBytes());
		
	}
	
	public void receive( byte[] msgBuff) {


		// layer3 has something important, lets write.
		//if(linkUp)
		//{
			TLVOutputStream tlv = new TLVOutputStream();
			try {
				tlv.writeData(msgBuff);
				byte[] data = tlv.toByteArray();
				this.streamData(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		//}
	}

	
}
