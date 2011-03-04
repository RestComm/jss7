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
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mobicents.protocols.ss7.isup.ISUPEvent;
import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

/**
 * @author baranowb
 * 
 */
public abstract class DoubleTimers extends EventTestHarness {

	// smaller timer - ie. T16
	protected long SMALLER_T;
	protected int SMALLER_T_ID;
	// bigger timer - ie. T17
	protected long BIGGER_T;
	protected int BIGGER_T_ID;
	protected long numOfTimeouts;
	// ie. RSC - T16 & T17
	protected ISUPMessage request; // message exchanged within
	protected ISUPMessage answer;

	@Before
	public void setUp() throws Exception {

		super.setUp();
		this.SMALLER_T = getSmallerT();
		this.BIGGER_T = getBiggerT();
		this.SMALLER_T_ID = getSmallerT_ID();
		this.BIGGER_T_ID = getBiggerT_ID();
		this.numOfTimeouts = BIGGER_T / SMALLER_T;
		this.request = getRequest();
		this.answer = getAnswer();
	}

	@After
	public void tearDown() throws Exception {
		super.provider.removeListener(this);
		super.tearDown();
	}

	protected abstract long getSmallerT();

	protected abstract long getBiggerT();

	// IDS
	protected abstract int getSmallerT_ID();

	protected abstract int getBiggerT_ID();

	
	/**
	 * return request sent after big timer hits, in most cases its orignial.
	 * @return
	 */
	protected ISUPMessage getAfterBigTRequest()
	{
		return getRequest();
	}
	
	@Test
	public void testBigTimeout() throws Exception {
		// add expected events on remote and local end
		List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
		List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();

		long startTStamp = System.currentTimeMillis();
		
		this.provider.sendMessage(this.request);
		MessageEventReceived eventReceived = new MessageEventReceived(startTStamp, new ISUPEvent(super.provider, this.request));
		expectedRemoteEventsReceived.add(eventReceived);
		// now lets create events we expect to be received by local and remote.
		int count = 0;
		while (count != this.numOfTimeouts) {
			long eventtStamp = startTStamp + (count + 1) * SMALLER_T;
			ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, SMALLER_T_ID);
			TimeoutEventReceived ter = new TimeoutEventReceived(eventtStamp, timeoutEvent);
			expectedLocalEvents.add(ter);

			ISUPEvent event = new ISUPEvent(super.provider, this.request);
			eventReceived = new MessageEventReceived(eventtStamp, event);
			expectedRemoteEventsReceived.add(eventReceived);
			count++;
		}
		

		// and one with tstamp for ie. T17 timeout.
		ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, BIGGER_T_ID);
		TimeoutEventReceived ter = new TimeoutEventReceived(startTStamp + BIGGER_T, timeoutEvent);
		expectedLocalEvents.add(ter);

		ISUPEvent event = new ISUPEvent(super.provider, getAfterBigTRequest());
		eventReceived = new MessageEventReceived(startTStamp + BIGGER_T, event);
		expectedRemoteEventsReceived.add(eventReceived);

		// now wait
		doWait(BIGGER_T + 500);
		// stop stack
		stack.stop();

		// now make compare
		super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
	}

	@Test
	public void testBigTimeoutWithAnswer() throws Exception {
		// add expected events on remote and local end
		List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
		List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();

		long startTStamp = System.currentTimeMillis();
		this.provider.sendMessage(this.request);
		MessageEventReceived eventReceived = new MessageEventReceived(startTStamp, new ISUPEvent(super.provider, this.request));
		expectedRemoteEventsReceived.add(eventReceived);

		// now lets create events we expect to be received by local and remote.
		int count = 0;
		while (count != this.numOfTimeouts) {
			long eventtStamp = startTStamp + (count + 1) * SMALLER_T;
			ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, SMALLER_T_ID);
			TimeoutEventReceived ter = new TimeoutEventReceived(eventtStamp, timeoutEvent);
			expectedLocalEvents.add(ter);

			ISUPEvent event = new ISUPEvent(super.provider, this.request);
			eventReceived = new MessageEventReceived(eventtStamp, event);
			expectedRemoteEventsReceived.add(eventReceived);
	        count++;
		}

		// and one with tstamp for ie. T17 timeout.
		ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, BIGGER_T_ID);
		TimeoutEventReceived ter = new TimeoutEventReceived(startTStamp + BIGGER_T, timeoutEvent);
		expectedLocalEvents.add(ter);

		ISUPEvent event = new ISUPEvent(super.provider, getAfterBigTRequest());
		eventReceived = new MessageEventReceived(startTStamp + BIGGER_T, event);
		expectedRemoteEventsReceived.add(eventReceived);

		// now wait
		doWait(BIGGER_T + 500);
		long tstamp = System.currentTimeMillis();
		doAnswer();
		event = new ISUPEvent(super.provider, this.answer);
		eventReceived = new MessageEventReceived(tstamp, event);
		expectedLocalEvents.add(eventReceived);
		doWait(1000);
		// stop stack
		stack.stop();
		// now make compare
		super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
	}

	@Test
	public void testSmallTimeoutWithAnswer() throws Exception {
		// add expected events on remote and local end
		List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
		List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();

		long startTStamp = System.currentTimeMillis();
		this.provider.sendMessage(this.request);
		MessageEventReceived eventReceived = new MessageEventReceived(startTStamp, new ISUPEvent(super.provider, this.request));
		expectedRemoteEventsReceived.add(eventReceived);

		// now lets create events we expect to be received by local and remote.

		long eventtStamp = startTStamp * SMALLER_T;
		ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, SMALLER_T_ID);
		TimeoutEventReceived ter = new TimeoutEventReceived(eventtStamp, timeoutEvent);
		expectedLocalEvents.add(ter);

		ISUPEvent event = new ISUPEvent(super.provider, this.request);
		eventReceived = new MessageEventReceived(eventtStamp, event);
		expectedRemoteEventsReceived.add(eventReceived);

		
		// now wait
		doWait(SMALLER_T + 500); //500 should be good even here.
		long tstamp = System.currentTimeMillis();
		doAnswer();
		event = new ISUPEvent(super.provider, this.answer);
		eventReceived = new MessageEventReceived(tstamp, event);
		expectedLocalEvents.add(eventReceived);
		doWait(1000);
		// now make compare
		// stop stack
		stack.stop();
		super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
	}
	
	@Test
	public void testNoTimeoutWithAnswer() throws Exception {
		// add expected events on remote and local end
		List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
		List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();

		this.provider.sendMessage(this.request);
		MessageEventReceived eventReceived = new MessageEventReceived(System.currentTimeMillis(), new ISUPEvent(super.provider, this.request));
		expectedRemoteEventsReceived.add(eventReceived);

		// now wait
		doWait(SMALLER_T/2); 
		long tstamp = System.currentTimeMillis();
		doAnswer();
		ISUPEvent event = new ISUPEvent(super.provider, this.answer);
		eventReceived = new MessageEventReceived(tstamp, event);
		expectedLocalEvents.add(eventReceived);
		doWait(1000);
		// now make compare
		// stop stack
		stack.stop();
		super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
	}
}
