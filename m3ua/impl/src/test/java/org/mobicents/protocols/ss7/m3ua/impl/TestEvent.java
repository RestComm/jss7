/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
