/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.isup.impl.stack.timers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.mobicents.protocols.ss7.isup.ISUPEvent;
import org.mobicents.protocols.ss7.isup.ISUPListener;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.ISUPStack;
import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.ISUPStackImpl;
import org.mobicents.protocols.ss7.isup.impl.message.AbstractISUPMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.StreamSelector;
import org.mobicents.ss7.linkset.oam.Layer4;
import org.mobicents.ss7.linkset.oam.Linkset;
import org.mobicents.ss7.linkset.oam.LinksetSelector;
import org.mobicents.ss7.linkset.oam.LinksetStream;

/**
 * @author baranowb
 * 
 */
public abstract class EventTestHarness /*extends TestCase*/ implements ISUPListener {

	protected ISUPStack stack;
	protected ISUPProvider provider;

	protected TimerTestLinkset isupLinkSet;

	// events received by by this listener
	protected List<EventReceived> localEventsReceived;
	// events sent to remote ISUP peer.
	protected List<EventReceived> remoteEventsReceived;
	@Before
	public void setUp() throws Exception {
		
		this.isupLinkSet = new TimerTestLinkset("", 1, 2, 3);
		this.stack = new ISUPStackImpl();
		this.stack.configure(getSpecificConfig());
		this.provider = this.stack.getIsupProvider();
		this.provider.addListener(this);
		((Layer4) this.stack).add(this.isupLinkSet);
		this.stack.start();
		localEventsReceived = new ArrayList<EventTestHarness.EventReceived>();
		remoteEventsReceived = new ArrayList<EventTestHarness.EventReceived>();
	}

	@After
	public void tearDown() throws Exception {
		
	}


	protected void compareEvents(List<EventReceived> expectedLocalEvents, List<EventReceived> expectedRemoteEventsReceived) {
		
		if (expectedLocalEvents.size() != this.localEventsReceived.size()) {
			fail("Size of local events: " + this.localEventsReceived.size() + ", does not equal expected events: " + expectedLocalEvents.size()+"\n"+
					doStringCompare(localEventsReceived, expectedLocalEvents));
		}

		if (expectedRemoteEventsReceived.size() != this.remoteEventsReceived.size()) {
			fail("Size of remote events: " + this.remoteEventsReceived.size() + ", does not equal expected events: " + expectedRemoteEventsReceived.size()+"\n"+
					doStringCompare(remoteEventsReceived, expectedRemoteEventsReceived));
		}
		
		for (int index = 0; index < expectedLocalEvents.size(); index++) {
			assertEquals("Local received event does not match, index[" + index + "]", expectedLocalEvents.get(index), localEventsReceived.get(index));
		}

		for (int index = 0; index < expectedLocalEvents.size(); index++) {
			assertEquals("Remote received event does not match, index[" + index + "]", expectedRemoteEventsReceived.get(index), remoteEventsReceived.get(index));
		}
	}

	protected String doStringCompare(List lst1,List lst2)
	{
		StringBuilder sb = new StringBuilder();
		int size1  = lst1.size();
		int size2  = lst2.size();
		int count = size1;
		if(count<size2)
		{
			count = size2;
		}
		
		for(int index = 0;count>index;index++)
		{
			String s1 = size1>index ? lst1.get(index).toString() : "NOP";
			String s2 = size2>index ? lst2.get(index).toString() : "NOP";
			sb.append(s1).append(" - ").append(s2).append("\n");
		}
		return sb.toString();
	}
	
	
	public void onEvent(ISUPEvent event) {
		this.localEventsReceived.add(new MessageEventReceived(System.currentTimeMillis(), event));

	}

	public void onTimeout(ISUPTimeoutEvent event) {
		this.localEventsReceived.add(new TimeoutEventReceived(System.currentTimeMillis(), event));

	}

