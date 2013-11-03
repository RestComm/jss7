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
package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.Arrays;

/**
 *
 * @author amit bhayani
 *
 */
public class TestEvent {

    private TestEventType testEventType;
    private boolean sent;
    private long timestamp;
    private Object[] event;
    private int sequence;

    public TestEvent(TestEventType eventType, long timestamp, Object[] event, int sequence) {
        super();
        this.testEventType = eventType;
        this.timestamp = timestamp;
        this.event = event;
        this.sequence = sequence;
    }

    public TestEventType getTestEventType() {
        return testEventType;
    }

    public boolean isSent() {
        return sent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object[] getEvent() {
        return event;
    }

    public int getSequence() {
        return sequence;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(event);
        result = prime * result + (sent ? 1231 : 1237);
        result = prime * result + sequence;
        result = prime * result + ((testEventType == null) ? 0 : testEventType.hashCode());
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
        if (!Arrays.equals(event, other.event))
            return false;
        if (sent != other.sent)
            return false;
        if (sequence != other.sequence)
            return false;
        if (testEventType != other.testEventType)
            return false;
        return true;
    }
}
