package org.mobicents.protocols.ss7.stream.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.stream.InterceptorHook;
import org.mobicents.protocols.ss7.stream.LinkStateProtocol;
import org.mobicents.protocols.ss7.stream.StreamForwarder;

public class M3UserAgent implements StreamForwarder, Runnable {
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
	private ArrayList<ByteBuffer> dataToSend = new ArrayList<ByteBuffer>();
	//one that we use to send :)
	//private ByteBuffer txBuff = ByteBuffer.allocate(8192);

	private Future runFuture;

	private boolean runnable;
	private LinkStateProtocol linkStateProtocol;

	// /////////////////
	// Some statics //
	// ////////////////
	

	public M3UserAgent() {
		super();
		// wont send empty buffer
		//this.txBuff.limit(0);
		this.linkStateProtocol = new LinkStateProtocol();
		this.linkStateProtocol.setStreamForwarder(this);
	}

	// ///////////////////////////
	// StreamForwarder method  //
	// //////////////////////////

	public String getLocalAddress() {
		return this.address.toString();
	}

	public int getLocalPort() {
		return this.port;
	}

	public void setLocalAddress(String address) throws UnknownHostException {
		this.address = InetAddress.getAllByName(address)[0];

	}

	public void setLocalPort(int port) {
		if (port > 0) {
			this.port = port;
		} else {
			// do nothing, use def
		}

	}

	public void setMtp3(Mtp3 mtp) {
		this.linkStateProtocol.setMtp3(mtp);

	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#addMtp3Listener(org.mobicents.protocols.ss7.stream.MTPListener)
	 */
	public void addMtp3Listener(Mtp3Listener lst) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#getRemoteAddress()
	 */
	public String getRemoteAddress() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#getRemotePort()
	 */
	public int getRemotePort() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#removeMtp3Listener(org.mobicents.protocols.ss7.stream.MTPListener)
	 */
	public void removeMtp3Listener(Mtp3Listener lst) {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#setRemoteAddress(java.lang.String)
	 */
	public void setRemoteAddress(String address) throws UnknownHostException {
		throw new UnsupportedOperationException();
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#setRemotePort(int)
	 */
	public void setRemotePort(int port) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#streamData(java.nio.ByteBuffer)
	 */
	public void streamData(ByteBuffer data) {
		//FIXME: Amit/Oleg this has to be changed to something else!
		//this.txBuff.put(data);
		byte[] ddd = new byte[data.limit()];
		System.arraycopy(data.array(), 0, ddd, 0, data.limit());
		
		
		ByteBuffer toSendData = LinkStateProtocol.copyToPosition(data);
		
		this.dataToSend.add(toSendData);
		
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
		if (logger.isInfoEnabled()) {
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

	public void run() {
		while (runnable) {
			try {
				Iterator selectedKeys = null;

				// Wait for an event one of the registered channels
				if (!connected) {

					// block till we have someone subscribing for data.
					this.connectSelector.select();

					selectedKeys = this.connectSelector.selectedKeys().iterator();
					// operate on keys set
					performKeyOperations(selectedKeys);

					// } else if (linkUp) {
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

									}
			} catch (ClosedSelectorException cse) {
				cse.printStackTrace();
				// check for server selector?
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
			// THIS MUST BE PRESENT!
			selectedKeys.remove();

			if (!key.isValid()) {
				// handle disconnect here?
				if (logger.isInfoEnabled()) {
					logger.info("Key became invalid: " + key);
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
		if (connected) {
			if (logger.isInfoEnabled()) {
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

		// if (connected) {
		// serverSocketChannel.close();
		// return;
		// }
		// lets strean state
		this.linkStateProtocol.transportUp();
	}

	private void write(SelectionKey key) throws IOException {

		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Write until there's not more data ?
		while (this.dataToSend.size() > 0) {
			ByteBuffer txBuff = this.dataToSend.get(0);
			if(txBuff == null)
			{
				this.dataToSend.remove(0);
				continue;
			}
			while (txBuff.remaining() > 0) {
				int sent = socketChannel.write(txBuff);
				if (sent == 0) {
					// buffer is filled?
					// lets move content thats left
					return;
				}

			}
			this.dataToSend.remove(0);
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
	

		// This will read everything, and if there is incomplete frame, it will
		// retain its partial content
		// so on next read it can continue to decode!
		this.readBuff.flip();
		
		try{
			this.linkStateProtocol.streamDataReceived(this.readBuff);
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		this.readBuff.clear();
		// this.layer3.send(si, ssf, this.readBuff.array());

	}

	private void handleClose(SelectionKey key) {
		if (logger.isDebugEnabled()) {
			logger.debug("Handling key close operations: " + key);
		}
		
		try {
			disconnect();
		} finally {
			// linkDown();
			// connected = false;
			// synchronized (this.hdlcHandler) {
			synchronized (this.writeSelector) {
				// this is to ensure buffer does not have any bad data.
				// this.txBuffer.clear();
				

			}
		}
		return;
	}

	private void disconnect() {
		this.linkStateProtocol.transportDown();
		this.linkStateProtocol.reset();
		
		//this.txBuff.limit(0);
		this.dataToSend.clear();
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


	public void setInterceptorHook(InterceptorHook ih) {
		this.linkStateProtocol.setInterceptorHook(ih);
		
	}

	public boolean isConnected() {
		return this.connected;
	}
	
	public LinkStateProtocol getLinkStateProtocol()
	{
			return this.linkStateProtocol;
	}
}
