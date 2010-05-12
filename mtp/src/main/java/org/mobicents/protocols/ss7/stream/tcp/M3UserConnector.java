/**
 * 
 */
package org.mobicents.protocols.ss7.stream.tcp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.stream.HDLCHandler;
import org.mobicents.protocols.ss7.stream.MTPListener;
import org.mobicents.protocols.ss7.stream.tlv.LinkStatus;
import org.mobicents.protocols.ss7.stream.tlv.TLVInputStream;
import org.mobicents.protocols.ss7.stream.tlv.TLVOutputStream;
import org.mobicents.protocols.ss7.stream.tlv.Tag;


/**
 * Connector on remote side. This class connects to M3UserAgent
 * @author baranowb
 *
 */
public class M3UserConnector extends MTPProviderImpl implements Runnable{
	public static final String _PROPERTY_IP = "server.ip";
	public static final String _PROPERTY_PORT = "server.port";

	private static final Logger logger = Logger.getLogger(M3UserConnector.class);
	
	private List<MTPListener> listeners = new ArrayList<MTPListener>();
	private Properties properties = new Properties();
	//used to indicate state of link
	private boolean linkUp = false;
	private boolean runnable;
	
	
	public M3UserConnector(Properties properties) {
		super();
		this.properties.putAll(properties);
		//wont send empty buffer
		this.txBuff.limit(0);

	}

	public void addMtpListener(MTPListener lst) {

		if (lst == null) {
			throw new NullPointerException("Listener must not be null.");
		}
		listeners.add(lst);
	}

	public void removeMtpListener(MTPListener lst) {
		if (lst == null) {
			throw new NullPointerException("Listener must not be null.");
		}
		if (!listeners.remove(lst)) {
			throw new IllegalArgumentException(
					"Listener is not in registered: " + lst);
		}
	}

	public void send( byte[] msg) throws IOException {
		if(!linkUp)
		{
			throw new IOException("Link is not up!");
			
		}
		TLVOutputStream tlv = new TLVOutputStream();
		tlv.writeData(msg);
		synchronized (this.writeSelector) { 
			this.hdlcHandler.addToTxBuffer(ByteBuffer.wrap(tlv.toByteArray()));
			this.writeSelector.notify();
		}
		
	}

	@Override
	public void start() throws StartFailedException, IllegalStateException {
		
		if(this.streamFuture!=null)
		{
			throw new IllegalStateException("Provider is already started!");
		}
		
		readProperties();
		initiateConnection();
		this.runnable = true;
		this.streamFuture=streamExecutor.submit(this);
		
		

	}
	public void stop()  throws IllegalStateException 
	{
		if(this.streamFuture!=null)
		{
			//throw new IllegalStateException("Provider already stoped!");
			return;
		}else
		{
			this.runnable = false;
			
		}
		
			
	

	}
	
