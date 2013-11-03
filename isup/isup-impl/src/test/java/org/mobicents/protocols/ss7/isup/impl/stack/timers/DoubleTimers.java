/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.isup.impl.stack.timers;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.isup.ISUPEvent;
import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

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

    @BeforeClass
    public void setUp() throws Exception {

        super.setUp();
        this.SMALLER_T = getSmallerT();
        this.BIGGER_T = getBiggerT();
        this.SMALLER_T_ID = getSmallerT_ID();
        this.BIGGER_T_ID = getBiggerT_ID();
        if (BIGGER_T % SMALLER_T == 0)
            this.numOfTimeouts = BIGGER_T / SMALLER_T - 1;
        else
            this.numOfTimeouts = BIGGER_T / SMALLER_T;

        this.request = getRequest();
        this.answer = getAnswer();
    }

    @AfterClass
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
     *
     * @return
     */
    protected ISUPMessage getAfterBigTRequest() {
        return getRequest();
    }

    // @Test(groups = { "functional.timer","timer.timeout.big"})
    public void testBigTimeout() throws Exception {
        // add expected events on remote and local end
        List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
        List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();

        long startTStamp = System.currentTimeMillis();

        this.provider.sendMessage(this.request, dpc);
        MessageEventReceived eventReceived = new MessageEventReceived(startTStamp, new ISUPEvent(super.provider, this.request,
                dpc));
        expectedRemoteEventsReceived.add(eventReceived);
        // now lets create events we expect to be received by local and remote.
        int count = 0;
        while (count != this.numOfTimeouts) {
            long eventtStamp = startTStamp + (count + 1) * SMALLER_T;
            ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, SMALLER_T_ID, dpc);
            TimeoutEventReceived ter = new TimeoutEventReceived(eventtStamp, timeoutEvent);
            expectedLocalEvents.add(ter);

            ISUPEvent event = new ISUPEvent(super.provider, this.request, dpc);
            eventReceived = new MessageEventReceived(eventtStamp, event);
            expectedRemoteEventsReceived.add(eventReceived);
            count++;
        }

        // and one with tstamp for ie. T17 timeout.
        ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, BIGGER_T_ID, dpc);
        TimeoutEventReceived ter = new TimeoutEventReceived(startTStamp + BIGGER_T, timeoutEvent);
        expectedLocalEvents.add(ter);

        ISUPEvent event = new ISUPEvent(super.provider, getAfterBigTRequest(), dpc);
        eventReceived = new MessageEventReceived(startTStamp + BIGGER_T, event);
        expectedRemoteEventsReceived.add(eventReceived);

        // now wait
        doWait(BIGGER_T + 500);
        // stop stack
        stack.stop();

        // now make compare
        super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
    }

    // @Test(groups = { "functional.timer","timer.timeout.big.answer"})
    public void testBigTimeoutWithAnswer() throws Exception {
        // add expected events on remote and local end
        List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
        List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();

        long startTStamp = System.currentTimeMillis();
        this.provider.sendMessage(this.request, dpc);
        MessageEventReceived eventReceived = new MessageEventReceived(startTStamp, new ISUPEvent(super.provider, this.request,
                dpc));
        expectedRemoteEventsReceived.add(eventReceived);

        // now lets create events we expect to be received by local and remote.
        int count = 0;
        while (count != this.numOfTimeouts) {
            long eventtStamp = startTStamp + (count + 1) * SMALLER_T;
            ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, SMALLER_T_ID, dpc);
            TimeoutEventReceived ter = new TimeoutEventReceived(eventtStamp, timeoutEvent);
            expectedLocalEvents.add(ter);

            ISUPEvent event = new ISUPEvent(super.provider, this.request, dpc);
            eventReceived = new MessageEventReceived(eventtStamp, event);
            expectedRemoteEventsReceived.add(eventReceived);
            count++;
        }

        // and one with tstamp for ie. T17 timeout.
        ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, BIGGER_T_ID, dpc);
        TimeoutEventReceived ter = new TimeoutEventReceived(startTStamp + BIGGER_T, timeoutEvent);
        expectedLocalEvents.add(ter);

        ISUPEvent event = new ISUPEvent(super.provider, getAfterBigTRequest(), dpc);
        eventReceived = new MessageEventReceived(startTStamp + BIGGER_T, event);
        expectedRemoteEventsReceived.add(eventReceived);

        // now wait
        doWait(BIGGER_T + 500);
        long tstamp = System.currentTimeMillis();
        doAnswer();
        event = new ISUPEvent(super.provider, this.answer, dpc);
        eventReceived = new MessageEventReceived(tstamp, event);
        expectedLocalEvents.add(eventReceived);
        doWait(1000);
        // stop stack
        stack.stop();
        // now make compare
        super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
    }

    // @Test(groups = { "functional.timer","timer.timeout.big.woanswer"})
    public void testSmallTimeoutWithAnswer() throws Exception {
        // add expected events on remote and local end
        List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
        List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();

        long startTStamp = System.currentTimeMillis();
        this.provider.sendMessage(this.request, dpc);
        MessageEventReceived eventReceived = new MessageEventReceived(startTStamp, new ISUPEvent(super.provider, this.request,
                dpc));
        expectedRemoteEventsReceived.add(eventReceived);

        // now lets create events we expect to be received by local and remote.

        long eventtStamp = startTStamp * SMALLER_T;
        ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(super.provider, this.request, SMALLER_T_ID, dpc);
        TimeoutEventReceived ter = new TimeoutEventReceived(eventtStamp, timeoutEvent);
        expectedLocalEvents.add(ter);

        ISUPEvent event = new ISUPEvent(super.provider, this.request, dpc);
        eventReceived = new MessageEventReceived(eventtStamp, event);
        expectedRemoteEventsReceived.add(eventReceived);

        // now wait
        doWait(SMALLER_T + 500); // 500 should be good even here.
        long tstamp = System.currentTimeMillis();
        doAnswer();
        event = new ISUPEvent(super.provider, this.answer, dpc);
        eventReceived = new MessageEventReceived(tstamp, event);
        expectedLocalEvents.add(eventReceived);
        doWait(1000);
        // now make compare
        // stop stack
        stack.stop();
        super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
    }

    // @Test(groups = { "functional.timer","timer.answer"})
    public void testNoTimeoutWithAnswer() throws Exception {
        // add expected events on remote and local end
        List<EventReceived> expectedRemoteEventsReceived = new ArrayList<EventReceived>();
        List<EventReceived> expectedLocalEvents = new ArrayList<EventReceived>();

        this.provider.sendMessage(this.request, dpc);
        MessageEventReceived eventReceived = new MessageEventReceived(System.currentTimeMillis(), new ISUPEvent(super.provider,
                this.request, dpc));
        expectedRemoteEventsReceived.add(eventReceived);

        // now wait
        doWait(SMALLER_T / 2);
        long tstamp = System.currentTimeMillis();
        doAnswer();
        ISUPEvent event = new ISUPEvent(super.provider, this.answer, dpc);
        eventReceived = new MessageEventReceived(tstamp, event);
        expectedLocalEvents.add(eventReceived);
        doWait(1000);
        // now make compare
        // stop stack
        stack.stop();
        super.compareEvents(expectedLocalEvents, expectedRemoteEventsReceived);
    }
}
