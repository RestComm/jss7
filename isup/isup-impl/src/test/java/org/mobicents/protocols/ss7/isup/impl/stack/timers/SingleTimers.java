/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
public abstract class SingleTimers extends EventTestHarness {

	protected int tid;
	protected long timeout;

	protected ISUPMessage request; // message exchanged within
	protected ISUPMessage answer;

	@Before
	public void setUp() throws Exception {

		super.setUp();
		this.tid = getT_ID();
		this.timeout = getT();
		this.request = getRequest();
		this.answer = getAnswer();
	}

	@After
	public void tearDown() throws Exception {
		super.provider.removeListener(this);
		super.tearDown();
	}

	protected abstract long getT();

	// IDS
	protected abstract int getT_ID();

	protected ISUPMessage getAfterTRequest()
	{
		return this.provider.getMessageFactory().createREL(getRequest().getCircuitIdentificationCode().getCIC());
	}
	@Test
	public void testWithTimeout() throws Exception
	{
		// add expected events on remote and local end
		List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
		List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();
		
		long startTStamp = System.currentTimeMillis();
		super.provider.sendMessage(this.request);
		MessageEventReceived eventReceived = new MessageEventReceived(startTStamp, new ISUPEvent(super.provider, this.request));
		expectedRemoteEventsReceived.add(eventReceived);
		ISUPMessage afterTimeoutMessage = getAfterTRequest();
		if(afterTimeoutMessage!=null)
		{
			eventReceived = new MessageEventReceived(startTStamp+getT(), new ISUPEvent(super.provider,afterTimeoutMessage));
			expectedRemoteEventsReceived.add(eventReceived);
		}
		ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, tid);
		TimeoutEventReceived ter = new TimeoutEventReceived(startTStamp + timeout, timeoutEvent);
		expectedLocalEvents.add(ter);
		doWait(timeout+1000);
		
		// stop stack
		stack.stop();

		// now make compare
		super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
	}
	@Test
	public void testWithoutTimeout() throws Exception
	{
		// add expected events on remote and local end
		List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
		List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();

		long startTStamp = System.currentTimeMillis();
		this.provider.sendMessage(this.request);
		MessageEventReceived eventReceived = new MessageEventReceived(startTStamp, new ISUPEvent(super.provider, this.request));
		expectedRemoteEventsReceived.add(eventReceived);

		doWait(timeout/2); //500 should be good even here.
		long tstamp = System.currentTimeMillis();
		doAnswer();
		ISUPEvent event = new ISUPEvent(super.provider, this.answer);
		eventReceived = new MessageEventReceived(tstamp, event);
		expectedLocalEvents.add(eventReceived);
		doWait(timeout); //wait more
		
		stack.stop();
		// now make compare
		super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
	}
	
}
