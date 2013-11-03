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

package org.mobicents.protocols.ss7.tcapAnsi;

import java.io.Serializable;

/**
 * @author baranowb
 *
 */
public class TestEvent implements Serializable {

    private EventType eventType;
    private boolean sent;
    private long timestamp;
    private Object event;
    private int sequence;

    public static TestEvent createReceivedEvent(EventType eventType, Object eventSource, int sequence) {
        TestEvent te = new TestEvent(eventType, false, System.currentTimeMillis(), eventSource, sequence);
        return te;
    }

    public static TestEvent createSentEvent(EventType eventType, Object eventSource, int sequence) {
        TestEvent te = new TestEvent(eventType, true, System.currentTimeMillis(), eventSource, sequence);
        return te;
    }

    public static TestEvent createReceivedEvent(EventType eventType, Object eventSource, int sequence, long stamp) {
        TestEvent te = new TestEvent(eventType, false, stamp, eventSource, sequence);
        return te;
    }

    public static TestEvent createSentEvent(EventType eventType, Object eventSource, int sequence, long stamp) {
        TestEvent te = new TestEvent(eventType, true, stamp, eventSource, sequence);
        return te;
    }

    /**
     * @param eventType
     * @param sent
     * @param timestamp
     * @param event
     */
    public TestEvent(EventType eventType, boolean sent, long timestamp, Object event, int sequence) {
        super();
        this.eventType = eventType;
        this.sent = sent;
        this.timestamp = timestamp;
        this.event = event;
        this.sequence = sequence;
    }

    public EventType getEventType() {
        return eventType;
    }

    public boolean isSent() {
        return sent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object getEvent() {
        return event;
    }

    public int getSequence() {
        return sequence;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result + ((eventSource == null) ? 0 : eventSource.hashCode());
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + (sent ? 1231 : 1237);
        result = prime * result + sequence;
        // dont use this ?
        // result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestEvent other = (TestEvent) obj;
        if (eventType != other.eventType)
            return false;
        if (sent != other.sent)
            return false;
        if (sequence != other.sequence)
            return false;
        if (timestamp != other.timestamp) {
            long v = timestamp - other.timestamp;
            v = Math.abs(v);
            // 600ms, this can happen if we run tests concurrently and its not a big deal :)
            if (v > 600) {
                return false;
            }
        }

        // now compare source!

        return true;
    }

    @Override
    public String toString() {
        return "TestEvent [eventType=" + eventType + ", sent=" + sent + ", timestamp=" + timestamp + ", eventSource=" + event
                + ", sequence=" + sequence + "]";
    }

}