	// method implemented by test, to answer stack.
	protected void doAnswer() {
		ISUPMessage answer = getAnswer();
		int opc = isupLinkSet.getOpc();
		int dpc = isupLinkSet.getApc();
		int si = Mtp3._SI_SERVICE_ISUP;
		int ni = isupLinkSet.getNi();
		int sls = 0;
		int ssi = ni << 2;

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		// encoding routing label
		bout.write((byte) (((ssi & 0x0F) << 4) | (si & 0x0F)));
		bout.write((byte) dpc);
		bout.write((byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6)));
		bout.write((byte) (opc >> 2));
		bout.write((byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4)));

		try {
			byte[] message = ((AbstractISUPMessage) answer).encode();
			bout.write(message);
			byte[] msg = bout.toByteArray();
			this.isupLinkSet.rxBuffer = msg;
		} catch (Exception e) {

			e.printStackTrace();
			fail("Failed on receive message: " + e);
		}
	}

	protected void doWait(long t) throws InterruptedException {
		Thread.currentThread().sleep(t);
	}

	/**
	 * return orignial request
	 * @return
	 */
	protected abstract ISUPMessage getRequest();
	
	
	/**
	 * return answer to be sent.
	 * @return
	 */
	protected abstract ISUPMessage getAnswer();

	
	
	/**
	 * callback method, it returns specific configuration properties for stack
	 * 
	 * @return
	 */
	protected abstract Properties getSpecificConfig();

	protected class EventReceived {
		private long tstamp;

		/**
		 * @param tstamp
		 */
		public EventReceived(long tstamp) {
			super();
			this.tstamp = tstamp;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EventReceived other = (EventReceived) obj;

			if (tstamp != other.tstamp) {
				// we have some tolerance
				if (other.tstamp - 100 < tstamp || other.tstamp + 100 > tstamp) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		}
	}

	protected class MessageEventReceived extends EventReceived {
		private ISUPEvent event;

		/**
		 * @param tstamp
		 */
		public MessageEventReceived(long tstamp, ISUPEvent event) {
			super(tstamp);
			this.event = event;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			MessageEventReceived other = (MessageEventReceived) obj;
			if (event == null) {
				if (other.event != null)
					return false;
				// FIXME: use event equal?
			} else if (event.getMessage().getMessageType().getCode() != other.event.getMessage().getMessageType().getCode())
				return false;
			return true;
		}

		public String toString() {
			return "MessageEventReceived [event=" + event + ", tstamp= " + super.tstamp + "]";
		}
	}

	protected class TimeoutEventReceived extends EventReceived {
		private ISUPTimeoutEvent event;

		public TimeoutEventReceived(long tstamp, ISUPTimeoutEvent event) {
			super(tstamp);
			this.event = event;
			
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			TimeoutEventReceived other = (TimeoutEventReceived) obj;
			if (event == null) {
				if (other.event != null)
					return false;
				// FIXME: use event equal?
			} else if (event.getMessage().getMessageType().getCode() != other.event.getMessage().getMessageType().getCode()) {
				return false;
			} else if (event.getTimerId() != other.event.getTimerId()) {
				return false;
			}

			return true;
		}

		public String toString() {
			return "TimeoutEventReceived [event=" + event + ", tstamp= " + super.tstamp + "]";
		}

	}

	public class TimerTestLinkset extends Linkset {

		byte[] rxBuffer;
		byte[] txBuffer;

		private static final String AS_NAME = "asname";

		private String asName;

		public TimerTestLinkset(String linksetName, int opc, int dpc, int ni) {
			super(linksetName, opc, dpc, ni);

		}

		
		protected void initialize() {
			this.linksetStream = new LinksetStreamImpl();
		}

		
		protected void configure() {

		}

		public String getAspName() {
			return asName;
		}

		/**
		 * Operations
		 */
		
		public void activate() throws Exception {
			throw new Exception();
		}

		
		public void deactivate() throws Exception {
			throw new Exception();
		}

		
		public void activateLink(String linkName) throws Exception {
			throw new Exception();
		}

		
		public void deactivateLink(String linkName) throws Exception {
			throw new Exception();
		}

		
		public void createLink(String[] arg0) throws Exception {
			throw new Exception();
		}

		
		public void deleteLink(String arg0) throws Exception {
			throw new Exception();
		}

		protected class LinksetStreamImpl extends LinksetStream {

			
			public String getName() {
				return linksetName;
			}

			
			public boolean poll(int op, int timeout) {
				try {
					Thread.currentThread().sleep(timeout);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}

			public void close() {
				// TODO Auto-generated method stub

			}

			public SelectorProvider provider() {
				return null;

			}

			public int read(byte[] paramArrayOfByte) throws IOException {
				if (rxBuffer != null) {
					System.arraycopy(rxBuffer, 0, paramArrayOfByte, 0, rxBuffer.length);
					int k = rxBuffer.length; 
					rxBuffer = null;
					return k;
				}
				return 0;
			}

			public SelectorKey register(StreamSelector selector) throws IOException {
				return ((LinksetSelector) selector).register(this);
			}

			public int write(byte[] msu) throws IOException {
				// here we have to parse ISUPMsg and store in receivedRemote
				long tstamp = System.currentTimeMillis();
				// FIXME: change this, dont copy over and over.
				int commandCode = msu[7];// 3(RL) + 1(SI)+2(CIC) -
				// http://pt.com/page/tutorials/ss7-tutorial/mtp
				byte[] payload = new byte[msu.length - 5];
				System.arraycopy(msu, 5, payload, 0, payload.length);
				// for post processing
				AbstractISUPMessage msg = (AbstractISUPMessage) provider.getMessageFactory().createCommand(commandCode);
				try {
					msg.decode(payload, provider.getParameterFactory());
					MessageEventReceived event = new MessageEventReceived(tstamp, new ISUPEvent(provider, msg));
					remoteEventsReceived.add(event);
					return msu.length;
				} catch (ParameterException e) {
					e.printStackTrace();
					fail("Failed on message write: " + e);
				}
				return 0;
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.mobicents.ss7.linkset.oam.Linkset#print(java.lang.StringBuffer,
		 * int, int)
		 */
		
		public void print(StringBuffer sb, int leftPad, int descPad) {
			// TODO Auto-generated method stub

		}
	}


}