	private void stopClient()
	{
		if(streamFuture!=null)
		{
			streamFuture.cancel(false);
			streamFuture = null;
		}
		this.listeners.clear();

		try {
			this.socketChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.socketChannel = null;
		this.connected = false;
	}
	
	private void readProperties() {
		serverPort = Integer.parseInt(properties.getProperty(_PROPERTY_PORT, "" + serverPort));
		serverAddress = properties.getProperty(_PROPERTY_IP, "" + serverAddress);
		
	}

	private String serverAddress = "127.0.0.1";
	private int serverPort = 1354;
	// we need this since lower layer is very time sensitivie, lets deliver on
	// different thread.
	//private ExecutorService executor = Executors.newFixedThreadPool(5);
	// client part for streaming
	private Selector connectSelector;
	private Selector writeSelector;
	private Selector readSelector;
	private ByteBuffer readBuff = ByteBuffer.allocate(8192);
	private ByteBuffer txBuff = ByteBuffer.allocate(8192);
	private ExecutorService streamExecutor = Executors.newSingleThreadExecutor();
	private Future streamFuture;
	private boolean connected = false;
	private SocketChannel socketChannel;
	// private LinkedList<ByteBuffer> txBuffer = new LinkedList<ByteBuffer>();
	private HDLCHandler hdlcHandler = new HDLCHandler();

	
	
	
	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	private void initiateConnection() {
		try {
			if (this.socketChannel != null) {

				try {
					this.socketChannel.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			this.socketChannel = SocketChannel.open();
			this.socketChannel.configureBlocking(false);
			this.connectSelector = SelectorProvider.provider().openSelector();
			this.writeSelector = SelectorProvider.provider().openSelector();
			this.readSelector = SelectorProvider.provider().openSelector();
			this.socketChannel.register(this.connectSelector,
					SelectionKey.OP_CONNECT);

			// Kick off connection establishment
			this.socketChannel.connect(new InetSocketAddress(
					this.serverAddress, this.serverPort));
			this.connectSelector.select();
			if (!this.socketChannel.finishConnect()) {
				throw new RuntimeException(
						"Failed to finish connection procedure!");
			}
			this.socketChannel
					.register(this.readSelector, SelectionKey.OP_READ);
			this.socketChannel.register(this.writeSelector,
					SelectionKey.OP_WRITE);
			connected = true;

		} catch (Exception e) {
			if(logger.isDebugEnabled())
			{
				e.printStackTrace();
			}else
			{
				logger.info("Failed to connect due to: "+e.getMessage());
			}
		} finally {
			try {
				if (!connected) {
					// linkDown();
					if (this.socketChannel != null) {
						try {
							this.socketChannel.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						this.socketChannel = null;
					}
					if(this.connectSelector != null)
					{
						this.connectSelector.close();
					}
					
					if(this.writeSelector != null)
					{
						this.writeSelector.close();
					}
					
					if(this.readSelector != null)
					{
						this.readSelector.close();
					}
					

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		try {

			while (runnable) {

				try {
					if (!connected) {
						initiateConnection();
						if (!connected) {
							if (logger.isEnabledFor(Level.ERROR)) {
								logger
										.error("Faield to connect to stream server at "
												+ this.serverAddress
												+ ":"
												+ this.serverPort);
							}
							Thread.currentThread().sleep(2000);
						}
						continue;
					}
					Iterator selectedKeys = null;

					// else we try I/O ops.
					if (this.readSelector.selectNow() > 0) {

						selectedKeys = this.readSelector.selectedKeys()
								.iterator();
						// operate on keys set

						performKeyOperations(selectedKeys);

					}

					if (this.writeSelector.selectNow() > 0) {
						
						selectedKeys = this.writeSelector.selectedKeys()
								.iterator();
						// operate on keys set

						performKeyOperations(selectedKeys);

					}
//					synchronized (this.writeSelector) {
//						this.writeSelector.wait(5);
//					}
					
				} catch (Exception ee) {
					ee.printStackTrace();
					logger.error("Something failed: ", ee);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stopClient();
	}

	private void performKeyOperations(Iterator selectedKeys) throws IOException {
		while (selectedKeys.hasNext()) {
			SelectionKey key = (SelectionKey) selectedKeys.next();
			//selectedKeys.remove();

			if (!key.isValid()) {
				// handle disconnect here?
				logger.error("Key has become invalid: "+key);
				continue;
			}

			// Check what event is available and deal with it
			if (key.isReadable()) {
				this.read(key);
			} else if (key.isWritable()) {
				this.write(key);
			}
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
			// the selection key and close the channel.
			if(logger.isDebugEnabled())
			{
				e.printStackTrace();
			}
			handleClose(key);
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			handleClose(key);
			return;
		}

		ByteBuffer[] readResult = null;
		this.readBuff.flip();
		if(logger.isInfoEnabled())
		{
			logger.info("Received data: "+this.readBuff);
		}
		while ((readResult = this.hdlcHandler.processRx(this.readBuff)) != null) {

			for (ByteBuffer b : readResult) {
				
				//here we can have link status or msg
				if(logger.isInfoEnabled())
				{
					logger.info("Processed data: "+b);
				}
				TLVInputStream tlvInputStream = new TLVInputStream(new ByteArrayInputStream(b.array()));
				int tag = tlvInputStream.readTag();
				if(tag == Tag._TAG_LINK_DATA)
				{
					//this can happen if link goes up before we are;
					if(!linkUp)
					{
						this.linkUp();
					}
					this.receive(tlvInputStream.readLinkData());
				}else if (tag == Tag._TAG_LINK_STATUS)
				{
					LinkStatus ls = tlvInputStream.readLinkStatus();
					switch(ls)
					{
					case LinkDown:
						this.linkDown();
						continue;
						//break;
					case LinkUp:
						this.linkUp();
						continue;
					}
				}else
				{
					logger.warn("Received weird message!");
				}
				
			}
		}
		this.readBuff.clear();

	}

	private void linkDown() {
		//FIXME: Proper acctions here?
		if(logger.isInfoEnabled())
		{
			logger.info("Received LinkDown!");
		}
		this.linkUp = false;
		//this.txBuff.clear();
		//this.txBuff.limit(0);
		//this.readBuff.clear();
		//this.readBuff.limit(0);
		
		for (MTPListener lst : listeners) {
			try {
				lst.linkDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	private void linkUp() {
		//this.txBuff.clear();
		//this.txBuff.limit(0);
		//this.readBuff.clear();
		//this.readBuff.limit(0);
		if(logger.isInfoEnabled())
		{
			logger.info("Received LinkUp!");
		}
		this.linkUp = true;
		for (MTPListener lst : listeners) {
			try {
				lst.linkUp();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}

	private void write(SelectionKey key)  {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		if (txBuff.remaining() > 0) {

			try {
				socketChannel.write(txBuff);
			} catch (IOException e) {
				
				if(logger.isDebugEnabled())
				{
					e.printStackTrace();
				}
				handleClose(key);
				return;
			}
			if (txBuff.remaining() > 0) {
				// buffer filled.
				return;
			} else {

			}
		}
	
		if (!this.hdlcHandler.isTxBufferEmpty()) {

			txBuff.clear();
			this.hdlcHandler.processTx(txBuff);
			txBuff.flip();
			try {
				socketChannel.write(txBuff);
			} catch (IOException e) {
				
				if(logger.isDebugEnabled())
				{
					e.printStackTrace();
				}
				handleClose(key);
				return;
			}

			if (txBuff.remaining() > 0) {
				// ... or the socket's buffer fills up
				return;
			}


		}

	}

	private void handleClose(SelectionKey key) {
		linkDown();
		try {

			SocketChannel socketChannel = (SocketChannel) key.channel();
			key.cancel();
			try {
				socketChannel.close();
			} catch (IOException e) {
				
				if(logger.isDebugEnabled())
				{
					e.printStackTrace();
				}
			}
			
		} finally {
			// linkDown();
			connected = false;
			//synchronized (this.hdlcHandler) {
			synchronized (this.writeSelector) {
				// this is to ensure buffer does not have any bad data.
				// this.txBuffer.clear();
				this.hdlcHandler.clearTxBuffer();

			}
		}
		return;
	}

	public void receive(byte[] arg2) {

		// FIXME: add si/ssi decode?
		//this.executor.execute(new DeliveryHandler(0, 0, arg2));
		new DeliveryHandler( arg2).run();

	}

	private class DeliveryHandler implements Runnable {


		private byte[] msg;

		public DeliveryHandler( byte[] msg) {
			super();

			this.msg = msg;
		}

		public void run() {

			for (MTPListener lst : listeners) {
				try {
					lst.receive( msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

}
